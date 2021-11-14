import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class syntax {
    Map<String, String> map = new HashMap<>();
    String[] token = new String[1000];
    int pointer = -1;
    String[] keyword = {"if", "else", "while", "break", "continue", "return"};
    String[] symbol = {"=", ";", "(", ")", "{", "}", "+", "*", "/", "<", ">", "=="};
    static Map<Character, Integer> dex = new HashMap<>();
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

        dex.put('0', 0);
        dex.put('1', 1);
        dex.put('2', 2);
        dex.put('3', 3);
        dex.put('4', 4);
        dex.put('5', 5);
        dex.put('6', 6);
        dex.put('7', 7);
        dex.put('8', 8);
        dex.put('9', 9);
        dex.put('a', 10);
        dex.put('A', 10);
        dex.put('b', 11);
        dex.put('B', 11);
        dex.put('c', 12);
        dex.put('C', 12);
        dex.put('d', 13);
        dex.put('D', 13);
        dex.put('e', 14);
        dex.put('E', 14);
        dex.put('f', 15);
        dex.put('F', 15);
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
                String pr = "Number(" + number + ")";
//                System.out.println(pr);
                token[++pointer] = number;
                if (index < tem.length()) {
                    spl(tem.substring(index));
                }
                continue;
            }
            if (isIdentifier(tem) != 0) {
                int index = isIdentifier(tem);
                String identifier = tem.substring(0, index);
                if(isKeyword(identifier).length()==0) {
                    String pr = "Ident(" + identifier + ")";
//                    System.out.println(pr);
                    token[++pointer] = identifier;
                } else {
//                    System.out.println(map.get(identifier));
                    token[++pointer] = identifier;
                }
                if (index < tem.length()) {
                    spl(tem.substring(index));
                }
                continue;
            }
            if (isSymbol(tem).length() > 0) {
                String symbol = isSymbol(tem);
//                System.out.println(map.get(symbol));
                token[++pointer] = symbol;
                if(symbol.equals("==")) {
                    spl(tem.substring(2));
                } else {
                    spl(tem.substring(1));
                }
                continue;
            }
//            System.out.println("Err");
            System.exit(0);
        }
    }

    int isDigit(String string) {
        if(decimal_const(string)!=-1) {
            return string.length();
        }
        if(octal_const(string)!=-1) {
            return string.length();
        }
        if(hex_const(string)!=-1) {
            return string.length();
        }
        return 0;
    }

    static int decimal_const(String string) {
        int len = string.length();
        if (string.charAt(0) <= '0' || string.charAt(0) > '9')
            return -1;
        for (int i = 0; i < len; i++) {
            char tem = string.charAt(i);
            if (tem < '0' || tem > '9')
                return -1;                  //careful
            if (i == len - 1) {
                return Integer.parseInt(string);
            }
        }
        return Integer.parseInt(string);
    }

    static int octal_const(String string) {
        int len = string.length();
        int sum = 0;
        if (string.charAt(0) != '0')
            return -1;
        for (int i = 0; i < len; i++) {
            char tem = string.charAt(i);
            if (tem < '0' || tem > '7')
                return -1;
            sum += (tem - '0')*Math.pow(8, i);
            if (i == len - 1) {
                return sum;
            }
        }
        return sum;
    }

    static int hex_const(String string) {
        int len = string.length();
        int sum = 0;
        if (string.charAt(0) != '0')
            return -1;
        if(string.length()>=2) {
            if(string.charAt(1)!='x' && string.charAt(1)!='X')
                return -1;
        } else return  -1;
        for (int i = 2; i < len; i++) {
            char tem = string.charAt(i);
            if (!dex.containsKey(tem))
                return -1;
            sum += dex.get((tem))*Math.pow(16, i);
            if (i == len - 1) {
                return sum;
            }
        }
        return sum;
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
        syntax analysis = new syntax();
        analysis.init();
        if(args.length!=0) {
            Scanner scanner = new Scanner(new File(args[0]));
            while(scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
                analysis.token[++analysis.pointer] = "linefeed";
            }
        }
        else {
            Scanner in = new Scanner(System.in);
            String path = in.nextLine();
            Scanner scanner = new Scanner(new File(path));
            while(scanner.hasNextLine()) {
                String s = scanner.nextLine();
                analysis.spl(s);
                analysis.token[++analysis.pointer] = "linefeed";
            }
        }
        System.out.println(Arrays.toString(analysis.token));
        System.out.println(analysis.pointer);
    }
}
