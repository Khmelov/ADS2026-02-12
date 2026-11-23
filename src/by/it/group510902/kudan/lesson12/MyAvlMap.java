package by.it.group510902.kudan.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    //узел авл дерева, h это высота поддерева у которого этот узел корень
    private static class Node {
        int key;
        String value;
        int h = 1;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    private int size;

    //высота узла, у пустого места высота 0
    private int h(Node x) {
        return x == null ? 0 : x.h;
    }

    //обычный поиск в дереве поиска, меньше идем влево, больше вправо
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

    //правый поворот, левый ребенок y становится корнем
    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;

        //сначала пересчитываем нижний узел, потом верхний
        y.h = Math.max(h(y.left), h(y.right)) + 1;
        x.h = Math.max(h(x.left), h(x.right)) + 1;
        return x;
    }

    //левый поворот, правый ребенок x становится корнем
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;

        x.h = Math.max(h(x.left), h(x.right)) + 1;
        y.h = Math.max(h(y.left), h(y.right)) + 1;
        return y;
    }

    //пересчитываем высоту и если разница высот детей больше одного, то балансируем
    private Node balance(Node x) {
        x.h = Math.max(h(x.left), h(x.right)) + 1;
        int b = h(x.left) - h(x.right);

        if (b > 1) {
            //слева тяжело, если тяжелее внутренняя часть то сначала левый поворот у ребенка
            if (h(x.left.left) < h(x.left.right)) {
                x.left = rotateLeft(x.left);
            }
            return rotateRight(x);
        }

        if (b < -1) {
            //справа тяжело, зеркально
            if (h(x.right.right) < h(x.right.left)) {
                x.right = rotateRight(x.right);
            }
            return rotateLeft(x);
        }

        return x;
    }

    //вставка нового ключа, которого точно еще нет в дереве
    private Node insert(Node x, int key, String value) {
        if (x == null) {
            return new Node(key, value);
        }

        if (key < x.key) {
            x.left = insert(x.left, key, value);
        } else {
            x.right = insert(x.right, key, value);
        }

        return balance(x);
    }

    //удаление ключа, который точно есть в дереве
    private Node delete(Node x, int key) {
        if (key < x.key) {
            x.left = delete(x.left, key);
        } else if (key > x.key) {
            x.right = delete(x.right, key);
        } else {
            //у узла меньше двух детей, заменяем его единственным ребенком
            if (x.left == null) {
                return x.right;
            }
            if (x.right == null) {
                return x.left;
            }

            //два ребенка, берем минимум правого поддерева и переносим его в этот узел
            Node m = x.right;
            while (m.left != null) {
                m = m.left;
            }
            x.key = m.key;
            x.value = m.value;
            x.right = delete(x.right, m.key);
        }

        return balance(x);
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
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node x = find((Integer) key);

        if (x == null) {
            return null;
        }

        //значение запоминаем до удаления, потому что при удалении узлы могут поменяться местами
        String old = x.value;
        root = delete(root, x.key);
        size--;
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

    public boolean containsValue(Object value) { return false; }
    public void putAll(Map<? extends Integer, ? extends String> m) { }
    public Set<Integer> keySet() { return null; }
    public Collection<String> values() { return null; }
    public Set<Entry<Integer, String>> entrySet() { return null; }
}