package by.it.group510901.jalilova.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height = 1;        // высота поддерева с корнем в этом узле
        Node left, right; //ссылки на другие Node

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right)); //1 (сам узел) + максимум из высот детей
    }

    private int balance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }


    private Node rotateRight(Node y) {
        Node x = y.left; //x новый корень
        Node B = x.right;
        x.right = y;
        y.left = B;//y берет левого ребенка x
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node B = y.left;
        y.left = x;
        x.right = B;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int b = balance(n);

        // LL
        if (b > 1 && balance(n.left) >= 0)
            return rotateRight(n);
        // LR дерево перевешивает в левую сторону, но излом уходит вправо
        if (b > 1 && balance(n.left) < 0) {
            n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        // RR
        if (b < -1 && balance(n.right) <= 0)
            return rotateLeft(n);
        // RL
        if (b < -1 && balance(n.right) > 0) {
            n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = new String[1];      // чтобы вернуть старое значение
        root = put(root, key, value, old);
        return old[0];
    }

    private Node put(Node n, int key, String value, String[] old) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = put(n.left, key, value, old);
        } else if (key > n.key) {
            n.right = put(n.right, key, value, old);
        } else {
            old[0] = n.value;              // ключ уже есть — меняем значение
            n.value = value;
            return n;
        }
        return rebalance(n);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node cur = root;
        while (cur != null) {
            if (k < cur.key) cur = cur.left;
            else if (k > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String[] removed = new String[1];
        root = remove(root, (Integer) key, removed);
        return removed[0];
    }

    private Node remove(Node n, int key, String[] removed) {
        if (n == null) return null;

        if (key < n.key) {
            n.left = remove(n.left, key, removed);
        } else if (key > n.key) {
            n.right = remove(n.right, key, removed);
        } else {
            // нашли узел
            removed[0] = n.value;
            size--;

            if (n.left == null) return n.right;
            if (n.right == null) return n.left;

            // два ребёнка — берём минимум из правого поддерева
            Node min = minNode(n.right);
            n.key = min.key;
            n.value = min.value;
            // удаляем этот минимум из правого поддерева
            String[] dummy = new String[1];
            n.right = remove(n.right, min.key, dummy);
            // size-- уже был сделан выше; при удалении минимума
            // счётчик не должен уменьшиться ещё раз
            size++;   // компенсируем
        }
        return rebalance(n);
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убираем последнее ", "
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inorder(n.right, sb);
    }


    //Заглушки

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}
