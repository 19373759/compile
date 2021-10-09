import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class analysis {
    Map<String, String> map = new HashMap<>();
    String[] keyword = {"if", "else", "while", "break", "continue", "return"};
    String[] symbol = {"=", ";", "(", ")", "{", "}", "+", "*", "/", "<", ">", "=="};

    void init() {
        map.put("=", "Assign");
        map.put(";", "Semicolon");
        map.put("(", "LPar");
        map.put(")", "RPar");
        map.put("{", "LBrace");
        map.put("}", "RBrace");
        map.put("+", "Plus");
        map.put("*", "Mult");
        map.put("/", "Div");
        map.put("<", "Lt");
        map.put(">", "Gt");
        map.put("==", "Eq");
        map.put("if","If");
        map.put("else","Else");
        map.put("while","While");
        map.put("break","Break");
        map.put("continue","Continue");
        map.put("return","Return");
    }

    void spl(String sentence) {
        String[] string = sentence.split("\\s+");
        for (String tem : string) {
            if(tem.length()==0) {
                continue;
            }
            if (isDigit(tem) != 0) {
                int index = isDigit(tem);
                String number = tem.substring(0, index);
                System.out.println("Number(" + number + ")");
                if (index < tem.length()) {
                    spl(tem.substring(index));
                }
                continue;
            }
            if (isIdentifier(tem) != 0) {
                int index = isIdentifier(tem);
                String identifier = tem.substring(0, index);
                if(isKeyword(identifier).length()==0) {
                    System.out.println("Ident(" + identifier + ")");
                } else {
                    System.out.println(map.get(identifier));
                }
                if (index < tem.length()) {
                    spl(tem.substring(index));
                }
                continue;
            }
            if (isSymbol(tem).length() > 0) {
                String symbol = isSymbol(tem);
                System.out.println(map.get(symbol));
                if(symbol.equals("==")) {
                    spl(tem.substring(2));
                } else {
                   spl(tem.substring(1));
                }
                continue;
            }
            System.out.println("Err");
            System.exit(0);
        }
    }

    int isDigit(String string) {
        int len = string.length();
        if (string.charAt(0) < '0' || string.charAt(0) > '9')
            return 0;
        for (int i = 0; i < len; i++) {
            char tem = string.charAt(i);
            if (tem < '0' || tem > '9')
                return i;
            if (i == len - 1) {
                return i + 1;
            }
        }
        return 0;
    }

    int isIdentifier(String string) {
        int len = string.length();
        char a = string.charAt(0);
        if (!((a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z') || a == '_' || (a >= '0' && a <= '9'))) {
            return 0;
        }
        for (int i = 0; i < len; i++) {
            char tem = string.charAt(i);
            if ((tem >= 'a' && tem <= 'z') || (tem >= 'A' && tem <= 'Z') || tem == '_' || (tem >= '0' && tem <= '9')) {
                if (i == len - 1) {
                    return i+1;
                }
            } else {
                return i;
            }
        }
        return 0;
    }

    String isKeyword(String string) {
        for (String tem : keyword) {
            if (tem.equals(string)) {
                return string;
            }
        }
        return "";
    }

    String isSymbol(String string) {
        int len = string.length();
        for (int i = 0; i < len; i++) {
            for (String tem : symbol) {
                String com = String.valueOf(string.charAt(i));
                if (tem.equals(com)) {
                    if (!tem.equals("=")) {
                        return tem;
                    } else {
                        if (i == len - 1 || string.charAt(i + 1) != '=') {
                            return "=";
                        } else {
                            return "==";
                        }
                    }
                }
            }
        }
        return "";
    }

    public static void main(String[] args) throws IOException {
        analysis analysis = new analysis();
        analysis.init();
        if(args.length!=0) {
            Scanner scanner = new Scanner(new File(args[0]));
            while(scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
            }
        }
        else {
            Scanner in = new Scanner(System.in);
            String path = in.nextLine();
            Scanner scanner = new Scanner(new File(path));
            while(scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
            }
        }
    }
}
