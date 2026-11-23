package by.it.group551003.kalach.lesson12;


import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class MySplayMap implements NavigableMap<Integer, String> {

    // ---------- узел дерева ----------
    private static class Node {
        int key;
        String value;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // ---------- простая запись для методов *Entry ----------
    private static class MyEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private final String value;

        MyEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    // ---------- splay (сверху вниз) ----------
    // поднимает в корень узел с ключом key, а если такого нет —
    // ближайший к нему по пути поиска
    private Node splay(Node t, int key) {
        if (t == null) return null;
        Node header = new Node(0, null);
        Node l = header, r = header;
        while (true) {
            if (key < t.key) {
                if (t.left == null) break;
                if (key < t.left.key) {            // zig-zig: поворот вправо
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) break;
                }
                r.left = t;                        // «привязываем» t к правому дереву
                r = t;
                t = t.left;
            } else if (key > t.key) {
                if (t.right == null) break;
                if (key > t.right.key) {           // zag-zag: поворот влево
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) break;
                }
                l.right = t;                       // «привязываем» t к левому дереву
                l = t;
                t = t.right;
            } else {
                break;
            }
        }
        l.right = t.left;
        r.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
    }

    // ---------- обход in-order без рекурсии (дерево может быть глубоким) ----------
    private Node[] inOrder() {
        Node[] result = new Node[size];
        Node[] stack = new Node[size];
        int sp = 0, idx = 0;
        Node cur = root;
        while (cur != null || sp > 0) {
            while (cur != null) {
                stack[sp++] = cur;
                cur = cur.left;
            }
            cur = stack[--sp];
            result[idx++] = cur;
            cur = cur.right;
        }
        return result;
    }

    // ---------- toString ----------
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Node[] nodes = inOrder();
        for (int i = 0; i < nodes.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(nodes[i].key).append('=').append(nodes[i].value);
        }
        sb.append('}');
        return sb.toString();
    }

    // ---------- put ----------
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        root = splay(root, key);
        if (root.key == key) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(key, value);
        if (key < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    // ---------- remove ----------
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        int k = (Integer) key;
        root = splay(root, k);
        if (root.key != k) return null;
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightPart = root.right;
            root = splay(root.left, k);   // k больше всех в левой части: максимум станет корнем
            root.right = rightPart;
        }
        size--;
        return old;
    }

    // ---------- get / containsKey / containsValue ----------
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        root = splay(root, (Integer) key);
        return root.key == (Integer) key ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || root == null) return false;
        root = splay(root, (Integer) key);
        return root.key == (Integer) key;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node n : inOrder()) {
            if (value == null ? n.value == null : value.equals(n.value)) return true;
        }
        return false;
    }

    // ---------- size / clear / isEmpty ----------
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

    // ---------- firstKey / lastKey ----------
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    // ---------- lower / floor / ceiling / higher ----------
    // наибольший ключ < key (или <= key, если inclusive)
    private Node findBelow(int key, boolean inclusive) {
        Node cur = root, res = null;
        while (cur != null) {
            if (cur.key < key || (inclusive && cur.key == key)) {
                res = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }

    // наименьший ключ > key (или >= key, если inclusive)
    private Node findAbove(int key, boolean inclusive) {
        Node cur = root, res = null;
        while (cur != null) {
            if (cur.key > key || (inclusive && cur.key == key)) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }

    private Integer keyOf(Node n) {
        return n == null ? null : n.key;
    }

    private Map.Entry<Integer, String> entryOf(Node n) {
        return n == null ? null : new MyEntry(n.key, n.value);
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyOf(findBelow(key, false));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOf(findBelow(key, true));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOf(findAbove(key, true));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOf(findAbove(key, false));
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return entryOf(findBelow(key, false));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return entryOf(findBelow(key, true));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return entryOf(findAbove(key, true));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return entryOf(findAbove(key, false));
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        if (root == null) return null;
        return entryOf(findAbove(firstKey(), true));
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        if (root == null) return null;
        return entryOf(findBelow(lastKey(), true));
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Map.Entry<Integer, String> e = firstEntry();
        if (e != null) remove(e.getKey());
        return e;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Map.Entry<Integer, String> e = lastEntry();
        if (e != null) remove(e.getKey());
        return e;
    }

    // ---------- headMap / tailMap / subMap (возвращают копии) ----------
    private MySplayMap range(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        for (Node n : inOrder()) {
            boolean okFrom = from == null || n.key > from || (fromInclusive && n.key == from);
            boolean okTo = to == null || n.key < to || (toInclusive && n.key == to);
            if (okFrom && okTo) result.put(n.key, n.value);
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return range(null, false, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return range(fromKey, true, null, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(null, false, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(fromKey, inclusive, null, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return range(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    // ---------- необязательные методы ----------
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
