package by.it.group551002.marochkov.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private class Node{
        int key;
        String data;
        Node left;
        Node right;
        int height;


        Node(Integer key,String value){
            this.key = key;
            this.data = value;
        }
    }

    private Node root;
    private int size;
    private String oldValue;
    private boolean found;
    private String removedValue;


    private int height(Node node){

        return (node == null) ? -1 : node.height;

    }

    private void updateHeight(Node node){
        node.height =  Math.max(height(node.left),height(node.right)) + 1;
    }

    private int balanceFactor(Node node){
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }

    private void swap(Node a,Node b){
        int a_key = a.key;
        a.key = b.key;
        b.key = a_key;
        String a_data = a.data;
        a.data = b.data;
        b.data = a_data;
    }


    private void rotateRight(Node node){
        swap(node,node.left);
        Node temp = node.right;
        node.right = node.left;
        node.left = node.right.left;
        node.right.left = node.right.right;
        node.right.right = temp;
        updateHeight(node.right);
        updateHeight(node);
    }


    private void rotateLeft(Node node){
        swap(node,node.right);
        Node temp = node.left;
        node.left = node.right;
        node.right = node.left.right;
        node.left.right = node.left.left;
        node.left.left = temp;
        updateHeight(node.left);
        updateHeight(node);
    }


    private Node balance(Node node){
        updateHeight(node);
        int balance = balanceFactor(node);
        if (balance == -2) {
            if (balanceFactor(node.left) == 1) rotateLeft(node.left);
            rotateRight(node);
        } else if (balance == 2) {
            if (balanceFactor(node.right) == -1) rotateRight(node.right);
            rotateLeft(node);
        }
        return node;
    }



    private Integer getMin(Node node){
        if(node.left == null) return node.key;
        return getMin(node.left);
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public void clear(){
        root = null;
        size = 0;
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

    private void printBranch(Node node, StringBuilder str) {
        if (node == null) return;
        printBranch(node.left, str);
        if (str.length() > 1) str.append(", ");
        str.append(node.key).append("=").append(node.data);
        printBranch(node.right, str);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        printBranch(root, str);
        str.append("}");
        return str.toString();
    }

        private Node findKey(Object key){
            Integer k = (Integer) key;
            Node curr = root;
            while(curr != null){
                if(k == curr.key) return curr;
                if(k < curr.key){
                    curr = curr.left;
                } else{
                    curr = curr.right;
                }
            }
            return null;
        }

    @Override
    public String get(Object key){
        Node node = findKey(key);
        return (node == null) ? null : node.data;
    }

    @Override
    public boolean containsKey(Object key){
        Node node = findKey(key);
        return node != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }


    private Node put(Node node,Integer key,String value){
        if(node == null){
            Node new_node = new Node(key,value);
            size++;
            return new_node;
        }

        if (key < node.key) {
            node.left = put(node.left, key, value);
        } else if (key > node.key) {
            node.right = put(node.right, key, value);
        } else {
            oldValue = node.data;
            node.data = value;
        }

        return balance(node);
    }

    @Override
    public String put(Integer key, String value){
        oldValue = null;
        root = put(root,key,value);


        return oldValue;
    }


    private Node remove(Node node,Integer key){
        if(node == null) return null;
        if(key > node.key){
            node.right = remove(node.right,key);
            return balance(node);
        }
        if(key < node.key){
            node.left = remove(node.left,key);
            return balance(node);
        }
        if(key == node.key && node.left == null){
            removedValue = node.data;
            found = true;
            node = node.right;
            return node;
        }
        if(key == node.key && node.right == null){
            removedValue = node.data;
            found = true;
            node = node.left;
            return node;

        }
        found = true;
        String old = node.data;
        Integer minKey = getMin(node.right);
        String minValue = get(minKey);
        node.key = minKey;
        node.data = minValue;
        node.right = remove(node.right, minKey);
        removedValue = old;
        return balance(node);
    }

    @Override
    public String remove(Object key){
        Integer k = (Integer) key;
        removedValue = null; found = false;
        root = remove(root,k);

        if(found){
            size--;
        }
        return  removedValue;

    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }


}
