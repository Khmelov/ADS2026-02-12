package by.it.group510902.makaruk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
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
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        String old = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        if (old == null) size++;
        return old;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        String old = get(key);
        if (old != null) {
            // Упрощенный и надежный подход для лабы: удаляем через временный сбор элементов
            // или обнуление и повторную вставку всех, кроме удаляемого
            MyRbMap temp = new MyRbMap();
            copyAllExcept(root, key, temp);
            root = temp.root;
            size--;
        }
        return old;
    }

    private void copyAllExcept(Node n, Integer skipKey, MyRbMap target) {
        if (n == null) return;
        copyAllExcept(n.left, skipKey, target);
        if (!n.key.equals(skipKey)) {
            target.put(n.key, n.value);
        }
        copyAllExcept(n.right, skipKey, target);
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsVal(root, value);
    }

    private boolean containsVal(Node n, Object val) {
        if (n == null) return false;
        if ((n.value == null && val == null) || (n.value != null && n.value.equals(val))) return true;
        return containsVal(n.left, val) || containsVal(n.right, val);
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHead(root, toKey, sub);
        return sub;
    }

    private void fillHead(Node n, Integer toKey, MyRbMap sub) {
        if (n == null) return;
        if (n.key.compareTo(toKey) < 0) {
            sub.put(n.key, n.value);
            fillHead(n.left, toKey, sub);
            fillHead(n.right, toKey, sub);
        } else {
            fillHead(n.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTail(root, fromKey, sub);
        return sub;
    }

    private void fillTail(Node n, Integer fromKey, MyRbMap sub) {
        if (n == null) return;
        if (n.key.compareTo(fromKey) >= 0) {
            sub.put(n.key, n.value);
            fillTail(n.left, fromKey, sub);
            fillTail(n.right, fromKey, sub);
        } else {
            fillTail(n.right, fromKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inOrder(n.right, sb);
    }

    // Заглушки
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
