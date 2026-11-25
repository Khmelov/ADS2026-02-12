package by.it.group510902.Tarnogurskaya.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
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
        if (root == null) return false;
        root = splay(root, (Integer) key);
        return root.key.equals(key);
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
        if (root == null) return null;
        root = splay(root, (Integer) key);
        if (root.key.equals(key)) {
            return root.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node newNode = new Node(key, value);
        if (cmp < 0) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) return null;
        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) {
            return null;
        }
        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSubtree = root.right;
            root = splay(root.left, (Integer) key);
            root.right = rightSubtree;
        }
        size--;
        return oldValue;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        addToMap(root, map, null, toKey, true);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        addToMap(root, map, fromKey, null, false);
        return map;
    }

    private void addToMap(Node node, MySplayMap map, Integer fromKey, Integer toKey, boolean head) {
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
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) < 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) <= 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) >= 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) > 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
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

    private Node splay(Node root, Integer key) {
        if (root == null) return null;
        Node header = new Node(null, null); //вспомогательный узел, не входит в основное дерево
        Node leftTreeMax = header; //последний узел временного левого дерева (узлы с ключами меньше искомого)
        Node rightTreeMin = header;//последний узел временного правого дерева (узлы с ключами больше искомого)

        while (true) {
            int cmp = key.compareTo(root.key);
            //ищем слева
            if (cmp < 0) {
                if (root.left == null) break;
                if (key.compareTo(root.left.key) < 0) {
                    //поворот зиг заг лево лево
                    Node temp = root.left;
                    root.left = temp.right;
                    temp.right = root;
                    root = temp;
                    if (root.left == null) break;
                }
                //переносим текущий корень в правое временное дерево
                rightTreeMin.left = root;
                //обновляем конец правого временного дерева
                rightTreeMin = root;
                //продолжаем поиск в левом поддереве
                root = root.left;
            }
            //ищем справа
            else if (cmp > 0) {
                if (root.right == null) break;
                //правее правого - зиг заг
                if (key.compareTo(root.right.key) > 0) {
                    Node temp = root.right;
                    root.right = temp.left;
                    temp.left = root;
                    root = temp;
                    if (root.right == null) break;
                }
                //переносим текущий корень в левое временное дерево
                leftTreeMax.right = root;
                //обновляем конец левого временного дерева
                leftTreeMax = root;
                //продолжаем поиск в правом поддереве
                root = root.right;
            } else {
                break;
            }
        }
        //присоединяем левое поддерево итогового корня к концу правого временного дерева
        leftTreeMax.right = root.left;
        //присоединяем правое поддерево итогового корня к концу левого временного дерева
        rightTreeMin.left = root.right;
        //переносим левое временное дерево в левое поддерево итогового корня
        root.left = header.right;
        //переносим правое временное дерево правое поддерево итогового корня
        root.right = header.left;
        return root;
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

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }
}