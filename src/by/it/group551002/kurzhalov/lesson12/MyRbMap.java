package by.it.group551002.kurzhalov.lesson12;

import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        boolean color;
        Node left, right, parent;

        Node(int key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    // ================= Вращения =================

    private void rotateLeft(Node x) {
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

    private void rotateRight(Node x) {
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

    // ================= Вставка =================

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node uncle = z.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void insert(int key, String value) {
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else {
                x.value = value;
                return;
            }
        }
        Node z = new Node(key, value, RED);
        z.parent = y;
        if (y == null) root = z;
        else if (key < y.key) y.left = z;
        else y.right = z;
        insertFixup(z);
        size++;
    }

    // ================= Удаление =================

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node findNode(int key) {
        Node n = root;
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    private boolean colorOf(Node n) {
        return n == null ? BLACK : n.color;
    }

    private Node leftOf(Node n) {
        return n == null ? null : n.left;
    }

    private Node rightOf(Node n) {
        return n == null ? null : n.right;
    }

    private void deleteFixup(Node x, Node xParent) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(xParent)) {
                Node w = rightOf(xParent);
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateLeft(xParent);
                    w = rightOf(xParent);
                }
                if (colorOf(leftOf(w)) == BLACK && colorOf(rightOf(w)) == BLACK) {
                    w.color = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (colorOf(rightOf(w)) == BLACK) {
                        if (leftOf(w) != null) leftOf(w).color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = rightOf(xParent);
                    }
                    w.color = xParent.color;
                    xParent.color = BLACK;
                    if (rightOf(w) != null) rightOf(w).color = BLACK;
                    rotateLeft(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                Node w = leftOf(xParent);
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateRight(xParent);
                    w = leftOf(xParent);
                }
                if (colorOf(rightOf(w)) == BLACK && colorOf(leftOf(w)) == BLACK) {
                    w.color = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (colorOf(leftOf(w)) == BLACK) {
                        if (rightOf(w) != null) rightOf(w).color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = leftOf(xParent);
                    }
                    w.color = xParent.color;
                    xParent.color = BLACK;
                    if (leftOf(w) != null) leftOf(w).color = BLACK;
                    rotateRight(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private void delete(int key) {
        Node z = findNode(key);
        if (z == null) return;
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;
        Node xParent;
        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minNode(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
                if (x != null) x.parent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == BLACK) {
            deleteFixup(x, xParent);
        }
        size--;
    }

    // ================= Map API =================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        Node existing = findNode(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        insert(key, value);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        if (n == null) return null;
        String old = n.value;
        delete((Integer) key);
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // ================= SortedMap =================

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return new SubMap(null, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return new SubMap(fromKey, null);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return new SubMap(fromKey, toKey);
    }

    // ================= toString =================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    // ================= SubMap (view) =================

    private class SubMap implements SortedMap<Integer, String> {
        private final Integer from;
        private final Integer to;

        SubMap(Integer from, Integer to) {
            this.from = from;
            this.to = to;
        }

        private boolean inRange(int key) {
            return (from == null || key >= from) && (to == null || key < to);
        }

        @Override
        public int size() {
            int count = 0;
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); it.next()) count++;
            return count;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            int k = (Integer) key;
            return inRange(k) && MyRbMap.this.containsKey(k);
        }

        @Override
        public boolean containsValue(Object value) {
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                String v = MyRbMap.this.get(it.next());
                if (value == null ? v == null : value.equals(v)) return true;
            }
            return false;
        }

        @Override
        public String get(Object key) {
            if (!(key instanceof Integer)) return null;
            int k = (Integer) key;
            return inRange(k) ? MyRbMap.this.get(k) : null;
        }

        @Override
        public String put(Integer key, String value) {
            if (!inRange(key)) throw new IllegalArgumentException("key out of range");
            return MyRbMap.this.put(key, value);
        }

        @Override
        public String remove(Object key) {
            if (!(key instanceof Integer)) return null;
            int k = (Integer) key;
            return inRange(k) ? MyRbMap.this.remove(k) : null;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends String> m) {
            for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }

        @Override
        public void clear() {
            Integer[] keys = new Integer[size()];
            int i = 0;
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                keys[i++] = it.next();
            }
            for (Integer k : keys) MyRbMap.this.remove(k);
        }

        @Override
        public Comparator<? super Integer> comparator() {
            return null;
        }

        @Override
        public Integer firstKey() {
            Iterator<Integer> it = keySet().iterator();
            if (!it.hasNext()) throw new NoSuchElementException();
            return it.next();
        }

        @Override
        public Integer lastKey() {
            Iterator<Integer> it = keySet().iterator();
            if (!it.hasNext()) throw new NoSuchElementException();
            Integer last = null;
            while (it.hasNext()) last = it.next();
            return last;
        }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            Integer newTo = (to == null || toKey < to) ? toKey : to;
            return new SubMap(from, newTo);
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            Integer newFrom = (from == null || fromKey > from) ? fromKey : from;
            return new SubMap(newFrom, to);
        }

        @Override
        public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            Integer newFrom = (from == null || fromKey > from) ? fromKey : from;
            Integer newTo = (to == null || toKey < to) ? toKey : to;
            return new SubMap(newFrom, newTo);
        }

        @Override
        public Set<Integer> keySet() {
            return new AbstractSet<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    return new RangeKeyIterator(from, to);
                }

                @Override
                public int size() {
                    int count = 0;
                    for (Iterator<Integer> it = iterator(); it.hasNext(); it.next()) count++;
                    return count;
                }
            };
        }

        @Override
        public Collection<String> values() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Entry<Integer, String>> entrySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                Integer k = it.next();
                if (!first) sb.append(", ");
                sb.append(k).append('=').append(MyRbMap.this.get(k));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
    }

    // ================= RangeKeyIterator =================

    private class RangeKeyIterator implements Iterator<Integer> {
        private Node[] stack = new Node[16];
        private int top = 0;
        private Node next;
        private final Integer from;
        private final Integer to;

        RangeKeyIterator(Integer from, Integer to) {
            this.from = from;
            this.to = to;
            pushLeft(root);
            advance();
        }

        private void pushLeft(Node n) {
            while (n != null) {
                if (top == stack.length) {
                    Node[] bigger = new Node[stack.length * 2];
                    System.arraycopy(stack, 0, bigger, 0, stack.length);
                    stack = bigger;
                }
                stack[top++] = n;
                n = n.left;
            }
        }

        private void advance() {
            next = null;
            while (top > 0) {
                Node node = stack[--top];
                pushLeft(node.right);
                if ((from == null || node.key >= from) && (to == null || node.key < to)) {
                    next = node;
                    return;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Integer next() {
            if (next == null) throw new NoSuchElementException();
            Integer k = next.key;
            advance();
            return k;
        }
    }

    // ================= keySet / values / entrySet =================

    @Override
    public Set<Integer> keySet() {
        return new AbstractSet<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new RangeKeyIterator(null, null);
            }

            @Override
            public int size() {
                return size;
            }
        };
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