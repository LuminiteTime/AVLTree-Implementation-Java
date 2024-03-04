// Mikhail Trifonov
// Ideas for rotating, balancing and insertion were taken from Lecture 07 (slide № 45), Tutorial 07 (slide № 16), and Wiki
// Idea about finding new height using maximum was taken from the lab by Alaa Aldin

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static private ArrayList<Node<Integer>> allNodes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        AVLTree<Integer> avlTree = new AVLTree<>();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            nums[i] = value;
            avlTree.insert(value, i);
        }

        for (int i = 0; i < n; i++) {
            allNodes.add(null);
        }

        System.out.println(n);

        printResult(avlTree.getRoot());
        for (int i = 0; i < n; i++) {
            Node<Integer> nodeNow = allNodes.get(i);
            int ind1 = nodeNow.getLeft() == null ? -1 : nodeNow.getLeft().getIndex() + 1;
            int ind2 = nodeNow.getRight() == null ? -1 : nodeNow.getRight().getIndex() + 1;
            System.out.println(nodeNow.getValue() + " " + ind1 + " " + ind2);
        }

        System.out.println(avlTree.getRoot().getIndex() + 1);
    }

    private static void printResult(Node<Integer> root) {
        printOneNode(root);
    }

    private static void printOneNode(Node<Integer> node) {
        if (node != null) {
            allNodes.set(node.getIndex(), node);
            printOneNode(node.getLeft());
            printOneNode(node.getRight());
        }
    }
}

class Node<T> {
    private T value;
    private int height;
    private Node<T> left;
    private Node<T> right;
    private int index;

    public Node(T value, int index) {
        this.value = value;
        this.height = 1;
        this.left = null;
        this.right = null;
        this.index = index;
    }

    public T getValue() {
        return value;
    }

    public int getHeight() {
        return height;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public int getIndex() {
        return index;
    }
}

class AVLTree<T extends Comparable<T>> {
    private Node<T> root;

    public AVLTree() {
        this.root = null;
    }

    public Node<T> getRoot() {
        return root;
    }

    private int notNullHeight(Node<T> node) {
        return (node == null) ? 0 : node.getHeight();
    }

    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.getLeft();
        Node<T> nodeForRotatingBETA = x.getRight();
        x.setRight(y);
        y.setLeft(nodeForRotatingBETA);
        y.setHeight(Math.max(notNullHeight(y.getLeft()), notNullHeight(y.getRight())) + 1);
        x.setHeight(Math.max(notNullHeight(x.getLeft()), notNullHeight(x.getRight())) + 1);
        return x;
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.getRight();
        Node<T> nodeForRotatingBETA = y.getLeft();
        y.setLeft(x);
        x.setRight(nodeForRotatingBETA);
        x.setHeight(Math.max(notNullHeight(x.getLeft()), notNullHeight(x.getRight())) + 1);
        y.setHeight(Math.max(notNullHeight(y.getLeft()), notNullHeight(y.getRight())) + 1);
        return y;
    }

    public void insert(T value, int index) {
        root = insert(root, value, index);
    }

    private Node<T> insert(Node<T> node, T value, int index) {
        if (node == null)
            return new Node<>(value, index);
        if (value.compareTo(node.getValue()) < 0)
            node.setLeft(insert(node.getLeft(), value, index));
        else if (value.compareTo(node.getValue()) > 0)
            node.setRight(insert(node.getRight(), value, index));

        node.setHeight(Math.max(notNullHeight(node.getLeft()), notNullHeight(node.getRight())) + 1);

        int balance = notNullHeight(node.getLeft()) - notNullHeight(node.getRight());

        if (balance > 1 && value.compareTo(node.getLeft().getValue()) < 0)
            return rotateRight(node);

        if (balance < -1 && value.compareTo(node.getRight().getValue()) > 0)
            return rotateLeft(node);

        if (balance > 1 && value.compareTo(node.getLeft().getValue()) > 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        if (balance < -1 && value.compareTo(node.getRight().getValue()) < 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }
}
