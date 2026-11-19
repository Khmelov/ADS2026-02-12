package by.it.group551001.belozorchik.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    // --- Базовые операции Splay-дерева (Rotations & Splay) ---

    private void rotate(Node x) {
        Node p = x.parent;
        if (p == null) return;
        Node g = p.parent;

        if (p.left == x) {
            p.left = x.right;
            if (x.right != null) x.right.parent = p;
            x.right = p;
            p.parent = x;
        } else {
            p.right = x.left;
            if (x.left != null) x.left.parent = p;
            x.left = p;
            p.parent = x;
        }

        x.parent = g;
        if (g != null) {
            if (g.left == p) g.left = x;
            else g.right = x;
        } else {
            root = x;
        }
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                rotate(x);
            } else if ((g.left == p) == (p.left == x)) {
                rotate(p);
                rotate(x);
            } else {
                rotate(x);
                rotate(x);
            }
        }
    }

    private Node find(Integer key) {
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
        if (last != null) splay(last);
        return null;
    }

    // --- Обязательные методы ---

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) return false;
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object val) {
        if (node == null) return false;
        if (val.equals(node.value)) return true;
        return containsValueHelper(node.left, val) || containsValueHelper(node.right, val);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node curr = root;
        Node p = null;
        int cmp = 0;
        while (curr != null) {
            p = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                splay(curr);
                String oldVal = curr.value;
                curr.value = value;
                return oldVal;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = p;
        if (cmp < 0) p.left = newNode;
        else p.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        if (node == null) return null;

        String oldVal = node.value;
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            Node leftSubTree = node.left;
            leftSubTree.parent = null;
            node.left = null;

            Node rightSubTree = node.right;
            rightSubTree.parent = null;
            node.right = null;

            Node maxLeft = leftSubTree;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);
            maxLeft.right = rightSubTree;
            if (rightSubTree != null) rightSubTree.parent = maxLeft;
            root = maxLeft;
        }
        size--;
        return oldVal;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

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
    public Integer lowerKey(Integer key) {
        Node curr = root;
        Integer result = null;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp <= 0) {
                curr = curr.left;
            } else {
                result = curr.key;
                curr = curr.right;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node curr = root;
        Integer result = null;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else {
                result = curr.key;
                curr = curr.right;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node curr = root;
        Integer result = null;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp <= 0) {
                result = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node curr = root;
        Integer result = null;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                result = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        headMapHelper(root, toKey, sub);
        return sub;
    }

    private void headMapHelper(Node node, Integer toKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            headMapHelper(node.left, toKey, sub);
            sub.put(node.key, node.value);
            headMapHelper(node.right, toKey, sub);
        } else {
            headMapHelper(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        tailMapHelper(root, fromKey, sub);
        return sub;
    }

    private void tailMapHelper(Node node, Integer fromKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            tailMapHelper(node.left, fromKey, sub);
            sub.put(node.key, node.value);
            tailMapHelper(node.right, fromKey, sub);
        } else {
            tailMapHelper(node.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        appendInOrder(node.right, sb);
    }

    // --- Остальные методы интерфейса NavigableMap (заглушки) ---

    @Override public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); } // Добавлено

    // Дополнительные методы Map, требуемые интерфейсом при полной реализации (если понадобятся компилятору в вашем JDK):
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public Comparator<? super Integer> comparator() { throw new UnsupportedOperationException(); }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean inclusiveKey1, Integer toKey, boolean inclusiveKey2) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
}