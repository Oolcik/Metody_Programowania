// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadanie 6
// tasowanie piosenek
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

public class Source {

    private static String[] arr;
    private static String commonPrefix;
    private static Scanner sc;

    // metoda wczytujaca tablice, dodatkowo ustawia pole commonPrefix na wartosc arr[0]
    private static void readArr(int size){
        arr = new String[size];
        for(int i = 0 ; i < size ; i++){
            arr[i] = sc.next();
        }
        commonPrefix = arr[0];
    }

    // metoda do wyswietlania tablicy
    public static void printArr(){
        int i;
        for(i  = 0 ; i < arr.length - 1 ; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println(arr[i]);
    }

    // funkcja aktualizuje wspolny prefiks, wspolny prefiks poczatkowo jest wartoscia nazwy pierwszej piosenki
    // w tablicy, funkcja porownuje commonPrefix z danym stringiem str znak po znaku i skraca go do wspolnej czesci
    // akcja liniowa w dlugosci stringa, dla zadania stala
    private static void updateCommonPrefix(String str){
        int i = 0;
        for(i = 0 ; i < commonPrefix.length() ; i++){
            if(i == str.length()) {
                break;
            }
            if(commonPrefix.charAt(i) != str.charAt(i)){
                break;
            }
        }
        commonPrefix = commonPrefix.substring(0, i);
    }

    // metoda do zamiany wartosci w tablicy, O(1)
    private static void swap(int i, int j){
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // metoda tasujaca piosenki i ustalajaca wspolny prefix, O(nlogn)
    // metoda: dziel i zwyciezaj, kazde kolejne wywolanie funkcji przyjmuje tablice mniejsza o polowe, co daje
    // logarytmiczny czas dzialania
    // algorytm: wyznaczam srodek tablicy (mid), nastepnie srodek pierwszej cwiartki
    // (k - indeks pierwszego elementu dzriej cwiartki) po czym cyklicznie przesuwam elementy od indeksu k
    // do indeksu mid + k - beg [a1 a2 a3 a4 a5 | b1 b2 b3 b4 b5 -> a1 a2 a3 b1 b2 | a4 a5 b3 b4 b5] i wywoluje funkcje
    // na podtablicach [beg, mid], [mid+1, end]. Jezeli podzial tablicy na pol skutkuje podtablicami o nieprzyatej
    // liczbie elementow, to przesuwam ostatnie elementy podtabluc na ich docelowe miejsca (pokazane nizej)
    public static void rearrange(int beg, int end){
        if(beg == end) return;
        if(end - beg == 1){
            //zostaly dwa elementy, na pewno sa w dobrej kolejnosci, wiec pozostaje tylko zaktualizowac wspolny prefix
            updateCommonPrefix(arr[beg]);
            updateCommonPrefix(arr[end]);
            return;
        }
        int mid = beg + (end - beg) / 2;
        if((end - beg + 1) % 2 == 1){
            // przypadek gdy pierwsza plyta ma jedna piosenke wiecej, np: a1 a2 a3 a4 a5 | b1 b2 b3 b4
            // mozemy w takim wypadku przesunac alement a5 na koniec na miejsce docelowe
            // akcja jest liniowa, wiec nie pogarsza zlozonosci
            String tmp = arr[mid];
            for(int i = mid + 1 ; i <= end ; i++){
                arr[i - 1] = arr[i];
            }
            arr[end] = tmp;
            end = end - 1;
            mid = beg + (end - beg) / 2;
            // aktualizacja wspolnego prefiksu
            updateCommonPrefix(arr[end]);
            if(end - beg == 1){
                //zostaly dwa elementy, na pewno sa w dobrej kolejnosci, wiec pozostaje tylko zaktualizowac wspolny prefix
                updateCommonPrefix(arr[beg]);
                updateCommonPrefix(arr[end]);
                return;
            }

        }
        if(((mid - beg + 1) % 2) == 1){
            // przypadek, gdy podtablice po podziale sa nieparzyste np: a1 a2 a3 a4 a5 | b1 b2 b3 b4 b5
            // mozemy przesuanac a5 na przedostatnie miejsce
            // akcja jest liniowa, wiec nie pogarsza docelowej zlozonosci nlogn
            String tmp = arr[mid];
            for(int i = mid + 1 ; i < end ; i++){
                arr[i - 1] = arr[i];
            }
            arr[end - 1] = tmp;
            end = end - 2;
            mid = beg + (end - beg) / 2;
            // dostajemy: a1 a2 a3 a4 | b1 b2 b3 b4 (a5 b5)
            //akyualizacja wspolnego prefixa w stosunu do wartosci przeniesionych na koniec
            updateCommonPrefix(arr[end + 1]);
            updateCommonPrefix(arr[end + 2]);
        }
        int k = beg + (mid - beg ) / 2 + 1;
        int l = mid + 1;
        for( int i = k, j = l ; i <= mid; i++, j++){
            swap(i, j);
            //a1 a2 a3 a4 a5 | b1 b2 b3 b4 b5 -> a1 a2 a3 b1 b2 | a4 a5 b3 b4 b5
        }
        rearrange(beg, mid);
        rearrange(mid + 1, end);
    }


    public static void main(String[] args) {
        sc = new Scanner(System.in);
        int z = sc.nextInt();
        for(int i = 0 ; i < z ; i++){
            int arrSize = sc.nextInt();
            readArr(arrSize);
            rearrange(0, arr.length - 1);
            printArr();
            System.out.println(commonPrefix);

        }
    }
}

//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
10
1
a
10
a1 a2 a3 a4 a5 b1 b2 b3 b4 b5
8
a1 a2 a3 a4 b1 b2 b3 b4
10
a1 a2 a3 a4 a5 aa1 aa2 aa3 aa4 aa5
8
a1 a2 a3 a4 aa1 aa2 aa3 aa4
4
jsrfbef jsrjd jsrsbn jsrdtyx
6
tjr jtr jtr jtr jtr jtr
3
asd fgh jkl
5
asdf asdg asdh asdj asdk
9
1 2 3 4 5 6 7 8 9
 */
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
a
a
a1 b1 a2 b2 a3 b3 a4 b4 a5 b5

a1 b1 a2 b2 a3 b3 a4 b4

a1 aa1 a2 aa2 a3 aa3 a4 aa4 a5 aa5
a
a1 aa1 a2 aa2 a3 aa3 a4 aa4
a
jsrfbef jsrsbn jsrjd jsrdtyx
jsr
tjr jtr jtr jtr jtr jtr

asd jkl fgh

asdf asdj asdg asdk asdh
asd
1 6 2 7 3 8 4 9 5


 */
//--------------------------------------------------------------------------------------------------------------------//
