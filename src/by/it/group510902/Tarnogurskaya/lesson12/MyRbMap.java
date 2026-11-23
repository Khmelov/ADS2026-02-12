package by.it.group510902.Tarnogurskaya.lesson12;

import java.util.SortedMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red;
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.red = true;
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

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (node.value == null ? value == null : node.value.equals(value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    private Node getNode(Integer key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        Node y = null; //родитель будущего нового узла
        Node x = root; //текущий узел, по которому мы перемещаемся
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right; //ищем место для вставки
            else {
                String old = x.value;
                x.value = value;
                return old; //если уже есть
            }
        }
        //создаем новый красный узел
        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) root = z;
        else {
            int cmp = key.compareTo(y.key);
            if (cmp < 0) y.left = z;
            else y.right = z;
        }
        z.left = null;
        z.right = null;
        z.red = true;
        size++;
        insertFixup(z);
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.red) {
            //родитель находится слева от дедушки
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; //дядя
                if (y != null && y.red) { //дядя красный
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else { //дядя черный
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                        //если текущий узел справа от родителя - левый поворот родителя
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateRight(z.parent.parent);
                }
            }
            //родитель находится справа от дедушки
            else {
                Node y = z.parent.parent.left;
                if (y != null && y.red) { //дядя красный
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                }
                else { //дядя черный
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                        //если текущий узел слева от родителя - правый поворот родителя
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.red = false;
    }

    private void rotateLeft(Node x) {
        Node y = x.right; //у это правый ребенок х
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

    @Override
    public String remove(Object key) {
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String removedValue = z.value;
        deleteNode(z);
        size--;
        return removedValue;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        Node xParent;
        boolean yOriginalRed = y.red;
        //у узла нет левого ребёнка
        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        }
        //у узла нет правого ребёнка
        else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        }
        //у узла есть оба ребёнка
        else {
            //ищем минимальный узел в правом поддереве z
            y = minimum(z.right);
            //запоминаем цвет
            yOriginalRed = y.red;
            //правый ребёнок y займёт его прежнее место
            x = y.right;
            //если y правый ребенок z, то после перемещения родителем х станет сам y
            if (y.parent == z) {
                xParent = y;
            } else {
                //иначе запоминаем прежнего родителя y
                xParent = y.parent;
                //заменяем у его собственным правым ребёнком.
                transplant(y, y.right);
                //подключаем к y правое поддерево удаляемого узла z
                y.right = z.right;
                //обновляем обратную ссылку на родителя
                y.right.parent = y;
            }
            transplant(z, y);
            //подключаем к y левое поддерево узла z
            y.left = z.left;
            //обновляем родителя левого ребёнка.
            y.left.parent = y;
            //новый узел y получает цвет удаляемого узла z
            y.red = z.red;
        }
        if (!yOriginalRed) {
            //исправляем нарушение перекрашиванием и поворотами
            deleteFixup(x, xParent);
        }
    }
    //заменяем узел u узлом v в дереве
    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private void deleteFixup(Node x, Node parent) {
        while (x != root && (x == null || !x.red)) {
            //x является левым ребёнком родителя
            if (x == parent.left) {
                Node w = parent.right; //брат
                if (w != null && w.red) { //брат красный
                    w.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    w = parent.right;
                }
                //брат черный, а оба его ребёнка тоже чёрные или отсутствуют
                if (w == null || ((w.left == null || !w.left.red) && (w.right == null || !w.right.red))) {
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    //правый ребёнок брата чёрный или отсутствует, но левый ребёнок красный.
                    if (w.right == null || !w.right.red) {
                        if (w.left != null) w.left.red = false;
                        w.red = true;
                        rotateRight(w);
                        w = parent.right;
                    }
                    //правый ребёнок брата красный
                    w.red = parent.red;
                    parent.red = false;
                    if (w.right != null) w.right.red = false;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                //x является правым ребёнком
                Node w = parent.left;
                if (w != null && w.red) {
                    w.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (w == null || ((w.right == null || !w.right.red) && (w.left == null || !w.left.red))) {
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || !w.left.red) {
                        if (w.right != null) w.right.red = false;
                        w.red = true;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.red = parent.red;
                    parent.red = false;
                    if (w.left != null) w.left.red = false;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        //если x существует, перекрашиваем его в чёрный.
        if (x != null) x.red = false;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, null, toKey, true);
        return map;
    } //возвращает все элементы, ключи которых меньше указанного значения

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, fromKey, null, false);
        return map;
    } //возвращает все элементы, ключи которых больше или равны указанному значению

    //обходит дерево и выбирает нужные элементы
    private void addToMap(Node node, MyRbMap map, Integer fromKey, Integer toKey, boolean head) {
        if (node == null) return;
        addToMap(node.left, map, fromKey, toKey, head);
        boolean include = true;
        if (head && toKey != null) {
            if (node.key.compareTo(toKey) >= 0) include = false;
        }
        if (!head && fromKey != null) {
            if (node.key.compareTo(fromKey) < 0) include = false;
        }
        if (include) {
            map.put(node.key, node.value);
        }
        addToMap(node.right, map, fromKey, toKey, head);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }



    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}