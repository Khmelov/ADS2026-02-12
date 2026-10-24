package by.it.group551002.voroshko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String>{

    private class Node {
        Integer key;
        String value;

        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    };

    private Node root;
    private int size;

    private int cmp(Integer a, Integer b) {
        return a.compareTo(b);
    }

    private void rotateLeft(Node a) {
        Node b = a.right;
        a.right = b.left;

        if (b.left != null) {
            b.left.parent = a;
        }

        b.parent = a.parent;

        if (a.parent == null) {
            root = b;
        } else if (a == a.parent.left) {
            a.parent.left = b;
        } else {
            a.parent.right = b;
        }

        b.left = a;
        a.parent = b;
    }

    private void rotateRight(Node a) {
        Node b = a.left;
        a.left = b.right;

        if (b.right != null) {
            b.right.parent = a;
        }

        b.parent = a.parent;

        if (a.parent == null) {
            root = b;
        } else if (a == a.parent.right) {
            a.parent.right = b;
        } else {
            a.parent.left = b;
        }

        b.right = a;
        a.parent = b;
    }

    private void splay(Node a) {
        while (a.parent != null) {
            Node p = a.parent;
            Node g = p.parent;

            if (g == null) {
                if (a == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (a == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (a == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (a == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    private Node findNode(Integer key) {
        Node cur = root, last = null;

        while (cur != null) {
            last = cur;
            int c = cmp(key, cur.key);

            if (c == 0) {
                splay(cur);
                return cur;
            }

            cur = (c < 0) ? cur.left : cur.right;
        }

        if (last != null) splay(last);

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);

        if (sb.length() > 1 && sb.charAt(sb.length() - 1) == ' ') {
            sb.setLength(sb.length() - 2);
        }

        sb.append('}');
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;

        inorder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inorder(n.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node cur = root, parent = null;
        int c = 0;

        while (cur != null) {
           parent = cur;
           c = cmp(key, cur.key);

           if (c == 0) {
               String old = cur.value;
               cur.value = value;
               splay(cur);
               return old;
           }
           cur = (c < 0) ? cur.left : cur.right;
        }

        Node n = new Node(key, value, parent);

        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }

        size++;
        splay(n);
        return null;
    }

    @Override
    public String remove(Object key) {
        Node n = findNode((Integer) key);

        if (n == null) return null;

        String old = n.value;
        size--;

        Node left = n.left;
        Node right = n.right;

        if (left != null) left.parent = null;
        if (right != null) right.parent = null;

        if (left == null) {
            root = right;
        } else {
            Node maxLeft = left;

            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            splay(maxLeft);
            maxLeft.right = right;

            if (right != null) right.parent = maxLeft;

            root = maxLeft;
        }

        return old;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return (n != null) ? n.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;

        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
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
        MySplayMap m = new MySplayMap();
        addRange(root, m, true, toKey, false, null);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        addRange(root, m, false, null, true, fromKey);
        return m;
    }

    private void addRange(Node n, MySplayMap m, boolean hasRight, Integer rightBound, boolean hasLeft, Integer leftBound) {
        if (n == null) return;

        addRange(n.left, m, hasRight, rightBound, hasLeft, leftBound);
        boolean ok = true;

        if (hasRight && cmp(n.key, rightBound) >= 0) ok = false;
        if (hasLeft && cmp(n.key, leftBound) < 0) ok = false;

        if (ok) m.put(n.key, n.value);

        addRange(n.right, m, hasRight, rightBound, hasLeft, leftBound);
    }

    @Override
    public Integer firstKey() {
        Node n = root;

        while (n.left != null) {
            n = n.left;
        }

        splay(n);

        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = root;

        while (n.right != null) {
            n = n.right;
        }

        splay(n);

        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lowerKeySearch(key);
    }

    private Integer lowerKeySearch(Integer key) {
        Node cur = root, best = null;

        while (cur != null) {
            int c = cmp(key, cur.key);

            if (c > 0) {
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
        Node cur = root, best = null;

        while (cur != null) {
            int c = cmp(key, cur.key);

            if (c == 0) {
                return cur.key;
            }

            if (c > 0) {
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
        Node cur = root, best = null;

        while (cur != null) {
            int c = cmp(key, cur.key);

            if (c == 0) {
                return cur.key;
            }

            if (c < 0) {
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
        Node cur = root, best = null;

        while (cur != null) {
            int c = cmp(key, cur.key);

            if (c < 0) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        return best == null ? null : best.key;
    }

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
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
