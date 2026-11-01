package by.it.group551002.marochkov.lesson12;



import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    enum Colour {RED,BLACK}



    private static class Node{
        int key;
        String value;
        Colour nodeColour = Colour.BLACK;
        Node left;
        Node right;
        Node parent;


        Node(Integer key,String value,Node nil){
            this.key = key;
            this.value = value;
            this.nodeColour = Colour.RED;
            this.left = nil;
            this.right = nil;
            this.parent = nil;
        }


    }

    private Node root;
    private Node nil;
    private int size;

    public MyRbMap() {
        nil = new Node(0, null,null);
        nil.nodeColour = Colour.BLACK;
        nil.left = nil.right = nil.parent = nil;
        root = nil;
    }




    private Node findKey(Object key){
        Integer k = (Integer) key;
        Node curr = root;
        while(curr != nil){
            if(k == curr.key) return curr;
            if(k < curr.key){
                curr = curr.left;
            } else{
                curr = curr.right;
            }
        }
        return null;
    }



    private void rotateRight(Node x){
        Node y = x.left;
        x.left = y.right;
        if (y.right != nil) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == nil) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;

    }


    private void rotateLeft(Node x){
            Node y = x.right;
            x.right = y.left;
            if (y.left != nil) y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == nil) root = y;
            else if (x == x.parent.left) x.parent.left = y;
            else x.parent.right = y;
            y.left = x;
            x.parent = y;

    }

    private void balanceInsert(Node node) {
        while (node.parent.nodeColour == Colour.RED) {
            Node grand = node.parent.parent;
            if (node.parent == grand.left) {
                Node uncle = grand.right;
                if (uncle.nodeColour == Colour.RED) {
                    node.parent.nodeColour = Colour.BLACK;
                    uncle.nodeColour = Colour.BLACK;
                    grand.nodeColour = Colour.RED;
                    node = grand;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.nodeColour = Colour.BLACK;
                    node.parent.parent.nodeColour = Colour.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = grand.left;
                if (uncle.nodeColour == Colour.RED) {
                    node.parent.nodeColour = Colour.BLACK;
                    uncle.nodeColour = Colour.BLACK;
                    grand.nodeColour = Colour.RED;
                    node = grand;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.nodeColour = Colour.BLACK;
                    node.parent.parent.nodeColour = Colour.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.nodeColour = Colour.BLACK;
    }

    @Override
    public String put(Integer key,String value){
        Node curr = root;
        Node parent = nil;
        while (curr != nil) {
            parent = curr;
            if (key < curr.key) curr = curr.left;
            else if (key > curr.key) curr = curr.right;
            else {
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value,nil);
        newNode.parent = parent;
        if (parent == nil) root = newNode;
        else if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;
        size++;
        balanceInsert(newNode);
        return null;
    }



    private int getChildrenCount(Node node){
        int count = 0;
        if(node.left != nil) count++;
        if(node.right != nil) count++;
        return count;
    }

    private Node getChildOrMock(Node node){
        return node.left != nil ? node.left : node.right;
    }

    private void transplantNode(Node toNode,Node fromNode){
        if(toNode == root) root = fromNode;
        else if(toNode == toNode.parent.left) toNode.parent.left = fromNode;
        else toNode.parent.right = fromNode;
        fromNode.parent = toNode.parent;
    }

    private void balanceRemove(Node node){
        while(node != root && node.nodeColour == Colour.BLACK){
            Node brother;
            if(node == node.parent.left){
                brother = node.parent.right;
                if(brother.nodeColour == Colour.RED){
                    brother.nodeColour = Colour.BLACK;
                    node.parent.nodeColour = Colour.RED;
                    rotateLeft(node.parent);
                    brother = node.parent.right;
                }
                if(brother.left.nodeColour == Colour.BLACK && brother.right.nodeColour == Colour.BLACK){
                    brother.nodeColour = Colour.RED;
                    node = node.parent;
                }else{
                    if(brother.right.nodeColour == Colour.BLACK){
                        brother.left.nodeColour = Colour.BLACK;
                        brother.nodeColour = Colour.RED;
                        rotateRight(brother);
                        brother = node.parent.right;
                    }
                    brother.nodeColour = node.parent.nodeColour;
                    node.parent.nodeColour = Colour.BLACK;
                    brother.right.nodeColour = Colour.BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            }else{
                brother = node.parent.left;
                if(brother.nodeColour == Colour.RED){
                    brother.nodeColour = Colour.BLACK;
                    node.parent.nodeColour = Colour.RED;
                    rotateRight(node.parent);
                    brother = node.parent.left;
                }
                if(brother.left.nodeColour == Colour.BLACK && brother.right.nodeColour == Colour.BLACK){
                   brother.nodeColour = Colour.RED;
                   node = node.parent;
                }else{
                    if(brother.left.nodeColour == Colour.BLACK){
                        brother.right.nodeColour = Colour.BLACK;
                        brother.nodeColour = Colour.RED;
                        rotateLeft(brother);
                        brother = node.parent.left;
                    }
                    brother.nodeColour = node.parent.nodeColour;
                    node.parent.nodeColour = Colour.BLACK;
                    brother.left.nodeColour = Colour.BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.nodeColour = Colour.BLACK;
    }





    @Override
    public String remove(Object key){
        Integer k = (Integer) key;
        Node nodeToDelete = findKey(k);
        if(nodeToDelete == null) return null;
        size--;
        String removed = nodeToDelete.value;
        Colour removedNodeColour = nodeToDelete.nodeColour;
        Node child;
        if(getChildrenCount(nodeToDelete) < 2){
            child = getChildOrMock(nodeToDelete);
            transplantNode(nodeToDelete,child);
        }else{
            Node minNode = findKey(getMin(nodeToDelete.right));
            nodeToDelete.key = minNode.key;
            nodeToDelete.value = minNode.value;
            removedNodeColour = minNode.nodeColour;
            child = getChildOrMock(minNode);
            transplantNode(minNode,child);
        }
        if(removedNodeColour == Colour.BLACK) balanceRemove(child);
        return removed;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }


    private Integer getMin(Node node){
        if(node.left == nil) return node.key;
        return getMin(node.left);
    }

    private Integer getMax(Node node) {
        if(node.right == nil) return node.key;
        return getMax(node.right);
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
        root = nil;
        size = 0;
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



    private void printBranch(Node node, StringBuilder str) {
        if (node == nil) return;
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


    private boolean containsString(Node node,Object value){
        if (node == nil) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsString(node.left, value) || containsString(node.right, value);
    }

    @Override
    public boolean containsValue(Object value){
        return containsString(root,value);
    }



    private void collectHead(Node node, int toKey, MyRbMap result) {
        if (node == nil) return;
        collectHead(node.left, toKey, result);
        if (node.key < toKey) {
            result.put(node.key, node.value);
            collectHead(node.right, toKey, result);
        }

    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);
        return result;
    }

    private void collectTail(Node node, int fromKey, MyRbMap result) {
        if (node == nil) return;
        if (node.key >= fromKey) {
            collectTail(node.left, fromKey, result);
            result.put(node.key, node.value);
        }

        collectTail(node.right, fromKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);
        return result;
    }





}
