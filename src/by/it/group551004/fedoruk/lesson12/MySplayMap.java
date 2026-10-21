package by.it.group551004.fedoruk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

// Splay-дерево с нисходящим (top-down) расширением
public class MySplayMap implements NavigableMap<Integer, String> {

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

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы splay-дерева          ///////
    /////////////////////////////////////////////////////////////////////////

    // Поднимает в корень узел с ключом key или последний узел на пути поиска
    private Node splay(Node t, Integer key) {
        if (t == null) {
            return null;
        }
        Node header = new Node(null, null);
        Node leftMax = header;  // Максимальный узел левого дерева
        Node rightMin = header; // Минимальный узел правого дерева
        while (true) {
            int cmp = key.compareTo(t.key);
            if (cmp < 0) {
                if (t.left == null) {
                    break;
                }
                if (key < t.left.key) {
                    // zig-zig: поворот вправо
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) {
                        break;
                    }
                }
                rightMin.left = t;
                rightMin = t;
                t = t.left;
            } else if (cmp > 0) {
                if (t.right == null) {
                    break;
                }
                if (key > t.right.key) {
                    // zag-zag: поворот влево
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) {
                        break;
                    }
                }
                leftMax.right = t;
                leftMax = t;
                t = t.right;
            } else {
                break;
            }
        }
        // Сборка: левое дерево, t, правое дерево
        leftMax.right = t.left;
        rightMin.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
    }

    private Node findNode(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        root = splay(root, (Integer) key);
        if (root != null && root.key.equals(key)) {
            return root;
        }
        return null;
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

    // Копирует в target все пары с ключами между from и to с учетом включения границ
    // null означает отсутствие соответствующей границы
    private void copyRange(Node node, Integer from, boolean fromInclusive,
                           Integer to, boolean toInclusive, MySplayMap target) {
        if (node == null) {
            return;
        }
        boolean aboveFrom = from == null || (fromInclusive ? node.key >= from : node.key > from);
        boolean belowTo = to == null || (toInclusive ? node.key <= to : node.key < to);
        if (aboveFrom) {
            copyRange(node.left, from, fromInclusive, to, toInclusive, target);
        }
        if (aboveFrom && belowTo) {
            target.put(node.key, node.value);
        }
        if (belowTo) {
            copyRange(node.right, from, fromInclusive, to, toInclusive, target);
        }
    }

    // Наибольший ключ, меньший key (или равный ему при inclusive)
    private Integer lower(Integer key, boolean inclusive) {
        Integer result = null;
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                result = current.key;
                if (cmp == 0) {
                    break;
                }
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    // Наименьший ключ, больший key (или равный ему при inclusive)
    private Integer higher(Integer key, boolean inclusive) {
        Integer result = null;
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                result = current.key;
                if (cmp == 0) {
                    break;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
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
        // Новый узел становится корнем, старый корень делится на два поддерева
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
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        // Удаляемый узел в корне: объединяем его поддеревья
        String old = node.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            root = splay(root.left, (Integer) key);
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(key) != null;
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
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lower(key, false);
    }

    @Override
    public Integer floorKey(Integer key) {
        return lower(key, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return higher(key, true);
    }

    @Override
    public Integer higherKey(Integer key) {
        return higher(key, false);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Необязательные методы                        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, null, false, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, fromKey, inclusive, null, false, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
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
