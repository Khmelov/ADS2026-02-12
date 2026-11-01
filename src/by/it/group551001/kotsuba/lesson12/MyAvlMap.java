package by.it.group551001.kotsuba.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private String tempValue;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size==0;
    }

    @Override
    public void clear(){
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key){
        return findNode(key) != null;
    }

    @Override
    public String get(Object key){
        Node node = findNode(key);
        return node != null? node.value : null;
    }

    @Override
    public String put(Integer key, String value){
        if(key ==null) return null;

        tempValue = null;
        root = put(root, key, value);
        return tempValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        tempValue = null;
        if (containsKey(key)) {
            root = remove(root, (Integer) key);
        }
        return tempValue;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private int height(Node n){
        return n==null? 0: n.height;
    }

    private int getBalanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node rotateRight(Node y){
        Node n1 = y.left;
        Node n2 = n1.right;
        n1.right = y;
        y.left = n2;

        updateHeight(y);
        updateHeight(n1);
        return n1;
    }

    private Node rotateLeft(Node y){
        Node n1 = y.right;
        Node n2 = n1.left;
        n1.left = y;
        y.right = n2;

        updateHeight(y);
        updateHeight(n1);
        return n1;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int balance = getBalanceFactor(n);

        if (balance > 1 && getBalanceFactor(n.left) >= 0) {
            return rotateRight(n);
        }

        if (balance > 1 && getBalanceFactor(n.left) < 0) {
            n.left = rotateLeft(n.left);
            return rotateRight(n);
        }

        if (balance < -1 && getBalanceFactor(n.right) <= 0) {
            return rotateLeft(n.right == null ? n : n);
        }

        if (balance < -1 && getBalanceFactor(n.right) > 0) {
            n.right = rotateRight(n.right);
            return rotateLeft(n);
        }

        return n;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            tempValue = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private Node remove(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            tempValue = node.value;
            size--;

            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node minNode = getMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = removeMin(node.right);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node findNode(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrderToString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderToString(node.right, sb);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
