package by.it.group510902.klimantovich.lesson01.lesson12;
import java.util.*;
public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key; // ключ элемента
        String value; // строковое значение
        Node left, right, parent; // ссылки на левого, правого потомков и родителя
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    private Node root; // корень splay-дерева
    private int size = 0; // счетчик элементов в мапе
    private void rightRotate(Node x) {
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
    private void leftRotate(Node x) {
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
    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) rightRotate(x.parent); // случай Zig
                else leftRotate(x.parent); // случай Zag
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rightRotate(x.parent.parent); rightRotate(x.parent); // случай Zig-Zig
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                leftRotate(x.parent.parent); leftRotate(x.parent); // случай Zag-Zag
            } else if (x == x.parent.left && x.parent == x.parent.parent.right) {
                rightRotate(x.parent); leftRotate(x.parent); // случай Zig-Zag
            } else {
                leftRotate(x.parent); rightRotate(x.parent); // случай Zag-Zig
            }
        }
    }
    private void toStringHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringHelper(node.left, sb); // рекурсивно собираю элементы по возрастанию ключей
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toStringHelper(node.right, sb);
    }
    //    Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    //    и работает на основе splay-дерева
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //    Метод toString() должен выводить элементы в порядке возрастания ключей
    //    Формат вывода: скобки (фигурные) и разделители
    //    (знак равенства и запятая с пробелом) должны
    //    быть такими же как в методе toString() обычной коллекции
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringHelper(root, sb); // запускаю рекурсивный обход дерева
        sb.append("}");
        return sb.toString();
    }
    @Override
    public int size() { return size; } // отдаю размер карты
    @Override
    public void clear() { root = null; size = 0; } // очищаю splay-дерево
    @Override
    public boolean isEmpty() { return size == 0; } // проверка на пустоту
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value); size++; return null;
        }
        Node current = root, parent = null;
        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                String oldVal = current.value; current.value = value;
                splay(current); return oldVal; // если ключ уже был, обновляю и продвигаю наверх
            }
        }
        Node newNode = new Node(key, value); newNode.parent = parent;
        if (key.compareTo(parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        size++; splay(newNode); // продвигаю новый добавленный элемент в корень дерева
        return null;
    }
    @Override
    public String remove(Object key) {
        Node current = root;
        while (current != null) {
            int cmp = ((Integer) key).compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else break;
        }
        if (current == null) return null;
        String removedValue = current.value; splay(current); // поднимаем удаляемый узел в корень
        if (current.left == null) {
            root = current.right; if (root != null) root.parent = null;
        } else {
            Node leftSubtree = current.left; leftSubtree.parent = null;
            Node maxLeft = leftSubtree;
            while (maxLeft.right != null) maxLeft = maxLeft.right; // ищу максимум в левом поддереве
            splay(maxLeft); maxLeft.right = current.right;
            if (current.right != null) current.right.parent = maxLeft;
            root = maxLeft;
        }
        size--; return removedValue;
    }
    @Override
    public String get(Object key) {
        Node current = root;
        while (current != null) {
            int cmp = ((Integer) key).compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else { splay(current); return current.value; } // если нашли узел, splay продвигает его в корень
        }
        return null;
    }
    @Override
    public boolean containsKey(Object key) { return get(key) != null; }
    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }
    @Override
    public boolean containsValue(Object value) { return containsValueHelper(root, value); }
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MySplayMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) subMap.put(node.key, node.value);
        if (node.key.compareTo(fromKey) > 0) subMapHelper(node.left, fromKey, toKey, subMap);
        if (node.key.compareTo(toKey) < 0) subMapHelper(node.right, fromKey, toKey, subMap);
    }
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap(); subMapHelper(root, Integer.MIN_VALUE, toKey, subMap); return subMap;
    }
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap(); subMapHelper(root, fromKey, Integer.MAX_VALUE, subMap); return subMap;
    }
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root; while (currentLeft(curr) != null) curr = curr.left; return curr.key;
    }
    private Node currentLeft(Node n) { return n.left; }
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root; while (curr.right != null) curr = curr.right; return curr.key;
    }
    @Override
    public Integer lowerKey(Integer key) {
        Node curr = root; Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) < 0) { best = curr.key; curr = curr.right; }
            else curr = curr.left;
        }
        return best; // возвращаю наибольший ключ, строго меньший переданного
    }
    @Override
    public Integer floorKey(Integer key) {
        Node curr = root; Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) <= 0) { best = curr.key; curr = curr.right; }
            else curr = curr.left;
        }
        return best; // возвращаю наибольший ключ, меньший или равный переданному
    }
    @Override
    public Integer ceilingKey(Integer key) {
        Node curr = root; Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) >= 0) { best = curr.key; curr = curr.left; }
            else curr = curr.right;
        }
        return best; // возвращаю наименьший ключ, больший или равный переданному
    }
    @Override
    public Integer higherKey(Integer key) {
        Node curr = root; Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) > 0) { best = curr.key; curr = curr.left; }
            else curr = curr.right;
        }
        return best; // возвращаю наименьший ключ, строго больший переданного
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) put(entry.getKey(), entry.getValue());
    }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}
