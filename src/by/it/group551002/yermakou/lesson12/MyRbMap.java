package by.it.group551002.yermakou.lesson12;

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
        Node left;
        Node right;
        Node parent;
        boolean color = RED;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    // Общий контейнер состояния дерева, чтобы subMap / headMap / tailMap видели актуальный root и size
    private static class TreeHolder {
        Node root;
        int size = 0;
    }

    private final TreeHolder holder;

    // Границы для поддеревьев (headMap / tailMap / subMap)
    private final Integer fromKey;
    private final Integer toKey;

    public MyRbMap() {
        this.holder = new TreeHolder();
        this.fromKey = null;
        this.toKey = null;
    }

    private MyRbMap(TreeHolder holder, Integer fromKey, Integer toKey) {
        this.holder = holder;
        this.fromKey = fromKey;
        this.toKey = toKey;
    }

    // =========================================================================
    // Обязательные к реализации методы
    // =========================================================================

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        checkKeyRange(key);

        Node t = holder.root;
        if (t == null) {
            holder.root = new Node(key, value, null);
            holder.root.color = BLACK;
            holder.size = 1;
            return null;
        }

        int cmp;
        Node parent;
        do {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                String oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        } while (t != null);

        Node e = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        fixAfterInsertion(e);
        holder.size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node p = getNode((Integer) key);
        if (p == null) {
            return null;
        }

        String oldValue = p.value;
        deleteNode(p);
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node p = getNode((Integer) key);
        return (p == null) ? null : p.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInSubtree(holder.root, value);
    }

    private boolean containsValueInSubtree(Node p, Object value) {
        if (p == null) return false;
        if (inRange(p.key)) {
            if (value == null ? p.value == null : value.equals(p.value)) {
                return true;
            }
        }
        return containsValueInSubtree(p.left, value) || containsValueInSubtree(p.right, value);
    }

    @Override
    public int size() {
        if (fromKey == null && toKey == null) {
            return holder.size;
        }
        int count = 0;
        Node current = getFirstNode();
        while (current != null && inRange(current.key)) {
            count++;
            current = successor(current);
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        if (fromKey == null && toKey == null) {
            holder.root = null;
            holder.size = 0;
        } else {
            Node current = getFirstNode();
            while (current != null && inRange(current.key)) {
                Node next = successor(current);
                deleteNode(current);
                current = next;
            }
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        return new MyRbMap(this.holder, this.fromKey, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        return new MyRbMap(this.holder, fromKey, this.toKey);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        return new MyRbMap(this.holder, fromKey, toKey);
    }

    @Override
    public Integer firstKey() {
        Node node = getFirstNode();
        if (node == null || !inRange(node.key)) {
            throw new java.util.NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        Node node = getLastNode();
        if (node == null || !inRange(node.key)) {
            throw new java.util.NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public String toString() {
        String result = "{";
        boolean first = true;

        Node current = getFirstNode();
        while (current != null && inRange(current.key)) {
            if (!first) {
                result += ", ";
            }
            result += current.key + "=" + current.value;
            first = false;
            current = successor(current);
        }

        result += "}";
        return result;
    }

    // =========================================================================
    // Вспомогательные методы поддеревьев (subMap/headMap/tailMap)
    // =========================================================================

    private boolean inRange(Integer key) {
        if (fromKey != null && key.compareTo(fromKey) < 0) return false;
        if (toKey != null && key.compareTo(toKey) >= 0) return false;
        return true;
    }

    private void checkKeyRange(Integer key) {
        if (!inRange(key)) {
            throw new IllegalArgumentException("Key out of range");
        }
    }

    private Node getNode(Integer key) {
        if (!inRange(key)) return null;
        Node p = holder.root;
        while (p != null) {
            int cmp = key.compareTo(p.key);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    private Node getFirstNode() {
        Node p = holder.root;
        if (p == null) return null;
        while (p.left != null) {
            p = p.left;
        }
        if (fromKey != null) {
            while (p != null && p.key.compareTo(fromKey) < 0) {
                p = successor(p);
            }
        }
        return p;
    }

    private Node getLastNode() {
        Node p = holder.root;
        if (p == null) return null;
        while (p.right != null) {
            p = p.right;
        }
        if (toKey != null) {
            while (p != null && p.key.compareTo(toKey) >= 0) {
                p = predecessor(p);
            }
        }
        return p;
    }

    private Node successor(Node t) {
        if (t == null) return null;
        if (t.right != null) {
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Node predecessor(Node t) {
        if (t == null) return null;
        if (t.left != null) {
            Node p = t.left;
            while (p.right != null) p = p.right;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    // =========================================================================
    // Балансировка Красно-Черного Дерева
    // =========================================================================

    private static boolean colorOf(Node p) {
        return (p == null ? BLACK : p.color);
    }

    private static Node parentOf(Node p) {
        return (p == null ? null : p.parent);
    }

    private static void setColor(Node p, boolean c) {
        if (p != null) p.color = c;
    }

    private static Node leftOf(Node p) {
        return (p == null) ? null : p.left;
    }

    private static Node rightOf(Node p) {
        return (p == null) ? null : p.right;
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null) r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null) holder.root = r;
            else if (p.parent.left == p) p.parent.left = r;
            else p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null) holder.root = l;
            else if (p.parent.right == p) p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != holder.root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        holder.root.color = BLACK;
    }

    private void deleteNode(Node p) {
        holder.size--;

        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) holder.root = replacement;
            else if (p == p.parent.left) p.parent.left = replacement;
            else p.parent.right = replacement;

            p.left = p.right = p.parent = null;

            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            holder.root = null;
        } else {
            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) p.parent.left = null;
                else if (p == p.parent.right) p.parent.right = null;
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != holder.root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = holder.root;
                }
            } else {
                Node sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = holder.root;
                }
            }
        }
        setColor(x, BLACK);
    }

    // =========================================================================
    // Заглушки для оставшихся методов интерфейса SortedMap
    // =========================================================================

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}