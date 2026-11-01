package by.it.group551002.yorsh.lesson12; // Измените имя пакета на нужное вам

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    // Вспомогательные повороты
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        if (y != null) y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        if (y != null) y.right = x;
        x.parent = y;
    }

    // Главная операция Splay — перемещение узла в корень
    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else {
                if (x == p.left && p == g.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else if (x == p.right && p == g.right) {
                    rotateLeft(g);
                    rotateLeft(p);
                } else if (x == p.right && p == g.left) {
                    rotateLeft(p);
                    rotateRight(g);
                } else {
                    rotateRight(p);
                    rotateLeft(g);
                }
            }
        }
    }

    // Поиск узла с вызовом splay для последнего посещенного узла
    private Node findNode(Integer key) {
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                splay(curr);
                return curr;
            }
        }
        splay(last);
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node curr = root;
        Node parent = null;
        int cmp = 0;

        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                splay(curr);
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;

        String oldValue = node.value;

        // При вызове findNode найденный узел уже перемещен в root
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else {
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;

            // Находим максимальный элемент в левом поддереве и делаем его корнем
            Node maxLeft = root;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);

            root.right = rightSubtree;
            if (rightSubtree != null) rightSubtree.parent = root;
        }

        size--;
        return oldValue;
    }

    // Навигационные методы по ключам

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) { // Строго меньше <
        Node curr = root;
        Integer res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) < 0) {
                res = curr.key;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) { // Меньше или равен <=
        Node curr = root;
        Integer res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) <= 0) {
                res = curr.key;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) { // Больше или равен >=
        Node curr = root;
        Integer res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) >= 0) {
                res = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) { // Строго больше >
        Node curr = root;
        Integer res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) > 0) {
                res = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return res;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        fillHeadMap(root, toKey, inclusive, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap subMap) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            subMap.put(node.key, node.value);
            fillHeadMap(node.left, toKey, inclusive, subMap);
            fillHeadMap(node.right, toKey, inclusive, subMap);
        } else {
            fillHeadMap(node.left, toKey, inclusive, subMap);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        fillTailMap(root, fromKey, inclusive, subMap);
        return subMap;
    }

    private void fillTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap subMap) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            subMap.put(node.key, node.value);
            fillTailMap(node.left, fromKey, inclusive, subMap);
            fillTailMap(node.right, fromKey, inclusive, subMap);
        } else {
            fillTailMap(node.right, fromKey, inclusive, subMap);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Необязательные методы интерфейса NavigableMap       ///////
    /////////////////////////////////////////////////////////////////////////

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
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}