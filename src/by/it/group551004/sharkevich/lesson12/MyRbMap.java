package by.it.group551004.sharkevich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red;

        Node(int key, String value, boolean red, Node parent) {
            this.key = key;
            this.value = value;
            this.red = red;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private void rotateLeft(Node n) {
        Node t1 = n.right;
        n.right = t1.left;
        if (t1.left != null) {
            t1.left.parent = n;
        }
        t1.parent = n.parent;
        if (n.parent == null) {
            root = t1;
        } else if (n == n.parent.left) {
            n.parent.left = t1;
        } else {
            n.parent.right = t1;
        }
        t1.left = n;
        n.parent = t1;
    }

    private void rotateRight(Node n) {
        Node t1 = n.left;
        n.left = t1.right;
        if (t1.right != null) {
            t1.right.parent = n;
        }
        t1.parent = n.parent;
        if (n.parent == null) {
            root = t1;
        } else if (n == n.parent.right) {
            n.parent.right = t1;
        } else {
            n.parent.left = t1;
        }
        t1.right = n;
        n.parent = t1;
    }

    private Node min(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    private Node max(Node n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    private Node findNode(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }

    private static boolean isRed(Node n) {
        return n != null && n.red;
    }

    private static Node leftOf(Node n) {
        return n == null ? null : n.left;
    }

    private static Node rightOf(Node n) {
        return n == null ? null : n.right;
    }

    private void fixAfterInsert(Node n) {
        n.red = true;
        while (n != root && isRed(n.parent)) {
            if (n.parent == n.parent.parent.left) {
                Node m = n.parent.parent.right;
                if (isRed(m)) {
                    n.parent.red = false;
                    m.red = false;
                    n.parent.parent.red = true;
                    n = n.parent.parent;
                } else {
                    if (n == n.parent.right) {
                        n = n.parent;
                        rotateLeft(n);
                    }
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateRight(n.parent.parent);
                }
            } else {
                Node m = n.parent.parent.left;
                if (isRed(m)) {
                    n.parent.red = false;
                    m.red = false;
                    n.parent.parent.red = true;
                    n = n.parent.parent;
                } else {
                    if (n == n.parent.left) {
                        n = n.parent;
                        rotateRight(n);
                    }
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateLeft(n.parent.parent);
                }
            }
        }
        root.red = false;
    }

    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node parent = null, curr = root;
        int cmp = 0;
        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }
        Node n = new Node(key, value,true, parent);
        if (parent == null) {
            root = n;
        } else if (cmp < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        size++;
        fixAfterInsert(n);
        return null;
    }

    public String remove(Object key) {
        return (key instanceof Integer) ? remove((Integer) key) : null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    public boolean containsKey(Integer key) {
        return findNode(key) != null;
    }

    public boolean containsValueHelper(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueHelper(n.left, value) || containsValueHelper(n.right, value);
    }

    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private void fixAfterDelete(Node x, Node xParent, boolean xIsLeft) {
        while (x != root && !isRed(x)) {
            if (xIsLeft) {
                Node w = rightOf(xParent);
                if (isRed(w)) {
                    w.red = false;
                    xParent.red = true;
                    rotateLeft(xParent);
                    w = rightOf(xParent);
                }
                if (!isRed(leftOf(w)) && !isRed(rightOf(w))) {
                    if (w != null) {
                        w.red = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                    xIsLeft = (xParent != null && x == xParent.left);
                } else {
                    if (!isRed(rightOf(w))) {
                        if (leftOf(w) != null) {
                            leftOf(w).red = false;
                        }
                        if (w != null) {
                            w.red = true;
                        }
                        rotateRight(w);
                        w = rightOf(xParent);
                    }
                    if (w != null) {
                        w.red = xParent.red;
                    }
                    xParent.red = false;
                    if (rightOf(w) != null) {
                        rightOf(w).red = false;
                    }
                    rotateLeft(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                Node w = leftOf(xParent);
                if (isRed(w)) {
                    w.red = false;
                    xParent.red = true;
                    rotateRight(xParent);
                    w = leftOf(xParent);
                }
                if (!isRed(leftOf(w)) && !isRed(rightOf(w))) {
                    if (w != null) {
                        w.red = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                    xIsLeft = (xParent != null && x == xParent.left);
                } else {
                    if (!isRed(leftOf(w))) {
                        if (rightOf(w) != null) {
                            rightOf(w).red = false;
                        }
                        if (w != null) {
                            w.red = true;
                        }
                        rotateLeft(w);
                        w = leftOf(xParent);
                    }
                    if (w != null) {
                        w.red = xParent.red;
                    }
                    xParent.red = false;
                    if (leftOf(w) != null) {
                        leftOf(w).red = false;
                    }
                    rotateRight(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) {
            x.red = false;
        }
        if (root != null) {
            root.red = false;
        }
    }

    private void deleteNode(Node n) {
        Node x, xParent, y = n;
        boolean yOrigRed = y.red;
        boolean xIsLeft = false;

        if (n.left == null) {
            x = n.right;
            xParent = n.parent;
            if (xParent != null) {
                xIsLeft = (n == xParent.left);
            }
            transplant(n, n.right);
        } else if (n.right == null) {
            x = n.left;
            xParent = n.parent;
            if (xParent != null) {
                xIsLeft = (n == xParent.left);
            }
            transplant(n, n.left);
        } else {
            y = min(n.right);
            yOrigRed = y.red;
            x = y.right;
            if (y.parent == n) {
                xParent = y;
                xIsLeft = false;
                if (x != null) {
                    x.parent = y;
                }
            } else {
                xParent = y.parent;
                xIsLeft = (y == y.parent.left);
                transplant(y, y.right);
                y.right = n.right;
                y.right.parent = y;
            }
            transplant(n, y);
            y.left = n.left;
            y.left.parent = y;
            y.red = n.red;
        }
        if (!yOrigRed) {
            fixAfterDelete(x, xParent, xIsLeft);
        }
    }

    public String remove(Integer key) {
        Node n = findNode(key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        deleteNode(n);
        size--;
        return old;
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        return (key instanceof Integer) && containsKey((Integer) key);
    }

    public String get(Object key) {
        return (key instanceof Integer) ? get((Integer) key) : null;
    }

    public String get(Integer key) {
        Node n = findNode(key);
        return n == null ? null : n.value;
    }

    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return min(root).key;
    }

    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return max(root).key;
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

    private void collectHead(Node n, Integer toKey, MyRbMap out) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(toKey) < 0) {
            collectHead(n.left, toKey, out);
            out.put(n.key, n.value);
            collectHead(n.right, toKey, out);
        } else {
            collectHead(n.left, toKey, out);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);
        return result;
    }

    private void collectTail(Node n, Integer fromKey, MyRbMap out) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(fromKey) >= 0) {
            collectTail(n.left, fromKey, out);
            out.put(n.key, n.value);
            collectTail(n.right, fromKey, out);
        } else {
            collectTail(n.right, fromKey, out);
        }
    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);
        return result;
    }

    private void buildString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) {
            return;
        }
        buildString(n.left, sb, first);
        if (!first[0]) {
            sb.append(", ");
        }
        first[0] = false;
        sb.append(n.key).append("=").append(n.value);
        buildString(n.right, sb, first);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }
}
