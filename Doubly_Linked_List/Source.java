// Aleksandra Wnuk
// metody programowania - labolatorium - zadanie 5
// lista podwojnie wiazana, bez glowy, ze wskaznikiem tylko na pierwszy
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
class LinkList{
    private Link first;

    //konsturktor, pusta lista
    public LinkList(){
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


}
public class Source {

    static public void invoke(String command, Scanner sc, LinkList list){
        switch (command){
            case "empty": {
                System.out.println("List is empty -> " + list.empty());
                break;
            }
            case "display": {
                System.out.print("Your list -> ");
                list.display();
                break;
            }
            case "add": {
                int val = sc.nextInt();
                list.addElem(val);
                break;
            }
            case "delete": {
                int val = sc.nextInt();
                list.delete(val);
                break;
            }
            case "deleteAll": {
                int val = sc.nextInt();
                list.deleteAll(val);
                break;
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LinkList list = new LinkList();
        String command;
        System.out.println("------------- OPERATIONS ON LIST -------------");
        System.out.println("Your list is now created and empty.");
        System.out.println("You can perform some operations on your list,\navaliable commands:");
        System.out.println();
        System.out.println("empty - checks if list is empty");
        System.out.println("display - displays elements in your list");
        System.out.println("add 'val'- adds element of value 'val' to your list (at the end)");
        System.out.println("delete 'val' - deletes element of vale 'val' from list (first occuring) ");
        System.out.println("deleteAll 'val' - deletes all elements of value 'val' from list");
        System.out.println("exit - exits program");
        System.out.println();
        do{
            System.out.println("enter command");
            command = sc.next();
            invoke(command, sc, list);
        }while(!command.equals("exit"));
        System.out.println("Bye!");
    }
}
