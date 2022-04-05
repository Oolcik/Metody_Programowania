import java.util.Random;
import java.util.Scanner;

public class source {
    //wypelnia tablice losowymi wartosciami z zakresu [-50,50]
    private static void randomFill(int[] arr){
        Random rand = new Random();
        for(int i = 0 ; i < arr.length ; i++){
            arr[i] = rand.nextInt(101) - 50;
        }
    }

    private static int kadane(int[] arr){
        int prevMaxSubarraySum = arr[0];
        int currMaxSubarraySum = arr[0];
        for(int i = 1 ; i < arr.length ; i++){
            currMaxSubarraySum = Math.max((arr[i] + currMaxSubarraySum), arr[i]);
            prevMaxSubarraySum = Math.max(prevMaxSubarraySum, currMaxSubarraySum);
        }
        return prevMaxSubarraySum;
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

        System.out.println("Największa suma w spójnej podtablicy: " + kadane(arr));

        //test na kilku oczywistych przypadkach
        System.out.println("test:");
        int[] tab1 = {1,1,1,1,1,1};  //6
        System.out.println("3: " + kadane(tab1));
        int[] tab2 = {-1, -1, 4, -1, -1, -1};  //4
        System.out.println("4: " + kadane(tab2));
        int[] tab3 = {-1, -1, 4, -1, 6, -1};  //9
        System.out.println("9: " + kadane(tab3));
        int[] tab4 = {-1, -1, 0, -1, -1, -1};  //0
        System.out.println("0: " + kadane(tab4));
        int[] tab5 = {8, -10, 4, -10, 3, -10};  //8
        System.out.println("8: " + kadane(tab5));
        int[] tab6 = {-8, -10, -4, -10, -3, -10};  //-3
        System.out.println("-3: " + kadane(tab6));

    }
}
