//ALeksandra Wnuk
//realizacja kolejki cyklicznej
import java.util.Scanner;

class Queue{
    private int[] arr;
    private int arrSize;
    private int beg;
    private int end;  //za ostatnim elementem

    public Queue(int size){
        arrSize = size + 1;
        arr = new int[size + 1];
        beg = 0;
        end = 0;
    }

    public boolean isEmpty(){
        return (beg == end);
    }

    public boolean isFull(){
        return (((end + 1) % arrSize) == beg);
    }

    public int getFirst(){
        return arr[beg];
    }

    //nie zapelniamy ostatniego elementu, sluzy do rozroznienia pustej kolejki od pelnej
    public void enqueue(int newElem){
        if(isFull()){
            System.out.println("Queue is full: you can not enqueue more elements");
        } else {
            arr[end] = newElem;
            end = (end + 1) % arrSize;
        }
    }
    //zwraca pierwszy element z kolejki
    public int dequeue(){
        if(isEmpty()){
            System.out.println("Queue is empty: you can not dequeue any element");
            return -1;  //jak nie ma nic w kolejce to zwraca -1;
        } else {
            int idx = beg;
            beg = (beg + 1) % arrSize;
            return arr[idx];
        }
    }
     public void printQueue(){
         if(isEmpty()) {
             System.out.println("Queue is empty");
         } else {
             int idx = beg;
             System.out.print("Your queue:\n> ");
             while( idx != end ){
                 System.out.print(arr[idx]);
                 idx = (idx + 1) % arrSize;
             }
             System.out.println();
         }
     }
}

public class Source {

    public static void queueManagement(char command, Queue queue, Scanner sc){
        int operand;
        switch (command){
            case 'E':
                operand = sc.nextInt();
                queue.enqueue(operand);
                break;
            case 'D':
                int elem = queue.dequeue();
                System.out.println("First element from your queue was: " + elem);
                break;
            case 'P':
                queue.printQueue();
                break;
            default:
                System.out.println("It is not a command, try again.");
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Queue myqueue;
        int size;
        System.out.println("Enter the size of your queue");
        size = sc.nextInt();
        myqueue = new Queue(size);
        System.out.println("Avaliable commands: E - enqueue, D - dequeue, P - print, Q - exit");
        System.out.println("Enter command");
        char command;
        command = sc.next().charAt(0);

        while(command != 'Q'){
            queueManagement(command, myqueue, sc);
            System.out.println("Enter command");
            command = sc.next().charAt(0);
        }

        System.out.println("Bye, bye...");


    }
}
