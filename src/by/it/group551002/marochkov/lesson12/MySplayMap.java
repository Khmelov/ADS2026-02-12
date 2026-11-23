package by.it.group551002.marochkov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer,String> {

    private static class Node{
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;


        Node(Integer key,String value){
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    Node root;
    int size;
    String oldValue;


    private void rotateRight(Node x){
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;

    }


    private void rotateLeft(Node x){
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;

    }

    private void rotateUp(Node node){
        if(node == node.parent.left){
            rotateRight(node.parent);
        }else{
            rotateLeft(node.parent);
        }
    }

    private void splay(Node node){
        while(node.parent != null){
            Node parent = node.parent;
            Node grand = parent.parent;

            if(grand == null){
                rotateUp(node);
            }else if((node == parent.left) == (parent == grand.left)){
                rotateUp(parent);
                rotateUp(node);
            }else{
                rotateUp(node);
                rotateUp(node);
            }
        }
        root = node;
    }

    private Integer getMin(Node node){
        if(node.left == null) return node.key;
        return getMin(node.left);
    }

    private Integer getMax(Node node) {
        if(node.right == null) return node.key;
        return getMax(node.right);
    }



    private void printBranch(Node node, StringBuilder str) {
        if (node == null) return;
        printBranch(node.left, str);
        if (str.length() > 1) str.append(", ");
        str.append(node.key).append("=").append(node.value);
        printBranch(node.right, str);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        printBranch(root, str);
        str.append("}");
        return str.toString();
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


    private Node findKey(Object key){
        int k = (Integer) key;
        Node curr = root;
        Node last = null;
        while(curr != null){
            last = curr;
            if(k == curr.key){
                splay(curr);
                return curr;
            }
            curr = (k < curr.key) ? curr.left : curr.right;
        }
        if(last != null) splay(last);
        return null;
    }




    @Override
    public Integer firstKey(){
        if(size == 0) throw new NoSuchElementException();
        return getMin(root);
    }

    @Override
    public Integer lastKey(){
        if(size == 0) throw new NoSuchElementException();
        return getMax(root);
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


    private boolean containsString(Node node, Object value){
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsString(node.left, value) || containsString(node.right, value);
    }

    @Override
    public boolean containsValue(Object value){
        return containsString(root,value);
    }



    @Override
    public String get(Object key){
        Node node = findKey(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key){
        Node node = findKey(key);
        return node != null;
    }


    @Override
    public String put(Integer key, String value){
        Node curr = root;
        Node parent = null;
        while(curr != null){
            parent = curr;
            if(key < curr.key) curr = curr.left;
            else if(key > curr.key) curr = curr.right;
            else {
                String old = curr.value;
                curr.value = value;
                splay(curr);
                return old;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if(parent == null) root = newNode;
        else if(key < parent.key) parent.left = newNode;
        else parent.right = newNode;
        size++;
        splay(newNode);
        return null;
    }


    @Override
    public String remove(Object key){
        Node node = findKey(key);
        if(node == null) return null;
        String removedValue = node.value;
        Node left = node.left;
        Node right = node.right;
        if(left != null) left.parent = null;
        if(right != null) right.parent = null;
        if(left == null){
            root = right;
        } else {
            root = left;
            Node max = left;
            while(max.right != null) max = max.right;
            splay(max);
            max.right = right;
            if(right != null) right.parent = max;
        }
        size--;
        return removedValue;

    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }


    @Override
    public Integer floorKey(Integer key){            // наибольший <= key
        int k = key;
        Node curr = root;
        Integer best = null;
        while(curr != null){
            if(curr.key == k) return curr.key;
            if(curr.key < k){ best = curr.key; curr = curr.right; }
            else curr = curr.left;
        }
        return best;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key){
        int k = key;
        Node curr = root;
        Integer best = null;
        while(curr != null){
            if(curr.key == k) return curr.key;
            if(curr.key > k){ best = curr.key; curr = curr.left; }
            else curr = curr.right;
        }
        return best;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key){
        int k = key;
        Node curr = root;
        Integer best = null;
        while(curr != null){
            if(curr.key < k){ best = curr.key; curr = curr.right; }
            else curr = curr.left;
        }
        return best;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key){
        int k = key;
        Node curr = root;
        Integer best = null;
        while(curr != null){
            if(curr.key > k){ best = curr.key; curr = curr.left; }
            else curr = curr.right;
        }
        return best;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    private void collectHead(Node node, int toKey, MySplayMap result) {
        if (node == null) return;
        collectHead(node.left, toKey, result);
        if (node.key < toKey) {
            result.put(node.key, node.value);
            collectHead(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        collectHead(root, toKey, result);
        return result;
    }

    private void collectTail(Node node, int fromKey, MySplayMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            collectTail(node.left, fromKey, result);
            result.put(node.key, node.value);
        }
        collectTail(node.right, fromKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        collectTail(root, fromKey, result);
        return result;
    }


}
