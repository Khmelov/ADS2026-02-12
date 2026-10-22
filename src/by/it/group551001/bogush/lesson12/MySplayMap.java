package by.it.group551001.bogush.lesson12;

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
        int key;
        String value;
        Node left, right, parent;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // ---------- splay-операции ----------

    // один поворот: x поднимается на место своего родителя
    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        if (p.left == x) {
            p.left = x.right;
            if (x.right != null) {
                x.right.parent = p;
            }
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) {
                x.left.parent = p;
            }
            x.left = p;
        }
        p.parent = x;
        x.parent = g;
        if (g == null) {
            root = x;
        } else if (g.left == p) {
            g.left = x;
        } else {
            g.right = x;
        }
    }

    // поднимает x в корень (zig, zig-zig, zig-zag)
    private void splay(Node x) {
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

    // ищет узел; если нашли - splay найденного, иначе splay последнего посещённого
    private Node find(int key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                splay(cur);
                return cur;
            }
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    // ---------- обходы ----------

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inOrder(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        inOrder(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null) {
            if (n.value == null) {
                return true;
            }
        } else if (value.equals(n.value)) {
            return true;
        }
        if (hasValue(n.left, value)) {
            return true;
        }
        return hasValue(n.right, value);
    }

    // head=true: ключи < bound (или <= при inclusive); head=false: ключи >= bound (или > без inclusive)
    private void collect(Node n, int bound, boolean head, boolean inclusive, MySplayMap res) {
        if (n == null) {
            return;
        }
        collect(n.left, bound, head, inclusive, res);
        boolean take;
        if (head) {
            if (inclusive) {
                take = n.key <= bound;
            } else {
                take = n.key < bound;
            }
        } else {
            if (inclusive) {
                take = n.key >= bound;
            } else {
                take = n.key > bound;
            }
        }
        if (take) {
            res.put(n.key, n.value);
        }
        collect(n.right, bound, head, inclusive, res);
    }

    // ---------- поиск соседей ----------

    private Integer lowerOrHigher(int key, boolean lower, boolean inclusive) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (lower) {
                boolean ok;
                if (inclusive) {
                    ok = cur.key <= key;
                } else {
                    ok = cur.key < key;
                }
                if (ok) {
                    res = cur;
                    cur = cur.right;
                } else {
                    cur = cur.left;
                }
            } else {
                boolean ok;
                if (inclusive) {
                    ok = cur.key >= key;
                } else {
                    ok = cur.key > key;
                }
                if (ok) {
                    res = cur;
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
        }
        if (res == null) {
            return null;
        }
        splay(res);
        return res.key;
    }

    // ---------- обязательные методы ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }
        Node created = new Node(key, value, parent);
        if (key < parent.key) {
            parent.left = created;
        } else {
            parent.right = created;
        }
        size++;
        splay(created);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        if (n == null) {
            return null;
        }
        // n уже в корне
        String old = n.value;
        Node l = n.left;
        Node r = n.right;
        if (l != null) {
            l.parent = null;
        }
        if (r != null) {
            r.parent = null;
        }
        if (l == null) {
            root = r;
        } else {
            root = l;
            Node m = l;
            while (m.right != null) {
                m = m.right;
            }
            splay(m); // максимум левого поддерева становится корнем, правого ребёнка у него нет
            m.right = r;
            if (r != null) {
                r.parent = m;
            }
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        if (n == null) {
            return null;
        }
        return n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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
        MySplayMap res = new MySplayMap();
        collect(root, toKey, true, false, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap res = new MySplayMap();
        collect(root, fromKey, false, true, res);
        return res;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lowerOrHigher(key, true, false);
    }

    @Override
    public Integer floorKey(Integer key) {
        return lowerOrHigher(key, true, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return lowerOrHigher(key, false, true);
    }

    @Override
    public Integer higherKey(Integer key) {
        return lowerOrHigher(key, false, false);
    }

    // ---------- версии с флагом inclusive (простая реализация) ----------

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        collect(root, toKey, true, inclusive, res);
        return res;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        collect(root, fromKey, false, inclusive, res);
        return res;
    }

    // ---------- остальное из интерфейса (на уровень C не нужно) ----------

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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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