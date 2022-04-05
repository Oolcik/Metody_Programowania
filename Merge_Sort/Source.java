// Aleksandra Wnuk
// metody programowania - labolatorium - zadanie 6
// merge sort
import java.util.Scanner;

//klasa wezla listy, przechowuje wartosc, referencje nastepnego i na poprzedniego elementu listy
class Link{
    public int value;
    public Link next;
    public Link prev;

    //konstruktor, tworzy wezel z dana wartoscia
    public Link(int value){
        this.value = value;
        next = null;
        prev = null;
    }
}

//klasa listy, przechowuje referencje pierwszego elementu
class LinkedList{
    public Link first;

    //konsturktor, pusta lista
    public LinkedList(){
        first = null;
    }

    //wyswietla elementy listy oddzielone spacjami z endl na koncu
    public void display(){
        Link p = first;
        while(p != null){
            System.out.print(p.value + " ");
            p = p.next;
        }
        System.out.println();
    }

    //zwraca warotsc logiczna pustosci listy
    public boolean empty(){
        return (first == null);
    }

    //zwraca refernecje ostatniego wezla
    private Link lastLink(){
        if(empty()) return null;
        Link last = first;
        while(last.next != null)
            last = last.next;
        return last;
    }

    //dodaje element do listy, na koncu
    public void addElem(int value){
        Link newElem = new Link(value);
        if(empty()){
            first = newElem;
        } else{
            Link last = lastLink();
            last.next = newElem;
            newElem.prev = last;
        }
    }

    //znajduje, po wartosci, element w podliscie przekazanej referencja wezla i zwraca jego referencje
    private Link findElem(Link sublist, int value){
        if(sublist == null) return null;
        Link tmp = sublist;
        while(tmp != null){
            if(tmp.value == value) return tmp;
            tmp = tmp.next;
        }
        return null;
    }

    //usuwa pierwszy wezel
    private void deleteFirst(){
        if(first.next == null)
            first = null;
        else {
            first = first.next;
            first.prev = null;
        }
    }

    //usuwa wezel
    private void deleteLink(Link link){
        if(link.prev == null)
            deleteFirst();
        else if(link.next == null)
            link.prev.next = null;
        else {
            link.prev.next = link.next;
            link.next.prev = link.prev;
        }
    }

    //usuwa element z listy (pierwsze wystapienie)
    public void delete(int value){
        Link toDel = findElem(first, value);
        deleteLink(toDel);
    }

    //usuwa wszystkie wystapienia elementu z listy
    public void deleteAll(int value){
        Link toDelete = findElem(first, value);
        while(toDelete != null){
            deleteLink(toDelete);
            toDelete = findElem(toDelete.next, value);
        }
    }

    public Link merge(Link fstSublist, Link secSublist){
        Link mergedList = null;
        if(fstSublist == null)
            return secSublist;
        if(secSublist == null)
            return fstSublist;
        if(fstSublist.value <= secSublist.value){
            mergedList = fstSublist;
            mergedList.next = merge(fstSublist.next, secSublist);
        } else{
            mergedList = secSublist;
            mergedList.next = merge(fstSublist, secSublist.next);
        }
        return mergedList;
    }

    public Link mergeSort(Link list){
        if(list == null || list.next == null)
            return list;
        Link listMiddle = getMiddle(list);
        Link listNextToMiddle = listMiddle.next;
        listMiddle.next = null;
        Link lhsSublist = mergeSort(list);
        Link rhsSublist = mergeSort(listNextToMiddle);
        Link sortedList = merge(lhsSublist, rhsSublist);
        return sortedList;
    }

    public Link getMiddle(Link first){
        if(first == null)
            return first;
        Link slow = first;
        Link fast = first;
        while(fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
public class Source {

    //merge z wykladu
    public static void merge(int[] arr, int leftIdx, int middleIdx, int rightIdx, int[] tmpArr){
        for (int i = leftIdx; i <= rightIdx; i++)
            tmpArr[i] = arr[i] ; // kopiuje A[] do B[]

        int i = leftIdx;
        int j = middleIdx + 1; // i, j – początki podtablic
        int k = leftIdx ;  // k-indeks w tablicy wynikowej

        while (i <= middleIdx && j <= rightIdx) {
            if ( tmpArr[i] <= tmpArr[j] )
                arr[k++] = tmpArr[i++] ;
            else
                arr[k++] = tmpArr[j++] ;
        }
        // przepisz pozostałe elementy niezakończonej podtablicy
        while (i <= middleIdx)
            arr[k++] = tmpArr[i++];
        while (j <= rightIdx)
            arr[k++] = tmpArr[j++];
    }

    //rekurencyjne kopiowanie tablicy
    public static void copyArrayRecursive(int[] arr, int[] arrCopy, int beg, int end){
        if(beg > end) return;
        arrCopy[beg] = arr[beg];
        copyArrayRecursive(arr, arrCopy, beg + 1, end);
    }
    //rekurencyjne scalanie tablic
    public static void mergeSubarraysRecursive(int[] arr, int leftIdx, int middleIdx, int rightIdx, int[] tmpArr,
                                               int i, int j, int k){
        if(i > middleIdx && j > rightIdx)
            return;

        if(i <= middleIdx && j <= rightIdx){
            if(tmpArr[i] <= tmpArr[j]){
                arr[k++] = tmpArr[i++];
                mergeSubarraysRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr, i, j, k);
            }
            else{
                arr[k++] = tmpArr[j++];
                mergeSubarraysRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr, i, j, k);
            }
        }
        // przepisz pozostałe elementy niezakończonej podtablicy
        else if(i <= middleIdx) {
            arr[k++] = tmpArr[i++];
            mergeSubarraysRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr, i, j, k);
        }
        else if(j <= rightIdx) {
            arr[k++] = tmpArr[j++];
            mergeSubarraysRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr, i, j, k);
        }
    }
    //merge rekurencyjny
    public static void mergeRecursive(int[] arr, int leftIdx, int middleIdx, int rightIdx, int[] tmpArr){
        copyArrayRecursive(arr, tmpArr, leftIdx, rightIdx); // kopiuje arr do tmpArr
        mergeSubarraysRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr, leftIdx, middleIdx + 1, leftIdx);
    }
    //rekurencyjny merge sort
    public static void mergeSortRecursive(int[] arr, int leftIdx, int rightIdx){
        if(leftIdx == rightIdx) return;
        int middleIdx = (leftIdx + rightIdx) / 2;
        mergeSortRecursive(arr, leftIdx, middleIdx);
        mergeSortRecursive(arr, middleIdx + 1, rightIdx);
        int[] tmpArr = new int[rightIdx + 1];
        mergeRecursive(arr, leftIdx, middleIdx, rightIdx, tmpArr);
    }

    public static void main(String[] args) {

        int[] arr = new int[]{3, 6 ,5, 4, 2, 0, 9, 1, 8, 7};
        int rightIdx = arr.length, leftIdx = 0;

        System.out.println("------------- ARRAY VERSION -------------");
        System.out.println("Array before recursive merge sort:");
        for(int i = 0 ; i < rightIdx ; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        mergeSortRecursive(arr, leftIdx, rightIdx - 1);
        System.out.println("Array after recursive merge sort:");
        for(int i = 0 ; i < rightIdx ; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        arr = new int[]{7, 3, 2, 5, 0, 9, 8, 1, 4, 6};

        System.out.println("------------- LIST VERSION --------------");
        LinkedList list = new LinkedList();
        for(int i = 0 ; i < rightIdx ; i++)
            list.addElem(arr[i]);
        System.out.println("List before merge sort for linked lists:");
        list.display();
        list.first = list.mergeSort(list.first);
        System.out.println("List after merge sort for linked lists:");
        list.display();
        System.out.println("-----------------------------------------");
    }
}
