// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadnie 3
// konwersja wyrazen z ONP do INF i odwrotnie
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

// stos stringow
class StringStack{
    private String[] stack;
    private int size;
    private int fill;

    public StringStack(int size){
        stack = new String[size];
        this.size = size;
        fill = 0;
    }

    // zakladam, ze nie przepelnie stosu
    public void push(String newElement){
        stack[fill] = newElement;
        fill++;
    }

    public String pop(){
        fill--;
        return stack[fill];
    }

    public String top(){
        return stack[fill - 1];
    }

    boolean empty(){
        return (fill == 0);
    }
}

// stos charow
class CharStack{
    private char[] stack;
    private int size;
    private int fill;

    public CharStack(int size){
        stack = new char[size];
        this.size = size;
        fill = 0;
    }

    // zakladam, ze nie przepelnie stosu
    public void push(char newElement){
        stack[fill] = newElement;
        fill++;
    }

    public char pop(){
        if(empty()) return '\0';
        fill--;
        return stack[fill];
    }

    public char top(){
        if(empty()) return 0;
        return stack[fill - 1];
    }

    boolean empty(){
        return (fill == 0);
    }
}

// stos intow
class IntStack{
    private int[] stack;
    private int size;
    private int fill;

    public IntStack(int size){
        stack = new int[size];
        this.size = size;
        fill = 0;
    }

    // zakladam, ze nie przepelnie stosu
    public void push(int newElement){
        stack[fill] = newElement;
        fill++;
    }

    public int pop(){
        if(empty()) return '\0';
        fill--;
        return stack[fill];
    }

    public int top(){
        if(empty()) return 0;
        return stack[fill - 1];
    }

    boolean empty(){
        return (fill == 0);
    }
}


// klasa do obslugi wyrazenia matematycznego
class Expression{
    public String expression;  // przechowuje wyrazeznie (bez niedozwolonych znakow) niekoniecznie poprawne
    private boolean isINF;  // true - wyraznienie jest INF, false - wyrazenie jest ONP

    private boolean isCorrect;  // true - wyrazenie jest poprawne (spelnia automat, warunek o nawiasach i operandach)

    //konstruktor: przyjmuje string (linia z wejscia) i ustawia wszystkie pola klasy
    public Expression(String expression){
        isINF = (expression.charAt(0) == 'I');
        this.expression = clearExpression(expression);
        if(isINF)
            isCorrect = expressionCorrectness();
    }

    // funkcja dotyczy tylko przypadku INF, dla ONP poprawnosc sprawdzana jest dopiero przy konwersji do INF
    public boolean isExpressionCorrect(){
        return isCorrect;
    }
    public boolean isExpressionINFtype() {return isINF;}
    public boolean isExpressionONPtype() {return !isINF;}

    //zwraca true jezeli znak jest dopuszczany w wyrazeniu matematycznym
    private boolean isSymbolAcceptable(char symbol){
        int symbolASCII = symbol;
        if(symbolASCII <= 122 && symbolASCII >= 97) return true;  // male litery
        if(isINF)
            if(symbol == '(' || symbol == ')') return true;
        if(symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '^') return true;
        if(symbol == '|' || symbol == '!' || symbol == '&' || symbol == '%' || symbol == '~') return true;
        if(symbolASCII <= 63 && symbolASCII >= 60) return true;  // < > = ?
        return false;
    }

    private boolean isVariable(char symbol){
        int symbolASCII = symbol;
        return (symbolASCII <= 122 && symbolASCII >= 97);
    }

    private boolean isTwoArgumentOperator(char symbol){
        int symbolASCII = symbol;
        if(symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/') return true;
        if(symbol == '|' || symbol == '^' || symbol == '&' || symbol == '%') return true;
        if(symbolASCII <= 63 && symbolASCII >= 60) return true;  // < > = ?
        return false;
    }

    private boolean isOneArgumentOperator(char symbol){
        int symbolASCII = symbol;
        return (symbol == '~' || symbol == '!');
    }

    private boolean isLeftBracket(char symbol){
        int symbolASCII = symbol;
        return (symbol == '(');
    }

    private boolean isRightBracket(char symbol){
        int symbolASCII = symbol;
        return (symbol == ')');
    }

    //przygotowuje wyrazenienie (pozbywa sie niechcianych znakow)
    private String clearExpression(String expression){
        String clearedExpr = "";
        for(int i = 0 ; i < expression.length() ; i++){
            char symbol = expression.charAt(i);
            if(isSymbolAcceptable(symbol))
                clearedExpr += symbol;
        }
        return clearedExpr;
    }

    // stan 0 automatu z zadania
    int state0(char symbol, int[] bracketCount){
        if(isLeftBracket(symbol)) {
            bracketCount[0] += 1;
            return 0;
        }
        if(isVariable(symbol)) return 1;
        if(isOneArgumentOperator(symbol)) return 2;
        return -1;
    }

    // stan 1 automatu z zadania
    int state1(char symbol, int[] bracketCount){
        if(isTwoArgumentOperator(symbol)) return 0;
        if(isRightBracket(symbol)) {
            bracketCount[0] -= 1;
            return 1;
        }
        return -1;
    }

    // stan 2 automatu z zadania
    int state2(char symbol, int[] bracketCount){
        if(isLeftBracket(symbol)) {
            bracketCount[0] += 1;
            return 0;
        }
        if(isVariable(symbol)) return 1;
        if(isOneArgumentOperator(symbol)) return 2;
        return -1;
    }

    // sprawdza poprawnosc wyrazenia (spelnienie automatu, warunku nawiasow i warunku dla operatorow i operandow)
    private boolean expressionCorrectness(){
        int state = 0;
        int[] bracketCount = new int[]{0};  //zmienna zliczajaca nawiasy, lewy nawias to +=1 prawy nawias to -=1
        int i = 0;
        while(i < expression.length()){
            if(bracketCount[0] < 0) return false;
            if(state == -1)
                return false;
            else if(state == 0)
                state = state0(expression.charAt(i), bracketCount);
            else if(state == 1)
                state = state1(expression.charAt(i), bracketCount);
            else
                state = state2(expression.charAt(i), bracketCount);
            i++;
        }
        return (state == 1 && bracketCount[0] == 0);
    }

    //zwraca priorytet operatora reprezentwany przez liczbe calkowita
    private int operatorPriority(char symbol){
        if(symbol == '|') return 1;
        if(symbol == '&') return 2;
        if(symbol == '?') return 3;
        if(symbol == '<' || symbol == '>') return 4;
        if(symbol == '+' || symbol == '-') return 5;
        if(symbol == '*' || symbol == '/' || symbol == '%') return 6;
        if(symbol == '^') return 7;
        if(symbol == '!' || symbol == '~') return 8;
        //if(symbol == '(' || symbol == ')') return 9;
        return -1;
    }

    // true - operator prawostronnie laczny
    private boolean rhsAssociavity(char symbol){
        if(symbol == '=' || symbol == '^' || symbol == '!' || symbol == '~') return true;
        return false;
    }

    //funkcja zmienia notacje z INP na ONP i zwraca string z wyrazeniem w zmienionej notajcji
    private String convertToONP(){
        CharStack stack = new CharStack(expression.length());
        String ONPexpression = "";  //string do przechowywania wyrazenie w notacji ONP
        int i = 0;
        //petla po elementach wyrazenia
        while(i < expression.length()){
            //jezeli element jest zmienna to jest wpisany do wyrazenia
            if(isVariable(expression.charAt(i))) {
                ONPexpression += " ";
                ONPexpression += expression.charAt(i);
            }
            //jezeli element jest lewym nawiasem to trafia na stos
            else if(isLeftBracket(expression.charAt(i)))
                stack.push(expression.charAt(i));
            //jezeli prawy nawias, to sciagamy ze stosu wszystko do lewego nawiasu (razem z nawiasem)
            else if(isRightBracket(expression.charAt(i))){
                char top = stack.pop();
                while(top != '('){
                    ONPexpression += " ";
                    ONPexpression += top;
                    top = stack.pop();
                }
            }
            //element jest operatorem
            else{
                int operatorPiority = operatorPriority(expression.charAt(i));  //priorytet operatora
                boolean isOperatorRhsA = rhsAssociavity(expression.charAt(i));  //czy operator jest prawostronnie laczny
                //jezeli prawostronnie laczny
                if(isOperatorRhsA){
                    //sciagamy wszystkie operatory o wiekszym priorytecie
                    while(!stack.empty() && operatorPiority < operatorPriority(stack.top())) {
                        ONPexpression += " ";
                        ONPexpression += stack.pop();
                    }
                }
                //jezeli lewostronnie laczny
                else{
                    //sciagamy wszystkie operatory o wiekszym/rownym priorytecie
                    while(!stack.empty() && operatorPiority <= operatorPriority(stack.top())) {
                        ONPexpression += " ";
                        ONPexpression += stack.pop();
                    }
                }
                //po sciagnieciu operatorow o wiekszym lub wiekszym/rownym priorytecie, wpychamy na stos aktualny operator
                stack.push(expression.charAt(i));
            }
            i++;
        }
        //sciagamy ze stousu wszystkie pozostale elementy
        while(!stack.empty()) {
            ONPexpression += " ";
            ONPexpression += stack.pop();
        }
        //zwracamy wyrazenie w notacji onp ze spacja przed i spacjami pomiedzy elementami
        return ONPexpression;
    }

    //funkcja wyswietlajaca wyrazenie w notacji ONP
    public void expressionInONP(){
        System.out.println("ONP:" + convertToONP());
    }

    private String convertToINF(){
        String INFexpression;
        StringStack stack = new StringStack(expression.length());  //stos dla skladniaki wyrazenia INF
        IntStack priorityStack = new IntStack(expression.length());  //stos dla priorytetow skladnikow wyrazenia
        int i = 0;
        //petla po elementach wyrazenia
        while(i < expression.length()){
            INFexpression = "";
            //jezeli element jest zmienna to trafia na stos i jej priorytet trafia na stos priorytetow
            if(isVariable(expression.charAt(i))){
                INFexpression = INFexpression + " " + expression.charAt(i);
                stack.push(INFexpression);
                //dla zmiennych przyjmuje najwyzszy priorytet
                priorityStack.push(9);
            }
            // operatory jednoargumentowe, prawostronnie laczne
            else if(expression.charAt(i) == '!' || expression.charAt(i) == '~'){
                //jak braknie zmiennych na stosie, zwaracam error (wyrazenie niepoprawne)
                if(stack.empty()) return "INF: error";
                int topPriority = priorityStack.pop(); //priorytet argumentu ze szczytu stosu
                INFexpression = stack.pop();  //argument na szczycie stosu
                //jezeli priorytet argumentu mniejszy od priorytetu operatora dodajemy nawias i wpychamy wynik na stos
                if(topPriority < 8)
                    stack.push(" " + expression.charAt(i) + " (" + INFexpression + " )");
                else
                    stack.push(" " + expression.charAt(i) + INFexpression);
                //wpychamy priorytet powyzszego wyniku na stos priorytetow (czyli priorytet operatora)
                priorityStack.push(8);

            }
            // operatory dwuargumentowe, prawostronnie laczne tzn: a # b # c = a # (b # c), gdzie # to dzialanie
            // to znaczy, ze a # b # c != (a # b) # c => z lewej strony trzeba dodawac nawias, gdy priorytet argumentu
            // jest nizszy/rowny od priprytetu operatora, a z prawej tylko gdy jest nizszy
            else if(expression.charAt(i) == '=' || expression.charAt(i) == '^'){
                if(stack.empty()) return "INF: error";
                String rhsArg = stack.pop();  //prawostronny argument
                int rhsArgPrio = priorityStack.pop();  //priorytet prawostronnego argumentu
                if(stack.empty()) return  "INF: error";
                String lhsArg = stack.pop();  //lewostronny argument
                int lhsArgPrio = priorityStack.pop();  //priorytet lewostronnego arg
                int opPrio = operatorPriority(expression.charAt(i)); //priorytet operatora
                //jezeli priorytet lewego argumentu jest nizszy/rowny od priorytetu oparatora: argument trafia w nawias
                if(lhsArgPrio <= opPrio)
                    INFexpression = INFexpression + " (" + lhsArg + " ) " + expression.charAt(i);
                else
                    INFexpression = INFexpression + lhsArg + " " + expression.charAt(i);
                //jezeli priorytet prawego argumentu jest nizszy od priorytetu oparatora: argument trafia w nawias
                if(rhsArgPrio < opPrio)
                    INFexpression = INFexpression + " (" + rhsArg + " )";
                else
                    INFexpression = INFexpression + rhsArg;
                // wpychamy dzialanie na stos jako nowy argument
                stack.push(INFexpression);
                // wpychamy priorytet nowego argumentu (dzialania) na stos, rowny prioryetowi operatora dzialania
                priorityStack.push(opPrio);
            }
            // operatory dwuargumentowe, lewostronnie laczne tzn: a # b # c = (a # b) # c, gdzie # to dzialanie
            // to znaczy, ze a # b # c != a # (b # c)
            // analogicznie do prawostronnych tylko '<' wymienia sie z '<='
            else{
                if(stack.empty()) return "INF: error";
                String rhsArg = stack.pop();
                int rhsArgPrio = priorityStack.pop();
                if(stack.empty()) return  "INF: error";
                String lhsArg = stack.pop();
                int lhsArgPrio = priorityStack.pop();
                int opPrio = operatorPriority(expression.charAt(i));
                //jak wyzej, tylko zamiast <= jest < ze wzgledu na lacznosc
                if(lhsArgPrio < opPrio)
                    INFexpression = INFexpression + " (" + lhsArg + " ) " + expression.charAt(i);
                else
                    INFexpression = INFexpression + lhsArg + " " + expression.charAt(i);
                //jak wyzej, tylko zamiast < jest <= ze wzgledu na lacznosc
                if(rhsArgPrio <= opPrio)
                    INFexpression = INFexpression + " (" + rhsArg + " ) ";
                else
                    INFexpression = INFexpression + rhsArg;
                stack.push(INFexpression);
                priorityStack.push(opPrio);
            }
            i++;
        }
        INFexpression = stack.pop();
        if(stack.empty())
            return "INF:" + INFexpression;
        else
            return "INF: error";  // jak stos nie jest pusty, to znaczy ze wyraznie nie bylo poprawne
    }

    //funkcja wyswietlajaca wyrazenie w notacji INF
    public void expressionInINF(){
        System.out.println(convertToINF());
    }

}

public class Source{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());
        while(n > 0){ //oddziel przypadki ONP od INF
            String line = sc.nextLine();
            Expression exp = new Expression(line);
            if(exp.isExpressionINFtype()) {
                if (exp.isExpressionCorrect()) {
                    exp.expressionInONP();
                } else {
                    System.out.println("ONP: error");
                }
            }
            else{
                exp.expressionInINF();
            }
            n--;
        }
    }
}

//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
22
ONP: !
ONP: nmm-
ONP: g e ***
ONP: b n * n m *
ONP: ()(( m ~ m ())(
ONP: f ,..,.331())()(
ONP: b e c ! * &
ONP: a n + c g - * t ^ v /
ONP: f h j b ^ ^ ^ d * k - j -
ONP: a ~ ~ ~ ~ ~ ~ ~ ~
ONP: a ~ ~ b b d - / f c * n - g ^ * t % + q =
INF: ()
INF: a + b + c (
INF: f * v * t *
INF: g* t* ) f* h
INF: a+b()()+c*d^j 4
INF: f ,.,.,,. .,
INF: b & e * ! c
INF: ( ( a + n ) * ( c - g )  ) ^ t / v
INF: f ^ h ^ j ^ b * d - k - j
INF: ~ ~ ~ ~ ~ ~ ~ ~ a
INF: ~ ~ a + b / ( b - d )  * ( f * c - n ) ^ g % t = q
*/
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
INF: error
INF: error
INF: error
INF: error
INF: error
INF: f
INF: b & e * ! c
INF: ( ( a + n ) * ( c - g )  ) ^ t / v
INF: f ^ h ^ j ^ b * d - k - j
INF: ~ ~ ~ ~ ~ ~ ~ ~ a
INF: ~ ~ a + b / ( b - d )  * ( f * c - n ) ^ g % t = q
ONP: error
ONP: error
ONP: error
ONP: error
ONP: error
ONP: f
ONP: b e c ! * &
ONP: a n + c g - * t ^ v /
ONP: f h j b ^ ^ ^ d * k - j -
ONP: a ~ ~ ~ ~ ~ ~ ~ ~
ONP: a ~ ~ b b d - / f c * n - g ^ * t % + q =
 */
//--------------------------------------------------------------------------------------------------------------------//