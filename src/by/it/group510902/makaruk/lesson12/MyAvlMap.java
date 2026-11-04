package by.it.group510902.makaruk.lesson12;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

    public class MyAvlMap implements Map<Integer, String> {

        private static class Node {
            Integer key;
            String value;
            Node left, right;
            int height = 1;
            Node(Integer k, String v) { key = k; value = v; }
        }

        private Node root;
        private int size = 0;

        private int height(Node n) { return n == null ? 0 : n.height; }
        private int balanceFactor(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

        private Node rotateRight(Node y) {
            Node x = y.left;
            y.left = x.right;
            x.right = y;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            return y;
        }

        // Универсальный метод балансировки узла (заменяет громоздкие проверки 4-х случаев)
        private Node balance(Node n) {
            n.height = Math.max(height(n.left), height(n.right)) + 1;
            int bf = balanceFactor(n);

            if (bf > 1) {
                if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
                return rotateRight(n);
            }
            if (bf < -1) {
                if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
                return rotateLeft(n);
            }
            return n;
        }

        @Override
        public String put(Integer key, String value) {
            if (key == null) throw new IllegalArgumentException();
            String old = get(key);
            root = insert(root, key, value);
            if (old == null) size++;
            return old;
        }

        private Node insert(Node n, Integer key, String value) {
            if (n == null) return new Node(key, value);
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n.left = insert(n.left, key, value);
            else if (cmp > 0) n.right = insert(n.right, key, value);
            else { n.value = value; return n; }
            return balance(n);
        }

        @Override
        public String remove(Object keyObj) {
            if (!(keyObj instanceof Integer)) return null;
            Integer key = (Integer) keyObj;
            String old = get(key);
            if (old != null) {
                root = remove(root, key);
                size--;
            }
            return old;
        }

        private Node remove(Node n, Integer key) {
            if (n == null) return null;
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n.left = remove(n.left, key);
            else if (cmp > 0) n.right = remove(n.right, key);
            else {
                if (n.left == null) return n.right;
                if (n.right == null) return n.left;
                Node min = findMin(n.right);
                n.key = min.key;
                n.value = min.value;
                n.right = remove(n.right, min.key);
            }
            return balance(n);
        }

        private Node findMin(Node n) {
            return n.left == null ? n : findMin(n.left);
        }

        @Override
        public String get(Object keyObj) {
            if (!(keyObj instanceof Integer)) return null;
            Integer key = (Integer) keyObj;
            Node curr = root;
            while (curr != null) {
                int cmp = key.compareTo(curr.key);
                if (cmp < 0) curr = curr.left;
                else if (cmp > 0) curr = curr.right;
                else return curr.value;
            }
            return null;
        }

        @Override
        public boolean containsKey(Object keyObj) {
            return get(keyObj) != null;
        }

        @Override
        public int size() { return size; }

        @Override
        public boolean isEmpty() { return size == 0; }

        @Override
        public void clear() { root = null; size = 0; }

        @Override
        public String toString() {
            if (isEmpty()) return "{}";
            StringBuilder sb = new StringBuilder("{");
            inOrder(root, sb);
            return sb.append("}").toString();
        }

        private void inOrder(Node n, StringBuilder sb) {
            if (n == null) return;
            inOrder(n.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(n.key).append("=").append(n.value);
            inOrder(n.right, sb);
        }

        // Остальные методы интерфейса Map (заглушки)
        @Override public boolean containsValue(Object v) { throw new UnsupportedOperationException(); }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) {
            for(Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
        }
        @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
        @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
        @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    }

