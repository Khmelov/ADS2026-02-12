package by.it.group551002.yorsh.lesson12; // измените имя пакета на нужное вам

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний узел АВЛ-дерева
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    // Вспомогательные методы для работы с высотой и балансом
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // Малый правый поворот
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Малый левый поворот
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Балансировка узла
    private Node balance(Node node) {
        updateHeight(node);
        int balance = getBalance(node);

        // Левый перевес
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left); // Большой правый поворот
            }
            return rotateRight(node);
        }

        // Правый перевес
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right); // Большой левый поворот
            }
            return rotateLeft(node);
        }

        return node;
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
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node current = root;
        Integer k = (Integer) key;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        String[] oldValue = new String[1];
        root = remove(root, (Integer) key, oldValue);
        if (oldValue[0] != null) {
            size--;
        }
        return oldValue[0];
    }

    private Node remove(Node node, Integer key, String[] oldValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, oldValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, oldValue);
        } else {
            oldValue[0] = node.value;
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node minNode = getMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = remove(node.right, minNode.key, new String[1]);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2); // Удаляем последнюю запятую с пробелом
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
    //////        Необязательные методы интерфейса Map                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public void putAll(Map<? extends Integer, ? extends String> m) { }
    @Override public boolean containsValue(Object value) { return false; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}