package by.it.group551001.docenko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

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

    private Node root;
    private int size;

    private static Integer toKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (Integer) key;
    }

    private static boolean colorOf(Node node) {
        return node == null ? BLACK : node.color;
    }

    private static Node parentOf(Node node) {
        return node == null ? null : node.parent;
    }

    private static Node leftOf(Node node) {
        return node == null ? null : node.left;
    }

    private static Node rightOf(Node node) {
        return node == null ? null : node.right;
    }

    private static void setColor(Node node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != null) {
            pivot.left.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == null) {
            root = pivot;
        } else if (node == node.parent.left) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        pivot.left = node;
        node.parent = pivot;
    }

    private void rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != null) {
            pivot.right.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == null) {
            root = pivot;
        } else if (node == node.parent.right) {
            node.parent.right = pivot;
        } else {
            node.parent.left = pivot;
        }
        pivot.right = node;
        node.parent = pivot;
    }

    private Node findNode(Integer key) {
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

    private void fixAfterInsert(Node x) {
        while (x != root && colorOf(parentOf(x)) == RED) {
            Node parent = parentOf(x);
            Node grand = parentOf(parent);
            if (parent == leftOf(grand)) {
                Node uncle = rightOf(grand);
                if (colorOf(uncle) == RED) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grand, RED);
                    x = grand;
                } else {
                    if (x == rightOf(parent)) {
                        x = parent;
                        rotateLeft(x);
                        parent = parentOf(x);
                    }
                    setColor(parent, BLACK);
                    setColor(grand, RED);
                    rotateRight(grand);
                }
            } else {
                Node uncle = leftOf(grand);
                if (colorOf(uncle) == RED) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grand, RED);
                    x = grand;
                } else {
                    if (x == leftOf(parent)) {
                        x = parent;
                        rotateRight(x);
                        parent = parentOf(x);
                    }
                    setColor(parent, BLACK);
                    setColor(grand, RED);
                    rotateLeft(grand);
                }
            }
        }
        root.color = BLACK;
    }

    private void fixAfterDelete(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sibling = rightOf(parentOf(x));
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sibling = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sibling)) == BLACK && colorOf(rightOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sibling)) == BLACK) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(x));
                    }
                    setColor(sibling, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sibling = leftOf(parentOf(x));
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sibling = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sibling)) == BLACK && colorOf(leftOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sibling)) == BLACK) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(x));
                    }
                    setColor(sibling, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }
        Node replacement = node.left != null ? node.left : node.right;
        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            node.left = node.right = node.parent = null;
            if (node.color == BLACK) {
                fixAfterDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == BLACK) {
                fixAfterDelete(node);
            }
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
    }

    private static void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendInOrder(node.right, sb);
    }

    private static boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private static boolean matches(Node node, Map<?, ?> other) {
        if (node == null) {
            return true;
        }
        Object otherValue = other.get(node.key);
        boolean same = node.value == null
                ? otherValue == null && other.containsKey(node.key)
                : node.value.equals(otherValue);
        return same && matches(node.left, other) && matches(node.right, other);
    }

    private static int hash(Node node) {
        if (node == null) {
            return 0;
        }
        int h = node.key.hashCode() ^ (node.value == null ? 0 : node.value.hashCode());
        return h + hash(node.left) + hash(node.right);
    }

    private static void copyRange(Node node, MyRbMap target, Integer from, Integer to) {
        if (node == null) {
            return;
        }
        boolean aboveFrom = from == null || node.key.compareTo(from) >= 0;
        boolean belowTo = to == null || node.key.compareTo(to) < 0;
        if (aboveFrom) {
            copyRange(node.left, target, from, to);
        }
        if (aboveFrom && belowTo) {
            target.put(node.key, node.value);
        }
        if (belowTo) {
            copyRange(node.right, target, from, to);
        }
    }

    private MyRbMap range(Integer from, Integer to) {
        MyRbMap result = new MyRbMap();
        copyRange(root, result, from, to);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        toKey(key);
        if (root == null) {
            root = new Node(key, value, null);
            root.color = BLACK;
            size = 1;
            return null;
        }
        Node parent;
        Node current = root;
        int cmp;
        do {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String old = current.value;
                current.value = value;
                return old;
            }
        } while (current != null);
        Node node = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        fixAfterInsert(node);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode(toKey(key));
        if (node == null) {
            return null;
        }
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = findNode(toKey(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(toKey(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
        return range(null, toKey(toKey));
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(toKey(fromKey), null);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (toKey(fromKey).compareTo(toKey(toKey)) > 0) {
            throw new IllegalArgumentException();
        }
        return range(fromKey, toKey);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
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
        Map<?, ?> other = (Map<?, ?>) o;
        return other.size() == size && matches(root, other);
    }

    @Override
    public int hashCode() {
        return hash(root);
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
