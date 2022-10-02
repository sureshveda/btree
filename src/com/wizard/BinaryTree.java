package com.wizard;

import com.wizard.exception.NoParentFoundException;

import java.util.*;

public class BinaryTree {

    Node root;

    public BinaryTree(int rootKey, int rootVal) {
        root = new Node(rootKey, rootVal);
    }



    public void add(int parentKey, int myKey, int value) throws NoParentFoundException {
        Node parentNode = findNode(parentKey);

        if (parentNode!=null) {
            if (parentNode.left==null) {
                parentNode.left = new Node(myKey, value);
            } else if (parentNode.right == null) {
                parentNode.right = new Node(myKey, value);
            } else {
                System.out.println("Unable to find a leaf spot for child node with key: " + myKey);
                //Throw exception here
            }
        } else {
            //System.out.println("No parent node found for key  : " + parentKey);
            throw new NoParentFoundException();
        }
    }

    public Node findNode(int parentKey) {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        stack.push(root);

        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            if (current.key == parentKey) {
                return current;
            }

            if (current.right != null)
                stack.push(current.right);

            if (current.left != null)
                stack.push(current.left);
        }

        return null;
    }

    private Node addRecursive(Node current, int key, int value) {

        if (current == null) {
            return new Node(key, value);
        }

        if (value < current.value) {
            current.left = addRecursive(current.left, key, value);
        } else if (value > current.value) {
            current.right = addRecursive(current.right, key, value);
        }

        return current;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        return getSizeRecursive(root);
    }

    private int getSizeRecursive(Node current) {
        return current == null ? 0 : getSizeRecursive(current.left) + 1 + getSizeRecursive(current.right);
    }

    public boolean containsNode(int value) {
        return containsNodeRecursive(root, value);
    }

    private boolean containsNodeRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }

        if (value == current.value) {
            return true;
        }

        return value < current.value
                ? containsNodeRecursive(current.left, value)
                : containsNodeRecursive(current.right, value);
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private Node deleteRecursive(Node current, int value) {
        if (current == null) {
            return null;
        }

        if (value == current.value) {
            // Case 1: no children
            if (current.left == null && current.right == null) {
                return null;
            }

            // Case 2: only 1 child
            if (current.right == null) {
                return current.left;
            }

            if (current.left == null) {
                return current.right;
            }

            // Case 3: 2 children
            int smallestValue = findSmallestValue(current.right);
            current.value = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value < current.value) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }

        current.right = deleteRecursive(current.right, value);
        return current;
    }

    private int findSmallestValue(Node root) {
        return root.left == null ? root.value : findSmallestValue(root.left);
    }

    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            visit(node.value);
            traverseInOrder(node.right);
        }
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            System.out.println(node);
            traversePreOrder(node.left);
            traversePreOrder(node.right);
        }
    }

    public void traversePostOrder(Node node) {
        if (node != null) {
            traversePostOrder(node.left);
            traversePostOrder(node.right);
            visit(node.value);
        }
    }

    public void traverseLevelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {

            Node node = nodes.remove();

            System.out.print(" " + node.value);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.right != null) {
                nodes.add(node.right);
            }
        }
    }

    public void traverseInOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            Node top = stack.pop();
            visit(top.value);
            current = top.right;
        }
    }

    public void traversePreOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        stack.push(root);

        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            visit(current.value);

            if (current.right != null)
                stack.push(current.right);

            if (current.left != null)
                stack.push(current.left);
        }
    }

    public List<Node> getNodeList() {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        stack.push(root);
        List<Node> nodeList = new ArrayList<>();
        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            nodeList.add(current);

            if (current.right != null)
                stack.push(current.right);

            if (current.left != null)
                stack.push(current.left);
        }
        return  nodeList;
    }


    public int computeAndValue() {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        stack.push(root);
        List<Integer> values = new ArrayList<>();
        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            values.add(current.value);
            if (current.right != null)
                stack.push(current.right);

            if (current.left != null)
                stack.push(current.left);
        }

        int retVal = values.get(0);
        for (Integer val : values) {
            retVal = (retVal & val);
        }
        return retVal;
    }


    public void traversePostOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node prev = root;
        Node current = root;
        stack.push(root);

        while (current != null && !stack.isEmpty()) {
            current = stack.peek();
            boolean hasChild = (current.left != null || current.right != null);
            boolean isPrevLastChild = (prev == current.right || (prev == current.left && current.right == null));

            if (!hasChild || isPrevLastChild) {
                current = stack.pop();
                visit(current.value);
                prev = current;
            } else {
                if (current.right != null) {
                    stack.push(current.right);
                }
                if (current.left != null) {
                    stack.push(current.left);
                }
            }
        }
    }

    private void visit(int value) {
        System.out.print(" " + value);
    }

    class Node {
        int key;
        int value;
        Node left;
        Node right;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            right = null;
            left = null;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return key == node.key &&
                    value == node.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
