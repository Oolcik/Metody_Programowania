import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class source {
    //funkcja znajduje najmniejszy element w tablicy
    private static int findMinElem(int[] arr){
        int min = 0;
        for(int i = 1 ; i < arr.length ; i++){
            if(arr[i] < arr[min]) {
                min = i;
            }
        }
        return min;
    }

    //funkcja sortuje algorytmemsortowania przez wstawianie z wartowniekiem
    private static void insertionSort(int[] arr){
        int min_idx = findMinElem(arr);
        int tmp = arr[0];
        arr[0] = arr[min_idx];
        arr[min_idx] = tmp;  //wartownik ustawiony

        for(int i = 1; i < arr.length ; i++){
            tmp = arr[i];
            int j = i - 1;
            while(tmp < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = tmp;
        }
    }

    //funkcja wypelnia tablce losowymi wartosciami z zakresu [0,100]
    private static void randomFill(int[] arr){
        Random rand = new Random();
        for(int i = 0 ; i < arr.length ; i++){
            arr[i] = rand.nextInt(101);
        }
    }

    //implementacja binary search, funkcja szuka najmniejszego indeksu podanego elementu
    //lub podaje indeks gdzie taki element by się znajdował
    private static int binarySearch(int[] arr, int val){
        int left = 0;
        int right = arr.length;  //ustawiam koniec na length, dzieki temu jak bede chciala element, ktory jest wiekszy
                                 // od kazdego w tablicy to dostane indeks o wart length, czyli 1 za koncem, ze wzgledu
                                 //na to jaki jest wzor na middle, nigdy nie bede czytac z tego elementu
        while(left != right){
            int middle = (left + right) / 2;
            if(val > arr[middle])
                left = middle + 1;
            else
                right = middle;
        }
        return left;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj dlugosc tablicy:");
        int length = sc.nextInt();
        int[] arr  = new int[length];

        //wypenienie tablicy losowymi wartosciami
        randomFill(arr);
        System.out.println("Twoja tablica wypelniona losowo:");
        for(int i = 0 ; i < length ; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        //sortowanie
        insertionSort(arr);
        System.out.println("Twoja tablica po posortowaniu:");
        for(int i = 0 ; i < length ; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        //szukaj indeksu elementu
        System.out.println("Podaj element ktorego indeks chcesz znalezc:");
        int val = sc.nextInt();
        System.out.println("Indeks tego elementu to: " + binarySearch(arr, val));

        //test sprawdzajacy zgodnosc z algorymem binarysearch z biblioteki Arrays
        System.out.println("Teraz sprawdzmy czy nasz binary sort jest zgodny z bibliotecznym binary sort:");
        System.out.println("dla randomowych 10 liczb sprawdzimy czy rezultaty są zgodne:");
        System.out.println("szuakna liczba | wynik mojego binarysearch | wynik kontolny | czy rowne");
        Random rand =  new Random();
        for(int i = 0 ; i < 10 ; i++){
            val = rand.nextInt(101);
            int myIdx = binarySearch(arr, val);
            int idx = Arrays.binarySearch(arr, val);
            if(idx < 0)
                idx = (-1)*(idx + 1); //tak jest zdefiniowyany wynik gdy liczby nie ma w tablicy (-(insertionPoint) - 1)
            System.out.println( val + " | " + myIdx + " | " + idx + " | " + (myIdx == idx));
        }

    }
}
