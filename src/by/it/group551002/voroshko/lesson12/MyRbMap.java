package by.it.group551002.voroshko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;

        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = RED;
        }
    };

    private Node root;
    private int size;

    private final Comparator<? super Integer> comparator;

    public MyRbMap() { this.comparator = null; }
    public MyRbMap(Comparator<? super Integer> comparator) { this.comparator = comparator; }

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

    private void fixInsert(Node c) {
        while (c.parent != null && c.parent.color == RED) {
            if (c.parent == c.parent.parent.left) {
                Node uncle = c.parent.parent.right;

                if (uncle != null && uncle.color == RED) {
                    c.parent.color = BLACK;
                    uncle.color = BLACK;
                    c.parent.parent.color = RED;
                    c = c.parent.parent;
                } else {
                    if (c == c.parent.right) {
                        c = c.parent;
                        rotateLeft(c);
                    }

                    c.parent.color = BLACK;
                    c.parent.parent.color = RED;
                    rotateRight(c.parent.parent);
                }
            } else {
                Node uncle = c.parent.parent.right;

                if (uncle != null && uncle.color == RED) {
                    c.parent.color = BLACK;
                    uncle.color = BLACK;
                    c.parent.parent.color = RED;
                    c = c.parent.parent;
                } else {
                    if (c == c.parent.left) {
                        c = c.parent;
                        rotateRight(c);
                    }

                    c.parent.color = BLACK;
                    c.parent.parent.color = RED;
                    rotateLeft(c.parent.parent);
                }
            }
        }
    }

    private void fixRemove(Node a, Node parent) {
        while (a != root && colorOf(a) == BLACK) {
            if (a == (parent == null ? null : parent.left)) {
                Node w = parent.right;

                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }

                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    w.color = RED;
                    a = parent;
                    parent = a.parent;
                } else {
                    if (colorOf(w.right) == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }

                    w.color = parent.color;
                    parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(parent);
                    a = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;

                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }

                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    w.color = RED;
                    a = parent;
                    parent = a.parent;
                } else {
                    if (colorOf(w.left) == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }

                    w.color = parent.color;
                    parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(parent);
                    a = root;
                    parent = null;
                }
            }
        }

        if (a != null) {
            a.color = BLACK;
        }
    }

    private boolean colorOf(Node n) {
        if (n == null) {
            return BLACK;
        } else {
            return n.color;
        }
    }

    private Node findNode(Integer key) {
        Node cur = root;

        while (cur != null) {
            int c = cmp(key, cur.key);
            if (c == 0) return cur;
            cur = c < 0 ? cur.left : cur.right;
        }
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
            root.color = BLACK;
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
                return old;
            }
            cur = c < 0 ? cur.left : cur.right;
        }

        Node a = new Node(key, value, parent);
        if (c < 0) {
            parent.left = a;
        } else {
            parent.right = a;
        }

        size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;

        Node c = findNode(key);
        if (c == null) return null;

        String old = c.value;
        Node a, aParent;
        boolean removedColor;

        if (c.left == null || c.right == null) {
            a = (c.left != null) ? c.left : c.right;
            aParent = c.parent;
            removedColor = c.color;
            replaceNode(c, a);
        } else {
            Node b = c.right;

            while (b.left != null) {
                b = b.left;
            }

            removedColor = b.color;
            a = b.right;

            if (b.parent == c) {
                aParent = b;
                if (a != null) a.parent = b;
            } else {
                aParent = b.parent;
                replaceNode(b, a);
                b.right = c.right;
                b.right.parent = b;
            }

            replaceNode(c, b);
            b.left = c.left;
            b.left.parent = b;
            b.color = c.color;
        }

        size--;

        if (removedColor == BLACK) {
            if (a != null && a.parent != null) aParent = a.parent;
            fixRemove(a, aParent);
        }

        return old;
    }

    private void replaceNode(Node a, Node b) {
        if (a.parent == null) {
            root = b;
        } else if (a == a.parent.left) {
            a.parent.left = b;
        } else {
            a.parent.right = b;
        }

        if (b != null) {
            b.parent = a.parent;
        }
    }

    @Override
    public String get(Object keyObj) {
        Node n = findNode((Integer) keyObj);

        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }

    @Override
    public boolean containsKey(Object keyObj) {
        return findNode((Integer) keyObj) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;

        if (value == null ? n.value == null : value.equals(n.value)) return true;

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
        MyRbMap m = new MyRbMap(comparator);
        addRange(m, root, toKey, true);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap m = new MyRbMap(comparator);
        addRange(m, root, fromKey, false);
        return m;
    }

    private void addRange(MyRbMap target, Node n, Integer bound, boolean head) {
        if (n == null) return;

        int c = cmp(n.key, bound);

        if (head) {
            if (c < 0) {
                addRange(target, n.left, bound, true);
                target.put(n.key, n.value);
                addRange(target, n.right, bound, true);
            } else {
                addRange(target, n.left, bound, true);
            }
        } else {
            if (c >= 0) {
                addRange(target, n.left, bound, false);
                target.put(n.key, n.value);
                addRange(target, n.right, bound, false);
            } else {
                addRange(target, n.right, bound, false);
            }
        }
    }

    @Override
    public Integer firstKey() {
        Node n = root;

        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = root;

        while (n.right != null) {
            n = n.right;
        }
        return n.key;
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
