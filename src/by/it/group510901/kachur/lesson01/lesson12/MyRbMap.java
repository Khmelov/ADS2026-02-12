
package by.it.group510901.kachur.lesson01.lesson12;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right; // Левый и правый потомки
        boolean color;    // Цвет связи с родительским узлом (RED или BLACK)
        int size;         // Количество узлов в поддереве

        Node(Integer key, String value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node root; // Корень дерева

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    // Проверка цвета узла
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // --- Базовые операции поиска ---

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node x, Object value) {
        if (x == null) return false;
        if ((x.value == null && value == null) || (x.value != null && x.value.equals(value))) {
            return true;
        }
        return containsValueRec(x.left, value) || containsValueRec(x.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return getRec(root, (Integer) key);
    }

    private String getRec(Node x, Integer key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    // --- Балансировочные операции LLRB ---

    // Поворот влево
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // Поворот вправо
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // Смена цветов
    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    // --- Вставка (put) ---

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        String oldValue = get(key);
        root = putRec(root, key, value);
        root.color = BLACK; // Корень всегда черный
        return oldValue;
    }

    private Node putRec(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED, 1);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = putRec(h.left, key, value);
        else if (cmp > 0) h.right = putRec(h.right, key, value);
        else h.value = value; // Ключ уже есть, обновляем значение

        // Исправление нарушений LLRB-дерева во время подъема
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    // --- Удаление (remove) ---

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        String oldValue = get(k);
        if (oldValue != null) {
            root = removeRec(root, k);
            if (root != null) root.color = BLACK;
        }
        return oldValue;
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

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node removeMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = removeMin(h.left);
        return balance(h);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node removeRec(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = removeRec(h.left, key);
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
            } else {
                h.right = removeRec(h.right, key);
            }
        }
        return balance(h);
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    @Override
    public void clear() {
        root = null;
    }

    // --- Методы SortedMap (firstKey, lastKey, headMap, tailMap) ---

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node x, Integer toKey, MyRbMap subMap) {
        if (x == null) return;
        int cmp = x.key.compareTo(toKey);
        if (cmp < 0) {
            subMap.put(x.key, x.value);
            fillHeadMap(x.left, toKey, subMap);
            fillHeadMap(x.right, toKey, subMap);
        } else {
            fillHeadMap(x.left, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node x, Integer fromKey, MyRbMap subMap) {
        if (x == null) return;
        int cmp = x.key.compareTo(fromKey);
        if (cmp >= 0) {
            subMap.put(x.key, x.value);
            fillTailMap(x.left, fromKey, subMap);
            fillTailMap(x.right, fromKey, subMap);
        } else {
            fillTailMap(x.right, fromKey, subMap);
        }
    }

    // --- toString() ---

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrderToString(node.right, sb);
        }
    }

    // --- Заглушки для прочих методов SortedMap / Map ---
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}