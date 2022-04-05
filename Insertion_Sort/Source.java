//sortowanie tablicy, algorytm: sortowanie przez w wstawianie z wartownikiem
import java.util.Scanner;

public class Source {

    private static int findMinElem(int[] arr){
        int min = 0;
        for(int i = 1 ; i < arr.length ; i++){
            if(arr[i] < arr[min]) {
                min = i;
            }
        }
        return min;
    }


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



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Podaj dlugosc tablicy:");
        int length = sc.nextInt();
        int[] arr  = new int[length];

        System.out.println("Podaj " + length + "elementów tablicy:");
        for(int i = 0 ; i < length ; i++){
            arr[i] = sc.nextInt();
        }

        //sortowanie
        insertionSort(arr);
        System.out.println("Twoja tablica po posortowaniu:");
        for(int i = 0 ; i < length ; i++){
            System.out.print(arr[i] + " ");
        }

    }
}
