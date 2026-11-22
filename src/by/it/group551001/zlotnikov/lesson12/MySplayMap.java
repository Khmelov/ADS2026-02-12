package by.it.group551001.zlotnikov.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private Node splay(Node x) {
        if (x == null) {
            return null;
        }
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.left && p == g.right) {
                rotateRight(p);
                rotateLeft(g);
            } else {
                rotateLeft(p);
                rotateRight(g);
            }
        }
        return x;
    }

    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                root = splay(cur);
                return root;
            }
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        if (last != null) {
            root = splay(last);
        }
        return null;
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    private boolean equalsRec(Node n, Map<?, ?> m) {
        if (n == null) {
            return true;
        }
        Object v = m.get(n.key);
        if (v == null && !m.containsKey(n.key)) {
            return false;
        }
        if (v == null ? n.value != null : !v.equals(n.value)) {
            return false;
        }
        return equalsRec(n.left, m) && equalsRec(n.right, m);
    }

    private int hashRec(Node n) {
        if (n == null) {
            return 0;
        }
        int h = (n.key == null ? 0 : n.key.hashCode())
                ^ (n.value == null ? 0 : n.value.hashCode());
        return h + hashRec(n.left) + hashRec(n.right);
    }

    private void copyHead(Node n, Integer toKey, MySplayMap dest) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(toKey) < 0) {
            copyHead(n.left, toKey, dest);
            dest.put(n.key, n.value);
            copyHead(n.right, toKey, dest);
        } else {
            copyHead(n.left, toKey, dest);
        }
    }

    private void copyHeadInclusive(Node n, Integer toKey, MySplayMap dest) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(toKey) <= 0) {
            copyHeadInclusive(n.left, toKey, dest);
            dest.put(n.key, n.value);
            copyHeadInclusive(n.right, toKey, dest);
        } else {
            copyHeadInclusive(n.left, toKey, dest);
        }
    }

    private void copyTail(Node n, Integer fromKey, MySplayMap dest) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(fromKey) >= 0) {
            copyTail(n.left, fromKey, dest);
            dest.put(n.key, n.value);
            copyTail(n.right, fromKey, dest);
        } else {
            copyTail(n.right, fromKey, dest);
        }
    }

    private void copyTailExclusive(Node n, Integer fromKey, MySplayMap dest) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(fromKey) > 0) {
            copyTailExclusive(n.left, fromKey, dest);
            dest.put(n.key, n.value);
            copyTailExclusive(n.right, fromKey, dest);
        } else {
            copyTailExclusive(n.right, fromKey, dest);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "{";
        Node[] stack = new Node[32];
        int top = 0;
        Node cur = root;
        boolean first = true;
        while (cur != null || top > 0) {
            while (cur != null) {
                if (top == stack.length) {
                    Node[] grown = new Node[stack.length * 2];
                    for (int i = 0; i < top; i++) {
                        grown[i] = stack[i];
                    }
                    stack = grown;
                }
                stack[top++] = cur;
                cur = cur.left;
            }
            cur = stack[--top];
            if (!first) {
                result += ", ";
            }
            result += cur.key + "=" + cur.value;
            first = false;
            cur = cur.right;
        }
        return result + "}";
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
        Node cur = root;
        while (true) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                root = splay(cur);
                return old;
            } else if (cmp < 0) {
                if (cur.left == null) {
                    Node z = new Node(key, value);
                    z.parent = cur;
                    cur.left = z;
                    size++;
                    root = splay(z);
                    return null;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    Node z = new Node(key, value);
                    z.parent = cur;
                    cur.right = z;
                    size++;
                    root = splay(z);
                    return null;
                }
                cur = cur.right;
            }
        }
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node found = findNode((Integer) key);
        if (found == null) {
            return null;
        }
        String old = found.value;
        Node left = found.left;
        Node right = found.right;
        if (left == null) {
            root = right;
            if (root != null) {
                root.parent = null;
            }
        } else if (right == null) {
            root = left;
            root.parent = null;
        } else {
            left.parent = null;
            right.parent = null;
            Node maxLeft = left;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            root = left;
            root = splay(maxLeft);
            root.right = right;
            right.parent = root;
        }
        found.left = null;
        found.right = null;
        found.parent = null;
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
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
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        if (inclusive) {
            copyHeadInclusive(root, toKey, result);
        } else {
            copyHead(root, toKey, result);
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        if (inclusive) {
            copyTail(root, fromKey, result);
        } else {
            copyTailExclusive(root, fromKey, result);
        }
        return result;
    }

    @Override
    public Integer firstKey() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Map is empty");
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Map is empty");
        }
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Node best = null;
        while (cur != null) {
            if (key.compareTo(cur.key) > 0) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Node best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp >= 0) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Node best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp <= 0) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best == null ? null : best.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Node best = null;
        while (cur != null) {
            if (key.compareTo(cur.key) < 0) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best == null ? null : best.key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы NavigableMap / Map           ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size) {
            return false;
        }
        return equalsRec(root, m);
    }

    @Override
    public int hashCode() {
        return hashRec(root);
    }
}