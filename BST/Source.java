import java.util.Scanner;

class Node{
    public Node leftNode;
    public Node rightNode;
    public int value;

    public Node(int value){
        this.value = value;
        leftNode = null;
        rightNode = null;
    }

    public void displayNode(){
        System.out.print("[" + value + "]");
    }
}

class Tree{
    public Node root;

    public Tree(){
        root = null;
    }

    public void addNode(int value){
        Node newNode = new Node(value);
        if(root == null){
            root = newNode;
        } else {
            Node node = root;
            while(true){
                if(value == node.value)
                    return;
                else if(value < node.value) {
                    if (node.leftNode == null){
                        node.leftNode = newNode;
                        return;
                    }
                    else
                    node = node.leftNode;
                } else {
                    if(node.rightNode == null) {
                        node.rightNode = newNode;
                        return;
                    }
                    else
                        node = node.rightNode;
                }
            }
        }
    }

    public Node search(int value){
        Node node = root;
        while(node != null){
            if(value == node.value)
                return node;
            else if(value < node.value)
                node = node.leftNode;
            else
                node = node.rightNode;
        }
        return null;
    }
}

public class Source {

    public static void displayTree(Node node, int h){
        if(node != null){
            displayTree(node.rightNode, h + 4);
            for(int i = 0 ; i < h ; i++){
                System.out.print(" ");
            }
            System.out.println("[" + node.value + "]");
            displayTree(node.leftNode, h + 4);
        }
    }

    public static void main(String[] args) {

        Tree tree = new Tree();
        tree.addNode(5);
        tree.addNode(1);
        tree.addNode(8);
        tree.addNode(2);
        tree.addNode(9);
        tree.addNode(7);
        tree.addNode(3);
        displayTree(tree.root, 0);
        System.out.println("-------------");
        Node search = tree.search(9);
        search.displayNode();
        System.out.println("-------------");

    }
}
