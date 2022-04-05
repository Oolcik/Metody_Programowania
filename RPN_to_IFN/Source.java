// Aleksandra Wnuk
// stos i zmiana notacji z ONP i INF
import java.util.Scanner;

class Pair{
    public String str;
    public int num;
    public Pair(String str, int num){
        this.str = str;
        this.num = num;
    }
}

class Stack{
    private Pair[] stack;
    private int size;
    private int topIdx;

    public Stack(int size){
        this.size = size;
        stack = new Pair[size];
        topIdx = -1;
    }

    boolean isEmpty(){
        return (topIdx == -1);
    }

    public Pair top(){
        if(isEmpty()){
            System.out.println("Stack is empty");
            return null;
        }
        return stack[topIdx];
    }

    public Pair pop(){
        if(isEmpty()){
            System.out.println("Stack is empty");
            return null;
        }
        topIdx -= 1;
        return stack[topIdx + 1];
    }

    public void push(Pair newElem){
        if(topIdx == size - 1){
            System.out.println("Stack is full");
            return;
        }
        topIdx += 1;
        stack[topIdx] = newElem;
    }

    public void printStack(){
        for(int i = 0 ; i <= topIdx ; i++){
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }
}



public class Source {

    public static boolean isLetterOrDigit(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c >= '0' && c <= '9');
    }

    public static String toINFfromONP(String equation){
        equation += " ";
        Stack stack = new Stack(20);
        Pair elem;
        String str;
        String INF = "";
        for(int i = 0 ; i < equation.length() ; i++){
            str = "";
            while(equation.charAt(i) != ' '){
                str += equation.charAt(i);
                i++;
            }
            if(isLetterOrDigit(str.charAt(0))){
                Pair tmp = new Pair(str, 0);
                stack.push(tmp);
            } else {
                if(str.charAt(0) == '-'){
                    Pair tmppair2 = stack.pop();
                    Pair tmppair1 = stack.pop();
                    String tmpstr = "";
                    tmpstr = tmpstr + tmppair1.str + "-" + tmppair2.str;
                    tmppair1.str = tmpstr;
                    tmppair1.num = 1;
                    stack.push(tmppair1);

                } else if(str.charAt(0) == '+'){
                    Pair tmppair2 = stack.pop();
                    Pair tmppair1 = stack.pop();
                    String tmpstr = "";
                    tmpstr = tmpstr + tmppair1.str + "+" + tmppair2.str;
                    tmppair1.str = tmpstr;
                    tmppair1.num = 1;
                    stack.push(tmppair1);

                } else if(str.charAt(0) == '~'){
                    Pair tmppair = stack.pop();
                    String tmpstr = "";
                    if(tmppair.num == 1)
                        tmpstr = tmpstr + "(" + "~" + "(" + tmppair.str + ")" + ")";
                    else
                        tmpstr = tmpstr + "(" + "~" + tmppair.str + ")";
                    tmppair.str = tmpstr;
                    tmppair.num = 0;
                    stack.push(tmppair);

                } else if(str.charAt(0) == '/'){
                    Pair tmppair2 = stack.pop();
                    Pair tmppair1 = stack.pop();
                    String tmpstr = "";
                    if(tmppair1.num == 1)
                        tmpstr += "(" + tmppair1.str + ")";
                    else
                        tmpstr += tmppair1.str;
                    tmpstr += "/";
                    if(tmppair2.num == 1 || tmppair2.num == 2 || tmppair2.num == 3)
                        tmpstr = tmpstr + "(" + tmppair2.str + ")";
                    else
                        tmpstr = tmpstr + tmppair2.str;
                    tmppair1.str = tmpstr;
                    tmppair1.num = 2;
                    stack.push(tmppair1);

                }else if(str.charAt(0) == '*'){
                    Pair tmppair2 = stack.pop();
                    Pair tmppair1 = stack.pop();
                    String tmpstr = "";
                    if(tmppair1.num == 1)
                        tmpstr += "(" + tmppair1.str + ")";
                    else
                        tmpstr += tmppair1.str;
                    tmpstr += "*";
                    if(tmppair2.num == 1 || tmppair2.num == 2)
                        tmpstr = tmpstr + "(" + tmppair2.str + ")";
                    else
                        tmpstr = tmpstr + tmppair2.str;
                    tmppair1.str = tmpstr;
                    tmppair1.num = 3;
                    stack.push(tmppair1);
                } else {
                    Pair tmppair2 = stack.pop();
                    Pair tmppair1 = stack.pop();
                    String tmpstr = "";
                    if(tmppair1.num == 0)
                        tmpstr = tmpstr + tmppair1.str;
                    else
                        tmpstr = tmpstr + "(" + tmppair1.str + ")";
                    tmpstr += "^";
                    if(tmppair2.num == 0)
                        tmpstr = tmpstr + tmppair2.str;
                    else
                        tmpstr = tmpstr + "(" + tmppair2.str + ")";
                    tmppair1.str = tmpstr;
                    tmppair1.num = 4;
                    stack.push(tmppair1);
                }
            }
        }
        Pair tmp = stack.pop();
        return tmp.str;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        System.out.println("Podaj wyraznie w notacji ONP,");
        System.out.println("oddzielaj spacjami wartosci od symboli dzialan,");
        System.out.println("dostępne działania: -, +, ~, *, /, ^.");
        str = sc.nextLine();
        System.out.println("Twoje wyrazenie w notacji infiksowej to:");
        String tmp = toINFfromONP(str);
        System.out.println(tmp);
        System.out.println("Dodatkowe przykłady");
        str = "2 7 + 3 / 14 3 - 4 * + 2 /";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b * c d * *";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b * c d / *";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b ^ c ^";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b c ^ ^";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b + c d - * d /";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a ~";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));
        str = "a b * ~ c + ~ 7 ^";
        System.out.println(str);
        System.out.println("> " + toINFfromONP(str));

    }

}
