package by.it.group551001.vinogradov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.max;

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
            height = 1;
        }
    }

    private Node root;
    private String last;
    private int size;

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    private String print_traverse(String s, Node r) {
        if (r == null) return s;
        s = print_traverse(s, r.left);
        if (s.length() > 1) s += ", ";
        s += r.key + "=" + r.value;
        return print_traverse(s, r.right);
    }

    private boolean find_traverse(Node r, int target) {
        if (r == null) return false;
        if (r.key > target) return find_traverse(r.left, target);
        if (r.key < target) return find_traverse(r.right, target);
        return true;
    }

    private String find_str_traverse(Node r, int target) {
        if (r == null) return null;
        if (r.key > target) return find_str_traverse(r.left, target);
        if (r.key < target) return find_str_traverse(r.right, target);
        return r.value;
    }

    private Node emplace_traverse(char c, Node r) {
        if (r == null) return null;

        if (c == 'l') {
            if (r.right == null) return r.left;
            r.right = emplace_traverse('l', r.right);
        } else {
            if (r.left == null) return r.right;
            r.left = emplace_traverse('r', r.left);
        }

        update_height(r);
        int lh = (r.left != null) ? r.left.height : 0;
        int rh = (r.right != null) ? r.right.height : 0;

        if (lh - rh > 1) {
            Node left = r.left;
            int llh = (left.left != null) ? left.left.height : 0;
            int lrh = (left.right != null) ? left.right.height : 0;
            if (llh >= lrh) r = small_right_balance(r);
            else r = big_right_balance(r);
        } else if (rh - lh > 1) {
            Node right = r.right;
            int rlh = (right.left != null) ? right.left.height : 0;
            int rrh = (right.right != null) ? right.right.height : 0;
            if (rrh >= rlh) r = small_left_balance(r);
            else r = big_left_balance(r);
        }
        return r;
    }

    private void update_height(Node r) {
        int lh = (r.left != null) ? r.left.height : 0;
        int rh = (r.right != null) ? r.right.height : 0;
        r.height = max(lh, rh) + 1;
    }

    private Node small_left_balance(Node r) {
        Node right = r.right;
        r.right = right.left;
        right.left = r;
        update_height(r);
        update_height(right);
        return right;
    }

    private Node big_left_balance(Node r) {
        Node right = r.right;
        Node right_left = right.left;
        r.right = right_left.left;
        right.left = right_left.right;
        right_left.left = r;
        right_left.right = right;
        update_height(r);
        update_height(right);
        update_height(right_left);
        return right_left;
    }

    private Node small_right_balance(Node r) {
        Node left = r.left;
        r.left = left.right;
        left.right = r;
        update_height(r);
        update_height(left);
        return left;
    }

    private Node big_right_balance(Node r) {
        Node left = r.left;
        Node left_right = left.right;
        r.left = left_right.right;
        left.right = left_right.left;
        left_right.right = r;
        left_right.left = left;
        update_height(r);
        update_height(left);
        update_height(left_right);
        return left_right;
    }

    private Node add(Node r, int target, String val) {
        if (r == null) {
            size++;
            return new Node(target, val);
        }

        if (target < r.key) {
            r.left = add(r.left, target, val);
        } else if (target > r.key) {
            r.right = add(r.right, target, val);
        } else {
            last = r.value;
            r.value = val;
            return r;
        }

        update_height(r);
        int lh = (r.left != null) ? r.left.height : 0;
        int rh = (r.right != null) ? r.right.height : 0;

        if (lh - rh > 1) {
            Node left = r.left;
            int llh = (left.left != null) ? left.left.height : 0;
            int lrh = (left.right != null) ? left.right.height : 0;
            if (llh >= lrh) r = small_right_balance(r);
            else r = big_right_balance(r);
        } else if (rh - lh > 1) {
            Node right = r.right;
            int rlh = (right.left != null) ? right.left.height : 0;
            int rrh = (right.right != null) ? right.right.height : 0;
            if (rrh >= rlh) r = small_left_balance(r);
            else r = big_left_balance(r);
        }
        return r;
    }

    private Node delete(Node r, int target) {
        if (r == null) return null;

        if (target < r.key) {
            r.left = delete(r.left, target);
        } else if (target > r.key) {
            r.right = delete(r.right, target);
        } else {
            last = r.value;
            size--;

            if (r.left == null) return r.right;
            if (r.right == null) return r.left;

            int lh = r.left.height;
            int rh = r.right.height;
            if (lh >= rh) {
                Node pred = r.left;
                while (pred.right != null) pred = pred.right;
                r.key = pred.key;
                r.value = pred.value;
                r.left = emplace_traverse('l', r.left);
            } else {
                Node succ = r.right;
                while (succ.left != null) succ = succ.left;
                r.key = succ.key;
                r.value = succ.value;
                r.right = emplace_traverse('r', r.right);
            }
        }

        update_height(r);
        int lh = (r.left != null) ? r.left.height : 0;
        int rh = (r.right != null) ? r.right.height : 0;

        if (lh - rh > 1) {
            Node left = r.left;
            int llh = (left.left != null) ? left.left.height : 0;
            int lrh = (left.right != null) ? left.right.height : 0;
            if (llh >= lrh) r = small_right_balance(r);
            else r = big_right_balance(r);
        } else if (rh - lh > 1) {
            Node right = r.right;
            int rlh = (right.left != null) ? right.left.height : 0;
            int rrh = (right.right != null) ? right.right.height : 0;
            if (rrh >= rlh) r = small_left_balance(r);
            else r = big_left_balance(r);
        }
        return r;
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
        if (key == null) throw new NullPointerException("key");
        last = null;
        root = add(root, key, value);
        return last;
    }

    @Override
    public String remove(Object key) {
        last = null;
        if (!(key instanceof Integer) || root == null) return null;

        int target = (Integer) key;
        root = delete(root, target);
        return last;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return find_str_traverse(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find_traverse(root, (Integer) key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
        last = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
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

