package by.it.group551002.yorsh.lesson12; // Поменяй пакет на свой

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    // Вспомогательные проверки цвета
    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
    }

    private Node getNode(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size++;
            return null;
        }

        Node curr = root;
        Node parent = null;
        int cmp = 0;

        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }

        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;

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
        Node node = getNode((Integer) key);
        if (node == null) return null;

        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }

        Node replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) root = replacement;
            else if (node == node.parent.left) node.parent.left = replacement;
            else node.parent.right = replacement;

            if (node.color == BLACK) fixAfterDeletion(replacement);
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == BLACK) fixAfterDeletion(node);
            if (node.parent != null) {
                if (node == node.parent.left) node.parent.left = null;
                else if (node == node.parent.right) node.parent.right = null;
                node.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && !isRed(x)) {
            if (x == x.parent.left) {
                Node sib = x.parent.right;
                if (isRed(sib)) {
                    sib.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    sib = x.parent.right;
                }
                if (!isRed(sib.left) && !isRed(sib.right)) {
                    sib.color = RED;
                    x = x.parent;
                } else {
                    if (!isRed(sib.right)) {
                        if (sib.left != null) sib.left.color = BLACK;
                        sib.color = RED;
                        rotateRight(sib);
                        sib = x.parent.right;
                    }
                    sib.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (sib.right != null) sib.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node sib = x.parent.left;
                if (isRed(sib)) {
                    sib.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    sib = x.parent.left;
                }
                if (!isRed(sib.right) && !isRed(sib.left)) {
                    sib.color = RED;
                    x = x.parent;
                } else {
                    if (!isRed(sib.left)) {
                        if (sib.right != null) sib.right.color = BLACK;
                        sib.color = RED;
                        rotateLeft(sib);
                        sib = x.parent.left;
                    }
                    sib.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (sib.left != null) sib.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            fillHeadMap(node.left, toKey, subMap);
            fillHeadMap(node.right, toKey, subMap);
        } else {
            fillHeadMap(node.left, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            fillTailMap(node.left, fromKey, subMap);
            fillTailMap(node.right, fromKey, subMap);
        } else {
            fillTailMap(node.right, fromKey, subMap);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Необязательные методы интерфейса SortedMap          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}