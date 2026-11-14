package by.it.group551004.pedko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap<Integer, String> implements SortedMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.red = true;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }

        return node.red;
    }

    private int compare(Object a, Object b) {
        java.lang.Integer x = (java.lang.Integer) a;
        java.lang.Integer y = (java.lang.Integer) b;
        return x.compareTo(y);
    }

    private boolean isLeftChild(Node node) {
        if (node.parent == null) {
            return false;
        }

        return node == node.parent.left;
    }

    private Node find(Object key) {
        Node node = root;

        while (node != null) {
            int c = compare(key, node.key);

            if (c == 0) {
                return node;
            }

            if (c < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
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

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;

        if (y.right != null) {
            y.right.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    private void fixInsert(Node node) {
        while (node.parent != null && node.parent.red) {
            Node parent = node.parent;
            Node grand = parent.parent;

            if (parent == grand.left) {
                Node uncle = grand.right;

                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;

                    node = grand;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.parent;
                    }
                    parent.red = false;
                    grand.red = true;

                    rotateRight(grand);
                }
            } else {
                Node uncle = grand.left;

                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;

                    node = grand;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                        parent = node.parent;
                    }
                    parent.red = false;
                    grand.red = true;

                    rotateLeft(grand);
                }
            }
        }

        root.red = false;
    }

    private void insert(Integer key, String value) {
        Node parent = null;
        Node current = root;

        while (current != null) {


            parent = current;
            int c = compare(key, current.key);

            if (c < 0) {
                current = current.left;
            } else {
                current = current.right;
            }

        }

        Node node = new Node(key, value, parent);

        if (parent == null) {
            root = node;
        } else if (compare(key, parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size = size + 1;

        fixInsert(node);
    }

    private void transplant(Node node, Node replacement) {
        if (node.parent == null) {
            root = replacement;
        } else if (node == node.parent.left) {
            node.parent.left = replacement;
        } else {
            node.parent.right = replacement;
        }

        if (replacement != null) {
            replacement.parent = node.parent;
        }
    }

    private void removeNode(Object key) {
        Node node = find(key);

        if (node == null) {
            return;
        }

        Node moved;
        Node child;
        Node childParent;

        boolean childIsLeft;
        boolean movedRed;

        if (node.left == null) {
            child = node.right;
            childParent = node.parent;

            childIsLeft = isLeftChild(node);
            movedRed = node.red;
            transplant(node, node.right);

        } else if (node.right == null) {
            child = node.left;
            childParent = node.parent;

            childIsLeft = isLeftChild(node);
            movedRed = node.red;
            transplant(node, node.left);
        } else {
            moved = minimum(node.right);
            movedRed = moved.red;
            child = moved.right;

            if (moved.parent == node) {
                childParent = moved;
                childIsLeft = false;
            } else {
                childParent = moved.parent;
                childIsLeft = true;
                transplant(moved, moved.right);
                moved.right = node.right;
                moved.right.parent = moved;
            }

            transplant(node, moved);
            moved.left = node.left;
            moved.left.parent = moved;
            moved.red = node.red;
        }

        size--;

        if (!movedRed) {
            fixDelete(child, childParent, childIsLeft);
        }
    }

    private void fixDelete(Node node, Node parent, boolean isLeft) {
        while (node != root && !isRed(node)) {
            if (parent == null) {
                break;
            }

            if (isLeft) {
                Node sibling = parent.right;

                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    sibling = parent.right;
                }

                if (!isRed(sibling.left) && !isRed(sibling.right)) {
                    sibling.red = true;
                    node = parent;
                    parent = node.parent;
                    isLeft = isLeftChild(node);
                } else {
                    if (!isRed(sibling.right)) {
                        sibling.left.red = false;
                        sibling.red = true;
                        rotateRight(sibling);
                        sibling = parent.right;
                    }
                    sibling.red = parent.red;
                    parent.red = false;
                    sibling.right.red = false;
                    rotateLeft(parent);
                    node = root;
                    parent = null;
                }
            } else {
                Node sibling = parent.left;

                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    sibling = parent.left;
                }

                if (!isRed(sibling.left) && !isRed(sibling.right)) {
                    sibling.red = true;
                    node = parent;
                    parent = node.parent;
                    isLeft = isLeftChild(node);
                } else {
                    if (!isRed(sibling.left)) {
                        sibling.right.red = false;
                        sibling.red = true;
                        rotateLeft(sibling);
                        sibling = parent.left;
                    }
                    sibling.red = parent.red;
                    parent.red = false;
                    sibling.left.red = false;
                    rotateRight(parent);
                    node = root;
                    parent = null;
                }
            }
        }

        if (node != null) {
            node.red = false;
        }
    }

    private java.lang.String buildString(Node node) {
        if (node == null) {
            return "";
        }

        java.lang.String left = buildString(node.left);
        java.lang.String current = node.key + "=" + node.value;
        java.lang.String right = buildString(node.right);

        if (left.length() == 0) {
            if (right.length() == 0) {
                return current;
            }
            return current + ", " + right;
        }

        if (right.length() == 0) {
            return left + ", " + current;
        }

        return left + ", " + current + ", " + right;
    }

    private boolean hasValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        if (hasValue(node.left, value)) {
            return true;
        }
        return hasValue(node.right, value);
    }

    private void addKeys(MyRbMap<Integer, String> target, Node node, Object bound, boolean head) {
        if (node == null) {
            return;
        }

        addKeys(target, node.left, bound, head);

        int c = compare(node.key, bound);

        if (head) {
            if (c < 0) {
                target.put(node.key, node.value);
            }
        } else {
            if (c >= 0) {
                target.put(node.key, node.value);
            }
        }

        addKeys(target, node.right, bound, head);
    }

    @Override
    public java.lang.String toString() {
        return "{" + buildString(root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        Node node = find(key);

        if (node != null) {
            String old = node.value;
            node.value = value;
            return old;
        }

        insert(key, value);
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        String old = node.value;
        removeNode(key);
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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
        if (root == null) {
            return null;
        }
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }

        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap<Integer, String> result = new MyRbMap<>();
        addKeys(result, root, toKey, true);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap<Integer, String> result = new MyRbMap<>();
        addKeys(result, root, fromKey, false);
        return result;
    }

    // не обязательные

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Set<Integer> keySet() { return null; }

    @Override
    public Collection<String> values() { return null; }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}