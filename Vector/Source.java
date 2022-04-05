import java.util.Scanner;

class Vector {

    private int[] arr;
    private Boolean isOrdered = false;
    private int elementCount = 0;


    public Vector(int length){
        arr = new int[length];
    }

    public void display(){
        if(elementCount == 0){
            System.out.println("elementCount = 0: array is empty");
            return;
        }
        for (int i = 0; i < elementCount - 1; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.print(arr[elementCount - 1]);
        System.out.println();
    }

    //jak tablica jest zapełniona to wyswietla komunikat
    public void insert(int element){
        if(elementCount >= arr.length) {
            System.out.println("Array is full, you can not insert more elements");
        }
        else if(!isOrdered) {
            arr[elementCount] = element;
            elementCount++;
        } else {
            int index = binarySearch(element);
            for(int i = elementCount ; i > index ; i--){
                arr[i] = arr[i - 1];
            }
            arr[index] = element;
            elementCount++;
        }
    }

    // usuwanie na indeksie
    public void delete(int index){
        if(index >= elementCount || index < 0) {
            System.out.println("There is no such index avaliable, check 'display'.");
            return;
        }
        elementCount--;
        for(int i = index ; i < elementCount ; i++){
            arr[i] = arr[i + 1];
        }
    }

    // jak nie ma elementu to wyswietla indeks po którym by się znalazł
    public int find( int element) {
        if(!isOrdered) {
            for(int i = 0 ; i < elementCount ; i++){
                if(arr[i] == element) return i;
            }
            return elementCount;
        } else {
            return binarySearch(element);
        }
    }

    // sortowanie, które sortojue i ustawia isOrdered na true;
    // wypisywanie (system.out.print etc)

    //binary search, wyswietla pierwsze wystapienie lub miejsce gdzie element by się znalazl
    private int binarySearch(int val) {
        int left = 0;
        int right = elementCount;
        while (left != right) {
            int middle = (left + right) / 2;
            if (val > arr[middle])
                left = middle + 1;
            else
                right = middle;
        }
        return left;
    }

    //funkcja znajduje najmniejszy element w tablicy
    private int findMinElem(){
        int min = 0;
        for(int i = 1 ; i < elementCount ; i++){
            if(arr[i] < arr[min]) {
                min = i;
            }
        }
        return min;
    }

    //funkcja sortuje algorytmemsortowania przez wstawianie z wartowniekiem
    public void sort(){
        int min_idx = findMinElem();
        int tmp = arr[0];
        arr[0] = arr[min_idx];
        arr[min_idx] = tmp;  //wartownik ustawiony

        for(int i = 1; i < elementCount ; i++){
            tmp = arr[i];
            int j = i - 1;
            while(tmp < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = tmp;
        }
        isOrdered = true;
    }
}

public class Source {

    public static String getCommand(Scanner sc){
        System.out.println("Enter the command:");
        return sc.next();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the length of the array");
        int length = sc.nextInt();
        Vector vec = new Vector(length);

// dostepne komendy:
// insert n
// delete i
// find n
// display
// exit
        System.out.println("Avaliable commands: insert, delete, find, sort, display, exit");
        String command = getCommand(sc);

        while(!command.equals("exit")){
            // wykonujemy komende
            invokeCommand(command, vec, sc);

            // bierzemy kolejna komende...
            command = getCommand(sc);
        }

        System.out.println("Bye...");
    }

    private static void invokeCommand(String command, Vector vec, Scanner sc) {
        int operand;
        switch (command){
            case "insert":
                operand = sc.nextInt();
                vec.insert(operand);
                break;
            case "delete":
                operand = sc.nextInt();
                vec.delete(operand);
                break;
            case "find":
                operand = sc.nextInt();
                System.out.println(vec.find(operand));
                break;
            case "sort":
                vec.sort();
                break;
            case "display":
                vec.display();
                break;
            default:
                System.out.println("It is not a command, try again.");
        }
    }
}