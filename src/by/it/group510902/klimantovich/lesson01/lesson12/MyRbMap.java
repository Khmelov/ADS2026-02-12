package by.it.group510902.klimantovich.lesson01.lesson12;
import java.util.*;
public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private static class Node {
        Integer key; // ключ элемента
        String value; // строковое значение
        Node left, right, parent; // ссылки на потомков и родителя
        boolean color; // цвет узла (RED или BLACK)
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED; // новые узлы всегда добавляются красными
        }
    }
    private Node root; // корень дерева
    private int size = 0; // счетчик элементов в карте
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left; // перебрасываю левое поддерево y в правое x
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent; // перепривязываю родителя
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }
    private void rightRotate(Node x) {
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
    private void fixInsert(Node k) {
        Node u;
        while (k.parent != null && k.parent.color == RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // дядя узла k
                if (u != null && u.color == RED) {
                    u.color = BLACK; // случай 1: перекрашиваю родителя, дядю и дедушку
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k); // случай 2: правый поворот
                    }
                    k.parent.color = BLACK; // случай 3: левый поворот и перекраска
                    k.parent.parent.color = RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                if (u != null && u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        root.color = BLACK; // корень дерева всегда должен быть черным
    }
    private void toStringHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringHelper(node.left, sb); // обхожу левое поддерево для сортировки по возрастанию
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toStringHelper(node.right, sb);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringHelper(root, sb); // запускаю сборку элементов в строку
        sb.append("}");
        return sb.toString();
    }
    @Override
    public int size() { return size; } // возвращаю размер карты
    @Override
    public void clear() { root = null; size = 0; } // очищаю дерево
    @Override
    public boolean isEmpty() { return size == 0; } // проверка на пустоту
    @Override
    public String put(Integer key, String value) {
        Node node = new Node(key, value);
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (node.key.compareTo(x.key) < 0) x = x.left;
            else if (node.key.compareTo(x.key) > 0) x = x.right;
            else {
                String oldVal = x.value; // если ключ нашелся, обновляю значение
                x.value = value;
                return oldVal;
            }
        }
        node.parent = y;
        if (y == null) root = node;
        else if (node.key.compareTo(y.key) < 0) y.left = node;
        else y.right = node;
        size++;
        fixInsert(node); // балансирую дерево после вставки
        return null;
    }
    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }
    private Node minimum(Node node) {
        while (node.left != null) node = node.left; // ищу самый левый узел поддерева
        return node;
    }
    private void fixDelete(Node x) {
        if (x == null) return;
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node s = x.parent.right; // брат узла x
                if (s.color == RED) {
                    s.color = BLACK; x.parent.color = RED;
                    leftRotate(x.parent); s = x.parent.right;
                }
                if (s.left.color == BLACK && s.right.color == BLACK) {
                    s.color = RED; x = x.parent;
                } else {
                    if (s.right.color == BLACK) {
                        s.left.color = BLACK; s.color = RED;
                        rightRotate(s); s = x.parent.right;
                    }
                    s.color = x.parent.color; x.parent.color = BLACK;
                    s.right.color = BLACK; leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node s = x.parent.left;
                if (s.color == RED) {
                    s.color = BLACK; x.parent.color = RED;
                    rightRotate(x.parent); s = x.parent.left;
                }
                if (s.right.color == BLACK && s.left.color == BLACK) {
                    s.color = RED; x = x.parent;
                } else {
                    if (s.left.color == BLACK) {
                        s.right.color = BLACK; s.color = RED;
                        leftRotate(s); s = x.parent.left;
                    }
                    s.color = x.parent.color; x.parent.color = BLACK;
                    s.left.color = BLACK; rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }
    @Override
    public String remove(Object key) {
        Node z = root;
        while (z != null) {
            int cmp = ((Integer) key).compareTo(z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else break; // нашли узел для удаления
        }
        if (z == null) return null;
        String removedValue = z.value;
        Node x, y = z;
        boolean yOriginalColor = y.color;
        if (z.left == null) {
            x = z.right; rbTransplant(z, z.right);
        } else if (z.right == null) {
            x = z.left; rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color; x = y.right;
            if (y.parent == z) { if (x != null) x.parent = y; }
            else {
                rbTransplant(y, y.right); y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }
            rbTransplant(z, y); y.left = z.left;
            y.left.parent = y; y.color = z.color;
        }
        size--;
        if (yOriginalColor == BLACK) fixDelete(x); // если удалили черный узел, восстанавливаем баланс
        return removedValue;
    }
    @Override
    public String get(Object key) {
        Node current = root;
        while (current != null) {
            int cmp = ((Integer) key).compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value; // нашли значение по ключу
        }
        return null;
    }
    @Override
    public boolean containsKey(Object key) { return get(key) != null; }
    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return true; // безопасное сравнение без кастинга типа
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }
    @Override
    public boolean containsValue(Object value) { return containsValueHelper(root, value); }
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) subMap.put(node.key, node.value);
        if (node.key.compareTo(fromKey) > 0) subMapHelper(node.left, fromKey, toKey, subMap);
        if (node.key.compareTo(toKey) < 0) subMapHelper(node.right, fromKey, toKey, subMap);
    }
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, Integer.MIN_VALUE, toKey, subMap); // отбираю элементы с ключом < toKey
        return subMap;
    }
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, fromKey, Integer.MAX_VALUE, subMap); // отбираю элементы с ключом >= fromKey
        return subMap;
    }
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minimum(root).key; // самый левый ключ в дереве
    }
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) current = current.right; // самый правый ключ в дереве
        return current.key;
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
}
