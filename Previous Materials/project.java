
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;


class project extends Frame
{
     Panel p;
     Font f;
     Label label_dna,label_motif,label_dfile,title,label_bstcodon;
     Label star1,star2,label_output;
     Button dbrowse,mbrowse,cbrowse,execute,exit;
     TextField dnatxt,dfiletxt,mfiletxt,cfiletxt,ofiletxt;
     FileDialog fd;


public project()
{
    setTitle("Codon Optimization");
     p=new Panel();
     p.setLayout(null);
     f=new Font("Courier New",Font.BOLD,20);
     p.setFont(f);
     title=new Label(" CODON OPTIMIZATION    ",1);
     star1=new Label("    --------------------------------");
     star2=new Label("    --------------------------------");
     title.reshape(212,50,385,22);
     star1.reshape(260,30,385,22);
     star2.reshape(260,75,385,22);
     title.setForeground(Color.white);
     star1.setForeground(Color.white);
     star2.setForeground(Color.white);
     p.add(title);
     p.add(star1);
     p.add(star2);
     label_dna=new Label("1) DNA_SEQUENCE of Source species:",1);
     label_dfile=new Label("File name for DNA_SEQUENCE:",1);
     label_motif=new Label("2) File name for Motif Info:",1);
     label_bstcodon=new Label("3) File name for BEST CODON (Target Species):",1);
     label_output=new Label("4) Output File-Name:",1);
     dbrowse=new Button("BROWSE");
     mbrowse=new Button("BROWSE");
     cbrowse=new Button("BROWSE");
     execute=new Button("EXECUTE");
     exit=new Button("EXIT");
     dnatxt=new TextField("",6000);
     dfiletxt=new TextField("",15);
     mfiletxt=new TextField("",15);
     cfiletxt=new TextField("",15);
     ofiletxt=new TextField("out.txt",15);
     label_dna.setForeground(Color.white);
     label_dfile.setForeground(Color.white);
     label_motif.setForeground(Color.white);
     label_bstcodon.setForeground(Color.white);
     label_output.setForeground(Color.white);
     p.add(label_dna);
     p.add(label_motif);
     p.add(label_dfile);
     p.add(label_bstcodon);
     p.add(label_output);
     p.add(dbrowse);
     p.add(mbrowse);
     p.add(cbrowse);
     p.add(execute);
     p.add(exit);
     label_dna.reshape(50,125,365,22);
     label_dfile.reshape(75,170,385,22);
     label_motif.reshape(35,245,305,22);
     label_bstcodon.reshape(35,320,450,22);
     label_output.reshape(15,385,250,25);
     execute.reshape(275,460,110,27);
     exit.reshape(425,460,110,27);
     dbrowse.reshape(655,170,95,27);
     mbrowse.reshape(575,245,95,27);
     cbrowse.reshape(665,320,95,27);
     dnatxt.reshape(435,150,340,25);
     dfiletxt.reshape(460,195,150,25);
     mfiletxt.reshape(375,270,150,25);
     cfiletxt.reshape(485,345,150,25);
     ofiletxt.reshape(275,410,150,25);
     add(dnatxt);
     add(dfiletxt);
     add(mfiletxt);
     add(cfiletxt);
     add(ofiletxt);
     add(p);
}



public static void main(String args[])
   {
     Frame f=new project();
      f.resize(800,575);
      f.setBackground(new Color(18,133,70));
      f.show();
    }



public boolean handleEvent(Event e)
{
   if(e.id==Event.WINDOW_DESTROY)
      System.exit(0);
   return super.handleEvent(e);
}


public boolean action (Event e,Object arg)
{
   String str=new String();
   String dnaip=new String();
   String filenm=new String();
   String motif_file=new String();
   String best_codon_file=new String();
   String output_file=new String();
   boolean flagdna=false;
     boolean flagmotif=false;
      if(arg.equals("EXIT")) System.exit(0);
      else
           if(arg.equals("BROWSE"))
             {
                fd=new FileDialog(this,"Open",FileDialog.LOAD);
                fd.setDirectory("C:\\codon\\");
                fd.show();
                filenm=fd.getDirectory()+fd.getFile();
                if(fd.getFile()!=null)
                  {
                     if(e.target.equals(dbrowse))
                       {
                          dfiletxt.setText(filenm);
                           try
                           {
                            BufferedReader f1=new BufferedReader(new FileReader(filenm)); 
                             str=f1.readLine();
                            }
                          catch(Exception exp)
                           {
                             System.out.println(exp);
                            }
                         dnatxt.setText(str);
                         }
                     else if(e.target.equals(mbrowse))
                             mfiletxt.setText(filenm);
                          else if(e.target.equals(cbrowse))
                              cfiletxt.setText(filenm);
                      }
              }
            else
                if(arg.equals("EXECUTE"))
                 {
                    dnaip=dnatxt.getText();
                    motif_file=mfiletxt.getText();
                    best_codon_file=cfiletxt.getText();

                    if(ofiletxt.getText().length()!=0)
                         output_file="C:\\codon\\output\\"+ofiletxt.getText();
                    else
                         output_file="C:\\codon\\output\\out.txt";

                    flagdna=codon.isvalid_dna(dnaip);
                   
                     flagmotif=motif.isvalid_motif(motif_file,best_codon_file);
                     
                     if((flagdna==true)||(flagmotif==true))
                      {
                        ErrorDialog err=new ErrorDialog(this);
                        err.show();
                      }
                     else
                       {
                          execute.disable();
                          codon c=new codon();
                          c.start(dnaip,motif_file,best_codon_file,output_file);
                         ConfirmDialog cd=new ConfirmDialog(this,output_file);
                        cd.show();
                        }
                      }
                     else
                           return super.action(e,arg);
                         return true;
                    }
                  }

/********************************************************************/
  class ErrorDialog extends Dialog
{
    Panel p;
    Font f,f1;
    Label error_dna,error_motif;
    Button ok;
//= = = = = = = = = = = = =  == = =  = = ==  = = = = = >>
public ErrorDialog(Frame parent)
{
    super(parent,"Error",true);
    f=new Font("Helvetica",Font.PLAIN,16);
    f1=new Font("Helvetica",Font.BOLD,18);
    p=new Panel();
    p.setLayout(null);
    p.setFont(f);
    error_dna=new Label(codon.dna_error,1);
    error_motif=new Label(motif.motif_error,1);
    ok=new Button("OK");
    ok.setFont(f1);
    p.add(error_dna);
    p.add(error_motif);
    p.add(ok);
    ok.reshape(175,155,95,27);
    error_dna.reshape(7,25,450,22);
    error_motif.reshape(7,80,450,22);
    add(p);
    this.reshape(200,175,450,250);
    this.setBackground(Color.white);
}

//== = = = = = = = = = = = = = = = = = =  = >>

public boolean handleEvent(Event e)
{
      if(e.id==Event.WINDOW_DESTROY && e.target==this)
      dispose();
      else super.handleEvent(e);
      motif.motif_error="";
      motif.motif_count=0;
      codon.dna_error="";
      return true;
}
//= = = = = = = = = = = = = = =  = = = = = = = = = = = = >>



public boolean action(Event e,Object arg)
{
       if(arg.equals("OK"))
       {
           dispose();
           motif.motif_error="";
           motif.motif_count=0;
           codon.dna_error="";
           return true;
       }
       return false;
}// End of class ErrorDialog
}
//**********************************************
class ConfirmDialog extends Dialog
{
      Panel p;
      Font f,f1;
      Label confirm,output_file,file_name;
      Button ok;
//= = = = = = = = = = = = = = =  = = = = = = = = = = = = >>

public ConfirmDialog(Frame parent,String file)
{
    super(parent,"Success",true);
    f1=new Font("TimesRoman",Font.BOLD,22);
    f=new Font("Courier New",Font.ITALIC,22);
    p=new Panel();
    p.setLayout(null);
    p.setFont(f);
    confirm=new Label("Execution Successful!!!",1);
    output_file=new Label("Output is stored in:",2);
    file_name=new Label(file,1);
   ok=new Button("OK");
    p.add(confirm);
    p.add(output_file);
    p.add(file_name);
   p.add(ok);
    ok.reshape(230,180,100,29);
    confirm.reshape(55,50,450,27);
    output_file.reshape(30,105,200,27);
    file_name.reshape(190,105,390,27);
    add(p);
    this.reshape(145,175,575,275);
    this.setBackground(Color.black);
    this.setForeground(Color.green);
   ok.setForeground(Color.black);
   ok.setFont(f1);
   file_name.setFont(f1);
   file_name.setForeground(Color.orange);
}


public boolean handleEvent(Event e)
{
     if(e.id==Event.WINDOW_DESTROY && e.target==this)
        dispose();
     else  super.handleEvent(e);
     return true;
}


public boolean action(Event e,Object arg)
{
   if(arg.equals("OK"))
   {
      dispose();
      return true;
   }
   return false;
 }
}//End of class ConfirmDialog




 class codon    
{
      
public static String dna_error=new String();
public static String best_codon_file=new String();
public static PrintStream pout;
private String output_file=new String();
private String dna_source=new String();
private String rna_source=new String();
private String amino_acid_seq=new String();
public static String opt_codon_seq=new String();

//= = = = = = = = = = = =  = = = = = = = = = = = = = = = = = = = >>

public static boolean isvalid_dna(String str)
{
     str=str.trim();
     str=str.toUpperCase();
     int l=str.length();
     int i;
     String c;
     if(l==0)
     { 
         dna_error="ERROR:Zero Length of DNA Sequence";
         return(true);
      }
      for(i=0;i<l;i++)
      {
         c=str.substring(i,i+1);
        if(!(c.equals("A")||c.equals("T")|| c.equals("C")|| c.equals("G")))
        {
           dna_error="ERROR:Wrong Nitrogenous Base-";
           dna_error=dna_error.concat(c);
           dna_error=dna_error.concat("in DNA Input");
           return(true);
       }
     }
     return(false);
}
//= = = = = = = = = = ==  = = == = = = == = = = ==  >>
     
public void start(String dnaip,String motif_file,String bstcodfl,String op)
{
    dna_source=dnaip;
    dna_source= dna_source.trim();
    dna_source= dna_source.toUpperCase();
    best_codon_file=bstcodfl;
    output_file=op;
    try
    {
       pout=new PrintStream(new FileOutputStream(op));
     }
     catch(Exception exp)
     {
     	System.out.println(exp);
     }
     
         rna_source=dna_source.replace('A','U');
         rna_source= rna_source.replace('T','A');
         rna_source= rna_source.replace('G','x');
         rna_source= rna_source.replace('C','y');
         rna_source= rna_source.replace('x','C');
         rna_source= rna_source.replace('y','G');
         try
         {
             amino_acid_seq=translation(rna_source);
          }
          catch(Exception exp)
          {
               System.out.println(exp);
          }
          print_output();
          
          opt_codon_seq=codon_optimization(amino_acid_seq,motif_file);
          pout.close();
       }
   

public String translation(String rna_source) throws IOException
{
   String str=new String();
   String codon=new String();
   String codon_name=new String();
   String amino_acid_name=new String();
   String amino_acid_seq=new String();
   int l=rna_source.length();
   int i;
   for(i=0;i<=(l-3);i=i+3)
   {
       codon=rna_source.substring(i,i+3);
       if(!(codon.equals("UAA")||codon.equals("UAG")||codon.equals("UGA")))
       {
           BufferedReader f1=new BufferedReader(new FileReader("c:\\codon\\genetic_code.txt"));
           str=f1.readLine();
         while(str!=null)
         {
             StringTokenizer t=new StringTokenizer(str,"\n\t\r");
             codon_name=t.nextToken();
             amino_acid_name=t.nextToken();
             if(codon.equals(codon_name))
             {
                 amino_acid_seq=amino_acid_seq+amino_acid_name;
                 break;
              }
              str=f1.readLine();
          }
          f1.close();
          
         
	           
        }
    }
          return(amino_acid_seq);
      }

//= = = = =  = == = = = == == = = = = = = == = = = = ==  >>

public String codon_optimization(String amino_acid_seq,String motif_file)
{
   motif m[]=new motif[motif.motif_count];
   int i=0;
   String str=new String();
   String motif_name=new String();
   float motif_value=0.0f;
   try
   {
      BufferedReader f1=new BufferedReader(new FileReader(motif_file));
      str=f1.readLine();
      while(str!=null)
       {
           str=str.trim();
           StringTokenizer t=new StringTokenizer(str,"\n\t\r");
           motif_name=t.nextToken();
           motif_name=motif_name.toUpperCase();
           motif_value=Float.valueOf(t.nextToken()).floatValue();
           m[i]=new motif();
           m[i].fill_motif_input(motif_name,motif_value);
           m[i].fill_head_tail_aasp_info();
           i++;
           str=f1.readLine();
        }
        f1.close();
    }
    catch(Exception exp)
    {
       System.out.println(exp);
     }
     for(i=0;i<motif.motif_count;i++)
     {
         m[i].print_motif_info();
        m[i].optimize(amino_acid_seq);
    }
    return(null);
 }

//= = = = = = = = = = = = = = = = = = = = = = >>

public void print_output()
{
   pout.println("DNA sequence of  Source species:");
   pout.println(" "+dna_source);
   pout.println("-------------------------- >>");
   pout.println("mRNA sequence of source species:");
   pout.println(" "+rna_source);
   pout.println("-------------------------- >>");
   pout.println("Amino acid sequence of  Source species:");
   pout.println(" "+amino_acid_seq);
   pout.println("");
   }
}



 class motif
{
    public static String motif_error=new String();    //Data members
	public static int motif_count=0;
private String motif_seq=new String();
private float desirability_value=0.0f;
private String sub_motif[]=new String[3];
private String aasp[]=new String[3];
private String head[]=new String[3];
private String tail[]=new String[3];


//========= = = = = = = = = = = == = = = = == =  >>
public static boolean isvalid_motif(String filenm,String best_codon_file)
{
/*This function checks whether there is any error in the motif input,  also check for best-codon filename. It returns true if error exists, else false if input is correct. */
boolean inputflag=false;
String str=new String();
String motif_name=new String();
float motif_value=0.0f;
if(filenm.length()==0)
{
        motif_error="ERROR:No input file for motifs.";
	return(true);
		}
try
{
BufferedReader f1=new BufferedReader(new FileReader(filenm));
str=f1.readLine();   //Open and read the Motifs
while(str!=null)
{
	inputflag=true;
str=str.trim();
StringTokenizer t=new StringTokenizer(str,"\n\t\r");
motif_count++;
motif_name=t.nextToken();
motif_value=Float.valueOf(t.nextToken()).floatValue();
motif_name=motif_name.toUpperCase();
int l=motif_name.length();
int i;
String c;
for(i=0;i<l;i++)
{
      c=motif_name.substring(i,i+1);
      if(!(c.equals("A")||c.equals("T")||c.equals("C")||c.equals("G")))
     {
        motif_error="ERROR:wrong Nitrogenous Base";
        motif_error=motif_error.concat(c);
        motif_error=motif_error.concat("In motif input");
        return(true);
    }
}
	if((motif_value>1.0)||(motif_value<-1.0))
	    {
            motif_error="ERROR:wrong desirability value:";                            
            motif_error=motif_error.concat(String.valueOf(motif_value));
            return(true);
	     }
				str=f1.readLine();
		}
		f1.close();		
			} //end of try
			catch(Exception exp)
{ 
	System.out.println(exp);
}
if(inputflag==false)
{
        motif_error="ERROR:No such motif inputs found";
	return(true);
}
if(best_codon_file.length()==0)
{
        motif_error="ERROR:No input file for Best-Codon";
	return(true);
}
return(false);
}

//======== = = = =  ==== >>
public void fill_motif_input(String motif_name,float motif_value)
{
	this.motif_seq=motif_name;
	this.desirability_value=motif_value;
	return;
}

//========== = = = = = = = = >>

public void fill_head_tail_aasp_info()
{

	int l=motif_seq.length();
	int r=(l%3);
	int i;
	for(i=0;i<=2;i++)
   {
      double x=(l-i)/3; // 2-1
      x=java.lang.Math.floor(x);
      int u_limit=(int)x; //1
      u_limit=(u_limit*3)+i; // 6-4
      sub_motif[i]=motif_seq.substring(i,u_limit);
      sub_motif[i]= sub_motif[i].replace('T','U'); // GAUAUA-AUA
      try
      {
         codon c=new codon();
         aasp[i]=c.translation(sub_motif[i]);
            }
            catch(Exception e)
      {
         System.out.println(e);
      }
	}
   head[0]="";    //Calculating head
	head[1]=motif_seq.substring(0,1); 
   head[2]= motif_seq.substring(0,2);
	if(r==0)
{
        tail[0]="";
	tail[1]= motif_seq.substring(l-2,l);
	tail[2]= motif_seq.substring(l-1,l);
	}
	else if(r==1)
	{
		tail[0]= motif_seq.substring(l-1,l);
                tail[1]="";
		tail[2]= motif_seq.substring(l-2,l);
	}
	else if(r==2)
{
	tail[0]= motif_seq.substring(l-2,l);
		tail[1]= motif_seq.substring(l-1,l);
                tail[2]="";
	}
	for(i=0;i<=2;i++)
{
        head[i]=head[i].replace('T','U');
        tail[i]=tail[i].replace('T','U');
}
}

//=========== = = = === = = = = = = =  >>

public void optimize(String amino_acid_seq)
{
/* This function maps the motifs in the Amino-acid sequence, such that the motif occurrence is maximized*/
	int i=0,j=0,aasp_len,len;
	String amino=new String();
	String sub_amino=new String();
	String head_amino=new String();
	String tail_amino=new String();
	String opt_codon_seq_rna=new String();  //Store the optimized sequence
	String opt_codon_seq_dna=new String();
	int left_match_tag=0;
	int right_match_tag=0;
	int c=0,c1=0,k=0,check=0;
	len=amino_acid_seq.length();
	String s[][]=new String[len][6];
// This array stores the ultimate potential occurrences set of target species
	int flag[]=new int[len];
	for(i=0;i<len;i++)
	{
		flag[i]=0;
	}
	for(i=0;i<len;i++)
	{
		for(j=0;j<6;j++)
		{
                        s[i][j]="";  //Initializes by null value
		}
	}
	for(i=0;i<=2;i++)
	{
		aasp_len=aasp[i].length();
		if(aasp_len!=0)
		{
		for(j=0;j<(len-aasp_len+1);j++) //Check each aasp sized sub_amino
		{
			sub_amino=amino_acid_seq.substring(j,j+aasp_len);
			check=0;
			for(k=j;k<=j+aasp_len-1;k++)
			{
		//Sub-amino has been matched together with all of this amino-acid
				if(flag[k]==1)
				{
					check=1;
					break;
				}
			}
	if(check==0)	
{
if(sub_amino.equals(aasp[i]))
{
if((j>0)&&((j+aasp_len-1)<(len-1)))
{
                 head_amino=amino_acid_seq.substring(j-1,j);
head_amino=amino_acid_seq.substring(j+aasp_len,j+aasp_len+1);
try
{
if(flag[j-1]==0)
{
	reverse_translation(head_amino,s,j-1);
}
if(flag[j+aasp_len]==0) //tail_amino yet not checked
{
	reverse_translation(tail_amino,s,j+aasp_len);
}
}
catch(Exception e)
{
	System.out.println(e);
}
	}
	else
	{
		if(j==0)
		{
                        head_amino="";
			tail_amino=amino_acid_seq.substring(j+aasp_len,j+aasp_len+1);
	try
{
	if(flag[j+aasp_len]==0)
	{
	reverse_translation(tail_amino,s,j+aasp_len);
	}
}
catch(Exception e)
{
	System.out.println(e);
}
}
else
{
	if((j+aasp_len-1)==(len-1))
	{
		head_amino=amino_acid_seq.substring(j-1,j);
		tail_amino="";
		try
		{
			if(flag[j-1]==0)
			{
				reverse_translation(head_amino,s,j-1);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
}
if(head_amino!=""&& tail_amino!="")
{
	left_match_tag=match_left(head[i],s,j-1);
right_match_tag=match_right(tail[i],s,j+aasp_len);
}
else
{
        if(head_amino=="")
	{
		right_match_tag=match_right(tail[i],s,j+aasp_len);
		left_match_tag=1;
	}
else
{
        if(tail_amino=="")
	{
		left_match_tag=match_left(head[i],s,j-1);
		right_match_tag=1;
	}
	}
}
if((left_match_tag==1)&&(right_match_tag==1))
{
	c1=0;
	for(c=j;c<=j+aasp_len-1;c++)
	{
		s[c][0]=sub_motif[i].substring(c1,c1+3);
		c1=c1+3;
		flag[c]=1;
	}
	if((j-1)>0)   //check starting boundary
		flag[j-1]=1;  //head matched
	if((j+aasp_len)<len)   //check ending boundary
		flag[j+aasp_len]=1;  //tail matched

        codon.pout.println("Matching start at index:"+(j+1));
        codon.pout.print("Sub_amino="+sub_amino+",");
        codon.pout.print("Head_Amino="+head_amino+",");
        codon.pout.println("Tail_Amino="+tail_amino+",");
	}
} //end of inner if
}  //end of check if
}  //end of inner for
} //end of outer if
} 

for(k=0;k<len;k++)
{
	if(flag[k]==0)
{
	    amino=amino_acid_seq.substring(k,k+1);
for(c=0;c<6;c++)
{
        s[k][c]="";  //clear all the garbage value
}
try
{
	best_codon(amino,s,k);
}
catch(Exception e)
{
	//System.out.println(e);
}
}
}
codon.pout.println("***************");
codon.pout.println("Set of potential Occurences");
for(k=0;k<len;k++)
{
	opt_codon_seq_rna=opt_codon_seq_rna.concat(s[k][0]);
        codon.pout.print(" " +amino_acid_seq.substring(k,k+1)+"--- > ");
	for(c=0;c<6;c++)
	{
	    if(s[k][c].length()!=0)
{
   codon.pout.print(s[k][c]);
   codon.pout.print(" ");
}
}
codon.pout.println(" ");
}
codon.pout.println("***********");
codon.pout.println("End of Motif Calculation **************");
codon.pout.println("");
codon.pout.println("-----------------------> >>");
codon.pout.println("Optimized mRNA Codon Sequence of Target species is:");
codon.pout.println(" "+opt_codon_seq_rna);
opt_codon_seq_dna=opt_codon_seq_rna.replace('A','T');
opt_codon_seq_dna=opt_codon_seq_dna.replace('U','A');
opt_codon_seq_dna=opt_codon_seq_dna.replace('G','x');
opt_codon_seq_dna=opt_codon_seq_dna.replace('C','y');
opt_codon_seq_dna=opt_codon_seq_dna.replace('x','C');
opt_codon_seq_dna=opt_codon_seq_dna.replace('y','G');

codon.pout.println("------------------------ >>");
codon.pout.println("Optimized DNA Codon Sequence of Target species is:");
codon.pout.println(" "+opt_codon_seq_dna);
codon.pout.print("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = ");
codon.pout.println("= = = = = = = = = =");
codon.pout.println(" ");
}




void reverse_translation(String amino,String[][] s,int k) throws IOException
{
/* This function performs the reverse TRANSLATION on an Amino-acid & returns corr. Set of codons. It uses a file,named,"genetic_code",where the mapping info from mRNA to Amino-acid is stored.(1sttoken is GENETIC_CODE is the codon-pattern & later is the corresponding Amino-acid) */

String str=new String();
String codon_name=new String();
String amino_acid_name=new String();
int i=0;
BufferedReader f1=new BufferedReader(new FileReader("c:\\codon\\genetic_code.txt"));
str=f1.readLine();
while(str!=null)
{
  StringTokenizer t=new StringTokenizer(str,"\n\t\r");
  codon_name=t.nextToken();
  amino_acid_name=t.nextToken();
if(amino.equals(amino_acid_name))
{
    s[k][i]=codon_name;
    i++;
}
str=f1.readLine();
}
f1.close();
}

// = = = = = = = = = = =>>

void best_codon(String amino,String[][] s,int k) throws IOException
{
   /*This function performs the reverse TRANSLATION on an  Amino-acid & returns corr. Set of codons. It uses a file, named, �human_best_codon", where the mapping info. of  best_codon is stored.(1st token in HUMAN_BEST_CODON is the Amino-acid & later is the corresponding codon-pattern.)*/
 String str=new String();
String codon_name=new String();
String amino_acid_name=new String();
int i=0;
BufferedReader f1=new BufferedReader(new FileReader(codon.best_codon_file));

str=f1.readLine();
while(str!=null)
{
  StringTokenizer t=new StringTokenizer(str,"\n\t\r");
  amino_acid_name=t.nextToken();
codon_name=t.nextToken();
if(amino.equals(amino_acid_name))
{
   s[k][i]=codon_name;
}
str=f1.readLine();
}
f1.close();
}

//= = == = = = == = = = =  = = = = = = = = >>

public int match_left(String head,String[][] s,int k)
{
   int head_len,flag=0,c=0,i=0;
   String matching_head=new String();
  String codon_name=new String();
  String filter[]=new String[6];
  for(c=0;c<6;c++)
 {
   filter[c]="";    //Store current matching result in a temporary array
 }
 head_len=head.length();
 for(c=0;c<6;c++)
{
 codon_name=s[k][c];
if(codon_name.length()!=0)
{
   matching_head=codon_name.substring(3-head_len,3);
     /* takes a substring of same length of head[i] from an inner position to last position of the codon of head_amino*/
  if(matching_head.equals(head))
{
  flag=1;
 filter[i]=codon_name;
 i++;
}
}
}
 
if(flag==1)
{
 s[k]=filter;
}
return(flag);
}

//= = = = = = = = = = = = = = = = = = = = = = ==  >>


public int match_right(String tail,String[][] s,int k)
{
   int tail_len,flag=0,c=0,i=0;
   String matching_tail=new String();
   String codon_name=new String();
   String filter[]=new String[6];
   for(c=0;c<6;c++)
   {
       filter[c] ="";
   }
   tail_len=tail.length();
   for(c=0;c<6;c++)
   {
      codon_name=s[k][c];
      if(codon_name.length()!=0)
      {
         matching_tail=codon_name.substring(0,tail_len); /*takes a substring of  same length of tail[i] from 1st position to inner position of the codon of tail_amino */

         if(matching_tail.equals(tail))
          {
             flag=1;
             filter[i]=codon_name;
             i++;
         }
      }
  }
  if(flag==1)
  {
     s[k]=filter;
  }
  return(flag);
}

//= = =  = == = = = == =  == = = = == = = == = = = == = = >>

public void print_motif_info()
{
   codon.pout.print("************** ");
   codon.pout.println("Motif Calculation  in details *************");
   codon.pout.print(motif_seq);
   codon.pout.print("  ");
   codon.pout.println(desirability_value);

  codon.pout.print(sub_motif[0]);
  codon.pout.print("  ");
  codon.pout.print(aasp[0]);
  codon.pout.print("  ");
  codon.pout.print(head[0]);
  codon.pout.print("   ");
  codon.pout.println(tail[0]);
  
  codon.pout.print(sub_motif[1]);
  codon.pout.print("  ");
  codon.pout.print(aasp[1]);
  codon.pout.print("  ");
  codon.pout.print(head[1]);
  codon.pout.print("   ");
  codon.pout.println(tail[1]);


  codon.pout.print(sub_motif[2]);
  codon.pout.print("  ");
  codon.pout.print(aasp[2]);
  codon.pout.print("  ");
  codon.pout.print(head[2]);
  codon.pout.print("   ");
  codon.pout.println(tail[2]);
}

} //End of motif class

