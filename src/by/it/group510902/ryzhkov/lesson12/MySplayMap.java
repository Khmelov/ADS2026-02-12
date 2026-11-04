package by.it.group510902.ryzhkov.lesson12;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root = null;
    private int size = 0;

    private void leftRotate(Node x) {
        Node y = x.right;
        if (y == null)
            return;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        if (y == null)
            return;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null)
            return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) {
                    rightRotate(p);
                } else {
                    leftRotate(p);
                }
            } else if (x == p.left && p == g.left) {
                rightRotate(g);
                rightRotate(p);
            } else if (x == p.right && p == g.right) {
                leftRotate(g);
                leftRotate(p);
            } else if (x == p.right && p == g.left) {
                leftRotate(p);
                rightRotate(g);
            } else {
                rightRotate(p);
                leftRotate(g);
            }
        }
    }

    private Node findNode(Integer key) {
        if (root == null)
            return null;
        Node curr = root;
        Node lastNonNull = root;
        while (curr != null) {
            lastNonNull = curr;
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                splay(curr);
                return curr;
            }
        }
        splay(lastNonNull);
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node curr = root;
        Node parent = null;
        int cmp = 0;
        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                String old = curr.value;
                curr.value = value;
                splay(curr);
                return old;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode);
        size++;
        return null;
    }

    public String get(Integer key) {
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    public String remove(Integer key) {
        Node node = findNode(key);
        if (node == null)
            return null;

        String oldVal = node.value;
        if (node.left == null) {
            root = node.right;
            if (root != null)
                root.parent = null;
        } else {
            Node maxLeft = node.left;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft); // maxLeft становится корнем левого поддерева
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;
            root.parent = null;
        }
        size--;
        return oldVal;
    }

    public boolean containsKey(Integer key) {
        return findNode(key) != null;
    }

    public boolean containsValue(String value) {
        return containsValueInTree(root, value);
    }

    private boolean containsValueInTree(Node node, String value) {
        if (node == null)
            return false;
        if (value == null && node.value == null)
            return true;
        if (value != null && value.equals(node.value))
            return true;
        return containsValueInTree(node.left, value) || containsValueInTree(node.right, value);
    }

    public Integer firstKey() {
        if (root == null)
            return null;
        Node curr = root;
        while (curr.left != null)
            curr = curr.left;
        splay(curr);
        return curr.key;
    }

    public Integer lastKey() {
        if (root == null)
            return null;
        Node curr = root;
        while (curr.right != null)
            curr = curr.right;
        splay(curr);
        return curr.key;
    }

    public Integer lowerKey(Integer key) {
        if (key == null)
            return null;
        Node curr = root;
        Node res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) < 0) {
                res = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (res != null)
            splay(res);
        return res == null ? null : res.key;
    }

    public Integer floorKey(Integer key) {
        if (key == null)
            return null;
        Node curr = root;
        Node res = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp == 0) {
                splay(curr);
                return curr.key;
            } else if (cmp < 0) {
                res = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (res != null)
            splay(res);
        return res == null ? null : res.key;
    }

    public Integer ceilingKey(Integer key) {
        if (key == null)
            return null;
        Node curr = root;
        Node res = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp == 0) {
                splay(curr);
                return curr.key;
            } else if (cmp > 0) {
                res = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (res != null)
            splay(res);
        return res == null ? null : res.key;
    }

    public Integer higherKey(Integer key) {
        if (key == null)
            return null;
        Node curr = root;
        Node res = null;
        while (curr != null) {
            if (curr.key.compareTo(key) > 0) {
                res = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (res != null)
            splay(res);
        return res == null ? null : res.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        java.util.TreeMap<Integer, String> subMap = new java.util.TreeMap<>();
        fillSubMapStandard(root, subMap, null, toKey);
        return subMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        java.util.TreeMap<Integer, String> subMap = new java.util.TreeMap<>();
        fillSubMapStandard(root, subMap, fromKey, null);
        return subMap;
    }

    private void fillSubMapStandard(Node current, java.util.TreeMap<Integer, String> target, Integer from, Integer to) {
        // В Splay-дереве вместо проверки на NIL используется обычный null
        if (current == null)
            return;

        fillSubMapStandard(current.left, target, from, to);

        boolean tailRight = (from == null || current.key.compareTo(from) >= 0);
        boolean headRight = (to == null || current.key.compareTo(to) < 0);

        if (tailRight && headRight) {
            target.put(current.key, current.value);
        }

        fillSubMapStandard(current.right, target, from, to);
    }

    public String toString() {
        if (root == null)
            return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void buildToString(Node node, StringBuilder sb) {
        if (node == null)
            return;
        buildToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        buildToString(node.right, sb);
    }

    // необязательные методы
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    public boolean containsValue(Object value) {
        return containsValueInTree(root, null);
    }

    public String get(Object key) {
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    public String remove(Object key) {
        Node node = findNode((Integer) key);
        if (node == null)
            return null;
        String oldVal = node.value;
        if (node.left == null) {
            root = node.right;
            if (root != null)
                root.parent = null;
        } else {
            Node maxLeft = node.left;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;
            root.parent = null;
        }
        size--;
        return oldVal;
    }

    // заглушки
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    public java.util.Comparator<? super Integer> comparator() {
        return null;
    }

    public java.util.Set<Integer> keySet() {
        return null;
    }

    public java.util.Collection<String> values() {
        return null;
    }

    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        return null;
    }

    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    public Map.Entry<Integer, String> firstEntry() {
        return null;
    }

    public Map.Entry<Integer, String> lastEntry() {
        return null;
    }

    public Map.Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    public Map.Entry<Integer, String> pollLastEntry() {
        return null;
    }

    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey,
            boolean toInclusive) {
        return null;
    }

    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

}
