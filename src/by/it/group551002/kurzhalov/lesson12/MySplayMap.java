package by.it.group551002.kurzhalov.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right, parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // ================= Splay =================

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

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if ((x == p.left && p == g.left) || (x == p.right && p == g.right)) {
                if (x == p.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else {
                    rotateLeft(g);
                    rotateLeft(p);
                }
            } else {
                if (x == p.left) {
                    rotateRight(p);
                    rotateLeft(g);
                } else {
                    rotateLeft(p);
                    rotateRight(g);
                }
            }
        }
    }

    // ИСПРАВЛЕНО: при неудачном поиске splay'им последний посещённый узел
    private Node splayFind(int key) {
        Node n = root;
        Node last = null;
        while (n != null) {
            last = n;
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else {
                splay(n);
                return n;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maxNode(Node n) {
        while (n.right != null) n = n.right;
        return n;
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
        return splayFind((Integer) key) != null;
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
        Node n = splayFind((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node existing = splayFind(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        // после splayFind root = ближайший по ключу узел
        Node n = new Node(key, value);
        if (key < root.key) {
            n.left = root.left;
            if (n.left != null) n.left.parent = n;
            n.right = root;
            root.left = null;
            root.parent = n;
        } else {
            n.right = root.right;
            if (n.right != null) n.right.parent = n;
            n.left = root;
            root.right = null;
            root.parent = n;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = splayFind((Integer) key);
        if (n == null) return null;
        String old = n.value;
        Node leftTree = n.left;
        Node rightTree = n.right;
        if (leftTree != null) leftTree.parent = null;
        if (rightTree != null) rightTree.parent = null;
        n.left = null;
        n.right = null;
        n.parent = null;
        if (leftTree == null) {
            root = rightTree;
        } else {
            root = leftTree;
            Node maxLeft = maxNode(root);
            splay(maxLeft);
            root.right = rightTree;
            if (rightTree != null) rightTree.parent = root;
        }
        size--;
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

    // ================= NavigableMap =================

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = minNode(root);
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = maxNode(root);
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = root;
        Node candidate = null;
        while (n != null) {
            if (key > n.key) {
                candidate = n;
                n = n.right;
            } else {
                n = n.left;
            }
        }
        return candidate == null ? null : candidate.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = root;
        Node candidate = null;
        while (n != null) {
            if (key > n.key) {
                candidate = n;
                n = n.right;
            } else if (key < n.key) {
                n = n.left;
            } else {
                return n.key;
            }
        }
        return candidate == null ? null : candidate.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = root;
        Node candidate = null;
        while (n != null) {
            if (key < n.key) {
                candidate = n;
                n = n.left;
            } else if (key > n.key) {
                n = n.right;
            } else {
                return n.key;
            }
        }
        return candidate == null ? null : candidate.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = root;
        Node candidate = null;
        while (n != null) {
            if (key < n.key) {
                candidate = n;
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return candidate == null ? null : candidate.key;
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

    // ================= NavigableMap дополнительные =================

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return new SubMap(null, toKey, false, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return new SubMap(fromKey, null, inclusive, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return new SubMap(fromKey, toKey, fromInclusive, toInclusive);
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

    private class SubMap implements NavigableMap<Integer, String> {
        private final Integer from;
        private final Integer to;
        private final boolean fromInclusive;
        private final boolean toInclusive;

        SubMap(Integer from, Integer to) {
            this(from, to, true, false);
        }

        SubMap(Integer from, Integer to, boolean fromInclusive, boolean toInclusive) {
            this.from = from;
            this.to = to;
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
        }

        private boolean inRange(int key) {
            if (from != null) {
                if (fromInclusive ? key < from : key <= from) return false;
            }
            if (to != null) {
                if (toInclusive ? key > to : key >= to) return false;
            }
            return true;
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
            return inRange(k) && MySplayMap.this.containsKey(k);
        }

        @Override
        public boolean containsValue(Object value) {
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                String v = MySplayMap.this.get(it.next());
                if (value == null ? v == null : value.equals(v)) return true;
            }
            return false;
        }

        @Override
        public String get(Object key) {
            if (!(key instanceof Integer)) return null;
            int k = (Integer) key;
            return inRange(k) ? MySplayMap.this.get(k) : null;
        }

        @Override
        public String put(Integer key, String value) {
            if (!inRange(key)) throw new IllegalArgumentException("key out of range");
            return MySplayMap.this.put(key, value);
        }

        @Override
        public String remove(Object key) {
            if (!(key instanceof Integer)) return null;
            int k = (Integer) key;
            return inRange(k) ? MySplayMap.this.remove(k) : null;
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
            for (Integer k : keys) MySplayMap.this.remove(k);
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
        public Integer lowerKey(Integer key) {
            Integer candidate = null;
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                int k = it.next();
                if (k < key) candidate = k;
                else break;
            }
            return candidate;
        }

        @Override
        public Integer floorKey(Integer key) {
            Integer candidate = null;
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                int k = it.next();
                if (k <= key) candidate = k;
                else break;
            }
            return candidate;
        }

        @Override
        public Integer ceilingKey(Integer key) {
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                int k = it.next();
                if (k >= key) return k;
            }
            return null;
        }

        @Override
        public Integer higherKey(Integer key) {
            for (Iterator<Integer> it = keySet().iterator(); it.hasNext(); ) {
                int k = it.next();
                if (k > key) return k;
            }
            return null;
        }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            return headMap(toKey, false);
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            return tailMap(fromKey, true);
        }

        @Override
        public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        @Override
        public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
            if (to == null || toKey < to || (toKey.equals(to) && !toInclusive)) {
                return new SubMap(from, toKey, fromInclusive, inclusive);
            }
            return new SubMap(from, to, fromInclusive, toInclusive);
        }

        @Override
        public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
            if (from == null || fromKey > from || (fromKey.equals(from) && !fromInclusive)) {
                return new SubMap(fromKey, to, inclusive, toInclusive);
            }
            return new SubMap(from, to, fromInclusive, toInclusive);
        }

        @Override
        public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInc,
                                                    Integer toKey, boolean toInc) {
            return new SubMap(fromKey, toKey, fromInc, toInc);
        }

        @Override
        public Entry<Integer, String> lowerEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> floorEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> ceilingEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> higherEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> firstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> lastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entry<Integer, String> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableMap<Integer, String> descendingMap() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableSet<Integer> descendingKeySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableSet<Integer> navigableKeySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Integer> keySet() {
            return new AbstractSet<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    return new RangeKeyIterator(from, to, fromInclusive, toInclusive);
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
                sb.append(k).append('=').append(MySplayMap.this.get(k));
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
        private final boolean fromInclusive;
        private final boolean toInclusive;

        RangeKeyIterator(Integer from, Integer to, boolean fromInclusive, boolean toInclusive) {
            this.from = from;
            this.to = to;
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
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

        private boolean inRange(int key) {
            if (from != null) {
                if (fromInclusive ? key < from : key <= from) return false;
            }
            if (to != null) {
                if (toInclusive ? key > to : key >= to) return false;
            }
            return true;
        }

        private void advance() {
            next = null;
            while (top > 0) {
                Node node = stack[--top];
                pushLeft(node.right);
                if (inRange(node.key)) {
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
                return new RangeKeyIterator(null, null, true, true);
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