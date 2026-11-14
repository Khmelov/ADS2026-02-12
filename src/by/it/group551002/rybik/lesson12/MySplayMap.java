package by.it.group551002.rybik.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;

        Node left;
        Node right;
        Node parent;


        Node(Integer key, String value){
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            parent = null;
        }
    }

    Node root = null;
    int size = 0;

    private void zig_zig(Node node, boolean isLeft){

        if (isLeft) {
            Node b = node.right;
            Node c = node.parent.right;

            node.right = node.parent;
            node.right.left = b;
            if (b != null)   b.parent = node.right;
            node.right.right = node.parent.parent;
            node.right.right.left = c;
            if (c != null)   c.parent = node.right.right;

            Node newParent = node.parent.parent.parent;
            node.parent.parent.parent = node.parent;
            node.parent.parent = node;
            node.parent = newParent;

            if (newParent == null){
                root = node;
            } else {
                if (newParent.left == node.right.right){
                    newParent.left = node;
                } else {
                    newParent.right = node;
                }
            }


        } else {
            Node b = node.left;
            Node c = node.parent.left;

            node.left = node.parent;
            node.left.right = b;
            if (b != null)   b.parent = node.left;
            node.left.left = node.parent.parent;
            node.left.left.right = c;
            if (c != null)   c.parent = node.left.left;

            Node newParent = node.parent.parent.parent;
            node.parent.parent.parent = node.parent;
            node.parent.parent = node;
            node.parent = newParent;

            if (newParent == null){
                root = node;
            } else {
                if (newParent.left == node.left.left){
                    newParent.left = node;
                } else {
                    newParent.right = node;
                }
            }
        }
    }

    private void zig_zag (Node node, boolean isLeft) {
        if (isLeft) {

            Node b = node.left;
            Node c = node.right;

            node.left = node.parent;
            node.left.right = b;
            if (b != null) b.parent = node.left;

            node.right = node.parent.parent;
            node.right.left = c;
            if (c != null) c.parent = node.right;


            node.parent = node.parent.parent.parent;
            if (node.parent == null)
                root = node;
            else {
                if (node.parent.right == node.right){
                    node.parent.right = node;
                } else {
                    node.parent.left = node;
                }
            }
            node.left.parent = node;
            node.right.parent = node;

        } else {
            Node b = node.right;
            Node c = node.left;

            node.right = node.parent;
            node.right.left = b;
            if (b != null) b.parent = node.right;

            node.left = node.parent.parent;
            node.left.right = c;
            if (c != null) c.parent = node.left;


            node.parent = node.parent.parent.parent;
            if (node.parent == null)
                root = node;
            else {
                if (node.parent.right == node.left){
                    node.parent.right = node;
                } else {
                    node.parent.left = node;
                }
            }

            node.left.parent = node;
            node.right.parent = node;
        }
    }

    private void zig(Node node, boolean isLeft) {

        if (isLeft) {
            if (node.right != null) node.right.parent = node.parent;
            node.parent.left = node.right;
            node.right = node.parent;
            node.right.parent = node;
            node.parent = null;
        } else {
            if (node.left != null) node.left.parent = node.parent;
            node.parent.right = node.left;
            node.left = node.parent;
            node.left.parent = node;
            node.parent = null;
        }
        root = node;
    }

    private void splay (Node node){
        while (node.parent != null) {
            Node p = node.parent;
            Node g = p.parent;

            boolean isLeft = (p.left == node);

            if (g == null) {
                zig(node, isLeft);
            } else {
                boolean pLeft = (g.left == p);
                if (isLeft == pLeft) {
                    zig_zig(node, pLeft);
                } else {
                    zig_zag(node, pLeft);
                }
            }
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



    public String toString() {
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
        if(root == null) {
            Node newNode = new Node(key, value);
            root = newNode;
            size++;
            return res;
        }

        Node current = root;
        Node prev = root;

        while (current != null){
            prev = current;
            if (key > current.key){
                current = current.right;
            } else if (key < current.key) {
                current = current.left;
            } else {
                res = current.value;
                break;
            }
        }

        if (current != null) {
            current.value = value;
            splay(current);
        } else {
            Node newNode = new Node(key, value);
            newNode.parent = prev;
            if (key < prev.key ) {
                prev.left = newNode;
            } else {
                prev.right = newNode;
            }
            splay(newNode);
            size++;
        }

        return res;
    }

    @Override
    public String get(Object key) {

        Node current = root;
        while (current != null){
            if ( (Integer) key < current.key) {
                current = current.left;
            } else if ( (Integer) key > current.key ){
                current = current.right;
            } else {
                break;
            }
        }

        return (current != null) ? current.value : null;
    }

    @Override
    public String remove(Object key) {

        Node current = root;
        while (current != null){
            if ( (Integer) key < current.key) {
                current = current.left;
            } else if ( (Integer) key > current.key ){
                current = current.right;
            } else {
                break;
            }
        }

        if (current == null) return null;
        splay(current);
        String res = current.value;

        if (root.left == null){
            root = root.right;
            if (root != null) root.parent = null;
        } else {
            Node newRoot = root.right;
            if (newRoot != null) newRoot.parent = null;

            root = root.left;
            root.parent = null;
            Node prevTemp = root;
            Node temp = root;

            while (temp != null) {
                prevTemp = temp;
                temp = temp.right;
            }
            splay(prevTemp);
            root.right = newRoot;
            if (newRoot != null) newRoot.parent = root;

        }
        size--;
        return res;
    }

    @Override
    public boolean containsKey(Object key) {
        Node current = root;
        while (current != null){
            if ( (Integer) key < current.key) {
                current = current.left;
            } else if ( (Integer) key > current.key ){
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }


    private boolean findValue(Node node, Object value){
        if (node == null) return false;

        if (node.value == value) return true;
        return (findValue(node.left, value) || findValue(node.right, value));
    }


    @Override
    public boolean containsValue(Object value) {

        return findValue(root, value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private void addToMap(Node node, MySplayMap map){

        if (node == null) return;

        addToMap(node.left, map);
        addToMap(node.right, map);

        map.put(node.key, node.value);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {

        MySplayMap newMap = new MySplayMap();
        Node cur = root;

        while (cur != null) {
            if (cur.key < toKey) {
                newMap.put(cur.key, cur.value);
                addToMap(cur.left, newMap);
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return newMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {

        MySplayMap newMap = new MySplayMap();
        Node cur = root;

        while (cur != null) {
            if (cur.key >= fromKey) {
                newMap.put(cur.key, cur.value);
                addToMap(cur.right, newMap);
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return newMap;
    }

    @Override
    public Integer firstKey() {
        if (size == 0) return null;

        Node temp = root;
        while (temp.left != null){
            temp = temp.left;
        }
        return temp.key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) return null;

        Node temp = root;
        while (temp.right != null){
            temp = temp.right;
        }
        return temp.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key < key) {
                res = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key <= key) {
                res = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key >= key) {
                res = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key > key) {
                res = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }


    /// //////////////////
    /// /
    ///
    ///


    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }






    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }



    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }





    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
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
