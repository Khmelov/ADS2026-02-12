package by.it.group551001.belozorchik.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Comparator;

public class MyRbMap implements SortedMap<Integer, String>{

    private static final boolean RED = true;
    private static final boolean BLACK = true;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
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
        if (x == null) return false;
        return x.color == RED;
    }

    //повороты
    //левый
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.parent = h.parent;
        if (h.parent == null) {
            root = x;
        } else if (h == h.parent.left) {
            h.parent.left = x;
        } else {
            h.parent.right = x;
        }
        x.left = h;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    //правый
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.parent = h.parent;
        if (h.parent == null) {
            root = x;
        } else if (h == h.parent.right) {
            h.parent.right = x;
        } else {
            h.parent.left = x;
        }
        x.right = h;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    //инверсия цветов
    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    //поиск переданного знач - рекурсивный обход узлов
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null;
    }

    //вставка эл - Left-Leaning Red-Black Tree подход
    //перем хран старое знач
    private String oldValueHolder;

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        oldValueHolder = null;
        root = put(root, null, key, value);
        root.color = BLACK;
        return oldValueHolder;
    }

    //+ рекурсивная вставка
    private Node put(Node h, Node parent, Integer key, String value) {
        //баз случ -
        if (h == null) {
            size++;
            Node node = new Node(key, value, RED);
            node.parent = parent;
            return node;
        }

        //рекурсивный списк
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, h, key, value);
        } else if (cmp > 0) {
            h.right = put(h.right, h, key, value);
        } else {    //совпал
            oldValueHolder = h.value;
            h.value = value;
            return h;
        }

        //!коррекция
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    //удаление
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || !containsKey(key)) return null;
        String val = get(key);
        root = remove(root, (Integer) key);
        if (root != null) root.color = BLACK;
        size--;
        return val;
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

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = remove(h.left, key);
            if (h.left != null) h.left.parent = h;
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = removeMin(h.right);
                if (h.right != null) h.right.parent = h;
            } else {
                h.right = remove(h.right, key);
                if (h.right != null) h.right.parent = h;
            }
        }
        return balance(h);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node removeMin(Node h) {
        if (h.left == null)
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = removeMin(h.left);
        if (h.left != null) h.left.parent = h;
        return balance(h);
    }

    //
    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            fillHeadMap(node.left, toKey, subMap);
            fillHeadMap(node.right, toKey, subMap);
        } else {
            fillHeadMap(node.left, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            fillTailMap(node.left, fromKey, subMap);
            fillTailMap(node.right, fromKey, subMap);
        } else {
            fillTailMap(node.right, fromKey, subMap);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }

    //заглушки интерфейса SortedMap / Map
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

}
