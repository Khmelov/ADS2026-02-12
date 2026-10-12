package by.it.group551004.frantskevich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
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
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size = 0;

    // нисходящий (top-down) splay: поднимает к корню узел с ключом key,
    // а если его нет - последний узел на пути поиска
    private Node splay(Node t, Integer key) {
        if (t == null) {
            return null;
        }
        Node header = new Node(null, null);
        Node l = header;
        Node r = header;
        while (true) {
            int cmp = key.compareTo(t.key);
            if (cmp < 0) {
                if (t.left == null) {
                    break;
                }
                if (key.compareTo(t.left.key) < 0) {
                    Node y = t.left;            // zig-zig: поворот вправо
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) {
                        break;
                    }
                }
                r.left = t;                     // привязываем к правому дереву
                r = t;
                t = t.left;
            } else if (cmp > 0) {
                if (t.right == null) {
                    break;
                }
                if (key.compareTo(t.right.key) > 0) {
                    Node y = t.right;           // zig-zig: поворот влево
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) {
                        break;
                    }
                }
                l.right = t;                    // привязываем к левому дереву
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

    private Node splayTo(Object key) {
        if (root == null) {
            return null;
        }
        Integer k = (Integer) key;
        root = splay(root, k);
        return k.equals(root.key) ? root : null;
    }

    // наибольший узел с ключом < key (или <= key при inclusive)
    private Node lowerNode(Integer key, boolean inclusive) {
        Node node = root;
        Node best = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                best = node;
                if (cmp == 0) {
                    break;
                }
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (best != null) {
            root = splay(root, best.key);
        }
        return best;
    }

    // наименьший узел с ключом > key (или >= key при inclusive)
    private Node higherNode(Integer key, boolean inclusive) {
        Node node = root;
        Node best = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                best = node;
                if (cmp == 0) {
                    break;
                }
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (best != null) {
            root = splay(root, best.key);
        }
        return best;
    }

    private Node minNode() {
        Node node = root;
        if (node != null) {
            while (node.left != null) {
                node = node.left;
            }
        }
        return node;
    }

    private Node maxNode() {
        Node node = root;
        if (node != null) {
            while (node.right != null) {
                node = node.right;
            }
        }
        return node;
    }

    private static Integer keyOrNull(Node node) {
        return node == null ? null : node.key;
    }

    private static Map.Entry<Integer, String> copy(Node node) {
        return node == null ? null : new Node(node.key, node.value);
    }

    private void toString(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        toString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        toString(node.right, sb);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        return value.equals(node.value)
               || containsValue(node.left, value)
               || containsValue(node.right, value);
    }

    // копирует в result пары из диапазона; from/to == null означает "без границы"
    private void copyRange(Node node, Integer from, boolean fromInclusive,
                           Integer to, boolean toInclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        int cmpFrom = from == null ? 1 : node.key.compareTo(from);
        int cmpTo = to == null ? -1 : node.key.compareTo(to);
        boolean aboveFrom = cmpFrom > 0 || (fromInclusive && cmpFrom == 0);
        boolean belowTo = cmpTo < 0 || (toInclusive && cmpTo == 0);
        if (cmpFrom > 0) {
            copyRange(node.left, from, fromInclusive, to, toInclusive, result);
        }
        if (aboveFrom && belowTo) {
            result.put(node.key, node.value);
        }
        if (cmpTo < 0) {
            copyRange(node.right, from, fromInclusive, to, toInclusive, result);
        }
    }

    private MySplayMap range(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, from, fromInclusive, to, toInclusive, result);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node node = new Node(key, value);
        if (cmp < 0) {
            node.left = root.left;
            node.right = root;
            root.left = null;
        } else {
            node.right = root.right;
            node.left = root;
            root.right = null;
        }
        root = node;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = splayTo(key);
        if (node == null) {
            return null;
        }
        if (root.left == null) {
            root = root.right;
        } else {
            // максимум левого поддерева поднимается в корень, у него нет правого сына
            Node right = root.right;
            root = splay(root.left, node.key);
            root.right = right;
        }
        size--;
        return node.value;
    }

    @Override
    public String get(Object key) {
        Node node = splayTo(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return splayTo(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return value != null && containsValue(root, value);
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
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return minNode().key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return maxNode().key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyOrNull(lowerNode(key, false));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOrNull(lowerNode(key, true));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOrNull(higherNode(key, true));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOrNull(higherNode(key, false));
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return copy(lowerNode(key, false));
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return copy(lowerNode(key, true));
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return copy(higherNode(key, true));
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return copy(higherNode(key, false));
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return copy(minNode());
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return copy(maxNode());
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Entry<Integer, String> entry = firstEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Entry<Integer, String> entry = lastEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return range(fromKey, fromInclusive, toKey, toInclusive);
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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
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
