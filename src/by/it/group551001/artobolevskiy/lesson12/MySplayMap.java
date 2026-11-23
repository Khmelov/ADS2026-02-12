package by.it.group551001.artobolevskiy.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private void rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.parent = h.parent;
        if (h.parent == null) {
            root = x;
        } else if (h == h.parent.left) {
            h.parent.left = x;
        } else {
            h.parent.right = x;
        }
        x.left = h;
        h.parent = x;
    }

    private void rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.parent = h.parent;
        if (h.parent == null) {
            root = x;
        } else if (h == h.parent.right) {
            h.parent.right = x;
        } else {
            h.parent.left = x;
        }
        x.right = h;
        h.parent = x;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else {
                if (x == p.left && p == g.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else if (x == p.right && p == g.right) {
                    rotateLeft(g);
                    rotateLeft(p);
                } else if (x == p.right && p == g.left) {
                    rotateLeft(p);
                    rotateRight(g);
                } else {
                    rotateRight(p);
                    rotateLeft(g);
                }
            }
        }
    }

    private Node findNode(Integer key) {
        if (root == null) return null;
        Node current = root;
        Node last = root;
        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }
        splay(last);
        return null;
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }

        StringBuilder s = new StringBuilder();
        s.append('{');
        dfs_inorder(root, s);
        s.delete(s.length() - 2, s.length());
        s.append('}');
        return s.toString();
    }

    private void dfs_inorder(Node node, StringBuilder s) {
        if (node == null) return;
        dfs_inorder(node.left, s);
        s.append(node.key).append('=').append(node.value).append(", ");
        dfs_inorder(node.right, s);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        NavigableMap<Integer, String> sub = new MySplayMap();
        for (Entry<Integer, String> e : entrySet()) {
            int cmpFrom = e.getKey().compareTo(fromKey);
            int cmpTo = e.getKey().compareTo(toKey);
            boolean lowOK = fromInclusive ? cmpFrom >= 0 : cmpFrom > 0;
            boolean highOK = toInclusive ? cmpTo <= 0 : cmpTo < 0;
            if (lowOK && highOK) {
                sub.put(e.getKey(), e.getValue());
            }
        }
        return sub;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        NavigableMap<Integer, String> sub = new MySplayMap();
        for (Entry<Integer, String> e : entrySet()) {
            int cmp = e.getKey().compareTo(toKey);
            if (inclusive ? cmp <= 0 : cmp < 0) {
                sub.put(e.getKey(), e.getValue());
            }
        }
        return sub;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        NavigableMap<Integer, String> sub = new MySplayMap();
        for (Entry<Integer, String> e : entrySet()) {
            int cmp = e.getKey().compareTo(fromKey);
            if (inclusive ? cmp >= 0 : cmp > 0) {
                sub.put(e.getKey(), e.getValue());
            }
        }
        return sub;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (root == null) return null;
        findNode(key);
        if (root.key.compareTo(key) < 0) {
            return root.key;
        }
        Node current = root.left;
        if (current == null) return null;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (root == null) return null;
        findNode(key);
        if (root.key.compareTo(key) <= 0) {
            return root.key;
        }
        Node current = root.left;
        if (current == null) return null;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null) return null;
        findNode(key);
        if (root.key.compareTo(key) >= 0) {
            return root.key;
        }
        Node current = root.right;
        if (current == null) return null;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (root == null) return null;
        findNode(key);
        if (root.key.compareTo(key) > 0) {
            return root.key;
        }
        Node current = root.right;
        if (current == null) return null;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node node = findNode((Integer) key);
        return node != null && node.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node != null && node.key.equals(key)) {
            return node.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        findNode(key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
        Node newNode = new Node(key, value);
        if (cmp < 0) {
            newNode.left = root.left;
            if (newNode.left != null) newNode.left.parent = newNode;
            newNode.right = root;
            root.left = null;
            root.parent = newNode;
        } else {
            newNode.right = root.right;
            if (newNode.right != null) newNode.right.parent = newNode;
            newNode.left = root;
            root.right = null;
            root.parent = newNode;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node node = findNode(k);
        if (node == null || !node.key.equals(k)) return null;

        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else if (root.right == null) {
            root = root.left;
            if (root != null) root.parent = null;
        } else {
            Node leftSub = root.left;
            leftSub.parent = null;
            Node rightSub = root.right;
            rightSub.parent = null;

            root = leftSub;
            Node max = leftSub;
            while (max.right != null) {
                max = max.right;
            }
            splay(max);
            root.right = rightSub;
            rightSub.parent = root;
        }
        size--;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = new LinkedHashSet<>();
        fillKeySet(root, set);
        return set;
    }

    private void fillKeySet(Node node, Set<Integer> set) {
        if (node == null) return;
        fillKeySet(node.left, set);
        set.add(node.key);
        fillKeySet(node.right, set);
    }

    @Override
    public Collection<String> values() {
        List<String> list = new ArrayList<>();
        fillValues(root, list);
        return list;
    }

    private void fillValues(Node node, List<String> list) {
        if (node == null) return;
        fillValues(node.left, list);
        list.add(node.value);
        fillValues(node.right, list);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> set = new LinkedHashSet<>();
        fillEntrySet(root, set);
        return set;
    }

    private void fillEntrySet(Node node, Set<Entry<Integer, String>> set) {
        if (node == null) return;
        fillEntrySet(node.left, set);
        set.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        fillEntrySet(node.right, set);
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        if (isEmpty()) return null;
        Integer k = firstKey();
        return new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (isEmpty()) return null;
        Integer k = lastKey();
        return new AbstractMap.SimpleEntry<>(k, get(k));
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Entry<Integer, String> e = firstEntry();
        if (e != null) {
            remove(e.getKey());
        }
        return e;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Entry<Integer, String> e = lastEntry();
        if (e != null) {
            remove(e.getKey());
        }
        return e;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        MySplayMap map = new MySplayMap();
        for (Entry<Integer, String> e : entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return (NavigableSet<Integer>) keySet();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        TreeSet<Integer> set = new TreeSet<>(Collections.reverseOrder());
        set.addAll(keySet());
        return set;
    }
}