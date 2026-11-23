package by.it.group551002.rybik.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private enum Color {
        RED,
        BLACK
    }
    private static class Node {

        Color color;
        Integer key;
        String value;

        Node left;
        Node right;
        Node parent;

        Node (Integer key, String value){
            this.key = key;
            this.value = value;
            this.color = Color.RED;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

    }

    private static Color getColor(Node n) {
        return (n == null) ? Color.BLACK : n.color;
    }

    private static void setColor(Node n, Color c) {
        if (n != null) n.color = c;
    }

    Node root = null;
    int size = 0;

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


    void leftRotate(Node n){

        Node b = n.right.left;
        if (b != null) b.parent = n;

        n.right.left = n;
        n.right.parent = n.parent;
        if ( n.parent != null ) {
            if (n.parent.left == n) {
                n.parent.left = n.right;
            } else {
                n.parent.right = n.right;
            }
        } else {
            this.root = n.right;
        }

        n.parent = n.right;
        n.right = b;

    }

    void rightRotate(Node n) {

        Node b = n.left.right;
        if (b != null) b.parent = n;

        n.left.right = n;
        n.left.parent = n.parent;
        if ( n.parent != null ) {
            if (n.parent.right == n) {
                n.parent.right = n.left;
            } else {
                n.parent.left = n.left;
            }
        } else {
            this.root = n.left;
        }

        n.parent = n.left;
        n.left = b;
    }




    @Override
    public String put(Integer key, String value) {

        if (root == null){
            Node newNode = new Node(key, value);
            newNode.color = Color.BLACK;
            root = newNode;
            size++;
            return null;
        }


        Node n = root;
        Node p = root;
        while (n != null){
            p = n;
            if (key > n.key) {
                n = n.right;
            } else if (key < n.key) {
                n = n.left;
            } else {
                String res = n.value;
                n.value = value;
                return res;
            }
        }

        Node newNode = new Node(key, value);
        if (key > p.key) {
            p.right = newNode;
        } else {
            p.left = newNode;
        }
        newNode.parent = p;
        size++;

        Node cur = newNode;

        while (getColor(cur.parent) == Color.RED) {
            Node par = cur.parent;
            Node g = par.parent;
            boolean pLeft = (g.left == par);
            boolean nLeft = (par.left == cur);
            Node u = pLeft ? g.right : g.left;

            if (getColor(u) == Color.BLACK) {
                Node top;

                if (pLeft) {
                    if (!nLeft) {
                        leftRotate(par);
                        top = cur;
                    } else {
                        top = par;
                    }
                    rightRotate(g);
                } else {
                    if (nLeft) {
                        rightRotate(par);
                        top = cur;
                    } else {
                        top = par;
                    }
                    leftRotate(g);
                }

                top.color = Color.BLACK;
                g.color = Color.RED;
                break;

            } else {
                par.color = Color.BLACK;
                u.color = Color.BLACK;
                g.color = Color.RED;
                cur = g;
            }
        }

        root.color = Color.BLACK;

        return null;
    }

    @Override
    public String remove(Object key) {

        if (root == null) return null;

        Node n = root;
        while ( n != null){
            if((Integer) key > n.key){
                n = n.right;
            } else if ((Integer) key < n.key) {
                n = n.left;
            } else {
                break;
            }
        }

        if (n == null) return null;

        Node toDelete = n;
        String res = toDelete.value;


        if (toDelete.left != null && toDelete.right != null) {
            n = toDelete.right;
            while (n.left != null){
                n = n.left;
            }
            toDelete.key = n.key;
            toDelete.value = n.value;
            toDelete = n;
        }

        if (toDelete.left == null) {
            if (toDelete.right == null){

                if (toDelete.parent == null) {
                    root = null;
                } else {
                    if (getColor(toDelete) == Color.BLACK) {
                        fixDelete(toDelete);
                    }
                    if (toDelete.parent.left == toDelete){
                        toDelete.parent.left = null;
                    } else {
                        toDelete.parent.right = null;
                    }
                }
            } else {

                if (toDelete.parent == null) {
                    root = toDelete.right;
                } else if (toDelete.parent.left == toDelete){
                    toDelete.parent.left = toDelete.right;
                } else {
                    toDelete.parent.right = toDelete.right;
                }
                toDelete.right.parent = toDelete.parent;
                toDelete.right.color = Color.BLACK;
            }
        } else {

            if (toDelete.parent == null) {
                root = toDelete.left;
            } else if (toDelete.parent.left == toDelete){
                toDelete.parent.left = toDelete.left;
            } else {
                toDelete.parent.right = toDelete.left;
            }
            toDelete.left.parent = toDelete.parent;
            toDelete.left.color = Color.BLACK;
        }


        size--;
        return res;
    }

    private void fixDelete(Node n) {

        while (n != root && getColor(n) == Color.BLACK) {

            Node p = n.parent;

            if (n == p.left) {
                Node s = p.right;

                if (getColor(s) == Color.RED) {
                    setColor(s, Color.BLACK);
                    setColor(p, Color.RED);
                    leftRotate(p);
                    s = p.right;
                }

                Node sl = s.left;
                Node sr = s.right;

                if (getColor(sl) == Color.BLACK && getColor(sr) == Color.BLACK) {
                    setColor(s, Color.RED);
                    n = p;
                } else {
                    if (getColor(sr) == Color.BLACK) {
                        setColor(sl, Color.BLACK);
                        setColor(s, Color.RED);
                        rightRotate(s);
                        s = p.right;
                    }
                    setColor(s, getColor(p));
                    setColor(p, Color.BLACK);
                    setColor(s.right, Color.BLACK);
                    leftRotate(p);
                    n = root;
                }

            } else {
                Node s = p.left;

                if (getColor(s) == Color.RED) {
                    setColor(s, Color.BLACK);
                    setColor(p, Color.RED);
                    rightRotate(p);
                    s = p.left;
                }

                Node sl = s.left;
                Node sr = s.right;

                if (getColor(sl) == Color.BLACK && getColor(sr) == Color.BLACK) {
                    setColor(s, Color.RED);
                    n = p;
                } else {
                    if (getColor(sl) == Color.BLACK) {
                        setColor(sr, Color.BLACK);
                        setColor(s, Color.RED);
                        leftRotate(s);
                        s = p.left;
                    }
                    setColor(s, getColor(p));
                    setColor(p, Color.BLACK);
                    setColor(s.left, Color.BLACK);
                    rightRotate(p);
                    n = root;
                }
            }
        }

        setColor(n, Color.BLACK);
    }



    @Override
    public String get(Object key) {

        Node cur = root;
        while ( cur != null){
            if((Integer) key > cur.key){
                cur = cur.right;
            } else if ((Integer) key < cur.key) {
                cur = cur.left;
            } else {
                return cur.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        Node cur = root;
        while ( cur != null){
            if((Integer) key > cur.key){
                cur = cur.right;
            } else if ((Integer) key < cur.key) {
                cur = cur.left;
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }




    private void addToMap(Node node, MyRbMap map){

        if (node == null) return;

        addToMap(node.left, map);
        addToMap(node.right, map);

        map.put(node.key, node.value);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {

        MyRbMap newMap = new MyRbMap();
        MyRbMap.Node cur = root;

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

        MyRbMap newMap = new MyRbMap();
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
        if (root == null) return null;
        Node cur = root;
        while ( cur.left != null){
            cur = cur.left;
        }
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node cur = root;
        while ( cur.right != null){
            cur = cur.right;
        }
        return cur.key;
    }



    /// /////////////////
    ///
    ///
    ///
    ///



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
