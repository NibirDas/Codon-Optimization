// Compile with:
//      javac Match2.java
// Run with:
//      java Match2 HEAGAWGHEE PAWHEAE

//  Class hierarchies
//  -----------------
//  Align                   general pairwise alignment
//     
//        NW                global alignment with simple gap costs
//        
//  Traceback               traceback pointers
//     
//  Substitution            substitution matrices with fast lookup
//     Blosum50             the BLOSUM50 substitution matrix
//  Output                  general text output
//     SystemOut            output to the console (in the application)
//     TextAreaOut          output to a TextArea (in the applet)

//  conventions: 
//   i in {0..n} indexes columns and sequence seq1
//   j in {0..m} indexes rows    and sequence seq2
//   k in {0..2} indexes states (in affine alignment)

// The class of substitution (scoring) matrices

abstract class Substitution {
  public int[][] score;

  void buildscore(String residues, int[][] residuescores) {
    // Allow lowercase and uppercase residues (ASCII code <= 127):
    score = new int[127][127];
    for (int i=0; i<residues.length(); i++) {
      char res1 = residues.charAt(i);
      for (int j=0; j<=i; j++) {
        char res2 = residues.charAt(j);
        score[res1][res2] = score[res2][res1] 
	  = score[res1][res2+32] = score[res2+32][res1] 
	  = score[res1+32][res2] = score[res2][res1+32] 
	  = score[res1+32][res2+32] = score[res2+32][res1+32] 
	  = residuescores[i][j];
      }
    }
  }

  abstract public String getResidues();
}


// The BLOSUM50 substitution matrix for amino acids (Durbin et al, p 16)

class Blosum50 extends Substitution {

  private String residues = "ARNDCQEGHILKMFPSTWYV";
  
  public String getResidues() 
  { return residues; }

  private int[][] residuescores = 
            /* A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V */
  { /* A */ {  5                                                          },
    /* R */ { -2, 7                                                       },
    /* N */ { -1,-1, 7                                                    },
    /* D */ { -2,-2, 2, 8                                                 },
    /* C */ { -1,-4,-2,-4,13                                              },
    /* Q */ { -1, 1, 0, 0,-3, 7                                           },
    /* E */ { -1, 0, 0, 2,-3, 2, 6                                        },
    /* G */ {  0,-3, 0,-1,-3,-2,-3, 8                                     },
    /* H */ { -2, 0, 1,-1,-3, 1, 0,-2,10                                  },
    /* I */ { -1,-4,-3,-4,-2,-3,-4,-4,-4, 5                               },
    /* L */ { -2,-3,-4,-4,-2,-2,-3,-4,-3, 2, 5                            },
    /* K */ { -1, 3, 0,-1,-3, 2, 1,-2, 0,-3,-3, 6                         },
    /* M */ { -1,-2,-2,-4,-2, 0,-2,-3,-1, 2, 3,-2, 7                      },
    /* F */ { -3,-3,-4,-5,-2,-4,-3,-4,-1, 0, 1,-4, 0, 8                   },
    /* P */ { -1,-3,-2,-1,-4,-1,-1,-2,-2,-3,-4,-1,-3,-4,10                },
    /* S */ {  1,-1, 1, 0,-1, 0,-1, 0,-1,-3,-3, 0,-2,-3,-1, 5             },
    /* T */ {  0,-1, 0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1, 2, 5          },
    /* W */ { -3,-3,-4,-5,-5,-1,-3,-3,-3,-3,-2,-3,-1, 1,-4,-4,-3,15       },
    /* Y */ { -2,-1,-2,-3,-3,-1,-2,-3, 2,-1,-1,-2, 0, 4,-3,-2,-2, 2, 8    },
    /* V */ {  0,-3,-3,-4,-1,-3,-3,-4,-4, 4, 1,-3, 1,-1,-3,-2, 0,-3,-1, 5 } 
            /* A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V */
  };

  public Blosum50() 
  { buildscore(residues, residuescores); }
}


// Pairwise sequence alignment 

abstract class Align {
  Substitution sub;             // substitution matrix
  int d;                        // gap cost
  String seq1, seq2;            // the sequences
  int n, m;                     // their lengths
  Traceback B0;                 // the starting point of the traceback

  final static int NegInf = Integer.MIN_VALUE/2; // negative infinity

  public Align(Substitution sub, int d, String seq1, String seq2) {
    this.sub = sub;
    this.seq1 = strip(seq1); this.seq2 = strip(seq2);
    this.d = d;
    this.n = this.seq1.length(); this.m = this.seq2.length();
  }

  public String strip(String s) {
    boolean[] valid = new boolean[127];
    String residues = sub.getResidues();
    for (int i=0; i<residues.length(); i++) {
      char c = residues.charAt(i);
      if (c < 96) 
	valid[c] = valid[c+32] = true;
      else
	valid[c-32] = valid[c] = true;
    }
    StringBuffer res = new StringBuffer(s.length());
    for (int i=0; i<s.length(); i++)
      if (valid[s.charAt(i)])
	res.append(s.charAt(i));
    return res.toString();
  }

  // Return two-element array containing an alignment with maximal score

  public String[] getMatch() {
    StringBuffer res1 = new StringBuffer();
    StringBuffer res2 = new StringBuffer();
    Traceback tb = B0;
    int i = tb.i, j = tb.j;
    while ((tb = next(tb)) != null) {
      if (i == tb.i) 
        res1.append('-');
      else
        res1.append(seq1.charAt(i-1)); 
      if (j == tb.j) 
        res2.append('-');
      else
        res2.append(seq2.charAt(j-1)); 
      i = tb.i; j = tb.j;
    }        
    String[] res = { res1.reverse().toString(), res2.reverse().toString() };
    return res;
  }

  public String fmtscore(int val) {
    if (val < NegInf/2) 
      return "-Inf";
    else
      return Integer.toString(val);
  }

  // Print the score, the F matrix, and the alignment
  public void domatch(Output out, String msg, boolean udskrivF) {
    out.println(msg + ":"); 
    out.println("Score = " + getScore());
    if (udskrivF) {
      out.println("The F matrix:");
      printf(out);
    }
    out.println("An optimal alignment:");
    String[] match = getMatch();
    out.println(match[0]);
    out.println(match[1]);
    out.println();
  }

  public void domatch(Output out, String msg) 
  { domatch(out, msg, true); }    

  // Get the next state in the traceback
  public Traceback next(Traceback tb)
  { return tb; }                // dummy implementation for the `smart' algs.

  // Return the score of the best alignment
  public abstract int getScore();

  // Print the matrix (matrices) used to compute the alignment
  public abstract void printf(Output out);

  // Auxiliary functions
  static int max(int x1, int x2) 
  { return (x1 > x2 ? x1 : x2); }

  static int max(int x1, int x2, int x3) 
  { return max(x1, max(x2, x3)); }

  static int max(int x1, int x2, int x3, int x4) 
  { return max(max(x1, x2), max(x3, x4)); }

  static String padLeft(String s, int width) {
    int filler = width - s.length();
    if (filler > 0) {           // and therefore width > 0
      StringBuffer res = new StringBuffer(width);
      for (int i=0; i<filler; i++)
        res.append(' ');
      return res.append(s).toString();
    } else
      return s;
  }
}


// Alignment with simple gap costs

abstract class AlignSimple extends Align {
  int[][] F;                    // the matrix used to compute the alignment
  Traceback2[][] B;             // the traceback matrix

  public AlignSimple(Substitution sub, int d, String seq1, String seq2) {
    super(sub, d, seq1, seq2);
    F = new int[n+1][m+1];
    B = new Traceback2[n+1][m+1];
  }

  public Traceback next(Traceback tb) {
    Traceback2 tb2 = (Traceback2)tb;
    return B[tb2.i][tb2.j];
  }

  public int getScore() 
  { return F[B0.i][B0.j]; }

  public void printf(Output out) {
    for (int j=0; j<=m; j++) {
      for (int i=0; i<F.length; i++)
	out.print(padLeft(fmtscore(F[i][j]), 5));
      out.println();
    }
  }
}


// Traceback objects

abstract class Traceback {
  int i, j;                     // absolute coordinates
}


// Traceback2 objects for simple gap costs

class Traceback2 extends Traceback {
  public Traceback2(int i, int j)
  { this.i = i; this.j = j; }
}


// Auxiliary classes for output

abstract class Output {
  public abstract void print(String s);
  public abstract void println(String s);
  public abstract void println();
}

class SystemOut extends Output {
  public void print(String s)
  { System.out.print(s); }

  public void println(String s)
  { System.out.println(s); }

  public void println()
  { System.out.println(); }
}


// algorithm (simple gap costs)

class NW extends AlignSimple {
  
  public NW(Substitution sub, int d, String sq1, String sq2) {
    super(sub, d, sq1, sq2);
    int n = this.n, m = this.m;
    int[][] score = sub.score;
    for (int i=1; i<=n; i++) {
      F[i][0] = -d * i;
      B[i][0] = new Traceback2(i-1, 0);
    }
    for (int j=1; j<=m; j++) {
      F[0][j] = -d * j;
      B[0][j] = new Traceback2(0, j-1);
    }
    for (int i=1; i<=n; i++)
      for (int j=1; j<=m; j++) {
        int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
        int val = max(F[i-1][j-1]+s, F[i-1][j]-d, F[i][j-1]-d);
        F[i][j] = val;
        if (val == F[i-1][j-1]+s)
          B[i][j] = new Traceback2(i-1, j-1);
        else if (val == F[i-1][j]-d)
          B[i][j] = new Traceback2(i-1, j);
        else if (val == F[i][j-1]-d)
          B[i][j] = new Traceback2(i, j-1);
        else
          throw new Error("NW 1");
      }
    B0 = new Traceback2(n, m);
  }
}



// Test algorithms

public class Match2 {
  public static void main(String[] args) {
    Output out = new SystemOut();
    String seq1 = args[0], seq2 = args[1];

    Substitution sub = new Blosum50();
    (new NW      (sub, 8,     seq1, seq2)).domatch(out, "GLOBAL ALIGNMENT");
    
  }
}
