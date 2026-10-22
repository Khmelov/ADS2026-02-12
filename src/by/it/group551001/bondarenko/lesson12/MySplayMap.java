package by.it.group551001.bondarenko.lesson12;

import java.util.*;

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
    private int size = 0;

    // Правый поворот
    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Левый поворот
    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Splay-операция: перемещает узел с заданным ключом (или ближайший к нему) в корень
    private Node splay(Node root, Integer key) {
        if (root == null || root.key.equals(key)) {
            return root;
        }

        // Ключ в левом поддереве
        if (key.compareTo(root.key) < 0) {
            if (root.left == null) return root;

            // Zig-Zig (Left Left)
            if (key.compareTo(root.left.key) < 0) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            }
            // Zig-Zag (Left Right)
            else if (key.compareTo(root.left.key) > 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) {
                    root.left = leftRotate(root.left);
                }
            }

            return (root.left == null) ? root : rightRotate(root);
        }
        // Ключ в правом поддереве
        else {
            if (root.right == null) return root;

            // Zag-Zig (Right Left)
            if (key.compareTo(root.right.key) < 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) {
                    root.right = rightRotate(root.right);
                }
            }
            // Zag-Zag (Right Right)
            else if (key.compareTo(root.right.key) > 0) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }

            return (root.right == null) ? root : leftRotate(root);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        Integer key = (Integer) keyObj;
        root = splay(root, key);
        return root != null && root.key.equals(key);
    }

    @Override
    public boolean containsValue(Object valueObj) {
        if (!(valueObj instanceof String)) return false;
        String value = (String) valueObj;
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, String value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        root = splay(root, key);
        if (root != null && root.key.equals(key)) {
            return root.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        if (root.key.equals(key)) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node newNode = new Node(key, value);
        if (key.compareTo(root.key) < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        size++;
        root = newNode;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        root = splay(root, key);

        if (root == null || !root.key.equals(key)) {
            return null;
        }

        String oldValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            Node temp = root.right;
            root = root.left;
            root = splay(root, key);
            root.right = temp;
        }
        size--;
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        root = splay(root, curr.key);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        root = splay(root, curr.key);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        root = splay(root, key);
        if (root.key.compareTo(key) < 0) {
            return root.key;
        }
        if (root.left == null) return null;
        Node curr = root.left;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        root = splay(root, key);
        if (root.key.compareTo(key) <= 0) {
            return root.key;
        }
        if (root.left == null) return null;
        Node curr = root.left;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        root = splay(root, key);
        if (root.key.compareTo(key) >= 0) {
            return root.key;
        }
        if (root.right == null) return null;
        Node curr = root.right;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        root = splay(root, key);
        if (root.key.compareTo(key) > 0) {
            return root.key;
        }
        if (root.right == null) return null;
        Node curr = root.right;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        headMapHelper(root, toKey, sub);
        return sub;
    }

    private void headMapHelper(Node node, Integer toKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            sub.put(node.key, node.value);
            headMapHelper(node.left, toKey, sub);
            headMapHelper(node.right, toKey, sub);
        } else {
            headMapHelper(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        tailMapHelper(root, fromKey, sub);
        return sub;
    }

    private void tailMapHelper(Node node, Integer fromKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            sub.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, sub);
            tailMapHelper(node.right, fromKey, sub);
        } else {
            tailMapHelper(node.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        appendInOrder(node.right, sb);
    }

    // --- Заглушки для оставшихся методов интерфейса NavigableMap, если потребуются компилятором ---

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override
    public Map.Entry<Integer, String> firstEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> lastEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override
    public Map.Entry<Integer, String> pollLastEntry() { return null; }
    @Override
    public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override
    public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override
    public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean inclusive, Integer toKey, boolean exclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
}