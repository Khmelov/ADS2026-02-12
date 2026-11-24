package by.it.group551003.parkhaniuk.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    public static class Node {
        Integer key;
        String val;

        Node left, right;
        int height;

        Node(Integer key, String val) {
            this.key = key;
            this.val = val;
            this.height = 1;
        }
    }
    private int size;
    private Node head;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            int leftHeight = height(node.left);
            int rightHeight = height(node.right);
            node.height = 1 + (leftHeight > rightHeight ? leftHeight : rightHeight);
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        updateHeight(node);
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }


    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(head, sb);
        sb.setLength(sb.length() - 2); // Отрезаем последнюю ", "
        return sb.append("}").toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.val).append(", ");
        inOrderTraversal(node.right, sb);
    }

    @Override
    public String put(Integer key, String val) {
        if (key == null) return null;

        String oldValue = get(key);
        head = put(head, key, val);
        if (oldValue == null) {
            size++;
        }

        return oldValue;
    }

    private Node put(Node node, Integer key, String val) {

        if (node == null) {
            return new Node(key, val);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, key, val);
        } else if (cmp > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String oldValue = get((Integer) key);
        if (oldValue == null) {
            return null;
        }
        head = remove(head, (Integer) key);
        size--;

        return oldValue;
    }

    private Node remove(Node node, Integer key) {

        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = remove(node.left, key);
        }
        else if (cmp > 0) {
            node.right = remove(node.right, key);
        }
        else {

            if (node.left == null) {
                return node.right;
            }

            if (node.right == null) {
                return node.left;
            }

            Node min = findMin(node.right);

            node.key = min.key;
            node.val = min.val;

            node.right = remove(node.right, min.key);
        }

        return balance(node);
    }

    private Node findMin(Node node) {

        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    @Override
    public String get(Object key) {
        Node cur = head;
        if(head == null)
            return null;

        while(cur != null && ((Integer)key).compareTo(cur.key) != 0) {
            if (((Integer)key).compareTo(cur.key) > 0) {
                cur = cur.right;
            }
            else
                cur = cur.left;
        }
        if (cur != null)
            return cur.val;

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get((Integer)key) != null;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
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

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

}
