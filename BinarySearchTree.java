package CSE216Hw_4;

import java.util.Iterator;
import java.util.*;
import java.util.function.Consumer;

public class BinarySearchTree<T> implements Iterable {
    private String name;
    private Node root;
    public BinarySearchTree(String name) {
        this.name = name;
    }

    public <T extends Comparable<T>> void addAll(List<T> someList) {
        int i = 0;
        if (root == null){
            Node<T> temp =  new Node<T>(someList.get(i));
            root = temp;
            i=i+1;
        }
        for (int j = i; j< someList.size(); j++){
            root.addFromTop(someList.get(j));
        }
    }

    @Override
    public Iterator iterator() {
        return null;    // can leave alone
    }

    @Override
    public void forEach(Consumer action) {
        forEachRec(root, action);
    }
    public void forEachRec(Node<T> node,Consumer action) {
        if (node != null) {
            forEachRec(node.left, action);
            action.accept(node.value);
            forEachRec(node.right, action);
        }
    }

    public String toString(){
        String tempString = "[" + name + "] ";
        return traversePreOrder(root, tempString);
    }
    public String traversePreOrder(Node<T> node, String temp) {
        if (node != null) {
            temp = temp + node.value;
            if (node.left != null){
                temp = temp + " L:(";
                temp = traversePreOrder(node.left, temp);
                temp = temp + ")";
            }
            if (node.right != null){
                temp = temp + " R:(";
                temp = traversePreOrder(node.right, temp);
                temp = temp + ")";
            }
        }
        else{
        }
        return temp;
    }

    public class Node<T> {
        private T value;
        private Node left;
        private Node right;

        public Node(T value){
            this.value = value;
            left = null;
            right = null;
        }

        private <T extends Comparable<T>> Node<T> addRec(Node<T> node, T Value) {
            if (node == null) {
                return new Node(Value);
            }

            if (Value.compareTo(node.value) < 0) {
                node.left = addRec(node.left, Value);
            } else if (Value.compareTo(node.value) > 0) {
                node.right = addRec(node.right, Value);
            } else {
                // value already exists
                return node;
            }

            return node;
        }

        public <T extends Comparable<T>> void addFromTop(T value) {
            root = addRec(root, value);
        }
    }
    public static <T extends Comparable<T>> List<T> merge(BinarySearchTree<T> t1, BinarySearchTree<T> t2){
        List<T> templist = new ArrayList<>();

        selfThread thread1 = new selfThread(t1, templist);
        selfThread thread2 = new selfThread(t2, templist);

        thread1.start();
        thread2.start();
        try{
            thread1.join();
            thread2.join();
        }catch(InterruptedException ignored){}

        mergeSort(templist,0,templist.size()-1);
        return templist;
    }

    public static <T extends Comparable<T>> void mergeSort(List<T> list, int i, int j){
        if (i < j) {
            int m = i + (j - i) / 2;
            mergeSort(list, i, m);
            mergeSort(list, m + 1, j);
            mergeSortSort(list, i, m, j);
        }
    }
    public static <T extends Comparable<T>> void mergeSortSort(List<T> list, int x, int m, int y)
    {
        int size1 = m - x + 1;
        int size2 = y - m;

        List<T> firstList = new ArrayList<>();
        List<T> secondList = new ArrayList<>();

        for (int i = 0; i < size1; ++i)
            firstList.add(list.get(x+i));
        for (int j = 0; j < size2; ++j)
            secondList.add(list.get(m+1+j));

        int i = 0, j = 0;

        int k = x;
        while (i < size1 && j < size2) {
            if (firstList.get(i).compareTo(secondList.get(j))<= 0) {
                list.set(k, firstList.get(i));
                i++;
            }
            else {
                list.set(k, secondList.get(j));
                j++;
            }
            k++;
        }

        while (i < size1) {
            list.set(k, firstList.get(i));
            i++;
            k++;
        }

        while (j < size2) {
            list.set(k, secondList.get(j));
            j++;
            k++;
        }
    }

    public static class selfThread <T extends Comparable<T>> extends Thread {
        BinarySearchTree<T> t1;
        List<T> listTemp;

        public selfThread(BinarySearchTree<T> tree, List<T> list) {
            this.t1 = tree;
            this.listTemp = list;
        }

        @Override
        public void start() {
            t1.forEach(x -> listTemp.add((T) x));
        }
    }


    public static void main(String... args) {
// each tree has a name, provided to its constructor
        BinarySearchTree<Integer> t1 = new BinarySearchTree<>("Oak");
// adds the elements to t1 in the order 5, 3, 0, and then 9
        t1.addAll(Arrays.asList(5, 3, 0, 9));
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>("Maple");
// adds the elements to t2 in the order 9, 5, and then 10
        t2.addAll(Arrays.asList(9, 5, 10));
        System.out.println(t1); // see the expected output for exact format
        t1.forEach(System.out::println); // iteration in increasing order
        System.out.println(t2); // see the expected output for exact format
        t2.forEach(System.out::println); // iteration in increasing order
        BinarySearchTree<String> t3 = new BinarySearchTree<>("Cornucopia");
        t3.addAll(Arrays.asList("coconut", "apple", "banana", "plum", "durian",
                "no durians on this tree!", "tamarind"));
        System.out.println(t3); // see the expected output for exact format
        t3.forEach(System.out::println); // iteration in increasing order

        System.out.print(merge(t1,t2));
    }
}
