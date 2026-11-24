package by.it.group551002.litovchenko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
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
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
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
                if ((p.left == x) == (g.left == p)) {
                    if (p.left == x) {
                        rotateRight(g);
                        rotateRight(p);
                    } else {
                        rotateLeft(g);
                        rotateLeft(p);
                    }
                } else {
                    if (p.left == x) {
                        rotateRight(p);
                        rotateLeft(g);
                    } else {
                        rotateLeft(p);
                        rotateRight(g);
                    }
                }
            }
        }
    }

    private Node findNode(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        Integer k = (Integer) key;
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                splay(curr);
                return curr;
            }
        }
        if (last != null) {
            splay(last);
        }
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
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
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
                splay(curr);
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode(key);
        if (node == null) return null;

        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else if (root.right == null) {
            root = root.left;
            if (root != null) root.parent = null;
        } else {
            Node leftTree = root.left;
            leftTree.parent = null;
            Node rightTree = root.right;
            rightTree.parent = null;

            Node maxLeft = leftTree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            root = leftTree;
            splay(maxLeft);
            root.right = rightTree;
            rightTree.parent = root;
        }
        size--;
        return oldValue;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node curr = root;
        Node best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) < 0) {
                best = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp <= 0) {
                best = curr;
                if (cmp == 0) break;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp >= 0) {
                best = curr;
                if (cmp == 0) break;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node curr = root;
        Node best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) > 0) {
                best = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap map = new MySplayMap();
        fillHeadMap(root, toKey, inclusive, map);
        return map;
    }

    private void fillHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            fillHeadMap(node.left, toKey, inclusive, map);
            map.put(node.key, node.value);
            fillHeadMap(node.right, toKey, inclusive, map);
        } else {
            fillHeadMap(node.left, toKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap map = new MySplayMap();
        fillTailMap(root, fromKey, inclusive, map);
        return map;
    }

    private void fillTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            fillTailMap(node.left, fromKey, inclusive, map);
            map.put(node.key, node.value);
            fillTailMap(node.right, fromKey, inclusive, map);
        } else {
            fillTailMap(node.right, fromKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        MySplayMap map = new MySplayMap();
        fillSubMap(root, fromKey, fromInclusive, toKey, toInclusive, map);
        return map;
    }

    private void fillSubMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap map) {
        if (node == null) return;
        int cmpFrom = node.key.compareTo(fromKey);
        int cmpTo = node.key.compareTo(toKey);
        boolean geFrom = cmpFrom > 0 || (fromInclusive && cmpFrom == 0);
        boolean leTo = cmpTo < 0 || (toInclusive && cmpTo == 0);
        if (geFrom && leTo) {
            fillSubMap(node.left, fromKey, fromInclusive, toKey, toInclusive, map);
            map.put(node.key, node.value);
            fillSubMap(node.right, fromKey, fromInclusive, toKey, toInclusive, map);
        } else if (!geFrom) {
            fillSubMap(node.right, fromKey, fromInclusive, toKey, toInclusive, map);
        } else {
            fillSubMap(node.left, fromKey, fromInclusive, toKey, toInclusive, map);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
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
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrder(node.right, sb);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) return false;
        return containsAllEntries(root, m);
    }

    private boolean containsAllEntries(Node node, Map<?, ?> m) {
        if (node == null) return true;
        Object val = m.get(node.key);
        if (!Objects.equals(node.value, val)) return false;
        if (val == null && !m.containsKey(node.key)) return false;
        return containsAllEntries(node.left, m) && containsAllEntries(node.right, m);
    }

    @Override
    public int hashCode() {
        return hashCode(root);
    }

    private int hashCode(Node node) {
        if (node == null) return 0;
        int h = Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
        return h + hashCode(node.left) + hashCode(node.right);
    }
}
