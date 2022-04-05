// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadanie 8
// Selekcja piosenek
// uzyty algoytm: mediana median (magiczne piatki) - pokazany w wykladzie, przerobiony na algorytm w miejscu
// * dzialanie algorytmu:
// chcemy znalezc k-ty najmniejszy element nieposortowanej tablicy
// 1) dzielimy tablice na 5cio elementowe podtablice (ostatnia moze byc mniejsza) i wyznaczamy ich mediany, mediany sa
// nastepnie wpisywane do podtablicy, w tym przypadku sa wpisywane na poczatku tablicy (przez zamiane indeksow),
// by zapewnic stala zlozonosc pamieciowa
// 2) rekurencyjne metoda jest wywolywana na podtablicy z medianami dla k rownego polowie ilosci median w tablicy
// median. znajdujemy w ten sposob mediane median
// 3) wykonujemy podzial tablicy (jak w quicksort, mniejsze na lewo od wartosci dzialacej, wieksze na prawo) wzgledem
// mediany median
// 4) jezeli ilosc elementow po lewej stronie od mediany median po podziale jest rowna k to zwracamy te pozycje. KONIEC
// 5) jezeli ilosc elementow po lewej stronie od mediany median po podziale jest wieksza od k to znaczy ze szukany
// indeks k jest w lewej czesci tablicy, wywolujemy funkcje rekurencyjnie po lewej stronie od mediany median
// 6) jezeli 5) i 6) nie są prawdziwe to wywolujemy funkcje rekurencyjnie po lewej stronie mediany median
// * zlozonosc czasowa:
// pesymistyczna zlozonosc czasowa prezentowanego algorytmu wynosi O(n). Indukcyjnie:
// punkt 1) algorytmu ma zlozonosc O(n), zaladamy tutaj, ze wyszukiwanie mediany ma zlozonosc O(1), punkt 2) ma
// zlozonosc O(n/5) = O(n) (zalozenie indukcyjne) , punkt 3) ma zlozonosc O(n), wiec w przypadku optymistycznym
// zlozoność wynosi O(n). W przypadku pesymistycznym trafimy do punktu 5) lub 6). Zauwazmy, że skoro dxzielimy tablice
// wzgledem mediany median to co najmniej polowa median jest >= od mediany median, czyli co najmniej polowa podtablic
// 5cio elementowych zawiera 3 elementy ktore sa > od mediany median, stąd liczba elementow wiekszych od mediany median
// wynosi co najmniej 3 * ((1 / 2) * (n / 5) - 2) = 3n/10 - 6, analogicznie liczba lementow mniejszych od mediany median
// wynosi co najmnie 3n/10 - 6. Funkcja wywoluje sie maksymalnie n - (3n/10 - 6) = 7n/10 + 6 w najgorszym przypadku.
// Mamy sad, rownanie rekurencyjne opisujce zlozonosc (dla pesymistycznego przypadku) postaci
// T(n) <= T(n/5) + T(7n/10 + 6) + O(n). Zakladajac indukcyjnie ze T(n) = cn: T(n) <= cn/5 + c(7n/10 + 6) + O(n) <=
// <= 9cn/10 + 6c + O(n) <= c'n, wiec zlozonosc pesymistyczna jest linowa (jednak stale musza byc duze)
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

public class Source {
    static int[] array;  //tablica przechowujaca dane podane na wejsciu
    static Scanner scan = new Scanner(System.in);

    // funkcja zamienia dwa elementy w tablicy
    private static void swap(int idx1, int idx2){  //ok
        int tmp = array[idx1];
        array[idx1] = array[idx2];
        array[idx2] = tmp;
    }

    // funkcja sortuje podtablice piecioelementowa (lub mniejsza) i zwraca mediane
    // z punktu widzenia całego algorytmu ma stala zlozonosc czasowa (max 25 przepisan)
    private static int median(int first, int last){ // ok
        // sortuje pozostale elementy
        for(int i = last ; i > first ; i--){
            for(int j = i - 1 ; j >= first ; j --){
                if(array[j] > array[i])
                    swap(i, j);
            }
        }
        return first + (last - first + 1) / 2;
    }

    // funkcja dostaje indeks z elementem wzgledem (pivot) ktorego bedzie robic podzial (elementy <= pivot na lewno,
    // > pivot na prawo) oraz indeksy first, last oznaczajace pierwszy i ostatni element podtablicy, w ktorej podzial
    // jest wykonywany
    // zwraca indeks na ktorym po podziale znajduje sie pivot
    private static int partition(int first, int last, int pivotIdx){  //ok
        int pivot = array[pivotIdx];
        swap(pivotIdx, last);
        int i = first;
        for(int j = first ; j < last ; j ++){
            if(array[j] <= pivot) {
                swap(i, j);
                i ++;
                if(i > last)
                    i = last;
            }
        }
        swap(i, last);
        return i;
    }

    // funkcja implementujaca algorytm madiana median, wartosc k jest na pewno poprawna (wieksza od 0 i mniejsza od
    // ilosci elementow tablicy)
    private static int medianOfMedians(int first, int last, int k){
        int medianLast = first - 1;  //wartosc przechowujaca indeks ostatniego elementu w podtablicy z medianami
        int median;  // indeks mediany
        int j;
        // sortowanie podtablic 5cio elementowych, i przepisywanie ich median na poczatek tablicy
        for(j = first ; j + 5 <= last ; j += 5){
            median = median(j, j + 4);
            medianLast++;
            swap(median, medianLast);
        }
        // sortowanie ostatniej podtablicy (ma 5 lub mniej elementow) i dodanie mediany za poprzednia mediana
        median = median(j, last);
        medianLast++;
        swap(median, medianLast);

        int medOfmed = first;
        // jezeli w podtablicy z medianami jest tylko jeden element to nie robimy wywolania rekurencyjnego
        if(medianLast > first){
            // wywolanie rekurencyjne dla podtablicy z medianami w celu znaleznienia mediany median
            medOfmed = medianOfMedians(first, medianLast, (medianLast - first + 1) / 2);
        }
        // podzial wzgledem mediany median
        int pivotIdx = partition(first, last, medOfmed);

        // jezeli ilosc elementow po lewej stronie od pivotIdx jest rowna k to znalezlismy indeks z k-tym elementem
        // i go zwracamy
        if(pivotIdx - first + 1 == k)
            return pivotIdx;
        // jezeli ilosc elementow po lewej stronie od pivotIdx  jest wieksza od k to wywolujemy funkcje po lewej
        // stronie pivotIdx
        if(pivotIdx - first + 1 > k)
            return medianOfMedians(first, pivotIdx - 1, k);
        // jezeli ilosc elementow po lewej stronie pivotIdx jest mniejsza od k to wywolujemy rekurencyjnie funkcje po
        // prawej stronie od pivotIdx i uwzgledniamy, ze wartosc k zwiekszy sie o ilosc elementow po lewej od pivotIdx
        return medianOfMedians(pivotIdx + 1, last, k - (pivotIdx - first + 1));
    }

    // funkcja zwraca indeks pod ktorym znajduje sie k-ty najmniejszy element tablicy
    // jezeli indeks jest niepoprawny zwraca -1
    public static int kthSmallestElem(int k){
        if(k > array.length || k < 1)
            return -1;
        else{
            return medianOfMedians(0, array.length - 1, k);
        }

    }

    public static void readArray(){
        int n = scan.nextInt();
        array = new int[n];
        for(int i = 0 ; i < n ; i++){
            array[i] = scan.nextInt();
        }
    }

    public static void main(String[] args) {
        int z = scan.nextInt();  //liczba zestawow danych
        for(int i = 0 ; i < z ; i++){
            readArray();
            int m = scan.nextInt();
            for(int j = 0 ; j < m ; j++){
                int k = scan.nextInt();
                int posOfK = kthSmallestElem(k);
                if(posOfK == -1){
                    System.out.println(k + " brak");
                } else {
                    System.out.println(k + " " + array[posOfK]);
                }
            }
        }
    }
}

//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
4
4
3 4 1 2
6
0 1 2 3 4 5
5
3 5 2 1 4
6
1 2 3 4 5 6
14
9 10 11 14 5 6 1 2 12 13 3 4 8 7
6
1 4 9 12 14 17
33
22 23 24 12 13 2 3 25 26 27 6 7 1 11 19 20 21 8 9 10 14 15 28 29 30 31 32 33 4 5 16 17 18
7
0 3 18 23 31 33 36
 */
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
0 brak
1 1
2 2
3 3
4 4
5 brak
1 1
2 2
3 3
4 4
5 5
6 brak
1 1
4 4
9 9
12 12
14 14
17 brak
0 brak
3 3
18 18
23 23
31 31
33 33
36 brak
 */
//--------------------------------------------------------------------------------------------------------------------//