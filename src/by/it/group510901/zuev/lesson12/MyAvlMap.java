package by.it.group510901.zuev.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс узла АВЛ-дерева
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
    private int size;

    // Вспомогательные переменные для возврата предыдущего значения
    private String prevValue;
    private boolean isNewKey;
    private String removedValue;
    private boolean isRemoved;

    // -------------------------------------------------------------
    //                     Методы АВЛ-дерева
    // -------------------------------------------------------------

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            int hl = height(node.left);
            int hr = height(node.right);
            node.height = 1 + (hl > hr ? hl : hr);
        }
    }

    // Малый правый поворот
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Малый левый поворот
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Балансировка узла
    private Node balance(Node node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balanceFactor = getBalance(node);

        // Левое поддерево перевешивает (Left-Left или Left-Right)
        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        // Правое поддерево перевешивает (Right-Right или Right-Left)
        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    // Вставка ключа и значения
    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            isNewKey = true;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Ключ уже существует: обновляем значение и сохраняем старое
            prevValue = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    // Поиск минимального узла в поддереве
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Удаление минимального узла с балансировкой
    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    // Рекурсивное удаление узла по ключу
    private Node remove(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            isRemoved = true;
            removedValue = node.value;

            if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            } else {
                Node minRight = findMin(node.right);
                node.key = minRight.key;
                node.value = minRight.value;
                node.right = removeMin(node.right);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    // -------------------------------------------------------------
    //              Обязательные к реализации методы
    // -------------------------------------------------------------

    @Override
    public String put(Integer key, String value) {
        prevValue = null;
        isNewKey = false;
        root = insert(root, key, value);
        if (isNewKey) {
            size++;
        }
        return prevValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        removedValue = null;
        isRemoved = false;
        root = remove(root, key);
        if (isRemoved) {
            size--;
        }
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get((Integer) key);
    }

    public String get(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return containsKey((Integer) key);
    }

    public boolean containsKey(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return true;
            }
        }
        return false;
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
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход (in-order) гарантирует возрастание ключей
    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrder(node.right, sb);
    }

    // -------------------------------------------------------------
    //              Остальные методы интерфейса Map
    // -------------------------------------------------------------

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}