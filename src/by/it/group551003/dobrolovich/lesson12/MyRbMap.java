package by.it.group551003.dobrolovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color = RED;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private boolean colorOf(Node p) {
        return p == null ? BLACK : p.color;
    }

    private Node parentOf(Node p) {
        return p == null ? null : p.parent;
    }

    private void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private Node leftOf(Node p) {
        return p == null ? null : p.left;
    }

    private Node rightOf(Node p) {
        return p == null ? null : p.right;
    }

    private void rotateLeft(Node p) {
        if (p == null) {
            return;
        }
        Node r = p.right;
        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }
        r.parent = p.parent;
        if (p.parent == null) {
            root = r;
        } else if (p.parent.left == p) {
            p.parent.left = r;
        } else {
            p.parent.right = r;
        }
        r.left = p;
        p.parent = r;
    }

    private void rotateRight(Node p) {
        if (p == null) {
            return;
        }
        Node l = p.left;
        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }
        l.parent = p.parent;
        if (p.parent == null) {
            root = l;
        } else if (p.parent.right == p) {
            p.parent.right = l;
        } else {
            p.parent.left = l;
        }
        l.right = p;
        p.parent = l;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private Node successor(Node p) {
        Node s = p.right;
        while (s.left != null) {
            s = s.left;
        }
        return s;
    }

    private void deleteNode(Node p) {
        size--;
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }
        Node replacement = p.left != null ? p.left : p.right;
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }
            p.left = null;
            p.right = null;
            p.parent = null;
            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private Node find(int key) {
        Node c = root;
        while (c != null) {
            if (key < c.key) {
                c = c.left;
            } else if (key > c.key) {
                c = c.right;
            } else {
                return c;
            }
        }
        return null;
    }

    private void write(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        write(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        write(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (n.value == null ? value == null : n.value.equals(value)) {
            return true;
        }
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    private void copyRange(Node n, MyRbMap dst, Integer from, Integer to) {
        if (n == null) {
            return;
        }
        if (from == null || n.key > from) {
            copyRange(n.left, dst, from, to);
        }
        if ((from == null || n.key >= from) && (to == null || n.key < to)) {
            dst.put(n.key, n.value);
        }
        if (to == null || n.key < to) {
            copyRange(n.right, dst, from, to);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        write(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node parent = null;
        Node t = root;
        boolean goLeft = false;
        while (t != null) {
            parent = t;
            if (key < t.key) {
                t = t.left;
                goLeft = true;
            } else if (key > t.key) {
                t = t.right;
                goLeft = false;
            } else {
                String old = t.value;
                t.value = value;
                return old;
            }
        }
        Node e = new Node(key, value, parent);
        if (parent == null) {
            root = e;
        } else if (goLeft) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        fixAfterInsertion(e);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        deleteNode(n);
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    @Override
    public int size() {
        return size;
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

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap r = new MyRbMap();
        copyRange(root, r, null, toKey);
        return r;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap r = new MyRbMap();
        copyRange(root, r, fromKey, null);
        return r;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap r = new MyRbMap();
        copyRange(root, r, fromKey, toKey);
        return r;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        return n.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}