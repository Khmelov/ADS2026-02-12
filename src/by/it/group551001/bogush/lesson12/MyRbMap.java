package by.it.group551001.bogush.lesson12;

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
        Node left, right, parent;
        boolean color = BLACK;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // ---------- вспомогательные методы (null-safe) ----------

    private boolean colorOf(Node p) {
        if (p == null) {
            return BLACK;
        }
        return p.color;
    }

    private Node parentOf(Node p) {
        if (p == null) {
            return null;
        }
        return p.parent;
    }

    private void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private Node leftOf(Node p) {
        if (p == null) {
            return null;
        }
        return p.left;
    }

    private Node rightOf(Node p) {
        if (p == null) {
            return null;
        }
        return p.right;
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

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    private Node successor(Node t) {
        if (t.right != null) {
            Node p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        Node p = t.parent;
        Node ch = t;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    // ---------- балансировка после вставки ----------

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

    // ---------- балансировка после удаления ----------

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

    private void deleteNode(Node p) {
        size--;
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }
        Node replacement;
        if (p.left != null) {
            replacement = p.left;
        } else {
            replacement = p.right;
        }
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

    // ---------- обходы ----------

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inOrder(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        inOrder(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null) {
            if (n.value == null) {
                return true;
            }
        } else if (value.equals(n.value)) {
            return true;
        }
        if (hasValue(n.left, value)) {
            return true;
        }
        return hasValue(n.right, value);
    }

    // копирует в res все узлы, у которых key < bound (head) либо key >= bound (tail)
    private void collect(Node n, int bound, boolean head, MyRbMap res) {
        if (n == null) {
            return;
        }
        collect(n.left, bound, head, res);
        if (head) {
            if (n.key < bound) {
                res.put(n.key, n.value);
            }
        } else {
            if (n.key >= bound) {
                res.put(n.key, n.value);
            }
        }
        collect(n.right, bound, head, res);
    }

    // ---------- обязательные методы ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            root.color = BLACK;
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                String old = cur.value;
                cur.value = value;
                return old;
            }
        }
        Node created = new Node(key, value, parent);
        if (key < parent.key) {
            parent.left = created;
        } else {
            parent.right = created;
        }
        size++;
        fixAfterInsertion(created);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
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
        Node n = findNode((Integer) key);
        if (n == null) {
            return null;
        }
        return n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
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
        MyRbMap res = new MyRbMap();
        collect(root, toKey, true, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap res = new MyRbMap();
        collect(root, fromKey, false, res);
        return res;
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

    // ---------- остальное из интерфейса (на уровень B не нужно) ----------

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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