package by.it.group551004.frantskevich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

// левостороннее красно-черное дерево (LLRB, Седжвик)
public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size = 0;
    private String oldValue;

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value);
        } else {
            oldValue = h.value;
            h.value = value;
        }
        return balance(h);
    }

    private Node min(Node h) {
        while (h.left != null) {
            h = h.left;
        }
        return h;
    }

    private Node max(Node h) {
        while (h.right != null) {
            h = h.right;
        }
        return h;
    }

    private Node removeMin(Node h) {
        if (h.left == null) {
            return null;
        }
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = removeMin(h.left);
        return balance(h);
    }

    // вызывается только если ключ точно есть в дереве
    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && h.right == null) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                Node next = min(h.right);
                h.key = next.key;
                h.value = next.value;
                h.right = removeMin(h.right);
            } else {
                h.right = remove(h.right, key);
            }
        }
        return balance(h);
    }

    private Node find(Object key) {
        Integer k = (Integer) key;
        Node node = root;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp == 0) {
                return node;
            }
            node = cmp < 0 ? node.left : node.right;
        }
        return null;
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

    // копирует в result все пары с ключами из [from, to); null означает "без границы"
    private void copyRange(Node node, Integer from, Integer to, MyRbMap result) {
        if (node == null) {
            return;
        }
        boolean aboveFrom = from == null || node.key.compareTo(from) >= 0;
        boolean belowTo = to == null || node.key.compareTo(to) < 0;
        if (aboveFrom) {
            copyRange(node.left, from, to, result);
        }
        if (aboveFrom && belowTo) {
            result.put(node.key, node.value);
        }
        if (belowTo) {
            copyRange(node.right, from, to, result);
        }
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
        oldValue = null;
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        Node node = find(key);
        if (node == null) {
            return null;
        }
        String result = node.value;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = remove(root, (Integer) key);
        if (root != null) {
            root.color = BLACK;
        }
        size--;
        return result;
    }

    @Override
    public String get(Object key) {
        Node node = find(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
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
        MyRbMap result = new MyRbMap();
        copyRange(root, null, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, null, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return max(root).key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, toKey, result);
        return result;
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
