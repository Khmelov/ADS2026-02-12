package by.it.group510902.kudan.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    //узел splay дерева, никаких доп полей для баланса нет
    private static class Node {
        int key;
        String value;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    private int size;

    //нисходящий splay, поднимает в корень узел с ключом key
    //если такого ключа нет, то в корне окажется ближайший к нему по пути поиска узел
    private Node splay(Node t, int key) {
        if (t == null) {
            return null;
        }

        //header это временный узел, в его right копится левое дерево, а в left правое
        Node header = new Node(0, null);
        Node l = header;
        Node r = header;

        while (true) {
            if (key < t.key) {
                if (t.left == null) {
                    break;
                }

                //zig zig, поворачиваем вправо
                if (key < t.left.key) {
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;

                    if (t.left == null) {
                        break;
                    }
                }

                //текущий узел уходит в правое дерево, спускаемся влево
                r.left = t;
                r = t;
                t = t.left;
            } else if (key > t.key) {
                if (t.right == null) {
                    break;
                }

                //zig zig, поворачиваем влево
                if (key > t.right.key) {
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;

                    if (t.right == null) {
                        break;
                    }
                }

                //текущий узел уходит в левое дерево, спускаемся вправо
                l.right = t;
                l = t;
                t = t.right;
            } else {
                break;
            }
        }

        //собираем дерево обратно, детей найденного узла вешаем на концы левого и правого деревьев
        l.right = t.left;
        r.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
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
    private void copy(Node x, MySplayMap m, int key, boolean head) {
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
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        //ключ уже есть, он теперь в корне, меняем значение
        if (root.key == key) {
            String old = root.value;
            root.value = value;
            return old;
        }

        //новый узел становится корнем, а старое дерево делится на две части вокруг него
        Node n = new Node(key, value);
        if (key < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }

        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) {
            return null;
        }

        int k = (Integer) key;
        root = splay(root, k);

        //нужного ключа нет
        if (root.key != k) {
            return null;
        }

        String old = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            //splay левого поддерева по этому же ключу поднимает его максимум, у него нет правого ребенка
            Node r = root.right;
            root = splay(root.left, k);
            root.right = r;
        }

        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (root == null) {
            return null;
        }

        int k = (Integer) key;
        root = splay(root, k);
        return root.key == k ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (root == null) {
            return false;
        }

        int k = (Integer) key;
        root = splay(root, k);
        return root.key == k;
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
        MySplayMap m = new MySplayMap();
        copy(root, m, toKey, true);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        //все ключи начиная с fromKey
        MySplayMap m = new MySplayMap();
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

    @Override
    public Integer lowerKey(Integer key) {
        //самый большой ключ строго меньше key
        Node x = root;
        Integer res = null;

        while (x != null) {
            if (x.key < key) {
                res = x.key;
                x = x.right;
            } else {
                x = x.left;
            }
        }

        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        //самый большой ключ меньше или равный key
        Node x = root;
        Integer res = null;

        while (x != null) {
            if (x.key <= key) {
                res = x.key;
                x = x.right;
            } else {
                x = x.left;
            }
        }

        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        //самый маленький ключ больше или равный key
        Node x = root;
        Integer res = null;

        while (x != null) {
            if (x.key >= key) {
                res = x.key;
                x = x.left;
            } else {
                x = x.right;
            }
        }

        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        //самый маленький ключ строго больше key
        Node x = root;
        Integer res = null;

        while (x != null) {
            if (x.key > key) {
                res = x.key;
                x = x.left;
            } else {
                x = x.right;
            }
        }

        return res;
    }

    public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    public Entry<Integer, String> floorEntry(Integer key) { return null; }
    public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    public Entry<Integer, String> higherEntry(Integer key) { return null; }
    public Entry<Integer, String> firstEntry() { return null; }
    public Entry<Integer, String> lastEntry() { return null; }
    public Entry<Integer, String> pollFirstEntry() { return null; }
    public Entry<Integer, String> pollLastEntry() { return null; }
    public NavigableMap<Integer, String> descendingMap() { return null; }
    public NavigableSet<Integer> navigableKeySet() { return null; }
    public NavigableSet<Integer> descendingKeySet() { return null; }
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    public Comparator<? super Integer> comparator() { return null; }
    public void putAll(Map<? extends Integer, ? extends String> m) { }
    public Set<Integer> keySet() { return null; }
    public Collection<String> values() { return null; }
    public Set<Entry<Integer, String>> entrySet() { return null; }
}