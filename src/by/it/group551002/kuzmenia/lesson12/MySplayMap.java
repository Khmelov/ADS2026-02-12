package by.it.group551002.kuzmenia.lesson12;

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
        Node left;
        Node right;
        Node parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Node n = minimum(root); n != null; n = successor(n)) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(n.key).append('=').append(n.value);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;

        if (root == null) {
            root = new Node(k, value);
            size = 1;
            return null;
        }

        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            if (k < cur.key) {
                cur = cur.left;
            } else if (k > cur.key) {
                cur = cur.right;
            } else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }

        Node node = new Node(k, value);
        node.parent = parent;
        if (k < parent.key) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;
        splay(node);
        return null;
    }
    
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = access((Integer) key);
        if (node == null) {
            return null;
        }
        String old = node.value;

        Node left = node.left;
        Node right = node.right;
        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }
        node.left = null;
        node.right = null;

        if (left == null) {
            root = right;
        } else {

            root = left;
            Node max = left;
            while (max.right != null) {
                max = max.right;
            }
            splay(max);
            root.right = right;
            if (right != null) {
                right.parent = root;
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
        Node node = access((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return access((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node n = minimum(root); n != null; n = successor(n)) {
            boolean same = (value == null) ? (n.value == null) : value.equals(n.value);
            if (same) {
                return true;
            }
        }
        return false;
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
        int bound = toKey;
        MySplayMap result = new MySplayMap();
        for (Node n = minimum(root); n != null; n = successor(n)) {
            if (n.key < bound || (inclusive && n.key == bound)) {
                result.put(n.key, n.value);
            } else {
                break;
            }
        }
        return result;
    }
    
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        int bound = fromKey;
        MySplayMap result = new MySplayMap();
        Node start = inclusive ? ceilingNode(bound) : higherNode(bound);
        for (Node n = start; n != null; n = successor(n)) {
            result.put(n.key, n.value);
        }
        return result;
    }
    
    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = minimum(root);
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
        Node n = lowerNode(key);
        if (n == null) {
            return null;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = floorNode(key);
        if (n == null) {
            return null;
        }
        splay(n);
        return n.key;
    }
    
    @Override
    public Integer ceilingKey(Integer key) {
        Node n = ceilingNode(key);
        if (n == null) {
            return null;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(key);
        if (n == null) {
            return null;
        }
        splay(n);
        return n.key;
    }

    private Node lowerNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (cur.key < key) {
                res = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }
    
    private Node floorNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (cur.key <= key) {
                res = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }
    
    private Node ceilingNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (cur.key >= key) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }

    private Node higherNode(int key) {
        Node cur = root;
        Node res = null;
        while (cur != null) {
            if (cur.key > key) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }
    
    private static Node minimum(Node n) {
        if (n == null) {
            return null;
        }
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    private static Node successor(Node n) {
        if (n.right != null) {
            return minimum(n.right);
        }
        Node child = n;
        Node p = n.parent;
        while (p != null && child == p.right) {
            child = p;
            p = p.parent;
        }
        return p;
    }

    private Node access(int key) {
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
    
    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;

        if (x == p.left) {
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

    
    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                rotate(x);
            } else if ((x == p.left) == (p == g.left)) {

                rotate(p);
                rotate(x);
            } else {

                rotate(x);
                rotate(x);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}