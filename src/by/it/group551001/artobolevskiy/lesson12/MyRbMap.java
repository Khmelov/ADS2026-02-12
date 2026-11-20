package by.it.group551001.artobolevskiy.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

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
        MyRbMap sub = new MyRbMap();
        fillSubMap(root, fromKey, toKey, sub);
        return sub;
    }

    private void fillSubMap(Node node, Integer fromKey, Integer toKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            fillSubMap(node.left, fromKey, toKey, sub);
            sub.put(node.key, node.value);
            fillSubMap(node.right, fromKey, toKey, sub);
        } else if (node.key.compareTo(fromKey) < 0) {
            fillSubMap(node.right, fromKey, toKey, sub);
        } else {
            fillSubMap(node.left, fromKey, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            fillHeadMap(node.left, toKey, sub);
            sub.put(node.key, node.value);
            fillHeadMap(node.right, toKey, sub);
        } else {
            fillHeadMap(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            fillTailMap(node.left, fromKey, sub);
            sub.put(node.key, node.value);
            fillTailMap(node.right, fromKey, sub);
        } else {
            fillTailMap(node.right, fromKey, sub);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
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
        Node node = getNode(key);
        return node == null ? null : node.value;
    }

    private Node getNode(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        Node parent = null;
        Node current = root;
        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        Node newNode = new Node(key, value, RED, parent);
        if (parent == null) {
            root = newNode;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixAfterInsert(newNode);
        size++;
        return null;
    }

    private void fixAfterInsert(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null)
                l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else
                p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    @Override
    public String remove(Object key) {
        Node node = getNode(key);
        if (node == null) return null;
        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node p) {
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left = replacement;
            else
                p.parent.right = replacement;

            p.left = p.right = p.parent = null;

            if (p.color == BLACK)
                fixAfterDelete(replacement);
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p.color == BLACK)
                fixAfterDelete(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    private Node successor(Node t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Node p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void fixAfterDelete(Node x) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == x.parent.left) {
                Node sib = x.parent.right;
                if (sib != null && sib.color == RED) {
                    sib.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    sib = x.parent.right;
                }
                if ((sib == null || sib.left == null || sib.left.color == BLACK) &&
                        (sib == null || sib.right == null || sib.right.color == BLACK)) {
                    if (sib != null) sib.color = RED;
                    x = x.parent;
                } else {
                    if (sib == null || sib.right == null || sib.right.color == BLACK) {
                        if (sib != null && sib.left != null) sib.left.color = BLACK;
                        if (sib != null) sib.color = RED;
                        rotateRight(sib);
                        sib = x.parent.right;
                    }
                    if (sib != null) {
                        sib.color = x.parent.color;
                        if (sib.right != null) sib.right.color = BLACK;
                    }
                    x.parent.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node sib = x.parent.left;
                if (sib != null && sib.color == RED) {
                    sib.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    sib = x.parent.left;
                }
                if ((sib == null || sib.right == null || sib.right.color == BLACK) &&
                        (sib == null || sib.left == null || sib.left.color == BLACK)) {
                    if (sib != null) sib.color = RED;
                    x = x.parent;
                } else {
                    if (sib == null || sib.left == null || sib.left.color == BLACK) {
                        if (sib != null && sib.right != null) sib.right.color = BLACK;
                        if (sib != null) sib.color = RED;
                        rotateLeft(sib);
                        sib = x.parent.left;
                    }
                    if (sib != null) {
                        sib.color = x.parent.color;
                        if (sib.left != null) sib.left.color = BLACK;
                    }
                    x.parent.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = new TreeSet<>();
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
}