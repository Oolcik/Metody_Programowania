//Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
//metody programowania, BaCa - zadnie 1
//najwieksza podtablica 2D z uzyciem algorytmu kadane dla tablicy 1D
//zlozonosc O(maxwym^3)
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

class Source {
    //algorytm kadane dla tablicy 1 wym., wersja z wykladu
    public static int kadane(int[] array1D, int size, int[] range){
        //range[0] indeks poczatku podtablicy
        //range[1] indeks konca podtablicy
        int maxSum = -1;  //-1 zeby wyroznic przypadek z samymi ujemnymi wartosciami
        int currentSum = 0;
        int currentSumBeg = 0;
        for(int i = 0 ; i < size ; i++){
            currentSum += array1D[i];
            if(currentSum < 0){
                currentSum = 0;
                currentSumBeg = i + 1;
            }
            else{
                if(currentSum > maxSum){
                    maxSum = currentSum;
                    range[0] = currentSumBeg;
                    range[1] = i;
                }
                //osobno rozwazony przypadek z zerowa suma, zeby w przypadku np {0, 4, 2, 2} zwrocilo {4, 2, 2}
                if(currentSum == 0){
                    currentSumBeg = i + 1;
                }
            }
        }
        return maxSum;
    }

    //funkcja wyznaczajaca maksymalna podtablice 2D
    //range[0] indeks gorny rzad
    //range[1] indeks dolny rzad
    //range[2] indeks lewa kolumna
    //range[3] indeks prawa kolumna
    public static int maximalSubarray2D(int[][] array2D, int rows, int columns, int[] range){
        int maxSum = -1;
        for(int top = 1 ; top < rows ; top++){
            for(int bottom = top ; bottom < rows ; bottom++){
                //w tym momencie mam ustalone dwa rzedy (top, bottom), gore i dol aktualnej podtablicy 2D
                //w pola rzedu zerowego wpisuje sumy elementow odpowiadajacych kolumn pomiedzy indeksami top i bottom
                for(int i = 0 ; i < columns ; i++){
                    if(top == 1)
                        array2D[0][i] = array2D[bottom][i];
                    else
                        array2D[0][i] = array2D[bottom][i] - array2D[top - 1][i];
                }
                int[] subrange = new int[2];
                //wykonuje algorytm kadane na zerowym rzedzie, zwroci mi to kolumny ograniczjace aktualna podtablice
                int currMaxSum = kadane(array2D[0],columns, subrange);
                //jezeli nowa suma jest taka sama jak dotychczasowa maksymalna to wybieram miejsza podtablice
                if(currMaxSum == maxSum) {
                    int sumOfElements = (range[1] - range[0] + 1) * (range[3] - range[2] + 1);
                    int currSumOfElements = (bottom - top + 1) * (subrange[1] - subrange[0] + 1);
                    if(currSumOfElements < sumOfElements) {
                        range[0] = top - 1;
                        range[1] = bottom - 1;
                        range[2] = subrange[0];
                        range[3] = subrange[1];
                    }
                }
                //jezeli aktualnie rozwazana podtablica ma sume wieksza od dotychczasowej maksymalnej to podmieniam
                if(currMaxSum > maxSum) {
                    maxSum = currMaxSum;
                    range[0] = top - 1;
                    range[1] = bottom - 1;
                    range[2] = subrange[0];
                    range[3] = subrange[1];
                }
            }
        }
        //if(maxSum == 0 && range[0] == 0 && range[1] == 0 && range[2] == 0 && range[3] == 0 && array2D[0][0] < 0)
          //  maxSum = -1;
        //leksykograficzna pierwszosc zwroconej podtablicy jest zapewniona przez algorytm
        return maxSum;
        //jezeli funkcja zwraca -1 to znaczy ze wszystkie elementy tablicy 2D byly ujemne
    }

    //metoda wczystuje pola do tablicy 2d, wartosci dodatnie sa przemnozone przez 3, ujemne przez 2
    //ponadto do kazdego elementu w kolumnie dodana jest suma elementw powyzej (column[i] = entry + column[i-1])
    //dzieki temu bedzie mozna skorzystac z algorytmu kadane w 1D
    //pierwszy zerowy rzad jest pomocniczy
    public static void readArr2D(Scanner sc, int[][] array2D, int rows, int columns){
        int entry;
        for(int i = 1 ; i < rows ; i++){
            for(int j = 0 ; j < columns ; j++){
                entry = sc.nextInt();
                if(entry < 0)
                    array2D[i][j]  = 2 * entry + array2D[i - 1][j];
                else
                    array2D[i][j] = 3 * entry + array2D[i - 1][j];
            }
        }
    }

    //funkcja wczytuje dane dla zestawu i wywoluje na nim odpowiednie funkcje
    public static void runSet(Scanner sc){
        int setNum = sc.nextInt();
        sc.next();
        int n = sc.nextInt();
        int m = sc.nextInt();

        int[][] array2D = new int[n + 1][m];

        readArr2D(sc, array2D, n + 1, m);

        int[] range = new int[4];

        int result = maximalSubarray2D(array2D, n + 1, m, range);
        if(result < 0){
            System.out.println(setNum + ": n=" + n + " m=" + m + ", ms= 0, mstab is empty");
        }
        else {
            System.out.print(setNum + ": n=" + n + " m=" + m + ", ms= " + result);
            System.out.println(", mstab= a[" + range[0] + ".." + range[1] + "][" + range[2] + ".." + range[3] + "]");
        }
    }

    public static void main(String[]args){

        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        for(int i = 0 ; i < a ; i++){
            runSet(sc);
        }
    }
}
//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
15
1 : 4 4
0 0 0 0
0 0 0 0
0 0 0 0
0 0 0 0
2 : 4 4
-1 -1 -1 -1
-1 -1 -1 -1
-1 -1 -1 -1
-1 -1 -1 -1
3 : 4 4
-1 -1 -1 -1
-1 -1 -1 -1
-1 -1 3 -1
-1 -1 -1 -1
4 : 4 4
-3 -3 1 -3
-3 -3 -3 -3
-3 1 -3 -3
-3 -3 -3 1
5 : 4 4
-1 -1 -1 3
-1 -1 -1 -1
-1 3 -1 -1
-1 -1 3 -1
6 : 4 4
3 3 3 3
3 3 3 3
3 3 3 3
3 3 3 3
7 : 1 6
-1 -1 3 -1 3 3
8 : 6 1
-1
-1
2
-1
3
-1
9 : 1 1
5
10 : 1 1
-5
11 : 3 4
0 -2 -3 -2
-4 -2 -3 -2
0 4 4 2
12 : 5 4
4 -1 3 -4
0 2 4 3
4 4 1 -1
3 -3 -1 -5
3 -3 -3 0
13 : 5 4
-1 -4 1 3
-1 1 3 1
-2 0 2 -5
0 1 4 -2
-2 -2 -1 -2
14 : 9 9
0 -3 -2 1 -3 4 3 -4 -4
-5 3 -5 1 -2 3 -3 1 -1
-5 -5 0 -3 2 1 -5 -3 4
-4 4 -1 -2 2 -5 -2 1 -5
0 0 -2 -3 -3 4 -2 -3 -2
-3 0 -1 -3 3 -2 -2 0 -2
2 4 0 0 -2 0 0 0 3
-5 1 -3 4 3 0 -4 3 -5
-4 1 -4 4 -2 -2 4 3 3
15 : 9 2
3 -5
0 4
-5 -4
-2 -1
-1 -1
3 -3
-1 0
0 -4
-5 -1
 */
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
1: n=4 m=4, ms= 0, mstab= a[0..0][0..0]
2: n=4 m=4, ms= 0, mstab is empty
3: n=4 m=4, ms= 9, mstab= a[2..2][2..2]
4: n=4 m=4, ms= 3, mstab= a[0..0][2..2]
5: n=4 m=4, ms= 14, mstab= a[2..3][1..2]
6: n=4 m=4, ms= 144, mstab= a[0..3][0..3]
7: n=1 m=6, ms= 25, mstab= a[0..0][2..5]
8: n=6 m=1, ms= 13, mstab= a[2..4][0..0]
9: n=1 m=1, ms= 15, mstab= a[0..0][0..0]
10: n=1 m=1, ms= 0, mstab is empty
11: n=3 m=4, ms= 30, mstab= a[2..2][1..3]
12: n=5 m=4, ms= 65, mstab= a[0..3][0..2]
13: n=5 m=4, ms= 33, mstab= a[1..3][1..2]
14: n=9 m=9, ms= 55, mstab= a[6..8][1..8]
15: n=9 m=2, ms= 12, mstab= a[1..1][1..1]
 */
//--------------------------------------------------------------------------------------------------------------------//
