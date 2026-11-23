package by.it.group551001.artobolevskiy.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        int key;
        String value;
        int height;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    Node root;
    int size;

    int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    int get_balance(Node node){
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    void update_height(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    Node right_rotate(Node node) {
        Node left = node.left;
        Node rightChildOfLeft = left.right;

        left.right = node;
        node.left = rightChildOfLeft;

        update_height(node);
        update_height(left);

        return left;
    }

    Node left_rotate(Node node) {
        Node right = node.right;
        Node leftChildOfRight = right.left;

        right.left = node;
        node.right = leftChildOfRight;

        update_height(node);
        update_height(right);

        return right;
    }

    Node balance(Node node) {
        if (node == null) return null;

        update_height(node);
        int balance = get_balance(node);

        if (balance > 1 && get_balance(node.left) >= 0) {
            return right_rotate(node);
        }

        if (balance > 1 && get_balance(node.left) < 0) {
            node.left = left_rotate(node.left);
            return right_rotate(node);
        }

        if (balance < -1 && get_balance(node.right) <= 0) {
            return left_rotate(node);
        }

        if (balance < -1 && get_balance(node.right) > 0) {
            node.right = right_rotate(node.right);
            return left_rotate(node);
        }

        return node;
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
        if(isEmpty()) {
            return "{}";
        }

        StringBuilder s = new StringBuilder();
        s.append("{");
        dfs_inorder(root, s);
        s.setLength(s.length() - 2);
        s.append("}");
        return s.toString();
    }

    void dfs_inorder(Node node, StringBuilder s) {
        if(node == null) {
            return;
        }
        dfs_inorder(node.left, s);
        s.append(node.key).append("=").append(node.value).append(", ");
        dfs_inorder(node.right, s);
    }

    @Override
    public boolean containsKey(Object key) {
        if(!(key instanceof Integer)) {
            return false;
        }
        return get(key) != null;
    }

    @Override
    public String get(Object key) {
        if(!(key instanceof Integer)) {
            return null;
        }

        int looking_for = (int) key;
        Node current = root;
        while (current != null) {
            if (current.key == looking_for) {
                return current.value;
            }
            else if (looking_for < current.key) {
                current = current.left;
            }
            else {
                current = current.right;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if(key == null) {
            return null;
        }

        String[] cont_for_old_val = new String[1];
        root = put(root, key, value, cont_for_old_val);

        return cont_for_old_val[0];
    }

    private Node put(Node node, int key, String value, String[] container) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key == node.key) {
            container[0] = node.value;
            node.value = value;
            return node;
        } else if (key < node.key) {
            node.left = put(node.left, key, value, container);
        } else {
            node.right = put(node.right, key, value, container);
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int looking_for = (Integer) key;
        String[] cont_for_old_val = new String[1];
        root = remove(root, looking_for, cont_for_old_val);

        return cont_for_old_val[0];
    }

    private Node remove(Node node, int key, String[] container) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = remove(node.left, key, container);
        } else if (key > node.key) {
            node.right = remove(node.right, key, container);
        } else {
            container[0] = node.value;
            size--;

            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                size++;
                node.right = remove(node.right, minNode.key, new String[1]);
            }
        }

        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
