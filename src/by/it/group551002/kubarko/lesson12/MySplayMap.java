package by.it.group551002.kubarko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.NavigableSet;

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
    private int size;

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != null) {
            pivot.left.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == null) {
            root = pivot;
        } else if (node == node.parent.left) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        pivot.left = node;
        node.parent = pivot;
    }

    private void rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != null) {
            pivot.right.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == null) {
            root = pivot;
        } else if (node == node.parent.right) {
            node.parent.right = pivot;
        } else {
            node.parent.left = pivot;
        }
        pivot.right = node;
        node.parent = pivot;
    }

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
    }

    private Node findAndSplay(Integer key) {
        Node current = root;
        Node last = null;
        while (current != null) {
            last = current;
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                splay(current);
                return current;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    private Node extreme(Node node, boolean maximum) {
        while (maximum ? node.right != null : node.left != null) {
            node = maximum ? node.right : node.left;
        }
        return node;
    }

    private Node neighbor(Integer key, int mode) {
        Node current = root;
        Node last = null;
        Node candidate = null;

        while (current != null) {
            last = current;
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                if (mode == 0) {
                    candidate = current.left == null ? candidate : extreme(current.left, true);
                } else if (mode == 1) {
                    candidate = current;
                } else if (mode == 2) {
                    candidate = current;
                } else {
                    candidate = current.right == null ? candidate : extreme(current.right, false);
                }
                break;
            }
            if (comparison < 0) {
                if (mode >= 2) {
                    candidate = current;
                }
                current = current.left;
            } else {
                if (mode <= 1) {
                    candidate = current;
                }
                current = current.right;
            }
        }

        if (candidate != null) {
            splay(candidate);
        } else if (last != null) {
            splay(last);
        }
        return candidate;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        Node[] stack = new Node[size];
        int top = 0;
        Node current = root;
        boolean first = true;
        while (current != null || top > 0) {
            while (current != null) {
                stack[top++] = current;
                current = current.left;
            }
            current = stack[--top];
            if (!first) {
                result.append(", ");
            }
            result.append(current.key).append("=").append(current.value);
            first = false;
            current = current.right;
        }
        return result.append("}").toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node existing = findAndSplay(key);
        if (existing != null) {
            String previous = existing.value;
            existing.value = value;
            return previous;
        }

        Node added = new Node(key, value, null);
        if (root == null) {
            root = added;
        } else if (key < root.key) {
            added.left = root.left;
            if (added.left != null) {
                added.left.parent = added;
            }
            added.right = root;
            root.parent = added;
            root.left = null;
        } else {
            added.right = root.right;
            if (added.right != null) {
                added.right.parent = added;
            }
            added.left = root;
            root.parent = added;
            root.right = null;
        }
        root = added;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node removed = findAndSplay(integerKey);
        if (removed == null) {
            return null;
        }

        String previous = removed.value;
        Node left = removed.left;
        Node right = removed.right;
        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }
        removed.left = null;
        removed.right = null;

        if (left == null) {
            root = right;
        } else {
            root = left;
            Node maximum = extreme(left, true);
            splay(maximum);
            root.right = right;
            if (right != null) {
                right.parent = root;
            }
        }
        size--;
        return previous;
    }

    @Override
    public String get(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node node = findAndSplay(integerKey);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        return findAndSplay(integerKey) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node[] stack = new Node[size];
        int top = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) {
                stack[top++] = current;
                current = current.left;
            }
            current = stack[--top];
            if (value == null ? current.value == null : value.equals(current.value)) {
                return true;
            }
            current = current.right;
        }
        return false;
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
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        copyRange(result, toKey, true);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        copyRange(result, fromKey, false);
        return result;
    }

    private void copyRange(MySplayMap result, Integer bound, boolean head) {
        Node[] stack = new Node[size];
        int top = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) {
                stack[top++] = current;
                current = current.left;
            }
            current = stack[--top];
            if (head ? current.key < bound : current.key >= bound) {
                result.put(current.key, current.value);
            }
            current = current.right;
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node first = extreme(root, false);
        splay(first);
        return first.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node last = extreme(root, true);
        splay(last);
        return last.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = neighbor(key, 0);
        return node == null ? null : node.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = neighbor(key, 1);
        return node == null ? null : node.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = neighbor(key, 2);
        return node == null ? null : node.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = neighbor(key, 3);
        return node == null ? null : node.key;
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                 Integer toKey, boolean toInclusive) {
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
    public void putAll(Map<? extends Integer, ? extends String> map) {
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
}
