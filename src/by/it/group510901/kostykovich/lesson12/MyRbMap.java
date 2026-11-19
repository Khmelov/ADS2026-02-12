package by.it.group510901.kostykovich.lesson12;

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
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = RED;
        }
    }

    private Node root;
    private int currentSize;

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        String res = inOrderToString(root);
        return "{" + res.substring(0, res.length() - 2) + "}";
    }

    private String inOrderToString(Node node) {
        if (node == null) {
            return "";
        }
        return inOrderToString(node.left)
                + node.key + "=" + node.value + ", "
                + inOrderToString(node.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        int k = key;
        Node t = root;
        if (t == null) {
            root = new Node(k, value, null);
            root.color = BLACK;
            currentSize = 1;
            return null;
        }

        Node parent;
        int cmp;
        do {
            parent = t;
            cmp = Integer.compare(k, t.key);
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

        Node newNode = new Node(k, value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        fixAfterInsertion(newNode);
        currentSize++;
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

    public String remove(Integer key) {
        return remove((Object) key);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node p = getNode((Integer) key);
        return (p == null) ? null : p.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return getNode((Integer) key) != null;
    }

    public boolean containsKey(Integer key) {
        return containsKey((Object) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInSubtree(root, value);
    }

    public boolean containsValue(String value) {
        return containsValue((Object) value);
    }

    private boolean containsValueInSubtree(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueInSubtree(node.left, value) || containsValueInSubtree(node.right, value);
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, int toKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key < toKey) {
            fillHeadMap(node.left, toKey, subMap);
            subMap.put(node.key, node.value);
            fillHeadMap(node.right, toKey, subMap);
        } else {
            fillHeadMap(node.left, toKey, subMap);
        }
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

    private void fillTailMap(Node node, int fromKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            fillTailMap(node.left, fromKey, subMap);
            subMap.put(node.key, node.value);
            fillTailMap(node.right, fromKey, subMap);
        } else {
            fillTailMap(node.right, fromKey, subMap);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node p = root;
        while (p.left != null) {
            p = p.left;
        }
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node p = root;
        while (p.right != null) {
            p = p.right;
        }
        return p.key;
    }

    private Node getNode(int key) {
        Node p = root;
        while (p != null) {
            if (key < p.key) {
                p = p.left;
            } else if (key > p.key) {
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    private boolean colorOf(Node p) {
        return (p == null) ? BLACK : p.color;
    }

    private Node parentOf(Node p) {
        return (p == null) ? null : p.parent;
    }

    private void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private Node leftOf(Node p) {
        return (p == null) ? null : p.left;
    }

    private Node rightOf(Node p) {
        return (p == null) ? null : p.right;
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
        root.color = BLACK;
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
    private void deleteNode(Node p) {
        currentSize--;
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
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Collection values() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
