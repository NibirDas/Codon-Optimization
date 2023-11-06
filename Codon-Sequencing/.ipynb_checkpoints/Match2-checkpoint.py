class Substitution:
    def __init__(self):
        self.score = []

    def build_score(self, residues, residuescores):
        self.score = [[0 for _ in range(127)] for _ in range(127)]
        for i in range(len(residues)):
            res1 = residues[i]
            for j in range(i + 1):
                res2 = residues[j]
                self.score[ord(res1)][ord(res2)] = self.score[ord(res2)][ord(res1)] \
                                                 = self.score[ord(res1)][ord(res2) + 32] \
                                                 = self.score[ord(res2) + 32][ord(res1)] \
                                                 = self.score[ord(res1) + 32][ord(res2)] \
                                                 = self.score[ord(res2)][ord(res1) + 32] \
                                                 = self.score[ord(res1) + 32][ord(res2) + 32] \
                                                 = self.score[ord(res2) + 32][ord(res1) + 32] \
                                                 = residuescores[i][j]

    def get_residues(self):
        pass

class Blosum50(Substitution):
    
    def __init__():
        residuescores = [
            # A  R   N   D   C   Q   E   G   H   I   L   K   M   F   P   S   T   W   Y   V
            [  5],
            [-2, 7],
            [-1,-1, 7],
            [-2,-2, 2, 8],
            [-1,-4,-2,-4,13],
            [-1, 1, 0, 0,-3, 7],
            [-1, 0, 0, 2,-3, 2, 6],
            [  0,-3, 0,-1,-3,-2,-3, 8],
            [-2, 0, 1,-1,-3, 1, 0,-2,10],
            [-1,-4,-3,-4,-2,-3,-4,-4,-4, 5],
            [-2,-3,-4,-4,-2,-2,-3,-4,-3, 2, 5],
            [-1, 3, 0,-1,-3, 2, 1,-2, 0,-3,-3, 6],
            [-1,-2,-2,-4,-2, 0,-2,-3,-1, 2, 3,-2, 7],
            [-3,-3,-4,-5,-2,-4,-3,-4,-1, 0, 1,-4, 0, 8],
            [-1,-3,-2,-1,-4,-1,-1,-2,-2,-3,-4,-1,-3,-4,10],
            [  1,-1, 1, 0,-1, 0,-1, 0,-1,-3,-3, 0,-2,-3,-1, 5],
            [  0,-1, 0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1, 2, 5],
            [-3,-3,-4,-5,-5,-1,-3,-3,-3,-3,-2,-3,-1, 1,-4,-4,-3,15],
            [-2,-1,-2,-3,-3,-1,-2,-3, 2,-1,-1,-2, 0, 4,-3,-2,-2, 2, 8],
            [  0,-3,-3,-4,-1,-3,-3,-4,-4, 4, 1,-3, 1,-1,-3,-2, 0,-3,-1, 5]
        ]
        residues = "ARNDCQEGHILKMFPSTWYV"
        super().build_score(residues, residuescores)
    
    def get_residues(self):
        return self.residues

class Align:
    NegInf = float("-inf")

    def __init__(self, sub, d, seq1, seq2):
        self.sub = sub
        self.seq1 = self.strip(seq1)
        self.seq2 = self.strip(seq2)
        self.d = d
        self.n = len(self.seq1)
        self.m = len(self.seq2)
        self.B0 = None

    def strip(self, s):
        valid = [False] * 127
        residues = self.sub.get_residues()
        for i in range(len(residues)):
            c = residues[i]
            if ord(c) < 96:
                valid[ord(c)] = valid[ord(c) + 32] = True
            else:
                valid[ord(c) - 32] = valid[ord(c)] = True
        res = []
        for i in range(len(s)):
            if valid[ord(s[i])]:
                res.append(s[i])
        return ''.join(res)

    def get_match(self):
        res1 = []
        res2 = []
        tb = self.B0
        i = tb.i
        j = tb.j
        while tb is not None:
            if i == tb.i:
                res1.append('-')
            else:
                res1.append(self.seq1[i - 1])
            if j == tb.j:
                res2.append('-')
            else:
                res2.append(self.seq2[j - 1])
            i = tb.i
            j = tb.j
            tb = self.next(tb)
        return [''.join(res1[::-1]), ''.join(res2[::-1])]

    def fmt_score(self, val):
        if val < self.NegInf / 2:
            return "-Inf"
        else:
            return str(val)

    def do_match(self, out, msg, udskrivF=True):
        out.println(msg + ":")
        out.println("Score = " + str(self.get_score()))
        if udskrivF:
            out.println("The F matrix:")
            self.printf(out)
        out.println("An optimal alignment:")
        match = self.get_match()
        out.println(match[0])
        out.println(match[1])
        out.println()

    def next(self, tb):
        return tb

    def get_score(self):
        pass

    def printf(self, out):
        pass

    @staticmethod
    def max(x1, x2):
        return max(x1, x2)

    @staticmethod
    def max_2(x1, x2, x3):
        return max(x1, max(x2, x3))

    @staticmethod
    def max_3(x1, x2, x3, x4):
        return max(Align.max(x1, x2), Align.max(x3, x4))

    @staticmethod
    def pad_left(s, width):
        filler = width - len(s)
        if filler > 0:
            return ' ' * filler + s
        else:
            return s

class AlignSimple(Align):
    def __init__(self, sub, d, seq1, seq2):
        super().___init___(sub, d, seq1, seq2)
        self.F = [[0] * (self.m + 1) for _ in range(self.n + 1)]
        self.B = [[None] * (self.m + 1) for _ in range(self.n + 1)]

    def next(self, tb):
        tb2 = tb
        return self.B[tb2.i][tb2.j]

    def get_score(self):
        return self.F[self.B0.i][self.B0.j]

    def printf(self, out):
        for j in range(self.m + 1):
            for i in range(len(self.F)):
                out.print(Align.pad_left(self.fmt_score(self.F[i][j]), 5))
            out.println()

class Traceback:
    def __init__(self):
        self.i = 0
        self.j = 0

class Traceback2(Traceback):
    def __init__(self, i, j):
        super().___init___()
        self.i = i
        self.j = j

class Output:
    def print(self, s):
        pass

    def println(self, s):
        pass

    def println(self):
        pass

class SystemOut(Output):
    def print(self, s):
        print(s, end='')

    def println(self, s):
        print(s)

    def println(self):
        print()

class NW(AlignSimple):
    def __init__(self, sub, d, seq1, seq2):
        super().___init___(sub, d, seq1, seq2)
        score = sub.score
        for i in range(1, self.n + 1):
            self.F[i][0] = -d * i
            self.B[i][0] = Traceback2(i - 1, 0)
        for j in range(1, self.m + 1):
            self.F[0][j] = -d * j
            self.B[0][j] = Traceback2(0, j - 1)
        for i in range(1, self.n + 1):
            for j in range(1, self.m + 1):
                s = score[ord(self.seq1[i - 1])][ord(self.seq2[j - 1])]
                val = Align.max_3(self.F[i - 1][j - 1] + s, self.F[i - 1][j] - d, self.F[i][j - 1] - d)
                self.F[i][j] = val
                if val == self.F[i - 1][j - 1] + s:
                    self.B[i][j] = Traceback2(i - 1, j - 1)
                elif val == self.F[i - 1][j] - d:
                    self.B[i][j] = Traceback2(i - 1, j)
                elif val == self.F[i][j - 1] - d:
                    self.B[i][j] = Traceback2(i, j - 1)
                else:
                    raise Exception("NW 1")
        self.B0 = Traceback2(self.n, self.m)

class Match2:
    @staticmethod
    def main(args):
        out = SystemOut()
        seq1 = args[0]
        seq2 = args[1]

        sub = Blosum50()
        nw = NW(sub, 8, seq1, seq2)
        nw.do_match(out, "GLOBAL ALIGNMENT")

if __name__ == "__main__":
    Match2.main(["HEAGAWGHEE", "PAWHEAE"])