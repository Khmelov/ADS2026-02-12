package by.it.group551001.bogush.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height = 1;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;
    private String oldValue; // служебное поле для put/remove
    private boolean found;   // служебное поле для put/remove

    // ---------- вспомогательные методы АВЛ ----------

    private int height(Node n) {
        if (n == null) {
            return 0;
        }
        return n.height;
    }

    private void update(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);
        if (hl > hr) {
            n.height = hl + 1;
        } else {
            n.height = hr + 1;
        }
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        update(y);
        update(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        return y;
    }

    private Node balance(Node n) {
        update(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        } else if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) {
            size++;
            found = false;
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else if (key > n.key) {
            n.right = insert(n.right, key, value);
        } else {
            oldValue = n.value;
            found = true;
            n.value = value;
            return n;
        }
        return balance(n);
    }

    private Node removeMin(Node n) {
        if (n.left == null) {
            return n.right;
        }
        n.left = removeMin(n.left);
        return balance(n);
    }

    private Node delete(Node n, int key) {
        if (n == null) {
            return null;
        }
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            found = true;
            oldValue = n.value;
            size--;
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }
            Node min = n.right;
            while (min.left != null) {
                min = min.left;
            }
            min.right = removeMin(n.right);
            min.left = n.left;
            return balance(min);
        }
        return balance(n);
    }

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inOrder(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        inOrder(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null) {
            if (n.value == null) {
                return true;
            }
        } else if (value.equals(n.value)) {
            return true;
        }
        if (hasValue(n.left, value)) {
            return true;
        }
        return hasValue(n.right, value);
    }

    // ---------- обязательные методы ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        found = false;
        oldValue = null;
        root = insert(root, key, value);
        if (found) {
            return oldValue;
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        found = false;
        oldValue = null;
        root = delete(root, (Integer) key);
        if (found) {
            return oldValue;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        if (n == null) {
            return null;
        }
        return n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
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

    // ---------- остальное из интерфейса Map (на уровень А не нужно) ----------

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}