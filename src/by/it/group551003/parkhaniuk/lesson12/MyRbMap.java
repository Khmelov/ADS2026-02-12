package by.it.group551003.parkhaniuk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public static class Node {
        Integer key;
        String val;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String val, boolean color, Node parent) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

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

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    @Override
    public String put(Integer key, String val) {
        if (key == null) return null;

        if (root == null) {
            root = new Node(key, val, BLACK, null);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.val;
                current.val = val;
                return oldValue;
            }
        }

        Node newNode = new Node(key, val, RED, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    private void fixAfterInsertion(Node x) {
        while (x != null && x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node uncle = x.parent.parent.right;
                if (isRed(uncle)) {
                    x.parent.color = BLACK;
                    uncle.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node uncle = x.parent.parent.left;
                if (isRed(uncle)) {
                    x.parent.color = BLACK;
                    uncle.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        Node p = getNode((Integer) key);
        if (p == null) return null;

        String oldValue = p.val;
        deleteNode(p);
        size--;
        return oldValue;
    }

    private Node getNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private void deleteNode(Node p) {
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.val = s.val;
            p = s;
        }

        Node replacement = (p.left != null) ? p.left : p.right;

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

    private Node successor(Node t) {
        if (t == null) return null;
        if (t.right != null) {
            Node p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        Node p = t.parent;
        Node ch = t;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
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
                    x = root;
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
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private boolean colorOf(Node p) {
        return (p == null ? BLACK : p.color);
    }

    private Node parentOf(Node p) {
        return (p == null ? null : p.parent);
    }

    private void setColor(Node p, boolean c) {
        if (p != null) p.color = c;
    }

    private Node leftOf(Node p) {
        return (p == null ? null : p.left);
    }

    private Node rightOf(Node p) {
        return (p == null ? null : p.right);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode((Integer) key);
        return node != null ? node.val : null;
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

        if ((value == null && node.val == null) || (value != null && value.equals(node.val))) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
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
    public Integer firstKey() {
        if (root == null) return null;
        Node p = root;
        while (p.left != null) {
            p = p.left;
        }
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node p = root;
        while (p.right != null) {
            p = p.right;
        }
        return p.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        buildHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void buildHeadMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) return;

        buildHeadMap(node.left, toKey, subMap);
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.val);
            buildHeadMap(node.right, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        buildTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void buildTailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            buildTailMap(node.left, fromKey, subMap);
            subMap.put(node.key, node.val);
        }
        buildTailMap(node.right, fromKey, subMap);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.val).append(", ");
        inOrderTraversal(node.right, sb);
    }









    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }
}