package by.it.group551001.vinogradov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean red;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    private Node grandparent(Node node) {
        if (node != null && node.parent != null) return node.parent.parent;
        else return null;
    }

    private Node uncle(Node node) {
        Node g = grandparent(node);
        if (g != null) {
            if (node.parent == g.left) return g.right;
            return g.left;
        }
        return null;
    }

    private Node sibling(Node node) {
        if (node == node.parent.left) return node.parent.right;
        return node.parent.left;
    }

    private void rotate_left(Node n) {
        Node pivot = n.right;
        if (pivot == null) return;

        Node child = pivot.left;

        pivot.parent = n.parent;
        if (n.parent == null) root = pivot;
        else if (n.parent.left == n) n.parent.left = pivot;
        else n.parent.right = pivot;

        pivot.left = n;
        n.parent = pivot;
        n.right = child;
        if (child != null) child.parent = n;
    }

    private void rotate_right(Node n) {
        Node pivot = n.left;
        if (pivot == null) return;

        Node child = pivot.right;

        pivot.parent = n.parent;
        if (n.parent == null) root = pivot;
        else if (n.parent.left == n) n.parent.left = pivot;
        else n.parent.right = pivot;

        pivot.right = n;
        n.parent = pivot;
        n.left = child;
        if (child != null) child.parent = n;
    }

    void insert(Node n) {
        if (n.parent == null) n.red = false;
        else if (!n.parent.red) return;
        else {
            Node p = n.parent;
            Node g = grandparent(n);
            Node u = uncle(n);
            if (u != null && u.red) {
                p.red = false;
                u.red = false;
                g.red = true;
                insert(g);
            }
            //left-left
            else if (g.left == p && p.left == n) rotate_right(g);
            //left-right
            else if (g.left == p && p.right == n) {
                rotate_left(p);
                n = n.right;
                p = n.parent;
                rotate_right(g);
            }
            //right-left
            else if (g.right == p && p.left == n) {
                rotate_right(p);
                n = n.left;
                p = n.parent;
                rotate_left(g);
            }
            //right-right
            else if (g.right == p && p.right == n) rotate_left(g);
            g.red = !g.red;
            p.red = !p.red;
        }
    }

    Node emplace(Node n) {
        if (n.left == null && n.right == null) {
            Node pred = n.parent;
            Node replacementSibling = null;
            if (pred != null) {
                replacementSibling = pred.left == n ? pred.right : pred.left;
                if (pred.left == n) pred.left = null;
                else pred.right = null;
            }
            else root = null;

            if (!n.red) return replacementSibling;
        }
        else if (n.left != null) {
            Node emp = n.left;
            while (emp.right != null) emp = emp.right;

            n.key = emp.key;
            n.value = emp.value;

            Node pred = emp.parent;
            Node replacement = emp.left;
            Node replacementSibling = pred.left == emp ? pred.right : pred.left;
            if (pred.left == emp) pred.left = replacement;
            else pred.right = replacement;
            if (replacement != null) replacement.parent = pred;

            if (!emp.red) {
                if (replacement == null || !replacement.red) return replacementSibling;
                replacement.red = false;
            }
        }
        else {
            Node emp = n.right;
            while (emp.left != null) emp = emp.left;

            n.key = emp.key;
            n.value = emp.value;

            Node pred = emp.parent;
            Node replacement = emp.right;
            Node replacementSibling = pred.left == emp ? pred.right : pred.left;
            if (pred.left == emp) pred.left = replacement;
            else pred.right = replacement;
            if (replacement != null) replacement.parent = pred;

            if (!emp.red) {
                if (replacement == null || !replacement.red) return replacementSibling;
                replacement.red = false;
            }
        }
        return null;
    }

    private void delete(Node n, Node parent, boolean nIsLeft) {
        if (n != null && n.red) {
            n.red = false;
            return;
        }

        if (parent == null) {
            if (n != null) n.red = false;
            return;
        }

        Node s = n == null
                ? (nIsLeft ? parent.right : parent.left)
                : sibling(n);
        Node p = parent;

        if (s != null && s.red) {
            p.red = true;
            s.red = false;
            if (nIsLeft) rotate_left(p);
            else rotate_right(p);
            s = n == null
                    ? (nIsLeft ? p.right : p.left)
                    : sibling(n);
        }

        if (s == null || (!s.red
                && (s.left == null || !s.left.red)
                && (s.right == null || !s.right.red))) {
            if (!p.red) {
                if (s != null) s.red = true;
                delete(p, p.parent, p.parent != null && p == p.parent.left);
            }
            else {
                if (s != null) s.red = true;
                p.red = false;
            }
        }

        if (s != null && !s.red) {
            if (nIsLeft && s.left != null && s.left.red
                    && (s.right == null || !s.right.red)) {
                s.left.red = false;
                rotate_right(s);
            }
            else if (!nIsLeft && s.right != null && s.right.red
                    && (s.left == null || !s.left.red)) {
                s.right.red = false;
                rotate_left(s);
            }

            s = n == null
                    ? (nIsLeft ? p.right : p.left)
                    : sibling(n);

            if (s != null && nIsLeft && s.right != null && s.right.red) {
                s.red = p.red;
                p.red = false;
                s.right.red = false;
                rotate_left(p);
            }
            else if (s != null && !nIsLeft && s.left != null && s.left.red) {
                s.red = p.red;
                p.red = false;
                s.left.red = false;
                rotate_right(p);
            }
        }

        if (root != null) root.red = false;
    }

    private String print_traverse(String s, Node node) {
        if (node == null) return s;

        s = print_traverse(s, node.left);
        if (s.length() > 1) s += ", ";
        s += node.key + "=" + node.value;

        return print_traverse(s, node.right);
    }

    private String find_traverse(Node node, int key) {
        if (node != null) {
            if (key < node.key) return find_traverse(node.left, key);
            else if (key > node.key) return find_traverse(node.right, key);
            else return node.value;
        }
        return null;
    }

    private boolean find_val_traverse(Node node, String value) {
        boolean f = false;
        if (node != null) {
            if (Objects.equals(node.value, value)) return true;
            f |= find_val_traverse(node.left, value);
            f |= find_val_traverse(node.right, value);
        }
        return f;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return print_traverse("{", root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        Node curr = root;
        Node pred = null;
        while(curr != null) {
            if (Objects.equals(curr.key, key)) {
                String res = curr.value;
                curr.value = value;
                return res;
            }
            else if (key < curr.key){
                pred = curr;
                curr = curr.left;
            }
            else {
                pred = curr;
                curr = curr.right;
            }
        }

        Node n = new Node(key, value);
        n.parent = pred;
        size++;
        if (pred == null) root = n;
        else if (key < pred.key) pred.left = n;
        else pred.right = n;
        insert(n);
        return null;
    }

    @Override
    public String remove(Object key) {
        String res = null;
        Node sib = null;
        Node curr = root;
        while(curr != null) {
            if (Objects.equals(curr.key, key)) {
                res = curr.value;
                sib = emplace(curr);
                size--;
                break;
            }
            else if ((int)key < curr.key){
                curr = curr.left;
            }
            else {
                curr = curr.right;
            }
        }

        if (sib != null && sib.parent != null) {
            Node parent = sib.parent;
            boolean replacementIsLeft = sib == parent.right;
            Node replacement = sibling(sib);
            delete(replacement, parent, replacementIsLeft);
        }
        return res;
    }

    @Override
    public String get(Object key) {
        return find_traverse(root, (int)key);
    }

    @Override
    public boolean containsKey(Object key) {
        return find_traverse(root, (int)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null && !(value instanceof String)) return false;
        return find_val_traverse(root, (String)value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException("toKey");
        MyRbMap res = new MyRbMap();
        Node[] st = new Node[size];
        int top = 0;
        Node curr = root;
        while (curr != null || top > 0) {
            while (curr != null) {
                st[top] = curr;
                top++;
                curr = curr.left;
            }
            top--;
            curr = st[top];

            if (curr.key >= toKey) break;
            res.put(curr.key, curr.value);
            curr = curr.right;
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException("toKey");
        MyRbMap res = new MyRbMap();
        Node[] st = new Node[size];
        int top = 0;
        Node curr = root;
        while (curr != null || top > 0) {
            while (curr != null) {
                st[top] = curr;
                top++;
                curr = curr.right;
            }
            top--;
            curr = st[top];

            if (curr.key < fromKey) break;
            res.put(curr.key, curr.value);
            curr = curr.left;
        }
        return res;
    }

    @Override
    public Integer firstKey() {
        if (size == 0) throw new NoSuchElementException();
        Node curr = root;
        while(curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) throw new NoSuchElementException();
        Node curr = root;
        while(curr.right != null) curr = curr.right;
        return curr.key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        throw notImplemented();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw notImplemented();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        throw notImplemented();
    }

    @Override
    public Set<Integer> keySet() {
        throw notImplemented();
    }

    @Override
    public Collection<String> values() {
        throw notImplemented();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw notImplemented();
    }
}
