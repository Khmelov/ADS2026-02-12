package by.it.group551004.gavrusev.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        MyAvlMap.Node left;
        MyAvlMap.Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private MyAvlMap.Node root = null;
    private int size = 0;
    private String oldValue = null;

    private int height(MyAvlMap.Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(MyAvlMap.Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(MyAvlMap.Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private MyAvlMap.Node rotateRight(MyAvlMap.Node y) {
        MyAvlMap.Node x = y.left;
        MyAvlMap.Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private MyAvlMap.Node rotateLeft(MyAvlMap.Node x) {
        MyAvlMap.Node y = x.right;
        MyAvlMap.Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private MyAvlMap.Node balance(MyAvlMap.Node node) {
        if (node == null) {
            return null;
        }
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private MyAvlMap.Node findMin(MyAvlMap.Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private MyAvlMap.Node deleteMin(MyAvlMap.Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(MyAvlMap.Node node, StringBuilder sb) {
        if (node != null) {
            inOrder(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrder(node.right, sb);
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        Integer k = (Integer) key;
        MyAvlMap.Node curr = root;
        while (curr != null) {
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        MyAvlMap.Node curr = root;
        while (curr != null) {
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = put(root, key, value);
        return oldValue;
    }

    private MyAvlMap.Node put(MyAvlMap.Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new MyAvlMap.Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            oldValue = node.value;
            node.value = value;
            return node;
        }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        oldValue = null;
        root = remove(root, (Integer) key);
        return oldValue;
    }

    private MyAvlMap.Node remove(MyAvlMap.Node node, Integer key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            oldValue = node.value;
            size--;
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            MyAvlMap.Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = deleteMin(node.right);
        }
        return balance(node);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map<?, ?> m)) {
            return false;
        }
        if (m.size() != size) {
            return false;
        }
        return checkEquals(root, m);
    }

    private boolean checkEquals(MyAvlMap.Node node, Map<?, ?> m) {
        if (node == null) {
            return true;
        }
        Object val = m.get(node.key);
        if (val == null ? (node.value != null || !m.containsKey(node.key)) : !val.equals(node.value)) {
            return false;
        }
        return checkEquals(node.left, m) && checkEquals(node.right, m);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Map              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
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
