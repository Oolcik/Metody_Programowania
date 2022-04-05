// Aleksandra Wnuk - 6
//--------------------------------------------------------------------------------------------------------------------//
// metody programowania - BaCa - zadanie 4
// pociagi
//--------------------------------------------------------------------------------------------------------------------//
import java.util.Scanner;

//klasa reprezentujaca wagon z nazwa wagonu i referencjami poprzedniego i nastepnego wagonu w pociagu
class Carriage{
    public String name;
    public Carriage prev;
    public Carriage next;

    public Carriage(){
        name = "";
        prev = null;
        next = null;
    }

    //konstruktor przyjmujacy nazwe pociagu
    public Carriage(String name){
        this.name = name;
        prev = null;
        next = null;
    }

    public void displayCarriageName(){
        System.out.print(" " + name);
    }

}

//klasa reprezentujaca pociag z nazwa, referencja nastepnego pociagu i referencja pierwszego elelentu listy cykl. wagoow
class Train{
    public String name;
    public Train next;
    public Carriage first;  //referencja pierwszego elementu listy cyklicznej wagonow

    //pusty konstruktor
    public Train(){
        name = "";
        next = null;
        first = null;
    }

    //konstruktor przyjmujacy nazwe pociagu i nazwe pierwszego wagonu
    public Train(String name, String firstCarriagName){
        this.name = name;
        next = null;
        Carriage newCarriage = new Carriage(firstCarriagName);
        first = newCarriage;
        newCarriage.next = newCarriage;
        newCarriage.prev = newCarriage;
    }

    //uzywam wstawiania ostatniego wagonu i zmieniam referencje first
    public void insertFirstCarriage(String carName){
        insertLastCarriage(carName);
        first = first.prev;
    }

    //wstawianie ostatniego wagonu
    public void insertLastCarriage(String carName) {
        Carriage newCarriage = new Carriage(carName);
        //jezeli mamy cykl
        if(first.prev.prev == first && first.prev != first){
            newCarriage.next = first;
            newCarriage.prev = first.prev;
            first.prev.prev = newCarriage;
            first.prev = newCarriage;
        //gdy nie ma cyklu
        } else {
            newCarriage.next = first;
            first.prev.next = newCarriage;
            newCarriage.prev = first.prev;
            first.prev = newCarriage;
        }
    }
    
    public void deleteFirstCarriage(){
        Carriage carToDelete = first;
        first = first.next;
        if(first.next == carToDelete){
            Carriage tmp = first.next;
            first.next = first.prev;
            first.prev = tmp;
        }
        first.prev = carToDelete.prev;

        if(carToDelete.prev.next == carToDelete)
            carToDelete.prev.next = first;
        else
            carToDelete.prev.prev = first;
    }

    public void deleteLastCarriage(){
        Carriage elemToDelete = first.prev;
        if(elemToDelete.prev == first)
            first.prev = elemToDelete.next;
        else
            first.prev = elemToDelete.prev;

        if(first.prev.next == elemToDelete)
            first.prev.next = first;
        else
            first.prev.prev = first;
    }

    //zmieniamy referencje first na poprzedni i zamieniamy referencje next i prev (powstaja dwa cykle)
    public void reverseTrain(){
        if(first.prev.next == first) {
            first = first.prev;
            Carriage tmp = first.next;
            first.next = first.prev;
            first.prev = tmp;
        } else{
            first = first.prev;
        }
    }

    public void displayTrain(){
        Carriage currCar = first;
        currCar.displayCarriageName();
        Carriage prevCar = currCar;
        currCar = currCar.next;

        while(currCar != first){
            //System.out.print(" ");
            currCar.displayCarriageName();
            //przypadek gdy mielismy cykl i musimy wyswietlac nie po next tylko po prev
            if(currCar.next == prevCar) {
                prevCar = currCar;
                currCar = currCar.prev;
            //wyswietlanie po next
            } else{
                prevCar = currCar;
                currCar = currCar.next;
            }
        }
        System.out.println();
    }

    public boolean oneCarriageLeft(){
        return (first.next == first);
    }

    void union(Train trainToAppend){
        Carriage last = first.prev;
        first.prev = trainToAppend.first.prev;
        if(trainToAppend.first.prev.next == trainToAppend.first)
            trainToAppend.first.prev.next = first;
        else
            trainToAppend.first.prev.prev = first;
        trainToAppend.first.prev = last;
        if(last.next == first)
            last.next = trainToAppend.first;
        else
            last.prev = trainToAppend.first;
    }
}

//klasa reprezentujaca liste pociagow
class TrainsList{
    Train trains;  //referencja listy pociagow

    public TrainsList(){
        trains = null;
    }

    private Train findLastTrain(){
        Train lastTrain = trains;
        if(trains == null)
            return null;
        while(lastTrain.next != null)
            lastTrain = lastTrain.next;
        return lastTrain;
    }

    //liniowo znajduje pociag po nazwie i zwraca referencje
    private Train findTrain(String trainName){
        Train train = trains;
        while(train != null){
            if(train.name.equals(trainName))
                return train;
            train = train.next;
        }
        return null;
    }

    //dodaje nowy pociag na poczatek
    public void addNewTrain(String trainName, String carriageName){
        Train checkIfTrainAlreadExist = findTrain(trainName);
        if(checkIfTrainAlreadExist != null){
            System.out.println("Train " + trainName + " already exists");
            return;
        }
        Train newTrain = new Train(trainName, carriageName);
        if(trains == null)
            trains = newTrain;
        else{
            newTrain.next = trains;
            trains = newTrain;
        }
    }

    public void insertLastCarriageTo(String trainName, String carriageName){
        Train train = findTrain(trainName);
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
        } else{
            train.insertLastCarriage(carriageName);
        }
    }

    public void insertFirstCarriageTo(String trainName, String carriageName){
        Train train = findTrain(trainName);
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
        } else{
            train.insertFirstCarriage(carriageName);
        }
    }

    public void displayTrain(String trainName){
        Train train = findTrain(trainName);
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
        } else{
            System.out.print(train.name + ":");
            train.displayTrain();
        }
    }

    public void reverseTrain(String trainName){
        Train train = findTrain(trainName);
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
        } else{
            train.reverseTrain();
        }
    }

    public void displayListOfTrains(){
        if(trains != null) {
            Train train = trains;
            System.out.print("Trains:");
            while (train != null) {
                System.out.print(" " + train.name);
                train = train.next;
            }
            System.out.println();
        } else{
            System.out.println("Trains: ");
        }
    }

    void deleteFirstCarriageIn(String trainName, String newTrainName){
        if(trains == null){
            System.out.println("Train " + trainName + " does not exist");
            return;
        }
        //nie uzywam findtrain, bo potrzebuje znalezc referencje do dwoch pociagow, robie to na raz w jednej petli
        Train prev = null;
        Train train = null;
        if(trains.name.equals(trainName))
            train = trains;
        boolean newTrainAlreadyExists = false;
        Train tmp = trains;
        while(tmp != null) {
            if (tmp.next != null && tmp.next.name.equals(trainName)) {
                prev = tmp;
                train = prev.next;
            }
            if (tmp.name.equals(newTrainName))
                newTrainAlreadyExists = true;
            tmp = tmp.next;
        }
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
            return;
        }
        if(newTrainAlreadyExists){
            System.out.println("Train " + newTrainName + " already exists");
            return;
        }
        String newCarriageName = train.first.name;
        if(train == trains){
            if(train.oneCarriageLeft())
                trains = train.next;
            else
                train.deleteFirstCarriage();
        } else if(train.oneCarriageLeft()){
            prev.next = train.next;
        } else{
            train.deleteFirstCarriage();
        }
        addNewTrain(newTrainName, newCarriageName);
    }

    public void deleteLastCarriageIn(String trainName, String newTrainName){
        if(trains == null){
            System.out.println("Train " + trainName + " does not exist");
            return;
        }
        Train prev = null;
        Train train = null;
        if(trains.name.equals(trainName))
            train = trains;
        boolean newTrainAlreadyExists = false;
        Train tmp = trains;
        while(tmp != null) {
            if (tmp.next != null && tmp.next.name.equals(trainName)) {
                prev = tmp;
                train = prev.next;
            }
            if (tmp.name.equals(newTrainName))
                newTrainAlreadyExists = true;
            tmp = tmp.next;
        }
        if(train == null){
            System.out.println("Train " + trainName + " does not exist");
            return;
        }
        if(newTrainAlreadyExists){
            System.out.println("Train " + newTrainName + " already exists");
            return;
        }
        String newCarriageName = train.first.prev.name;
        if(train == trains){
            if(train.oneCarriageLeft())
                trains = train.next;
            else
                train.deleteLastCarriage();
        } else if(train.oneCarriageLeft()){
            prev.next = train.next;
        } else{
            train.deleteLastCarriage();
        }
        addNewTrain(newTrainName, newCarriageName);
    }

    void union(String firstTrainName, String secondTrainName){
        //if(firstTrainName.equals(secondTrainName)) return;
        Train train1 = null;
        Train train2 = null;
        Train prev2 = null;
        Train tmp = trains;
        while(tmp != null){
            if(tmp.name.equals(firstTrainName))
                train1 = tmp;
            if(tmp.name.equals(secondTrainName))
                train2 = tmp;
            if(tmp.next != null && tmp.next.name.equals(secondTrainName))
                prev2 = tmp;
            tmp = tmp.next;
        }

        if(train1 == null){
            System.out.println("Train " + firstTrainName + " does not exist");
            return;
        }
        if(train2 == null){
            System.out.println("Train " + secondTrainName + " does not exist");
            return;
        }

        train1.union(train2);
        if(train2 == trains) {
            trains = trains.next;
        } else{
            prev2.next = train2.next;
        }
    }
}



public class Source {
    //funkcja do wywolywania komend ze standardowego wejscia, przyjmuje linie i dzieli ja na komedy i parametry komend
    public static void callCommand(String command, TrainsList list){
        String[] parts = command.split(" ");
        switch(parts[0]){
            case "New":{
                list.addNewTrain(parts[1], parts[2]);
                break;
            }
            case "InsertFirst":{
                list.insertFirstCarriageTo(parts[1], parts[2]);
                break;
            }
            case "InsertLast":{
                list.insertLastCarriageTo(parts[1], parts[2]);
                break;
            }
            case "Display":{
                list.displayTrain(parts[1]);
                break;
            }
            case "Trains":{
                list.displayListOfTrains();
                break;
            }
            case "Reverse":{
                list.reverseTrain(parts[1]);
                break;
            }
            case "Union":{
                list.union(parts[1], parts[2]);
                break;
            }
            case "DelFirst":{
                list.deleteFirstCarriageIn(parts[1], parts[2]);
                break;
            }
            case "DelLast":{
                list.deleteLastCarriageIn(parts[1], parts[2]);
                break;
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int z = Integer.parseInt(sc.nextLine());
        while(z > 0){
            TrainsList list = new TrainsList();
            int n = Integer.parseInt(sc.nextLine());
            while(n > 0){
                String command = sc.nextLine();
                callCommand(command, list);
                n--;
            }
            z--;
        }

    }
}

//--------------------------------------------------------------------------------------------------------------------//
// < test.in
/*
1
111
New T1 w3
InsertLast T1 w4
InsertLast T1 w5
InsertLast T1 w6
InsertLast T1 w7
InsertFirst T1 w2
Reverse T1
InsertLast T1 w1
InsertFirst T1 w8
InsertFirst T1 w9
Reverse T1
InsertLast T1 w10
Reverse T1
InsertLast T1 w0
Reverse T1
Display T1
Trains
DelLast T1 a
Display T1
DelFirst T1 b
Display T1
DelFirst T1 c
Display T1
Reverse T1
Display T1
InsertLast T1 w1
Display T1
InsertLast T1 w0
Display T1
InsertFirst T1 w10
Display T1
Reverse T1
Display T1
DelFirst T1 0
DelFirst T1 1
DelFirst T1 2
DelFirst T1 3
DelFirst T1 4
DelFirst T1 5
DelFirst T1 6
DelFirst T1 7
DelFirst T1 8
DelFirst T1 9
DelFirst T1 10
Trains
Union 0 1
Union 0 2
Union 0 3
Reverse 0
Union 5 4
Union 5 0
Reverse 5
Union 5 6
Union 8 9
Union 7 8
Union 5 7
Reverse 5
Union 10 5
Reverse 10
Display 10
Trains
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Reverse 10
Display 10
Union 10 10
Trains
Display 10
Display c
DelFirst c c
New T a
InsertLast T b
Display T
Reverse T
Reverse T
InsertLast T c
Insert Last T d
Reverse T
Reverse T
Reverse T
InsertLast T A
InsertLast T B
Reverse T
InsertLast T e
Display T
New W w1
InsertLast W w2
InsertLast W w3
InsertLast W w4
Reverse W
DelLast W z
DelLast W x
Reverse W
InsertLast W w5
InsertFirst W w6
Reverse W
InsertLast W w7
InsertFirst W w8
Display W
 */
//--------------------------------------------------------------------------------------------------------------------//
// > test.out
/*
T1: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
Trains: T1
T1: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9
T1: w1 w2 w3 w4 w5 w6 w7 w8 w9
T1: w2 w3 w4 w5 w6 w7 w8 w9
T1: w9 w8 w7 w6 w5 w4 w3 w2
T1: w9 w8 w7 w6 w5 w4 w3 w2 w1
T1: w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
T1: w10 w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
T1: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
Trains: 10 9 8 7 6 5 4 3 2 1 0 c b a
10: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
Trains: 10 c b a
10: w10 w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
10: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
10: w10 w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
10: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
10: w10 w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
10: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
10: w10 w9 w8 w7 w6 w5 w4 w3 w2 w1 w0
10: w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10
Trains: c b a
Train 10 does not exist
c: w1
Train c already exists
T: a b
T: B A a b c e
W: w8 w5 w4 w3 w6 w7
 */
//--------------------------------------------------------------------------------------------------------------------//