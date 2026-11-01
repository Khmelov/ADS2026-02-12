package by.it.group551001.kotsuba.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
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
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        this.root = null;
        this.size = 0;
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
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    @Override
    public String get(Object key) {
        Node node = getNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;

        Node t = root;
        if (t == null) {
            root = new Node(key, value, BLACK, null);
            size = 1;
            return null;
        }

        Node parent;
        int cmp;
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

        Node e = new Node(key, value, RED, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }

        fixAfterInsertion(e);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node p = getNode(key);
        if (p == null) {
            return null;
        }

        String oldValue = p.value;
        deleteNode(p);
        return oldValue;
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) return null;
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MyRbMap subMap = new MyRbMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private Node getNode(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
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

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    private boolean isRed(Node p) {
        return p != null && p.color == RED;
    }

    private boolean isBlack(Node p) {
        return p == null || p.color == BLACK;
    }

    private void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private Node parentOf(Node p) {
        return p == null ? null : p.parent;
    }

    private Node leftOf(Node p) {
        return p == null ? null : p.left;
    }

    private Node rightOf(Node p) {
        return p == null ? null : p.right;
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            l.parent = p.parent;
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }
            l.right = p;
            p.parent = l;
        }
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (isRed(y)) {
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
                if (isRed(y)) {
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
        root.color = BLACK;
    }

    private void deleteNode(Node p) {
        size--;

        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;

            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }

            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && isBlack(x)) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));

                if (isRed(sib)) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (isBlack(leftOf(sib)) && isBlack(rightOf(sib))) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (isBlack(rightOf(sib))) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, parentOf(x) == null ? BLACK : parentOf(x).color);
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));

                if (isRed(sib)) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (isBlack(rightOf(sib)) && isBlack(leftOf(sib))) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (isBlack(leftOf(sib))) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, parentOf(x) == null ? BLACK : parentOf(x).color);
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private Node successor(Node t) {
        if (t == null) {
            return null;
        } else if (t.right != null) {
            Node p = t.right;
            while (p.left != null) {
                p = p.left;
            }
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

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrderToString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderToString(node.right, sb);
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }
        fillHeadMap(node.left, toKey, subMap);
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            fillHeadMap(node.right, toKey, subMap);
        }
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) >= 0) {
            fillTailMap(node.left, fromKey, subMap);
            subMap.put(node.key, node.value);
            fillTailMap(node.right, fromKey, subMap);
        } else {
            fillTailMap(node.right, fromKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
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
