package by.it.group551004.fedoruk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

// Левостороннее красно-черное дерево (LLRB, Седжвик)
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

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы КЧ-дерева             ///////
    /////////////////////////////////////////////////////////////////////////

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

    private Node findNode(Object key) {
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp == 0) {
                return current;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    private Node insert(Node h, Integer key, String value) {
        if (h == null) {
            return new Node(key, value, RED);
        }
        if (key < h.key) {
            h.left = insert(h.left, key, value);
        } else {
            h.right = insert(h.right, key, value);
        }
        return balance(h);
    }

    private Node minNode(Node h) {
        while (h.left != null) {
            h = h.left;
        }
        return h;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            return null;
        }
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = deleteMin(h.left);
        return balance(h);
    }

    // Ключ обязательно присутствует в дереве
    private Node delete(Node h, Integer key) {
        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.equals(h.key) && h.right == null) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.equals(h.key)) {
                Node min = minNode(h.right);
                h.key = min.key;
                h.value = min.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
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

    // Копирует в target все пары с ключами из диапазона [from, to)
    // null означает отсутствие соответствующей границы
    private void copyRange(Node node, Integer from, Integer to, MyRbMap target) {
        if (node == null) {
            return;
        }
        boolean aboveFrom = from == null || node.key >= from;
        boolean belowTo = to == null || node.key < to;
        if (aboveFrom) {
            copyRange(node.left, from, to, target);
        }
        if (aboveFrom && belowTo) {
            target.put(node.key, node.value);
        }
        if (belowTo) {
            copyRange(node.right, from, to, target);
        }
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
        Node node = findNode(key);
        if (node != null) {
            String old = node.value;
            node.value = value;
            return old;
        }
        root = insert(root, key, value);
        root.color = BLACK;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        String old = node.value;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, (Integer) key);
        if (root != null) {
            root.color = BLACK;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
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
        return minNode(root).key;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Необязательные методы                        ///////
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
