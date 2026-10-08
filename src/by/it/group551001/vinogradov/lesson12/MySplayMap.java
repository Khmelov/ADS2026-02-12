package by.it.group551001.vinogradov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private void rotate_left(Node n) {
        Node pivot = n.right;
        if (pivot == null) return;

        Node child = pivot.left;

        pivot.parent = n.parent;
        if (n.parent == null) root = pivot;
        else if (n.parent.left == n) n.parent.left = pivot;
        else n.parent.right = pivot;

        pivot.left = n;
        n.parent = pivot;
        n.right = child;
        if (child != null) child.parent = n;
    }

    private void rotate_right(Node n) {
        Node pivot = n.left;
        if (pivot == null) return;

        Node child = pivot.right;

        pivot.parent = n.parent;
        if (n.parent == null) root = pivot;
        else if (n.parent.left == n) n.parent.left = pivot;
        else n.parent.right = pivot;

        pivot.right = n;
        n.parent = pivot;
        n.left = child;
        if (child != null) child.parent = n;
    }

    private void slay(Node n) {
        while(n.parent != null) {
            Node p = n.parent;
            if (p.parent == null) {
                if (n == p.left) rotate_right(p);
                else rotate_left(p);
            } else {
                if (n == p.left) {
                    if (p == p.parent.left) {
                        rotate_right(p.parent);
                        rotate_right(p);
                    } else {
                        rotate_right(p);
                        rotate_left(n.parent);
                    }
                } else {
                    if (p == p.parent.left) {
                        rotate_left(p);
                        rotate_right(n.parent);
                    } else {
                        rotate_left(p.parent);
                        rotate_left(p);
                    }
                }
            }
        }
    }

    private Node merge(Node t1, Node t2) {
        if (t1 == null) {
            if (t2 != null) t2.parent = null;
            return t2;
        }
        Node curr = t1;
        Node mx = t1;
        while(curr.right != null) {
            curr = curr.right;
            mx = curr;
        }
        slay(mx);
        mx.right = t2;
        if (t2 != null) t2.parent = mx;
        return mx;
    }

    private void split(Node x) {
        if (x == null) return;
        slay(x);
        split(x.right);
    }

    private String print_traverse(String s, Node node) {
        if (node == null) return s;

        s = print_traverse(s, node.left);
        if (s.length() > 1) s += ", ";
        s += node.key + "=" + node.value;

        return print_traverse(s, node.right);
    }

    private String find_traverse(Node node, int key) {
        if (node != null) {
            if (key < node.key) return find_traverse(node.left, key);
            else if (key > node.key) return find_traverse(node.right, key);
            else {
                slay(node);
                return node.value;
            }
        }
        return null;
    }

    private boolean find_val_traverse(Node node, String value) {
        boolean f = false;
        if (node != null) {
            if (Objects.equals(node.value, value)) return true;
            f |= find_val_traverse(node.left, value);
            f |= find_val_traverse(node.right, value);
        }
        return f;
    }

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return print_traverse("{", root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        Node node = new Node(key, value);
        if (root == null) {
            root = node;
            size++;
            return null;
        }
        Node curr = root;
        while (true) {
            if (key > curr.key) {
                if (curr.right!= null) curr = curr.right;
                else {
                    curr.right = node;
                    node.parent = curr;
                    slay(node);
                    size++;
                    return null;
                }
            }
            else if (key < curr.key) {
                if (curr.left != null) curr = curr.left;
                else {
                    curr.left = node;
                    node.parent = curr;
                    slay(node);
                    size++;
                    return null;
                }
            }
            else {
                String res = curr.value;
                curr.value = value;
                slay(curr);
                return res;
            }
        }
    }

    @Override
    public String remove(Object key) {
        Node curr = root;
        while (curr != null) {
            if ((int)key < curr.key) {
                curr = curr.left;
            }
            else if ((int)key > curr.key) {
                curr = curr.right;
            }
            else {
                String res = curr.value;
                slay(curr);
                size--;
                root = merge(curr.left, curr.right);
                return res;
            }
        }
        return null;
    }

    @Override
    public String get(Object key) {
        return find_traverse(root, (int)key);
    }

    @Override
    public boolean containsKey(Object key) {
        return find_traverse(root, (int)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null && !(value instanceof String)) return false;
        return find_val_traverse(root, (String)value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException("toKey");
        MySplayMap res = new MySplayMap();
        Node[] st = new Node[size];
        int top = 0;
        Node curr = root;
        while (curr != null || top > 0) {
            while (curr != null) {
                st[top] = curr;
                top++;
                curr = curr.left;
            }
            top--;
            curr = st[top];

            if (curr.key >= toKey) break;
            res.put(curr.key, curr.value);
            curr = curr.right;
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException("toKey");
        MySplayMap res = new MySplayMap();
        Node[] st = new Node[size];
        int top = 0;
        Node curr = root;
        while (curr != null || top > 0) {
            while (curr != null) {
                st[top] = curr;
                top++;
                curr = curr.right;
            }
            top--;
            curr = st[top];

            if (curr.key < fromKey) break;
            res.put(curr.key, curr.value);
            curr = curr.left;
        }
        return res;
    }
    @Override
    public Integer firstKey() {
        if (size == 0) throw new NoSuchElementException();
        Node curr = root;
        while(curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (size == 0) throw new NoSuchElementException();
        Node curr = root;
        while(curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Integer res = null;
        Node curr = root;
        while (curr != null) {
            if (key <= curr.key) {
                curr = curr.left;

            } else {
                res = curr.key;
                curr = curr.right;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        Integer res = null;
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                curr = curr.left;
            }
            else if(key > curr.key) {
                res = curr.key;
                curr = curr.right;
            }
            else return curr.key;
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Integer res = null;
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                res = curr.key;
                curr = curr.left;
            }
            else if (key > curr.key){
                curr = curr.right;
            }
            else return curr.key;
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        Integer res = null;
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                res = curr.key;
                curr = curr.left;

            } else {
                curr = curr.right;
            }
        }
        return res;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        throw notImplemented();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw notImplemented();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw notImplemented();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw notImplemented();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw notImplemented();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw notImplemented();
    }

    @Override
    public NavigableMap<Integer, String> subMap(
            Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw notImplemented();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw notImplemented();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw notImplemented();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        throw notImplemented();
    }

    @Override
    public Set<Integer> keySet() {
        throw notImplemented();
    }

    @Override
    public Collection<String> values() {
        throw notImplemented();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw notImplemented();
    }
}
