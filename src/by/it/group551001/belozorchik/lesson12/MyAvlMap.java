package by.it.group551001.belozorchik.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;    //высота нового узла всегда = 1
        }
    }

    private Node root;
    private int size;

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

    //сравниваем искомый ключ с ключом текущ узла
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node current = root;

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
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get((Integer) key) != null;
    }

    //вспомогательные методы для авл

    //высота листа = 1
    //высота пустого узла = 0
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    //вычисление bf
    //> 1, дерево перевешено вправо
    //< -1 - влево
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.right) - height(n.left);
    }

    //пересчет высоты текущ узла после доб/удал/поворота
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    //балансировка
    //перевес влево
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    //перевес вправо
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    //решаем, какой поворот примен
    private Node balance(Node node) {
        int balanceFactor = getBalance(node);

        //левый перевес
        if (balanceFactor < -2) {
            if (getBalance(node.left) > 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }

        //правый перевес
        if (balanceFactor > 2) {
            if (getBalance(node.right) < 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        //вспомог м для отслеживания старого знач при обновлении
        String[] oldValue = new String[1];
        root = putRecursive(root, key, value, oldValue);
        return oldValue[0];
    }

    private Node putRecursive(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node; //ключ сущ, высота не менялась
        }

        updateHeight(node);
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String[] oldValue = new String[1];
        root = removeRecursive(root, (Integer) key, oldValue);
        return oldValue[0];
    }

    private Node removeRecursive(Node node, Integer key, String[] oldValue) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key, oldValue);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key, oldValue);
        } else {
            oldValue[0] = node.value;
            size--;

            //случай 1: 0 или 1 потомок
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                //случай 2: 2 потомка - ищем мин в правом поддереве
                Node temp = getMin(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = removeRecursive(node.right, temp.key, new String[1]);
                size++;
            }
        }

        if (node == null) return null;

        updateHeight(node);
        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;

        //обходим левое поддерево
        inOrderTraversal(node.left, sb);
        //обработка текущ узла
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        //обходим правое поддерево
        inOrderTraversal(node.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Нереализованные методы интерфейса Map
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
