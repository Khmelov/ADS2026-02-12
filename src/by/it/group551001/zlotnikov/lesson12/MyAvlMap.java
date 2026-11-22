package by.it.group551001.zlotnikov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        int lh = height(n.left);
        int rh = height(n.right);
        n.height = 1 + (lh > rh ? lh : rh);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int bf = height(n.left) - height(n.right);
        if (bf > 1) {
            if (height(n.left.left) < height(n.left.right)) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (height(n.right.right) < height(n.right.left)) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node putRec(Node n, Integer key, String value, String[] old) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = putRec(n.left, key, value, old);
        } else if (cmp > 0) {
            n.right = putRec(n.right, key, value, old);
        } else {
            old[0] = n.value;
            n.value = value;
            return n;
        }
        return rebalance(n);
    }

    private Node removeRec(Node n, Integer key, String[] old) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = removeRec(n.left, key, old);
        } else if (cmp > 0) {
            n.right = removeRec(n.right, key, old);
        } else {
            old[0] = n.value;
            if (n.left == null) {
                size--;
                return n.right;
            }
            if (n.right == null) {
                size--;
                return n.left;
            }
            Node min = n.right;
            while (min.left != null) {
                min = min.left;
            }
            n.key = min.key;
            n.value = min.value;
            String[] dummy = new String[1];
            n.right = removeRec(n.right, min.key, dummy);
        }
        return rebalance(n);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    private boolean equalsRec(Node n, Map<?, ?> m) {
        if (n == null) {
            return true;
        }
        Object v = m.get(n.key);
        if (v == null && !m.containsKey(n.key)) {
            return false;
        }
        if (v == null ? n.value != null : !v.equals(n.value)) {
            return false;
        }
        return equalsRec(n.left, m) && equalsRec(n.right, m);
    }

    private int hashRec(Node n) {
        if (n == null) {
            return 0;
        }
        int h = (n.key == null ? 0 : n.key.hashCode()) ^ (n.value == null ? 0 : n.value.hashCode());
        return h + hashRec(n.left) + hashRec(n.right);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "{";
        Node[] stack = new Node[32];
        int top = 0;
        Node cur = root;
        boolean first = true;
        while (cur != null || top > 0) {
            while (cur != null) {
                if (top == stack.length) {
                    Node[] grown = new Node[stack.length * 2];
                    for (int i = 0; i < top; i++) {
                        grown[i] = stack[i];
                    }
                    stack = grown;
                }
                stack[top++] = cur;
                cur = cur.left;
            }
            cur = stack[--top];
            if (!first) {
                result += ", ";
            }
            result += cur.key + "=" + cur.value;
            first = false;
            cur = cur.right;
        }
        return result + "}";
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        String[] old = new String[1];
        root = putRec(root, key, value, old);
        return old[0];
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        String[] old = new String[1];
        root = removeRec(root, (Integer) key, old);
        return old[0];
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node n = root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        Integer k = (Integer) key;
        Node n = root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return true;
            }
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

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Map                          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size) {
            return false;
        }
        return equalsRec(root, m);
    }

    @Override
    public int hashCode() {
        return hashRec(root);
    }
}