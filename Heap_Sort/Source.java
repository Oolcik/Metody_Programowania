public class Source {
    public static void downHeap(int[] arr, int k, int size) {
        int tmp = arr[k];
        int j;
        while(k < size / 2) {
            j = 2 * k + 1;
            if(j < size - 1 && arr[j] < arr[j + 1])
                j++;
            if(tmp >= arr[j])
                break;
            arr[k] = arr[j];
            k = j;
        }
        arr[k] = tmp;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void heapSort(int[] arr, int size) {
        for(int k = (size - 1) / 2 ; k >= 0 ; k--) {
            downHeap(arr, k, size);
        }
        int n = size;
        while(n > 0) {
            swap(arr, 0 , n - 1);
            n--;
            downHeap(arr, 0, n);
        }
    }

    public static void printArr(int[] arr, int size) {
        System.out.print("array:");
        for(int i = 0 ; i < size ; i++) {
            System.out.print(" " + arr[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {8,0,4,5,6,7,8,4,9,1,2,9,1,2,3,3,5,0,6,7};
        int arrSize = arr.length;

        printArr(arr, arrSize);
        heapSort(arr, arrSize);
        printArr(arr, arrSize);
    }
}
