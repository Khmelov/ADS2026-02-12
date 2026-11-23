package by.it.group551003.kravchenkovikk.lesson12;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String>{

    private Node root;
    private int size;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    private static class Node {

        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private Node rightRotate(Node P) {
        Node L = P.left;
        P.left = L.right;
        L.right = P;
        P.height = Math.max(getHeight(P.left), getHeight(P.right)) + 1;
        L.height = Math.max(getHeight(L.left), getHeight(L.right)) + 1;
        return L;
    }

    private Node leftRotate(Node P) {
        Node R = P.right;
        P.right = R.left;
        R.left = P;
        P.height = Math.max(getHeight(P.left), getHeight(P.right)) + 1;
        R.height = Math.max(getHeight(R.left), getHeight(R.right)) + 1;
        return R;
    }

    private Node balance(Node node) {
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }
        return node;
    }

    private Node add(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node (key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = add(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = add(node.right, key, value);
        }
        return balance(node);
    }

    private Node erase(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) < 0) {
            node.left = erase(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = erase(node.right, key);
        } else {
            size--;
            if (node.left == null || node.right == null) {
                if (node.left != null) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            } else {
                Node min = node.right;
                while (min.left != null) {
                    min = min.left;
                }
                node.key = min.key;
                node.value = min.value;
                size++;
                node.right = erase(node.right, min.key);
            }
        }
        if (node == null) {
            return null;
        }
        return balance(node);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder str = new StringBuilder("{");
        Node[] stack = new Node [100];
        int top = - 1;
        Node curr = root;
        while (curr != null || top >= 0) {
            while (curr != null) {
                top++;
                stack[top] = curr;
                curr = curr.left;
            }
            curr = stack[top];
            top--;
            str.append(curr.key).append("=").append(curr.value).append(", ");
            curr = curr.right;
        }
        str.setLength(str.length() - 2);
        str.append("}");
        return str.toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node curr = root;
        while (curr != null) {
            if (key.compareTo(curr.key) < 0) {
                curr = curr.left;
            } else if (key.compareTo(curr.key) > 0) {
                curr = curr.right;
            } else {
                String oldValue = curr.value;
                curr.value = value;
                return oldValue;
            }
        }
        root = add(root, key, value);
        return null;
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            root = erase(root, (Integer) key);
        }
        return oldValue;
    }

    @Override
    public String get(Object key) {
        Node curr = root;
        while (curr != null) {
            if (curr.key.compareTo((Integer)key) < 0) {
                curr = curr.right;
            } else if (curr.key.compareTo((Integer)key) > 0) {
                curr = curr.left;
            } else {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        String isContains = get(key);
        if (isContains != null) {
            return true;
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

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
