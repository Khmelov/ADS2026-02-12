package by.it.group551001.Barusevich.lesson12;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

final class IntStringTree {
    static final int AVL = 0;
    static final int RED_BLACK = 1;
    static final int SPLAY = 2;

    private final int mode;
    private Node root;
    private int size;

    IntStringTree(int mode) {
        this.mode = mode;
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }

    void clear() {
        root = null;
        size = 0;
    }

    boolean containsKey(Integer key) {
        requireKey(key);
        Node found = findNode(key);
        if (mode == SPLAY && found != null) splay(found);
        return found != null;
    }

    String get(Integer key) {
        requireKey(key);
        Node found = findNode(key);
        if (mode == SPLAY && found != null) splay(found);
        return found == null ? null : found.value;
    }

    String put(Integer key, String value) {
        requireKey(key);
        if (mode == AVL) return putAvl(key, value);
        if (mode == RED_BLACK) return putRedBlack(key, value);
        return putSplay(key, value);
    }

    String remove(Integer key) {
        requireKey(key);
        if (mode == AVL) return removeAvl(key);
        if (mode == RED_BLACK) return removeRedBlack(key);
        return removeSplay(key);
    }

    boolean containsValue(String value) {
        int[] keys = keys(false);
        for (int key : keys) {
            if (equal(value, getWithoutSplay(key))) return true;
        }
        return false;
    }

    Integer firstKey() {
        Node n = minimum(root);
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    Integer lastKey() {
        Node n = maximum(root);
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    Integer lowerKey(Integer key) {
        requireKey(key);
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (current.key < key) {
                result = current.key;
                current = current.right;
            } else current = current.left;
        }
        return result;
    }

    Integer floorKey(Integer key) {
        requireKey(key);
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (current.key <= key) {
                result = current.key;
                current = current.right;
            } else current = current.left;
        }
        return result;
    }

    Integer ceilingKey(Integer key) {
        requireKey(key);
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (current.key >= key) {
                result = current.key;
                current = current.left;
            } else current = current.right;
        }
        return result;
    }

    Integer higherKey(Integer key) {
        requireKey(key);
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (current.key > key) {
                result = current.key;
                current = current.left;
            } else current = current.right;
        }
        return result;
    }

    int[] keys(boolean descending) {
        int[] result = new int[size];
        int[] stack = new int[Math.max(1, size)];
        Node[] nodes = new Node[Math.max(1, size)];
        int top = 0;
        int count = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) {
                nodes[top++] = current;
                current = descending ? current.right : current.left;
            }
            current = nodes[--top];
            result[count++] = current.key;
            current = descending ? current.left : current.right;
        }
        if (count != result.length) {
            int[] exact = new int[count];
            System.arraycopy(result, 0, exact, 0, count);
            return exact;
        }
        return result;
    }

    Set<Map.Entry<Integer, String>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public int size() {
                return IntStringTree.this.size;
            }

            @Override
            public void clear() {
                IntStringTree.this.clear();
            }

            @Override
            public Iterator<Map.Entry<Integer, String>> iterator() {
                int[] snapshot = keys(false);
                return new Iterator<>() {
                    int index;
                    Integer last;

                    @Override
                    public boolean hasNext() {
                        return index < snapshot.length;
                    }

                    @Override
                    public Map.Entry<Integer, String> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        last = snapshot[index++];
                        return new TreeEntry(IntStringTree.this, last);
                    }

                    @Override
                    public void remove() {
                        if (last == null) throw new IllegalStateException();
                        IntStringTree.this.remove(last);
                        last = null;
                    }
                };
            }

            @Override
            public boolean contains(Object object) {
                if (!(object instanceof Map.Entry<?, ?> entry) || !(entry.getKey() instanceof Integer key)) return false;
                Node node = findNode(key);
                return node != null && equal(node.value, entry.getValue());
            }

            @Override
            public boolean remove(Object object) {
                if (!contains(object)) return false;
                IntStringTree.this.remove((Integer) ((Map.Entry<?, ?>) object).getKey());
                return true;
            }
        };
    }

    Set<Integer> keySet() {
        return new AbstractSet<>() {
            @Override
            public int size() {
                return IntStringTree.this.size;
            }

            @Override
            public boolean contains(Object key) {
                return key instanceof Integer integer && containsKey(integer);
            }

            @Override
            public boolean remove(Object key) {
                if (!(key instanceof Integer integer) || !containsKey(integer)) return false;
                IntStringTree.this.remove(integer);
                return true;
            }

            @Override
            public void clear() {
                IntStringTree.this.clear();
            }

            @Override
            public Iterator<Integer> iterator() {
                int[] snapshot = keys(false);
                return new Iterator<>() {
                    int index;
                    Integer last;

                    @Override
                    public boolean hasNext() {
                        return index < snapshot.length;
                    }

                    @Override
                    public Integer next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        last = snapshot[index++];
                        return last;
                    }

                    @Override
                    public void remove() {
                        if (last == null) throw new IllegalStateException();
                        IntStringTree.this.remove(last);
                        last = null;
                    }
                };
            }
        };
    }

    Collection<String> values() {
        return new AbstractCollection<>() {
            @Override
            public int size() {
                return IntStringTree.this.size;
            }

            @Override
            public boolean contains(Object value) {
                return value instanceof String string ? containsValue(string) : value == null && containsNullValue();
            }

            @Override
            public void clear() {
                IntStringTree.this.clear();
            }

            @Override
            public Iterator<String> iterator() {
                int[] snapshot = keys(false);
                return new Iterator<>() {
                    int index;
                    Integer last;

                    @Override
                    public boolean hasNext() {
                        return index < snapshot.length;
                    }

                    @Override
                    public String next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        last = snapshot[index++];
                        return getWithoutSplay(last);
                    }

                    @Override
                    public void remove() {
                        if (last == null) throw new IllegalStateException();
                        IntStringTree.this.remove(last);
                        last = null;
                    }
                };
            }
        };
    }

    String mapString(boolean descending) {
        StringBuilder out = new StringBuilder("{");
        int[] ordered = keys(descending);
        for (int i = 0; i < ordered.length; i++) {
            if (i > 0) out.append(", ");
            out.append(ordered[i]).append('=').append(getWithoutSplay(ordered[i]));
        }
        return out.append('}').toString();
    }

    IntStringTree copyRange(Integer low, boolean lowInclusive, Integer high, boolean highInclusive) {
        IntStringTree copy = new IntStringTree(mode);
        int[] ordered = keys(false);
        for (int key : ordered) {
            if (low != null && (key < low || (!lowInclusive && key == low))) continue;
            if (high != null && (key > high || (!highInclusive && key == high))) continue;
            copy.put(key, getWithoutSplay(key));
        }
        return copy;
    }

    private boolean containsNullValue() {
        int[] ordered = keys(false);
        for (int key : ordered) if (getWithoutSplay(key) == null) return true;
        return false;
    }

    private String getWithoutSplay(int key) {
        Node n = findNode(key);
        return n == null ? null : n.value;
    }

    private static void requireKey(Integer key) {
        if (key == null) throw new NullPointerException("null key");
    }

    private static boolean equal(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }

    private Node findNode(int key) {
        Node current = root;
        while (current != null) {
            if (key == current.key) return current;
            current = key < current.key ? current.left : current.right;
        }
        return null;
    }

    private String putAvl(int key, String value) {
        Node existing = findNode(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insertAvl(root, key, value, null);
        size++;
        return null;
    }

    private Node insertAvl(Node node, int key, String value, Node parent) {
        if (node == null) return new Node(key, value, parent);
        if (key < node.key) node.left = insertAvl(node.left, key, value, node);
        else node.right = insertAvl(node.right, key, value, node);
        updateHeight(node);
        Node balanced = balanceAvl(node);
        balanced.parent = parent;
        return balanced;
    }

    private String removeAvl(int key) {
        Node existing = findNode(key);
        if (existing == null) return null;
        String old = existing.value;
        root = deleteAvl(root, key);
        if (root != null) root.parent = null;
        size--;
        return old;
    }

    private Node deleteAvl(Node node, int key) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = deleteAvl(node.left, key);
            if (node.left != null) node.left.parent = node;
        } else if (key > node.key) {
            node.right = deleteAvl(node.right, key);
            if (node.right != null) node.right.parent = node;
        } else {
            if (node.left == null) {
                Node right = node.right;
                if (right != null) right.parent = node.parent;
                return right;
            }
            if (node.right == null) {
                Node left = node.left;
                if (left != null) left.parent = node.parent;
                return left;
            }
            Node successor = minimum(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = deleteAvl(node.right, successor.key);
            if (node.right != null) node.right.parent = node;
        }
        updateHeight(node);
        Node balanced = balanceAvl(node);
        return balanced;
    }

    private static int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private static void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private static int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node balanceAvl(Node node) {
        int factor = balanceFactor(node);
        if (factor == 2) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
                if (node.right != null) node.right.parent = node;
            }
            return rotateLeft(node);
        }
        if (factor == -2) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
                if (node.left != null) node.left.parent = node;
            }
            return rotateRight(node);
        }
        return node;
    }

    private Node rotateLeft(Node node) {
        Node pivot = node.right;
        Node parent = node.parent;
        node.right = pivot.left;
        if (node.right != null) node.right.parent = node;
        pivot.left = node;
        node.parent = pivot;
        pivot.parent = parent;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private Node rotateRight(Node node) {
        Node pivot = node.left;
        Node parent = node.parent;
        node.left = pivot.right;
        if (node.left != null) node.left.parent = node;
        pivot.right = node;
        node.parent = pivot;
        pivot.parent = parent;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private String putRedBlack(int key, String value) {
        Node parent = null;
        Node current = root;
        while (current != null) {
            parent = current;
            if (key == current.key) {
                String old = current.value;
                current.value = value;
                return old;
            }
            current = key < current.key ? current.left : current.right;
        }
        Node inserted = new Node(key, value, parent);
        inserted.red = true;
        if (parent == null) root = inserted;
        else if (key < parent.key) parent.left = inserted;
        else parent.right = inserted;
        size++;
        fixInsert(inserted);
        return null;
    }

    private void fixInsert(Node node) {
        while (node != root && isRed(node.parent)) {
            Node parent = node.parent;
            Node grand = parent.parent;
            if (parent == grand.left) {
                Node uncle = grand.right;
                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;
                    node = grand;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeftRb(node);
                        parent = node.parent;
                        grand = parent.parent;
                    }
                    parent.red = false;
                    grand.red = true;
                    rotateRightRb(grand);
                }
            } else {
                Node uncle = grand.left;
                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;
                    node = grand;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRightRb(node);
                        parent = node.parent;
                        grand = parent.parent;
                    }
                    parent.red = false;
                    grand.red = true;
                    rotateLeftRb(grand);
                }
            }
        }
        root.red = false;
        root.parent = null;
    }

    private String removeRedBlack(int key) {
        Node target = findNode(key);
        if (target == null) return null;
        String old = target.value;
        Node moved = target;
        boolean movedWasRed = moved.red;
        Node replacement;
        Node replacementParent;
        if (target.left == null) {
            replacement = target.right;
            replacementParent = target.parent;
            transplant(target, target.right);
        } else if (target.right == null) {
            replacement = target.left;
            replacementParent = target.parent;
            transplant(target, target.left);
        } else {
            moved = minimum(target.right);
            movedWasRed = moved.red;
            replacement = moved.right;
            if (moved.parent == target) {
                replacementParent = moved;
                if (replacement != null) replacement.parent = moved;
            } else {
                replacementParent = moved.parent;
                transplant(moved, moved.right);
                moved.right = target.right;
                moved.right.parent = moved;
            }
            transplant(target, moved);
            moved.left = target.left;
            moved.left.parent = moved;
            moved.red = target.red;
        }
        size--;
        if (!movedWasRed) fixDelete(replacement, replacementParent);
        if (root != null) {
            root.red = false;
            root.parent = null;
        }
        return old;
    }

    private void fixDelete(Node node, Node parent) {
        while (node != root && isBlack(node)) {
            if (parent == null) break;
            if (node == parent.left) {
                Node sibling = parent.right;
                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateLeftRb(parent);
                    sibling = parent.right;
                }
                if (sibling == null) {
                    node = parent;
                    parent = node.parent;
                } else if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    sibling.red = true;
                    node = parent;
                    parent = node.parent;
                } else {
                    if (isBlack(sibling.right)) {
                        if (sibling.left != null) sibling.left.red = false;
                        sibling.red = true;
                        rotateRightRb(sibling);
                        sibling = parent.right;
                    }
                    sibling.red = parent.red;
                    parent.red = false;
                    if (sibling.right != null) sibling.right.red = false;
                    rotateLeftRb(parent);
                    node = root;
                    parent = null;
                }
            } else {
                Node sibling = parent.left;
                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateRightRb(parent);
                    sibling = parent.left;
                }
                if (sibling == null) {
                    node = parent;
                    parent = node.parent;
                } else if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    sibling.red = true;
                    node = parent;
                    parent = node.parent;
                } else {
                    if (isBlack(sibling.left)) {
                        if (sibling.right != null) sibling.right.red = false;
                        sibling.red = true;
                        rotateLeftRb(sibling);
                        sibling = parent.left;
                    }
                    sibling.red = parent.red;
                    parent.red = false;
                    if (sibling.left != null) sibling.left.red = false;
                    rotateRightRb(parent);
                    node = root;
                    parent = null;
                }
            }
        }
        if (node != null) node.red = false;
    }

    private void transplant(Node oldNode, Node newNode) {
        if (oldNode.parent == null) root = newNode;
        else if (oldNode == oldNode.parent.left) oldNode.parent.left = newNode;
        else oldNode.parent.right = newNode;
        if (newNode != null) newNode.parent = oldNode.parent;
    }

    private void rotateLeftRb(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != null) pivot.left.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node == node.parent.left) node.parent.left = pivot;
        else node.parent.right = pivot;
        pivot.left = node;
        node.parent = pivot;
    }

    private void rotateRightRb(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != null) pivot.right.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node == node.parent.right) node.parent.right = pivot;
        else node.parent.left = pivot;
        pivot.right = node;
        node.parent = pivot;
    }

    private static boolean isRed(Node node) {
        return node != null && node.red;
    }

    private static boolean isBlack(Node node) {
        return node == null || !node.red;
    }

    private String putSplay(int key, String value) {
        Node parent = null;
        Node current = root;
        while (current != null) {
            parent = current;
            if (key == current.key) {
                String old = current.value;
                current.value = value;
                splay(current);
                return old;
            }
            current = key < current.key ? current.left : current.right;
        }
        Node inserted = new Node(key, value, parent);
        if (parent == null) root = inserted;
        else if (key < parent.key) parent.left = inserted;
        else parent.right = inserted;
        size++;
        splay(inserted);
        return null;
    }

    private String removeSplay(int key) {
        Node target = findNode(key);
        if (target == null) return null;
        splay(target);
        String old = target.value;
        if (target.left == null) {
            root = target.right;
            if (root != null) root.parent = null;
        } else {
            Node left = target.left;
            left.parent = null;
            root = left;
            Node maximum = maximum(left);
            splay(maximum);
            root.right = target.right;
            if (root.right != null) root.right.parent = root;
        }
        size--;
        return old;
    }

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grand = parent.parent;
            if (grand == null) {
                if (node == parent.left) rotateRightSplay(parent);
                else rotateLeftSplay(parent);
            } else if (node == parent.left && parent == grand.left) {
                rotateRightSplay(grand);
                rotateRightSplay(parent);
            } else if (node == parent.right && parent == grand.right) {
                rotateLeftSplay(grand);
                rotateLeftSplay(parent);
            } else if (node == parent.right && parent == grand.left) {
                rotateLeftSplay(parent);
                rotateRightSplay(grand);
            } else {
                rotateRightSplay(parent);
                rotateLeftSplay(grand);
            }
        }
        root = node;
    }

    private void rotateLeftSplay(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != null) pivot.left.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node == node.parent.left) node.parent.left = pivot;
        else node.parent.right = pivot;
        pivot.left = node;
        node.parent = pivot;
    }

    private void rotateRightSplay(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != null) pivot.right.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node == node.parent.left) node.parent.left = pivot;
        else node.parent.right = pivot;
        pivot.right = node;
        node.parent = pivot;
    }

    private static Node minimum(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    private static Node maximum(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    private static final class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        int height = 1;
        boolean red;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private static final class TreeEntry implements Map.Entry<Integer, String> {
        private final IntStringTree tree;
        private final Integer key;

        TreeEntry(IntStringTree tree, Integer key) {
            this.tree = tree;
            this.key = key;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return tree.getWithoutSplay(key);
        }

        @Override
        public String setValue(String value) {
            return tree.put(key, value);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry<?, ?> entry)) return false;
            return equal(key, entry.getKey()) && equal(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

        @Override
        public String toString() {
            return key + "=" + getValue();
        }
    }
}
