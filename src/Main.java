// Mikhail Trifonov
// Ideas for rotating, balancing and insertion were taken from Lecture 07 (slide № 45), Tutorial 07 (slide № 16), and Wiki
// Idea about finding new height using maximum was taken from the lab by Alaa Aldin

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static ArrayList<ArrayList<Node<Integer>>> res = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        AVLTree<Integer> avlTree = new AVLTree<>();
        int mx = -10000;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            mx = Math.max(mx, value);
            nums[i] = value;
            avlTree.insert(value);
        }
        for (int i = 0; i < mx + 1; i++) {
            res.add(null);
        }

        System.out.println(n);

        printResult(avlTree.getRoot());
        for (int i = 0; i < n; i++) {
            ArrayList<Node<Integer>> nodes = res.get(nums[i]);
            StringBuilder line = new StringBuilder();
            line.append(nums[i]).append(" ");
            int ind1 = nodes.get(0) == null ? -1 : findIndexByElement(nums, nodes.get(0).getValue(), n) + 1;
            int ind2 = nodes.get(1) == null ? -1 : findIndexByElement(nums, nodes.get(1).getValue(), n) + 1;
            line.append(ind1).append(" ");
            line.append(ind2);
            System.out.println(line);
        }

        System.out.println(findIndexByElement(nums, avlTree.getRoot().getValue(), n) + 1);
    }

    private static int findIndexByElement(int[] nums, int a, int n) {
        for (int i = 0; i < n; i++) {
            if (nums[i] == a) return i;
        }
        return -1;
    }

    private static void printResult(Node<Integer> root) {
        printOneNode(root);
    }

    private static void printOneNode(Node<Integer> node) {
        if (node != null) {
            ArrayList<Node<Integer>> nodes = new ArrayList<>();
            nodes.add(node.getLeft());
            nodes.add(node.getRight());
            res.set(node.getValue(), nodes);
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

    public Node(T value) {
        this.value = value;
        this.height = 1;
        this.left = null;
        this.right = null;
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

    public void insert(T value) {
        root = insert(root, value);
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null)
            return new Node<>(value);
        if (value.compareTo(node.getValue()) < 0)
            node.setLeft(insert(node.getLeft(), value));
        else if (value.compareTo(node.getValue()) > 0)
            node.setRight(insert(node.getRight(), value));

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
