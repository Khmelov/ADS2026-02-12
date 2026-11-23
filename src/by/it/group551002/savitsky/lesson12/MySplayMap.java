package by.it.group551002.savitsky.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private void rotateLeft(Node nodeToDown) {
        Node newRoot = nodeToDown.right;

        nodeToDown.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.parent = nodeToDown;
        }

        newRoot.parent = nodeToDown.parent;
        if (nodeToDown.parent == null) {
            root = newRoot;
        } else if (nodeToDown == nodeToDown.parent.left) {
            nodeToDown.parent.left = newRoot;
        } else {
            nodeToDown.parent.right = newRoot;
        }

        newRoot.left = nodeToDown;
        nodeToDown.parent = newRoot;
    }

    private void rotateRight(Node nodeToDown) {
        Node newRoot = nodeToDown.left;

        nodeToDown.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.parent = nodeToDown;
        }

        newRoot.parent = nodeToDown.parent;
        if (nodeToDown.parent == null) {
            root = newRoot;
        } else if (nodeToDown == nodeToDown.parent.right) {
            nodeToDown.parent.right = newRoot;
        } else {
            nodeToDown.parent.left = newRoot;
        }

        newRoot.right = nodeToDown;
        nodeToDown.parent = newRoot;
    }

    private void splay(Node node) {
        while (node.parent != null) {

            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }

            } else if (node == parent.left && parent == grandparent.left) {
                rotateRight(grandparent);
                rotateRight(parent);

            } else if (node == parent.right && parent == grandparent.right) {
                rotateLeft(grandparent);
                rotateLeft(parent);

            } else if (node == parent.right && parent == grandparent.left) {
                rotateLeft(parent);
                rotateRight(grandparent);

            } else {
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
    }

    private Node findLast(int key) {
        Node current = root;
        Node last = null;

        while (current != null) {
            last = current;

            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current;
            }
        }

        return last;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        int intKey = (Integer) key;

        Node last = findLast(intKey);
        if (last == null) {
            return false;
        }

        splay(last);
        return last.key == intKey;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null) {
            if (node.value == null) {
                return true;
            }
        } else {
            if (value.equals(node.value)) {
                return true;
            }
        }

        if (containsValue(node.left, value)) {
            return true;
        }

        return containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        int intKey = (Integer) key;

        Node last = findLast(intKey);
        if (last == null) {
            return null;
        }

        splay(last);

        return last.key == intKey ? last.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node last = findLast(key);
        splay(last);

        if (root.key == key) {
            String old = root.value;
            root.value = value;
            return old;
        }

        Node oldRoot = root;
        Node newNode = new Node(key, value, null);

        if (key < oldRoot.key) {
            newNode.right = oldRoot;
            newNode.left = oldRoot.left;
            if (oldRoot.left != null) {
                oldRoot.left.parent = newNode;
            }
            oldRoot.left = null;
        } else {
            newNode.left = oldRoot;
            newNode.right = oldRoot.right;
            if (oldRoot.right != null) {
                oldRoot.right.parent = newNode;
            }
            oldRoot.right = null;
        }

        oldRoot.parent = newNode;
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }

        int intKey = (Integer) key;

        Node last = findLast(intKey);
        splay(last);

        if (root.key != intKey) {
            return null;
        }

        String old = root.value;
        Node leftTree = root.left;
        Node rightTree = root.right;

        if (leftTree != null) {
            leftTree.parent = null;
        }
        if (rightTree != null) {
            rightTree.parent = null;
        }

        if (leftTree == null) {
            root = rightTree;
        } else {
            root = leftTree;

            Node maxNode = root;
            while (maxNode.right != null) {
                maxNode = maxNode.right;
            }

            splay(maxNode);

            root.right = rightTree;
            if (rightTree != null) {
                rightTree.parent = root;
            }
        }

        size--;

        return old;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node current = root;
        Node best = null;

        while (current != null) {
            if (current.key < key) {
                best = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (best == null) {
            return null;
        }

        return best.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node current = root;
        Node best = null;

        while (current != null) {
            if (current.key == key) {
                return current.key;
            }
            if (current.key < key) {
                best = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (best == null) {
            return null;
        }

        return best.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root;
        Node best = null;

        while (current != null) {
            if (current.key == key) {
                return current.key;
            }
            if (current.key > key) {
                best = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (best == null) {
            return null;
        }

        return best.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node current = root;
        Node best = null;

        while (current != null) {
            if (current.key > key) {
                best = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (best == null) {
            return null;
        }

        return best.key;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        while (current.left != null) {
            current = current.left;
        }

        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        while (current.right != null) {
            current = current.right;
        }

        return current.key;
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);

        return result;
    }

    private void collectHead(Node node, int toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        collectHead(node.left, toKey, result);
        if (node.key < toKey) {
            result.put(node.key, node.value);
            collectHead(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);

        return result;
    }

    private void collectTail(Node node, int fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        if (node.key >= fromKey) {
            collectTail(node.left, fromKey, result);
            result.put(node.key, node.value);
        }
        collectTail(node.right, fromKey, result);
    }

    @Override
    public String toString() {
        return "{" + inOrder(root) + "}";
    }

    private String inOrder(Node node) {
        if (node == null) {
            return "";
        }

        String left = inOrder(node.left);
        String right = inOrder(node.right);

        String result = node.key + "=" + node.value;
        if (!left.isEmpty()) {
            result = left + ", " + result;
        }
        if (!right.isEmpty()) {
            result = result + ", " + right;
        }

        return result;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
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
