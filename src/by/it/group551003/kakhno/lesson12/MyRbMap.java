package by.it.group551003.kakhno.lesson12;


import java.util.*;

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

    private final Node NIL = new Node(null, null, BLACK);
    private Node root = NIL;
    private int size = 0;

    public MyRbMap() {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }

    private Node findNode(Integer key) {
        Node current = root;

        while (current != NIL) {
            int comparison = key.compareTo(current.key);

            if (comparison == 0) {
                return current;
            } else if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return NIL;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node parent = NIL;
        Node current = root;

        while (current != NIL) {
            parent = current;
            int comparison = key.compareTo(current.key);

            if (comparison == 0) {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            } else if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node node = new Node(key, value, RED);
        node.left = NIL;
        node.right = NIL;
        node.parent = parent;

        if (parent == NIL) {
            root = node;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size++;
        fixInsert(node);

        return null;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != NIL) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == NIL) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != NIL) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        if (node.parent == NIL) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    private void fixInsert(Node node) {
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
        root.parent = NIL;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        if (node == NIL) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode((Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueNode(root, value);
    }

    private boolean containsValueNode(Node node, Object value) {
        if (node == NIL) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValueNode(node.left, value)
                || containsValueNode(node.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        if (node == NIL) {
            return null;
        }

        String oldValue = node.value;
        Node removed = node;
        boolean originalColor = removed.color;
        Node replacement;

        if (node.left == NIL) {
            replacement = node.right;
            transplant(node, node.right);
        } else if (node.right == NIL) {
            replacement = node.left;
            transplant(node, node.left);
        } else {
            removed = minimum(node.right);
            originalColor = removed.color;
            replacement = removed.right;

            if (removed.parent == node) {
                replacement.parent = removed;
            } else {
                transplant(removed, removed.right);
                removed.right = node.right;
                removed.right.parent = removed;
            }

            transplant(node, removed);
            removed.left = node.left;
            removed.left.parent = removed;
            removed.color = node.color;
        }

        size--;

        if (originalColor == BLACK) {
            fixDelete(replacement);
        }

        return oldValue;
    }

    private void transplant(Node oldNode, Node newNode) {
        if (oldNode.parent == NIL) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }

        newNode.parent = oldNode.parent;
    }

    private Node minimum(Node node) {
        while (node.left != NIL) {
            node = node.left;
        }

        return node;
    }

    private Node maximum(Node node) {
        while (node.right != NIL) {
            node = node.right;
        }

        return node;
    }

    private void fixDelete(Node node) {
        while (node != root && node.color == BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;

                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }

                if (sibling.left.color == BLACK
                        && sibling.right.color == BLACK) {
                    sibling.color = RED;
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

                if (sibling.right.color == BLACK
                        && sibling.left.color == BLACK) {
                    sibling.color = RED;
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
        root = NIL;
        size = 0;
        NIL.parent = NIL;
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) {
            throw new NoSuchElementException();
        }

        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) {
            throw new NoSuchElementException();
        }

        return maximum(root).key;
    }

    @Override
    public MyRbMap headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyHead(root, toKey, result);

        return result;
    }

    private void copyHead(Node node, Integer toKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            copyHead(node.left, toKey, result);
            result.put(node.key, node.value);
            copyHead(node.right, toKey, result);
        } else {
            copyHead(node.left, toKey, result);
        }
    }

    @Override
    public MyRbMap tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyTail(root, fromKey, result);

        return result;
    }

    private void copyTail(Node node, Integer fromKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            copyTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            copyTail(node.right, fromKey, result);
        } else {
            copyTail(node.right, fromKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(
            Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }

        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException();
        }

        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, toKey, result);

        return result;
    }

    private void copyRange(
            Node node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            copyRange(node.left, fromKey, toKey, result);
        }

        if (node.key.compareTo(fromKey) >= 0
                && node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }

        if (node.key.compareTo(toKey) < 0) {
            copyRange(node.right, fromKey, toKey, result);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result);
        result.append("}");

        return result.toString();
    }

    private void appendInOrder(Node node, StringBuilder result) {
        if (node == NIL) {
            return;
        }

        appendInOrder(node.left, result);

        if (result.length() > 1) {
            result.append(", ");
        }

        result.append(node.key).append("=").append(node.value);

        appendInOrder(node.right, result);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry
                : map.entrySet()) {
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

