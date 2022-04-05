// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadanie 10
// Wielki turniej ciagow, pomysl:
// wartosci aktualnie biorace udzial w rundzie sa wrzucane na kopiec, poczatkowo pierwsza kolumna danych. Wrzucenie
// danych na kopiec (stworzenie kopca z pierwszej kolumny) ma zlozonosc liniowa (O(n)). Nastepnie sprawdzane jest czy
// wartosc na wierzchiolku kopca jest jedyna (nie ma dziecka o tej samej wartosci), jezeli jest jedyny to dodaje punkt
// odpowiedniemu graczowi i podmieniam wartosc na szczycie stosu na kolejna w wierszu dla tego gracza, nastepnie
// przywracam wlasnosc kopca metoda downheap z wykladu o zlozonosci O(logn) (wykona sie maksymalnie m razy). Jezeli
// wartosc na szczycie kopca nie jest jedyna, to dla kazdej wartosi w kopcu podmieniam ja na nastepna w wierszu (O(n))
// i tworze kopiec z tych danych tak, jak na samym poczatku (O(n)), wykonuje to r razy, gdzie r to ilosc remisow.
// Powtarzam te akcje az wykonam m rund.
// Zlozonosc obliczeniowa (r + 1) * O(n) + m * O(log(n))
// w zadaniu wykorzystuje dodatkowa tablice o rozmiarze int * n do przechowywania punktow dla graczy oraz tablice o
// rozmiarze 2 * int * n dla kopca co daje O(n) dodatkowej pamieci.
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

class Node {
    public int playerIdx;
    public int dataIdx;

    public Node(int playerIdx, int dataIdx) {
        this.playerIdx = playerIdx;
        this.dataIdx = dataIdx;
    }
}

class MaxHeap {
    Node[] heap;
    int roundsNum;
    int[] points;
    int size;

    public MaxHeap(int heapSize, int roundsNum) {
        heap = new Node[heapSize];
        this.roundsNum = roundsNum;
        points = new int[heapSize];
        size = roundsNum;
    }

    // tworzy kopiec z danej tablicy, O(n)
    // https://www.geeksforgeeks.org/building-heap-from-array/
    private void heapify(int i, int[][] data) {
        int largest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

        if((leftChild < heap.length) && (data[heap[leftChild].playerIdx][heap[leftChild].dataIdx] >
                                                                data[heap[largest].playerIdx][heap[largest].dataIdx]))
            largest = leftChild;
        if((rightChild < heap.length) && (data[heap[rightChild].playerIdx][heap[rightChild].dataIdx] >
                                                                data[heap[largest].playerIdx][heap[largest].dataIdx]))
            largest = rightChild;
        if(largest != i) {
            Node tmp = heap[i];
            heap[i] = heap[largest];
            heap[largest] = tmp;
            heapify(largest, data);
        }
    }

    // funkcja wykonuje sie raz na poczatku zestawu danych
    // tworzy kopiec, O(n)
    public void create(int[][] data) {
        if(size == 0) return;
        for(int i = 0 ; i < heap.length ; i++) {
            heap[i] = new Node(i, 0);
        }
        for(int i = ((heap.length / 2) - 1) ; i >= 0 ; i--) {
            heapify(i, data);
        }
    }

    //zwraca false jak przekroczymy ilosc mozliwych rund
    // funkcja wykonuje sie w przypadku remisu (r razy), O(n) jak w create
    public void modifyHeap(int[][] data) {
        for(int i = 0 ; i < heap.length ; i++) {
            heap[i].dataIdx += 1;
            if(heap[i].dataIdx >= roundsNum) return;
        }
        for(int i = ((heap.length / 2) - 1) ; i >= 0 ; i--) {
            heapify(i, data);
        }
        //return true;
    }

    public void show(int[][] data) {
        for(int i = 0 ; i < heap.length ; i++) {
            System.out.print(" " + data[heap[i].playerIdx][heap[i].dataIdx]);
        }
        System.out.println();
    }

    //downheap dla korzenia
    //algorytm z wykladu, O(logn)
    public void downHeap(int[][] data) {
        int k = 0;
        Node tmp = heap[0];
        int j;
        while(k < (heap.length / 2)) {
            j = 2 * k + 1;
            if(j < heap.length - 1 && data[heap[j].playerIdx][heap[j].dataIdx] <
                                                                    data[heap[j + 1].playerIdx][heap[j + 1].dataIdx])
                j++;
            if(data[tmp.playerIdx][tmp.dataIdx] >= data[heap[j].playerIdx][heap[j].dataIdx])
                break;
            heap[k] = heap[j];
            k = j;
        }
        heap[k] = tmp;
    }

    //O(logn)
    public void drawMax(int[][] data) {
        points[heap[0].playerIdx] += 1;
        heap[0].dataIdx += 1;
        if(heap[0].dataIdx >= roundsNum) return;
        downHeap(data);
        //return true;
    }

    public void printPoints(String[] names) {
        for(int i = 0 ; i < heap.length ; i++) {
            System.out.println(names[i] + " - " + points[i] + " pkt.");
        }
    }

    //runda, dzialanie opisane na poczatku pliku
    public void match(int[][] data, String[] names) {
        int n = roundsNum;
        while(n > 0) {
            if(size == 0) break;
            //show(data);
            if(heap.length == 1) {
                //if(!drawMax(data)) break;
                drawMax(data);
            } else if(heap.length == 2) {
                    if(data[heap[0].playerIdx][heap[0].dataIdx] == data[heap[1].playerIdx][heap[1].dataIdx]) {
                        //if(!modifyHeap(data)) break;
                        modifyHeap(data);
                    } else {
                        drawMax(data);
                    }
            } else if(data[heap[0].playerIdx][heap[0].dataIdx] == data[heap[1].playerIdx][heap[1].dataIdx] ||
                    data[heap[0].playerIdx][heap[0].dataIdx] == data[heap[2].playerIdx][heap[2].dataIdx]) {
                //if(!modifyHeap(data)) break;
                modifyHeap(data);
            } else {
                //if(!drawMax(data)) break;
                drawMax(data);
            }
            n -= 1;
        }
        printPoints(names);
    }
}

public class Source {

    static String[] players;
    static int[][] array2D;
    static int[] points;
    static Scanner scan = new Scanner(System.in);

    public static void readData(int playersNum, int roundsNum) {
        players = new String[playersNum];
        array2D = new int[playersNum][roundsNum];
        points = new int[playersNum];

        for(int i = 0 ; i < playersNum ; i ++) {
            players[i] = scan.next();
            for(int j = 0 ; j < roundsNum ; j++) {
                array2D[i][j] = scan.nextInt();
            }
        }
    }

    public static void main(String[] args) {
        int z = scan.nextInt();
        for(int i = 0 ; i < z ; i++) {
            int n = scan.nextInt();
            int m = scan.nextInt();
            readData(n, m);
            MaxHeap myHeap = new MaxHeap(n, m);
            myHeap.create(array2D);
            myHeap.match(array2D, players);
        }
    }
}

//--------------------------------------------------------------------------------------------------------------------//
/*
< test.in
10
1 1
a 5
2 2
a 1 2
b 1 1
2 1
a 1
b 2
2 1
a 2
b 1
5 0
aaa
bbb
cc
ddd
eee
3 1
a 1
b 1
c 0
3 1
a 1
b 2
c 3
3 2
a 1 2
b 1 2
c 1 2
3 2
a 1 2
b 1 2
c 1 3
3 2
a 1 1
b 0 0
c 0 0
 */
//--------------------------------------------------------------------------------------------------------------------//
/*
> test.out
a - 1 pkt.
a - 1 pkt.
b - 0 pkt.
a - 0 pkt.
b - 1 pkt.
a - 1 pkt.
b - 0 pkt.
aaa - 0 pkt.
bbb - 0 pkt.
cc - 0 pkt.
ddd - 0 pkt.
eee - 0 pkt.
a - 0 pkt.
b - 0 pkt.
c - 0 pkt.
a - 0 pkt.
b - 0 pkt.
c - 1 pkt.
a - 0 pkt.
b - 0 pkt.
c - 0 pkt.
a - 0 pkt.
b - 0 pkt.
c - 1 pkt.
a - 2 pkt.
b - 0 pkt.
c - 0 pkt.
 */
//--------------------------------------------------------------------------------------------------------------------//