package by.it.group551001.zlotnikov.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

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
            this.red = true;
        }
    }

    private static final Node NIL;
    static {
        NIL = new Node(null, null);
        NIL.red = false;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }

    private Node root = NIL;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private Node findNode(Integer key) {
        Node n = root;
        while (n != NIL) {
            int cmp = key.compareTo(n.key);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return n;
            }
        }
        return null;
    }

    private Node minimum(Node n) {
        while (n.left != NIL) {
            n = n.left;
        }
        return n;
    }

    private Node maximum(Node n) {
        while (n.right != NIL) {
            n = n.right;
        }
        return n;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void insertFixup(Node z) {
        while (z.parent.red) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.red = false;
    }

    private void deleteFixup(Node x) {
        while (x != root && !x.red) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.red && !w.right.red) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (!w.right.red) {
                        w.left.red = false;
                        w.red = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    w.right.red = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (!w.right.red && !w.left.red) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (!w.left.red) {
                        w.right.red = false;
                        w.red = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    w.left.red = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.red = false;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == NIL) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    private boolean equalsRec(Node n, Map<?, ?> m) {
        if (n == NIL) {
            return true;
        }
        Object v = m.get(n.key);
        if (v == null && !m.containsKey(n.key)) {
            return false;
        }
        if (v == null ? n.value != null : !v.equals(n.value)) {
            return false;
        }
        return equalsRec(n.left, m) && equalsRec(n.right, m);
    }

    private int hashRec(Node n) {
        if (n == NIL) {
            return 0;
        }
        int h = (n.key == null ? 0 : n.key.hashCode())
                ^ (n.value == null ? 0 : n.value.hashCode());
        return h + hashRec(n.left) + hashRec(n.right);
    }

    private void copyHead(Node n, Integer toKey, MyRbMap dest) {
        if (n == NIL) {
            return;
        }
        if (n.key.compareTo(toKey) < 0) {
            copyHead(n.left, toKey, dest);
            dest.put(n.key, n.value);
            copyHead(n.right, toKey, dest);
        } else {
            copyHead(n.left, toKey, dest);
        }
    }

    private void copyTail(Node n, Integer fromKey, MyRbMap dest) {
        if (n == NIL) {
            return;
        }
        if (n.key.compareTo(fromKey) >= 0) {
            copyTail(n.left, fromKey, dest);
            dest.put(n.key, n.value);
            copyTail(n.right, fromKey, dest);
        } else {
            copyTail(n.right, fromKey, dest);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "{";
        Node[] stack = new Node[32];
        int top = 0;
        Node cur = root;
        boolean first = true;
        while (cur != NIL || top > 0) {
            while (cur != NIL) {
                if (top == stack.length) {
                    Node[] grown = new Node[stack.length * 2];
                    for (int i = 0; i < top; i++) {
                        grown[i] = stack[i];
                    }
                    stack = grown;
                }
                stack[top++] = cur;
                cur = cur.left;
            }
            cur = stack[--top];
            if (!first) {
                result += ", ";
            }
            result += cur.key + "=" + cur.value;
            first = false;
            cur = cur.right;
        }
        return result + "}";
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node y = NIL;
        Node x = root;
        int cmp = 0;
        while (x != NIL) {
            y = x;
            cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                String old = x.value;
                x.value = value;
                return old;
            }
        }
        Node z = new Node(key, value);
        z.parent = y;
        if (y == NIL) {
            root = z;
        } else if (cmp < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.left = NIL;
        z.right = NIL;
        insertFixup(z);
        size++;
        NIL.parent = NIL;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node z = findNode((Integer) key);
        if (z == null) {
            return null;
        }
        String old = z.value;
        Node y = z;
        boolean yOriginalRed = y.red;
        Node x;
        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalRed = y.red;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.red = z.red;
        }
        size--;
        if (!yOriginalRed) {
            deleteFixup(x);
        }
        NIL.parent = NIL;
        return old;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyHead(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyTail(root, fromKey, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Map is empty");
        }
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Map is empty");
        }
        return maximum(root).key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы SortedMap / Map              ///////
    /////////////////////////////////////////////////////////////////////////

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
        if (m == null) {
            throw new NullPointerException();
        }
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size) {
            return false;
        }
        return equalsRec(root, m);
    }

    @Override
    public int hashCode() {
        return hashRec(root);
    }
}