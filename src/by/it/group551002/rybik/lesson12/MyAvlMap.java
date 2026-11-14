package by.it.group551002.rybik.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {


    int size = 0;
    Node root = null;

    private class Node {

        int heigth;
        Integer key;
        String value;
        Node left;
        Node right;



        Node(Integer key, String value){
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.heigth = 0;
        }

    }

    private int newHeigth(Node node){
        int left = (node.left == null) ? -1 : node.left.heigth;
        int right = (node.right == null) ? -1 : node.right.heigth;
        return 1 + Math.max(left, right);
    }

    private int balanceFactor(Node node){
        int left = (node.left == null) ? -1 : node.left.heigth;
        int right = (node.right == null) ? -1 : node.right.heigth;
        return left - right;
    }


    private Node rightRotate (Node node){
        Node temp = (node.left).right;
        node.left.right = node;

        Node newParent = node.left;
        node.left = temp;

        node.heigth = newHeigth(node);
        newParent.heigth = newHeigth(newParent);

        return newParent;
    }

    private Node leftRotate (Node node){
        Node temp = (node.right).left;
        node.right.left = node;

        Node newParent = node.right;
        node.right = temp;

        node.heigth = newHeigth(node);
        newParent.heigth = newHeigth(newParent);

        return newParent;
    }


    private Node findNode( Node currentNode, Integer key){

        if (currentNode == null) return null;

        if(currentNode.key > key){
            return findNode(currentNode.left, key);
        } else if (currentNode.key < key){
            return findNode(currentNode.right, key);
        } else {
            return currentNode;
        }
    }


    private void inorder(Node node, StringBuilder sb){
        if (node == null) return;

        inorder(node.left, sb);

        sb.append(node.key);
        sb.append("=");
        sb.append(node.value);
        sb.append(", ");

        inorder(node.right, sb);
    }

    public String toString(){
        if (size == 0) return "{}";

        StringBuilder sb = new StringBuilder("{");

        inorder(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        String res = null;

        if (root == null) {
            root = new Node(key, value);
            size++;
            return res;
        }


        Node prevNode = root;
        Node currentNode = root;

        ArrayDeque<Node> stack = new ArrayDeque<Node>();


         while (currentNode != null) {

             stack.add(currentNode);

             if (currentNode.key > key) {
                 prevNode = currentNode;
                 currentNode = currentNode.left;

             } else if (currentNode.key < key) {
                 prevNode = currentNode;
                 currentNode = currentNode.right;
             } else {
                 break;
             }
         }

         if (currentNode != null){
             res = currentNode.value;
             currentNode.value = value;

         } else {

             Node newNode = new Node(key, value);

             if (key < prevNode.key ) {
                 prevNode.left = newNode;
             } else {
                 prevNode.right = newNode;
             }


             while (!stack.isEmpty()){
                 Node current = stack.pollLast();
                 Node parent = stack.peekLast();

                 Node temp = current;

                 if( balanceFactor(current) == -2){
                     if (balanceFactor(current.right) > 0)
                         current.right = rightRotate(current.right);

                     temp = leftRotate(current);

                 } else if (balanceFactor(current)  == 2){
                     if (balanceFactor(current.left) < 0)
                         current.left = leftRotate(current.left);

                     temp = rightRotate(current);

                 }

                 if (temp != current){
                     if (parent == null){
                         root = temp;
                     } else if (parent.left == current){
                         parent.left = temp;
                     } else if (parent.right == current) {
                         parent.right = temp;
                     }
                 }

                 current.heigth = newHeigth(current);
             }


             size++;
         }

        return res;
    }

    @Override
    public String remove(Object key) {

        Node currentNode = root;
        ArrayDeque<Node> stack = new ArrayDeque<Node>();


        while (currentNode != null) {
            stack.add(currentNode);

            if (currentNode.key > (Integer) key) {
                currentNode = currentNode.left;

            } else if (currentNode.key < (Integer) key) {
                currentNode = currentNode.right;
            } else {
                break;
            }
        }

        if (currentNode == null) return null;

        stack.pollLast();
        Node toDelete = stack.peekLast();

        if (currentNode.right != null) {
            Node temp = currentNode.right;
            Node prevTemp = temp;
            while (temp != null){
                stack.add(temp);
                prevTemp = temp;
                temp = temp.left;
            }

            stack.peekLast().left = prevTemp.right;

            if (toDelete.left == currentNode){
                toDelete.left = prevTemp;
            } else {
                toDelete.right = prevTemp;
            }

        } else if (currentNode.left != null){
            if (toDelete.left == currentNode){
                toDelete.left = currentNode.left;
            } else {
                toDelete.right = currentNode.left;
            }
        } else {
            if (toDelete.left == currentNode){
                toDelete.left = null;
            } else {
                toDelete.right = null;
            }
        }


        while (!stack.isEmpty()){

            Node current = stack.pollLast();
            Node parent = stack.peekLast();

            Node temp = current;

            if( balanceFactor(current) == -2){
                if (balanceFactor(current.right) > 0)
                    current.right = rightRotate(current.right);

                temp = leftRotate(current);

            } else if (balanceFactor(current)  == 2){
                if (balanceFactor(current.left) < 0)
                    current.left = leftRotate(current.left);

                temp = rightRotate(current);

            }

            if (temp != current){
                if (parent == null){
                    root = temp;
                } else if (parent.left == current){
                    parent.left = temp;
                } else if (parent.right == current) {
                    parent.right = temp;
                }
            }

            current.heigth = newHeigth(current);
        }

        size--;
        return currentNode.value;
    }

    @Override
    public String get(Object key) {

        Node currentNode = root;


        while (currentNode != null) {

            if (currentNode.key > (Integer) key) {
                currentNode = currentNode.left;

            } else if (currentNode.key < (Integer) key) {
                currentNode = currentNode.right;
            } else {
                break;
            }
        }

        return (currentNode == null) ? null : currentNode.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Node currentNode = root;

        while (currentNode != null) {

            if (currentNode.key > (Integer) key) {
                currentNode = currentNode.left;

            } else if (currentNode.key < (Integer) key) {
                currentNode = currentNode.right;
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }


    ///
    ///
    ///


    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
