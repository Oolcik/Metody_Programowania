// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania, BaCa - zadnie 2
// Program ma na celu okreslic ile trojkatow mozna uzyskac z podanych na wejsciu odcinkow (dlugosci odcinkow umieszone
// sa w tablicy umieszczone sa w tablicy).
// Pomysl wykonania: odcinki w tablicy umieszone sa w sposob losowy to powoduje, ze aby znalezc liczbe trojkatow
// trzeba znalezc wszystkie liczby spelniajace nierownosci trojkata a + b < c (a, b, c - boki trojkata). Sprawdzenie
// wszystkich trojek skutkuje zlozonoscia O(n^3), mozna jednak przed przystapieniem do zliczania trojkatow posortowac
// tablice (od wartosci najmniejszej do najwiekszej w tym przypadku). Wyszukiwanie trojkatow odbywa sie wowczas
// w zlozonosci O(n^2) [opisane poniezej]. Zlozonosc algorytmu sortujacego (tutaj insertion sort) to O(n^2).
// Wyszukiwanie trojkatow:
// tablica jest posortowana rosnaco, wybieramy trzy pierwsze pola tablicy jako pierwsze sprawdzane boki
// (arr[0] = a, arr[1] = b, arr[2] = c).
// wykonujemy petle po a (do a < arrsize - 2), dla ustalonego a, ustalamy b jako nastepny element, i c jako kolejny,
// **jezeli wybrane boki spelniaja nierownosc trojkata to zwiekszamy c az do momentu gdy warunek ten nie bedzie spelniony,
// z racji posortowania tablicy wiemy, ze wszystkie b pomiedzy a i c spelniaja warunek (bo zwieksza sie suma a+ b),
// wiec z kazdym dzialajacym c zwiekszamy sume trojkatow o (c - b). Gdy znajdziemy c ktore nie dziala zwiekszamy b**
// fragment pomiedzy ** powtarzamy az do c < arrsize, i przechodzimy do kolejnej iteracji petli po a. Podsumowujac,
// a przechodzi po calej tablicy (n) i dla kazdego ustalonego a przechodzimy do konca z pozostalymi indekasmi (n^2).
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

public class Source {
    //funkcja znajdujaca minimum w tablicy, uzywana do znalezienia wartownika w insertion sort
    public static int findMinElem(long[] arr, int size){
        int min = 0;
        for(int i = 0 ; i < size ; i++){
            if(arr[i] < arr[min])
                min = i;
        }
        return min;
    }
    //insertion sort z wartownikiem
    public static void insertionSort(long[] arr, int size){
        int min_idx = findMinElem(arr, size);
        long tmp = arr[0];
        arr[0] = arr[min_idx];
        arr[min_idx] = tmp;

        for(int i = 1 ; i < size ; i++){
            tmp = arr[i];
            int j = i - 1;
            while (tmp < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = tmp;
        }
    }

    //funkcja zliczajaca trojkaty mozliwe do uzyskania z odcinkow zapisanch w tablicy
    public static long numOfTriangles(long[] arr, int size){
        if(size < 3) return 0;
        int a, b, c;
        long count = 0;
        for(a = 0 ; a < size - 2 ; a ++){
            b = a + 1;
            c = a + 2;
            while( c < size){
                if(arr[a] + arr[b] > arr[c]){
                    count += c - b;
                    c++;
                } else {
                    b++;
                    if(b == c)
                        c++;
                }
            }
        }
        return count;
    }
    //funkcja ktora zapisuje tablice z wejscia i wywoluje na niej sortowanie i szukanie trojkatow
    public static long perform(int size, Scanner sc){
        long[] arr = new long[size];
        for(int i = 0 ; i < size ; i++){
            arr[i] = sc.nextInt();
        }
        insertionSort(arr, size);
        return numOfTriangles(arr, size);

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfSets = sc.nextInt();
        int arrSize;
        //petla po zestawach danych
        while(numOfSets > 0){
            arrSize = sc.nextInt();
            System.out.println("Num_triangles= " + perform(arrSize, sc));
            numOfSets--;
        }

    }
}
//--------------------------------------------------------------------------------------------------------------------//
// < test.in
/*
10
2
0 1
5
0 0 1 2 2
5
1 1 1 0 0
5
1 2 1 0 0
5
1 1 1 1 1
7
1 1 1 1 1 1 1
10
1 1 1 1 1 1 1 1 1 1
10
1 0 1 0 1 1 0 1 0 1
6
1 2 1 2 1 2
6
1 2 3 1 2 3
 */
//--------------------------------------------------------------------------------------------------------------------//
// > test.out
/*
Num_triangles= 0
Num_triangles= 1
Num_triangles= 1
Num_triangles= 0
Num_triangles= 10
Num_triangles= 35
Num_triangles= 120
Num_triangles= 20
Num_triangles= 11
Num_triangles= 8
*/
//--------------------------------------------------------------------------------------------------------------------//