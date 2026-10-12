package by.it.group551001.bochilo.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    //Создайте аналог NavigableMap на основе splay-дерева БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    // поднимает узел с ключом key (или последний узел на пути поиска) в корень поддерева
    private Node splay(Node h, Integer key) {
        if (h == null) {
            return null;
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            if (h.left == null) {
                return h;
            }
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) {
                    h.left = rotateLeft(h.left);
                }
            }
            return h.left == null ? h : rotateRight(h);
        } else if (cmp > 0) {
            if (h.right == null) {
                return h;
            }
            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) {
                    h.right = rotateRight(h.right);
                }
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }
            return h.right == null ? h : rotateLeft(h);
        }
        return h;
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        inOrder(node.right, sb);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // копирует в result пары с ключами из диапазона [from, to)
    private void copyRange(Node node, MySplayMap result, Integer from, Integer to) {
        if (node == null) {
            return;
        }
        boolean aboveFrom = from == null || node.key.compareTo(from) >= 0;
        boolean belowTo = to == null || node.key.compareTo(to) < 0;
        if (aboveFrom) {
            copyRange(node.left, result, from, to);
        }
        if (aboveFrom && belowTo) {
            result.put(node.key, node.value);
        }
        if (belowTo) {
            copyRange(node.right, result, from, to);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
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
        if (root == null) {
            return null;
        }
        Integer k = (Integer) key;
        root = splay(root, k);
        if (k.compareTo(root.key) != 0) {
            return null;
        }
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            root = splay(root.left, k);
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (root == null) {
            return null;
        }
        Integer k = (Integer) key;
        root = splay(root, k);
        return k.compareTo(root.key) == 0 ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (root == null) {
            return false;
        }
        Integer k = (Integer) key;
        root = splay(root, k);
        return k.compareTo(root.key) == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
        MySplayMap result = new MySplayMap();
        copyRange(root, result, null, toKey);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        copyRange(root, result, fromKey, null);
        return result;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MySplayMap result = new MySplayMap();
        copyRange(root, result, fromKey, toKey);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Integer result = null;
        Node node = root;
        while (node != null) {
            if (node.key.compareTo(key) < 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Integer result = null;
        Node node = root;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) {
                return node.key;
            }
            if (cmp < 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Integer result = null;
        Node node = root;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) {
                return node.key;
            }
            if (cmp > 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Integer result = null;
        Node node = root;
        while (node != null) {
            if (node.key.compareTo(key) > 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Методы, не требуемые заданием                ///////
    /////////////////////////////////////////////////////////////////////////

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
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
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
