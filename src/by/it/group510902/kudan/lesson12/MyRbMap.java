package by.it.group510902.kudan.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    //узел красно черного дерева, red true значит что ссылка на него из родителя красная
    //новый узел всегда красный
    private static class Node {
        int key;
        String value;
        boolean red = true;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    private int size;

    //пустое место считается черным
    private boolean isRed(Node x) {
        return x != null && x.red;
    }

    //обычный поиск в дереве поиска
    private Node find(int key) {
        Node x = root;

        while (x != null) {
            if (key == x.key) {
                return x;
            }
            x = key < x.key ? x.left : x.right;
        }

        return null;
    }

    //левый поворот, красная правая ссылка становится левой
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    //правый поворот, красная левая ссылка становится правой
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    //меняем цвета узла и обоих детей на противоположные
    private void flip(Node h) {
        h.red = !h.red;
        h.left.red = !h.left.red;
        h.right.red = !h.right.red;
    }

    //чиним правило дерева после вставки или удаления в поддереве
    private Node balance(Node h) {
        //красная ссылка не должна быть правой
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }

        //две красные ссылки подряд слева, поворачиваем вправо
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }

        //обе ссылки красные, поднимаем красный цвет вверх
        if (isRed(h.left) && isRed(h.right)) {
            flip(h);
        }

        return h;
    }

    //h красный, а левый ребенок и его левый ребенок черные, делаем красным левого или его ребенка
    private Node moveRedLeft(Node h) {
        flip(h);

        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flip(h);
        }

        return h;
    }

    //зеркально для правой стороны
    private Node moveRedRight(Node h) {
        flip(h);

        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flip(h);
        }

        return h;
    }

    //вставка нового ключа, которого точно еще нет в дереве
    private Node insert(Node h, int key, String value) {
        if (h == null) {
            return new Node(key, value);
        }

        if (key < h.key) {
            h.left = insert(h.left, key, value);
        } else {
            h.right = insert(h.right, key, value);
        }

        return balance(h);
    }

    //удаление минимума из поддерева
    private Node deleteMin(Node h) {
        if (h.left == null) {
            return null;
        }

        //спускаемся влево, по пути гарантируем что слева есть красный узел
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = deleteMin(h.left);
        return balance(h);
    }

    //удаление ключа, который точно есть в дереве
    private Node delete(Node h, int key) {
        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }

            //нашли ключ и справа ничего нет, значит узел лист
            if (key == h.key && h.right == null) {
                return null;
            }

            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }

            if (key == h.key) {
                //заменяем узел минимумом из правого поддерева и удаляем этот минимум
                Node m = h.right;
                while (m.left != null) {
                    m = m.left;
                }
                h.key = m.key;
                h.value = m.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }

        return balance(h);
    }

    //обход слева направо, ключи выводятся по возрастанию
    private void walk(Node x, StringBuilder sb) {
        if (x == null) {
            return;
        }

        walk(x.left, sb);

        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(x.key).append("=").append(x.value);

        walk(x.right, sb);
    }

    //копируем в новую карту узлы с ключом меньше key если head true, иначе с ключом не меньше key
    private void copy(Node x, MyRbMap m, int key, boolean head) {
        if (x == null) {
            return;
        }

        copy(x.left, m, key, head);

        if (head ? x.key < key : x.key >= key) {
            m.put(x.key, x.value);
        }

        copy(x.right, m, key, head);
    }

    //ищем значение обходом всего дерева
    private boolean has(Node x, Object v) {
        if (x == null) {
            return false;
        }

        return (v == null ? x.value == null : v.equals(x.value)) || has(x.left, v) || has(x.right, v);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        walk(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node x = find(key);

        //ключ уже есть, меняем только значение и возвращаем старое
        if (x != null) {
            String old = x.value;
            x.value = value;
            return old;
        }

        root = insert(root, key, value);

        //корень всегда черный
        root.red = false;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node x = find((Integer) key);

        if (x == null) {
            return null;
        }

        String old = x.value;

        //если оба ребенка корня черные, красим корень в красный чтобы было что двигать вниз
        if (!isRed(root.left) && !isRed(root.right)) {
            root.red = true;
        }

        root = delete(root, x.key);
        size--;

        //корень снова черный
        if (root != null) {
            root.red = false;
        }

        return old;
    }

    @Override
    public String get(Object key) {
        Node x = find((Integer) key);
        return x == null ? null : x.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return has(root, value);
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        //все ключи строго меньше toKey
        MyRbMap m = new MyRbMap();
        copy(root, m, toKey, true);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        //все ключи начиная с fromKey
        MyRbMap m = new MyRbMap();
        copy(root, m, fromKey, false);
        return m;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        //минимум это самый левый узел
        Node x = root;
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        //максимум это самый правый узел
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    public Comparator<? super Integer> comparator() { return null; }
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    public void putAll(Map<? extends Integer, ? extends String> m) { }
    public Set<Integer> keySet() { return null; }
    public Collection<String> values() { return null; }
    public Set<Entry<Integer, String>> entrySet() { return null; }
}