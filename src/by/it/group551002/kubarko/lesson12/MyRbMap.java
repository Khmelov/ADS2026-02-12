package by.it.group551002.kubarko.lesson12;

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
        boolean color;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private final Node nil = new Node(null, null, BLACK);
    private Node root = nil;
    private int size;

    public MyRbMap() {
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;
    }

    private Node find(Integer key) {
        Node current = root;
        while (current != nil) {
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                return current;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return nil;
    }

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != nil) {
            pivot.left.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == nil) {
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
        if (pivot.right != nil) {
            pivot.right.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == nil) {
            root = pivot;
        } else if (node == node.parent.right) {
            node.parent.right = pivot;
        } else {
            node.parent.left = pivot;
        }
        pivot.right = node;
        node.parent = pivot;
    }

    private void fixAfterInsert(Node node) {
        while (node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private Node minimum(Node node) {
        while (node.left != nil) {
            node = node.left;
        }
        return node;
    }

    private void transplant(Node oldNode, Node newNode) {
        if (oldNode.parent == nil) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }
        newNode.parent = oldNode.parent;
    }

    private void fixAfterRemove(Node node) {
        while (node != root && node.color == BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if (sibling.left.color == BLACK && sibling.right.color == BLACK) {
                    if (sibling != nil) {
                        sibling.color = RED;
                    }
                    node = node.parent;
                } else {
                    if (sibling.right.color == BLACK) {
                        sibling.left.color = BLACK;
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    sibling.right.color = BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling.right.color == BLACK && sibling.left.color == BLACK) {
                    if (sibling != nil) {
                        sibling.color = RED;
                    }
                    node = node.parent;
                } else {
                    if (sibling.left.color == BLACK) {
                        sibling.right.color = BLACK;
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    sibling.left.color = BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.color = BLACK;
    }

    private void appendInOrder(Node node, StringBuilder result, boolean[] first) {
        if (node == nil) {
            return;
        }
        appendInOrder(node.left, result, first);
        if (!first[0]) {
            result.append(", ");
        }
        result.append(node.key).append("=").append(node.value);
        first[0] = false;
        appendInOrder(node.right, result, first);
    }

    private void copyRange(Node node, MyRbMap result, Integer bound, boolean head) {
        if (node == nil) {
            return;
        }
        copyRange(node.left, result, bound, head);
        if (head ? node.key < bound : node.key >= bound) {
            result.put(node.key, node.value);
        }
        copyRange(node.right, result, bound, head);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result, new boolean[]{true});
        return result.append("}").toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node parent = nil;
        Node current = root;
        while (current != nil) {
            parent = current;
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                String previous = current.value;
                current.value = value;
                return previous;
            }
            current = comparison < 0 ? current.left : current.right;
        }

        Node added = new Node(key, value, RED);
        added.left = nil;
        added.right = nil;
        added.parent = parent;
        if (parent == nil) {
            root = added;
        } else if (key < parent.key) {
            parent.left = added;
        } else {
            parent.right = added;
        }
        size++;
        fixAfterInsert(added);
        return null;
    }

    @Override
    public String remove(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node removed = find(integerKey);
        if (removed == nil) {
            return null;
        }

        String previous = removed.value;
        Node replacement = removed;
        boolean originalColor = replacement.color;
        Node child;
        if (removed.left == nil) {
            child = removed.right;
            transplant(removed, removed.right);
        } else if (removed.right == nil) {
            child = removed.left;
            transplant(removed, removed.left);
        } else {
            replacement = minimum(removed.right);
            originalColor = replacement.color;
            child = replacement.right;
            if (replacement.parent == removed) {
                child.parent = replacement;
            } else {
                transplant(replacement, replacement.right);
                replacement.right = removed.right;
                replacement.right.parent = replacement;
            }
            transplant(removed, replacement);
            replacement.left = removed.left;
            replacement.left.parent = replacement;
            replacement.color = removed.color;
        }
        size--;
        if (originalColor == BLACK) {
            fixAfterRemove(child);
        }
        nil.parent = nil;
        return previous;
    }

    @Override
    public String get(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node node = find(integerKey);
        return node == nil ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        return find(integerKey) != nil;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == nil) {
            return false;
        }
        return (value == null ? node.value == null : value.equals(node.value))
                || containsValue(node.left, value)
                || containsValue(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = nil;
        nil.parent = nil;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyRange(root, result, toKey, true);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyRange(root, result, fromKey, false);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == nil) {
            throw new NoSuchElementException();
        }
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == nil) {
            throw new NoSuchElementException();
        }
        Node current = root;
        while (current.right != nil) {
            current = current.right;
        }
        return current.key;
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
    public void putAll(Map<? extends Integer, ? extends String> map) {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
