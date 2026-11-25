package by.it.group510902.Tarnogurskaya.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
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
    public boolean containsKey(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return true;
        }
        return false;
    }

    @Override
    public String get(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = put(root, key, value);
        return oldValue;
    } //добавляет новую пару, возвращает старое значение, если такой ключ уже есть

    private String oldValue;

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else {
            oldValue = node.value;
            node.value = value;
            return node;
        }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        removedValue = null; //значение удаляемого элемента
        removedFlag = false; //действительно ли элемент был удалён
        root = remove(root, (Integer) key);
        if (removedFlag) size--;
        return removedValue;
    }

    private String removedValue;
    private boolean removedFlag;

    private Node remove(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key);
        else if (cmp > 0) node.right = remove(node.right, key);
        else {
            removedFlag = true;
            removedValue = node.value;
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
                //если хотя бы одного потомка нет, заменяем удаляемый оставшимся потомком
            }
            else {
                Node min = node.right; //ищем минимальный элемент в правом поддереве
                while (min.left != null) min = min.left;
                node.key = min.key;
                node.value = min.value; //копируем ключ и значение найденного узла в удаляемый узел
                node.right = removeMin(node.right); //удаляем первоначальный узел из правого поддерева
            }
        }
        if (node == null) return null;
        return balance(node);
    }

    private Node removeMin(Node node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left); //спускаемся влево до конца и возвращаем правого на место удаляемого
        return balance(node);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append('=').append(node.value).append(", ");
        inOrder(node.right, sb);
        //симметричный обход левое поддерево - текущий узел - правое поддерево
    }

    private int height(Node n) { return n == null ? 0 : n.height; }

    private void updateHeight(Node n) {
        int l = height(n.left), r = height(n.right);
        n.height = 1 + (l > r ? l : r); //высота узла = 1 + максимальная высота его поддеревьев
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        //нарушение баланса слева
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
            //если bf левого потомка отрицательный - левый поворот этого потомка, потом правый поворот текущего узла
        }
        //нарушение баланса справа
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
            //если bf правого потомка положительный - правый поворот этого потомка, потом левый поворот текущего узла
        }
        //дерево сбалансировано
        return n;
    }
    //левый ребёнок поднимается, родитель опускается вправо
    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }
    //правый ребёнок поднимается, родитель опускается влево
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}