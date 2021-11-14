import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class grammar {
    static int pointer = -1;
    static int size;
    static ArrayList<String> output = new ArrayList<>();
    static int line = 0;

    static void CompUnit(String[] buff) {
        pointer++;
        isLineFeed(buff);
        FuncDef(buff);
    }

    static void FuncDef(String[] buff) {
        if (pointer <= size - 1) {
            FuncType(buff);
        } else {
            System.out.println("funcdef pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            Ident(buff);
        } else {
            System.out.println("funcdef pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            if (!buff[pointer].equals("(")) {
                System.out.println("(");
                err();
            } else {
                output.add("(");
                pointer++;
            }
        } else {
            System.out.println("funcdef pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            if (!buff[pointer].equals(")")) {
                System.out.println(")");
                err();
            } else {
                output.add(")");
                pointer++;
            }
        } else {
            System.out.println("funcdef pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            Block(buff);
            isLineFeed(buff);
            if (pointer != size) {
                System.out.println("funcdef1 pointer" + (size - 1) + " " + pointer);
                err();
            }
        } else {
            System.out.println("funcdef2 pointer");
            err();
        }
    }

    static void FuncType(String[] buff) {
        isLineFeed(buff);
        if (!buff[pointer].equals("int")) {
            System.out.println("int");
            err();
        } else {
            output.add("define dso_local i32");
            pointer++;
        }
    }

    static void Ident(String[] buff) {
        isLineFeed(buff);
        if (!buff[pointer].equals("main")) {
            System.out.println("main");
            err();
        } else {
            output.add("@main");
            pointer++;
        }
    }

    static void Block(String[] buff) {
        isLineFeed(buff);
        if (!buff[pointer].equals("{")) {
            System.out.println("{");
            err();
        } else {
            output.add("{");
            pointer++;
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            Stmt(buff);
        } else {
            System.out.println("block pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            if (!buff[pointer].equals("}")) {
                System.out.println("}");
                err();
            } else {
                output.add("}");
                pointer++;
            }
        } else {
            System.out.println("block pointer");
            err();
        }
        isLineFeed(buff);
    }

    static void Stmt(String[] buff) {
        isLineFeed(buff);
        if (pointer <= size - 1) {
            if (!buff[pointer].equals("return")) {
                System.out.println("return");
                err();
            } else {
                output.add("ret i32 ");
                pointer++;
            }
        } else {
            System.out.println("stmt pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            digit(buff);
        } else {
            System.out.println("stmt pointer");
            err();
        }
        isLineFeed(buff);
        if (pointer <= size - 1) {
            if (!buff[pointer].equals(";")) {
                System.out.println(";");
                err();
            } else {        // needn't add
                pointer++;
            }
        }
        isLineFeed(buff);
    }

    static void digit(String[] buff) {
        int number;
        if (syntax.decimal_const(buff[pointer]) != -1) {
            number = syntax.decimal_const(buff[pointer]);
            output.add(String.valueOf(number));
            pointer++;
            return;
        }
        if (syntax.octal_const(buff[pointer]) != -1) {
            number = syntax.octal_const(buff[pointer]);
            output.add(String.valueOf(number));
            pointer++;
            return;
        }
        if (syntax.hex_const(buff[pointer]) != -1) {
            number = syntax.hex_const(buff[pointer]);
            output.add(String.valueOf(number));
            pointer++;
            return;
        }
        System.out.println("digit");
        err();
    }

    static void isLineFeed(String[] buff) {
        if (pointer <= size - 1) {
            while (buff[pointer].equals("linefeed")) {
                output.add("");
                pointer++;
                if (pointer > size - 1) {
                    break;
                }
            }
        }
    }

    static void err() {
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        syntax analysis = new syntax();
        analysis.init();
        PrintStream ps = new PrintStream("./log.txt");
        System.setOut(ps);
        if (args.length != 0) {
            Scanner scanner = new Scanner(new File(args[0]));
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
                analysis.token[++analysis.pointer] = "linefeed";
                line++;
            }
        } else {
            Scanner in = new Scanner(System.in);
            String path = in.nextLine();
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
                analysis.token[++analysis.pointer] = "linefeed";
                line++;
            }
        }
        size = analysis.pointer + 1;
//        System.out.println(Arrays.toString(analysis.token));
        CompUnit(analysis.token);
        for(String tem :output) {
            if(tem.equals("")) {
                System.out.println();
            }
            else {
                System.out.print(tem);
            }
        }
    }
}
