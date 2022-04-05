// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadanie 7
// quicksort dla tabeli
// uzylam algorytmu pod linkiem: https://stackoverflow.com/questions/5451726/switching-columns-of-a-2d-array
// z drobnymi modyfikacjami, zeby spelnic warunki zadania
//--------------------------------------------------------------------------------------------------------------------//
//dozwolone importy
import java.util.Locale;
import java.util.Scanner;
import java.text.DecimalFormat;


public class Source {

    static int numOfTableColumns;  //liczba kolumn w tabeli
    static int numOfTableRows;  //liczba wierszy w tabeli (bez wiersza z naglowkami kolumn)
    static String[] columnsHeadlines;  //tablica na naglowki kolumn
    static float[][] floatTable;  //tablica na floaty
    static Scanner scan = new Scanner(System.in);
    static DecimalFormat formatter = new DecimalFormat("0.####");  //format wypisywania floatow na stdout
    static float precision = 0.0001f;  //precyzja porownywania floatow

    // funkcja wczytuje tablice, najpierw jej rozmiar, potem naglowki i na koncu floaty
    public static void readTable(){
        scan.useLocale(Locale.GERMAN);
        numOfTableColumns = scan.nextInt();
        numOfTableRows = scan.nextInt();
        columnsHeadlines = new String[numOfTableColumns];
        floatTable = new float[numOfTableRows][numOfTableColumns];

        for(int i = 0 ; i < numOfTableColumns ; i++)
            columnsHeadlines[i] = scan.next();

        for(int i = 0 ; i < numOfTableRows ; i++)
            for(int j = 0 ; j < numOfTableColumns ; j++)
                floatTable[i][j] = scan.nextFloat();
    }

    //funkcja wyswietl atablice z kolumnami oddzielownymi tabulatorem
    private static void printTable(){
        int i;
        for(i = 0 ; i < numOfTableColumns - 1 ; i++)
            System.out.print(columnsHeadlines[i] + "\t");

        System.out.println(columnsHeadlines[i]);
        for(i = 0 ; i < numOfTableRows ; i++){
            int j;
            for(j = 0 ; j < numOfTableColumns - 1 ; j++)
                System.out.print(formatter.format(floatTable[i][j]) + "\t");
            System.out.println(formatter.format(floatTable[i][j]));
        }
    }

    // funkcja wyswietla komne o podanym argumentem indeksie (razem z naglowkiem)
    private static void printColumn(int columnIdx){
        System.out.println(columnsHeadlines[columnIdx]);
        for(int i = 0 ; i < numOfTableRows ; i++){
            System.out.println(formatter.format(floatTable[i][columnIdx]));
        }

    }

    // funkcja znajduje kolumne po nazwie
    private static int findColumnIdx(String columnHeadline){
        for(int i = 0 ; i < numOfTableColumns ; i++) {
            if (columnsHeadlines[i].equals(columnHeadline)) return i;
        }
        return -1;
    }

    //funkcja znajdujaca najmniejszy element w przedziale kolumny (wartownik dla insert sort)
    private static int findMinElem(int beg, int end, int columnIdx){
        int minElemIdx = beg;
        for(int i = beg ; i <= end ; i++){
            //if(floatTable[i][columnIdx] < floatTable[minElemIdx][columnIdx])
            if(floatTable[minElemIdx][columnIdx] - floatTable[i][columnIdx] >= precision)
                minElemIdx = i;
        }
        return minElemIdx;
    }

    //insertion sort na przedziale kolumny
    private static void insertionSort(int beg, int end, int columnIdx){
        int minIdx = findMinElem(beg, end, columnIdx);
        float[] tmp = floatTable[beg];
        floatTable[beg] = floatTable[minIdx];
        floatTable[minIdx] = tmp;  // wartownik usawiony na poczatku przedzialu

        for(int i = beg + 1 ; i <= end ; i++){
            tmp = floatTable[i];  // wybieram element tablicy (w tym  przypadku bire referencje na caly rzad tabeli
                                  // i potem odnosze sie do odpowiedniej kolumny: tej wzgledem ktorej sortuje)
            int j = i - 1;
            while(floatTable[j][columnIdx] - tmp[columnIdx] >= precision){
            //while(tmp[columnIdx] < floatTable[j][columnIdx]){  // wstawiam element na miejsce, przesuwajac pozostale
                floatTable[j + 1] = floatTable[j];
                j--;
            }
            floatTable[j + 1] = tmp;
        }
    }

    // funkcja dzielaca dla insertion sort, wybiera element podzialu (pivot) i wszystkie elementy mniejsze przenosi
    // na lewno a wieksze na prawo, zwraca polozenie pivotu
    private static int partition(int left, int right, int columnIdx){
        float pivot = floatTable[(left + right) / 2][columnIdx];
        while(left <= right) {
            //while(floatTable[right][columnIdx] > pivot)
            while(floatTable[right][columnIdx] - pivot >= precision)
                right--;
            //while(floatTable[left][columnIdx] < pivot)
            while(pivot - floatTable[left][columnIdx] >= precision)
                left++;
            if(left <= right){
                float[] tmpRow = floatTable[right];
                floatTable[right] = floatTable[left];
                floatTable[left] = tmpRow;
                left++;
                right--;
            }
        }
        return left;
    }

    // funkcja znajduje w kolumnie nastepny ujemny element
    private static int findNextRight(int left, int columnIdx){
        for(int i = left ; i < numOfTableRows ; i++){
            if(floatTable[i][columnIdx] < 0) return i;
        }
        return numOfTableRows - 1;
    }

    // iteracyjny quicksort
    // idea: majac tablice [..nieposortowane elem..], dzielimy ja funkcja partition: [..< pivot..][pivot][..> pivot..]
    // ostatni element prawej podtablicy zmieniamy na ujemny: [..< pivot..][pivot][..> pivot..][-elem], ten ujemny
    // element bedzie nam dawal informacje o ostatnim indeksie prawej podtablicy do posortowania.
    // Ustawiamy leftIdx zostawiamy a rightIdx ustawiamy na indeks przed pivotem:
    // [left][..< pivot..][right][pivot][..> pivot..][-elem] i powtarzamy akcje az do momentu gdy
    // najbardziej lewa podtablica bedzie miala mniej niz 20 elementow, wtedy ja sortujemy za pomoca insertion sort
    // po posortowaniu najbardziej lewej podtablicy ustawiamy lewy indeks za ostatnim pivotem:
    // [..posortowane..][pivot][lewy][.......] zmieniac po drodze ujemna wartosc prawego, wyznaczamy nowy prawy jako
    // najblizsza ujemna liczba: [..posortowane..][pivot][lewy][.....][prawy][.......] i wykonujemy sortowanie na
    // podtablicy od lewego do prawego i tak az dojedziemy do jej konca. (zauwazmy, ze pivoty sa zawsze na prawidlowych
    // miejscach w tablicy)
    private static void quickSort(int columnIdx){
        int left = 0;  // indeks lewy (w przypadku kolumny gorny)
        int right = numOfTableRows - 1;  // indeks prawy (dolny)
        int q; // do przechowywania polozen pivotu, elementy na tych indeksach beda we wlasciwym miejscu w kolumnie
        int i = 0; // pomocnicza, zlicza ile podzialow kolumny (na podkolumny <-> 'wywolania quicksorta') juz wykonano
        int tmpRight = right; // zmienna prawego indeksu dla podtablicy <-> indeks prawy dla 'kolejnego wywolania'
        while(true){
            // zmianna zliczajaca podialy zmniajsza sie przy kazdym wejsciu do petli, bo oznacza, ze podtablica
            // najbardziej po lewej zostala posortowana
            i--;
            // jezeli indeks lewy mniejszy od prawego, to nie posortowalismy jeszcze wszystkiego
            while(left < tmpRight) {
                // jezeli elementow do posortowania w aktualnej podkolumnie jest mniej niz 20 to wywoluje insertion sort
                if(tmpRight - left < 20){
                    insertionSort(left, tmpRight, columnIdx);
                    left = tmpRight;
                } else {
                    q = partition(left, tmpRight, columnIdx); // pivot
                    //zmiana ostatniego elementu w podtablicy na ujemny
                    floatTable[tmpRight][columnIdx] = -floatTable[tmpRight][columnIdx];
                    //ustawienie konca podtablicy zaraz przed pivotem, powstaje nowa podtablica od left do tmpRight
                    tmpRight = q - 1;
                    // zwiekszamy zmienna zliczajaca ile podzialow juz dokonano
                    i++;
                }
            }
            if(i < 0) break;  // przerwamy: posortowalismy juz wszystkie podtablice
            // zwiekszamy lewy zeby przejsc za pivot, bo pivot jest zawsze na prawidlowym miejscu
            left++;
            // znajdujemy koniec nastepnej podtablicy do posortowania
            tmpRight = findNextRight(left, columnIdx);
            //zamieniamy wczesniej zmieniony element na ujemny z powrotem na wartosc dodatnia
            floatTable[tmpRight][columnIdx] = -floatTable[tmpRight][columnIdx];
        }
    }

    // funkcja pomocnicza do wyswietlania wyniku
    public static void print(String columnsToPrint, String columnToSortName){
        System.out.println("$ " + columnsToPrint + " " + columnToSortName);
        int columnIdx = findColumnIdx(columnToSortName);
        if(columnIdx != -1) {
            quickSort(columnIdx);
            if(columnsToPrint.equals("all"))
                printTable();
            else
                printColumn(columnIdx);
        } else {
            System.out.println("invalid column name: " + columnToSortName);
        }
    }

    public static void main(String[] args) {
        // ustawinie lokalizacji -> wczytywanie/wyswitlanie liczby rzeczywistej z przecinkiem jako separatorem
        int numOfSets = scan.nextInt();
        for(int i = 0 ; i < numOfSets ; i++){
            readTable();
            int numOfCommands = scan.nextInt();
            for(int j = 0 ; j < numOfCommands ; j++){
                String columnsToPrint = scan.next();
                String columnToSortName = scan.next();
                print(columnsToPrint, columnToSortName);
            }
        }
    }
}

//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
5
3 5
aaa bbb ccc
1,4 6,8 4,7
1,5 8,5 0,9
1,7 7,9 2,0
1,5 4,0 6
1,6 3,3 2,3
8
single aaa
all aaa
single bbb
all bbb
single ccc
all ccc
single ddd
all ddd
3 5
aaa bbb ccc
1,1111 6,8 4,7
1,1113 8,5 0,9
1,1115 7,9 2,0
1,1114 4,0 6
1,1112 3,3 2,3
8
single aaa
all aaa
single bbb
all bbb
single ccc
all ccc
single ddd
all ddd
1 30
aaa
26
4
5 23 24 25 8 9 17 18 19 27 20 21
22 12 14 15 28 29 1 2 30
10 11 6 7 3 13 16
4
all aaa
all bbb
single aaa
single bbb
1 60
aaa
26 4 5 23 24 25 8 9 17 18 19 27 20 21
22 12 14 15 28 29 1 2 30 10 11 6 7 3 13 16
26 4 5 23 24 25 8 9 17 18 19 27 20 21
22 12 14 15 28 29 1 2 30 10 11 6 7 3 13 16
4
all aaa
all bbb
single aaa
single bbb
2 30
aaa bbb
1   26
2   4
3   5
4   23
5   24
6   25
7   8
8   9
9   17
10  18
11  19
12  27
13  20
14  21
15  22
16  12
17  14
18  15
19  28
20  29
21  1
22  2
23  30
24  10
25  11
26  6
27  7
28  3
29  13
30  16
6
single aaa
all aaa
single bbb
all bbb
single ccc
all ccc
 */
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
$ single aaa
aaa
1,4
1,5
1,5
1,6
1,7
$ all aaa
aaa	bbb	ccc
1,4	6,8	4,7
1,5	8,5	0,9
1,5	4	6
1,6	3,3	2,3
1,7	7,9	2
$ single bbb
bbb
3,3
4
6,8
7,9
8,5
$ all bbb
aaa	bbb	ccc
1,6	3,3	2,3
1,5	4	6
1,4	6,8	4,7
1,7	7,9	2
1,5	8,5	0,9
$ single ccc
ccc
0,9
2
2,3
4,7
6
$ all ccc
aaa	bbb	ccc
1,5	8,5	0,9
1,7	7,9	2
1,6	3,3	2,3
1,4	6,8	4,7
1,5	4	6
$ single ddd
invalid column name: ddd
$ all ddd
invalid column name: ddd
$ single aaa
aaa
1,1111
1,1112
1,1113
1,1114
1,1115
$ all aaa
aaa	bbb	ccc
1,1111	6,8	4,7
1,1112	3,3	2,3
1,1113	8,5	0,9
1,1114	4	6
1,1115	7,9	2
$ single bbb
bbb
3,3
4
6,8
7,9
8,5
$ all bbb
aaa	bbb	ccc
1,1112	3,3	2,3
1,1114	4	6
1,1111	6,8	4,7
1,1115	7,9	2
1,1113	8,5	0,9
$ single ccc
ccc
0,9
2
2,3
4,7
6
$ all ccc
aaa	bbb	ccc
1,1113	8,5	0,9
1,1115	7,9	2
1,1112	3,3	2,3
1,1111	6,8	4,7
1,1114	4	6
$ single ddd
invalid column name: ddd
$ all ddd
invalid column name: ddd
$ all aaa
aaa
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
$ all bbb
invalid column name: bbb
$ single aaa
aaa
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
$ single bbb
invalid column name: bbb
$ all aaa
aaa
1
1
2
2
3
3
4
4
5
5
6
6
7
7
8
8
9
9
10
10
11
11
12
12
13
13
14
14
15
15
16
16
17
17
18
18
19
19
20
20
21
21
22
22
23
23
24
24
25
25
26
26
27
27
28
28
29
29
30
30
$ all bbb
invalid column name: bbb
$ single aaa
aaa
1
1
2
2
3
3
4
4
5
5
6
6
7
7
8
8
9
9
10
10
11
11
12
12
13
13
14
14
15
15
16
16
17
17
18
18
19
19
20
20
21
21
22
22
23
23
24
24
25
25
26
26
27
27
28
28
29
29
30
30
$ single bbb
invalid column name: bbb
$ single aaa
aaa
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
$ all aaa
aaa	bbb
1	26
2	4
3	5
4	23
5	24
6	25
7	8
8	9
9	17
10	18
11	19
12	27
13	20
14	21
15	22
16	12
17	14
18	15
19	28
20	29
21	1
22	2
23	30
24	10
25	11
26	6
27	7
28	3
29	13
30	16
$ single bbb
bbb
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
$ all bbb
aaa	bbb
21	1
22	2
28	3
2	4
3	5
26	6
27	7
7	8
8	9
24	10
25	11
16	12
29	13
17	14
18	15
30	16
9	17
10	18
11	19
13	20
14	21
15	22
4	23
5	24
6	25
1	26
12	27
19	28
20	29
23	30
$ single ccc
invalid column name: ccc
$ all ccc
invalid column name: ccc
*/
//--------------------------------------------------------------------------------------------------------------------//