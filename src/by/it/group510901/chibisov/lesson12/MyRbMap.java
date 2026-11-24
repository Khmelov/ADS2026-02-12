package by.it.group510901.chibisov.lesson12;

import java.util.*;

public class MyRbMap<K, V> implements SortedMap<K, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Object key;
        Object value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Object key, Object value) {
            this.key = key;
            this.value = value;
            color = RED;
        }
        Node(boolean color) {
            this.color = color;
        }
    }

    boolean isLeftChild(Node n) {
        return n.parent.left == n;
    }

    boolean isRightChild(Node n) {
        return n.parent.right == n;
    }

    private final Node NIL = new Node(BLACK);

    private Node root = NIL;
    private int size;

    public MyRbMap() {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = null;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<? super K>) first).compareTo((K) second);
    }

    private void rotateRight(Node node) {
        Node pivot = node.left;
        Node orphan = pivot.right;

        pivot.right = node;
        node.left = orphan;

        pivot.parent = node.parent;

        if (node.parent == null) {
            root = pivot;
        } else if (isLeftChild(node)) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        node.parent = pivot;
        if (orphan != NIL) {
            orphan.parent = node;
        }
    }

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        Node orphan = pivot.left;

        pivot.left = node;
        node.right = orphan;

        pivot.parent = node.parent;

        if (node.parent == null) {
            root = pivot;
        } else if (isLeftChild(node)) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        node.parent = pivot;
        if (orphan != NIL) {
            orphan.parent = node;
        }
    }

    private void balanceAfterInsertion(Node n) {
        // C1
        Node p = n.parent;
        if (p == null) {
            n.color = BLACK;
            return;
        }
        // C2
        if (p.color == BLACK) {
            return;
        }
        // C3
        Node g = p.parent;
        Node u = isLeftChild(p) ? g.right : g.left;
        if (u.color == RED) {
            p.color = BLACK;
            u.color = BLACK;
            g.color = RED;
            balanceAfterInsertion(g);
            return;
        }
        if (isLeftChild(p)) {
            // C4-L
            if (isRightChild(n)) {
                rotateLeft(p);
                n.color = BLACK;
            } else {
                p.color = BLACK;
            }
            // C5-L
            rotateRight(g);
        } else {
            // C4-R
            if (isLeftChild(n)) {
                rotateRight(p);
                n.color = BLACK;
            } else {
                p.color = BLACK;
            }
            // C5-R
            rotateLeft(g);
        }
        g.color = RED;
    }

    private void balanceAfterDeletion(Node n) {
        // C1
        Node p = n.parent;
        if (p == null) {
            return;
        }
        // C2
        Node s = isLeftChild(n) ? p.right : p.left;
        if (s.color == RED) {
            if (isLeftChild(n)) {
                rotateLeft(p);
            } else {
                rotateRight(p);
            }
            p.color = RED;
            s.color = BLACK;
            balanceAfterDeletion(n);
            return;
        }
        Node s_left = s.left;
        Node s_right = s.right;
        if (s.color == BLACK && s_left.color == BLACK && s_right.color == BLACK) {
            // C3
            if (p.color == BLACK) {
                s.color = RED;
                balanceAfterDeletion(p);
                return;
            }
            // C4
            else {
                p.color = BLACK;
                s.color = RED;
                return;
            }
        }
        // C5
        if (isLeftChild(n) && s_left.color == RED && s_right.color == BLACK) {
            rotateRight(s);
            s_left.color = BLACK;
            s.color = RED;
            s = s_left;
        } else if (isRightChild(n) && s_right.color == RED && s_left.color == BLACK) {
            rotateLeft(s);
            s_right.color = BLACK;
            s.color = RED;
            s = s_right;
        }
        s_left = s.left;
        s_right = s.right;
        // C6
        if (isLeftChild(n)) {
            rotateLeft(p);
            s_right.color = BLACK;
        } else {
            rotateRight(p);
            s_left.color = BLACK;
        }
        s.color = p.color;
        p.color = BLACK;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) return "{}";
        sb.append('{');
        inOrder(root, sb);
        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == NIL) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
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
        root = NIL;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException();
        Node currentPrev = null;
        Node current = root;
        while (current != NIL) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                V oldValue = (V) current.value;
                current.value = value;
                return oldValue;
            }
        }
        current = new Node(key, value);
        current.left = NIL;
        current.right = NIL;
        current.parent = currentPrev;

        if (currentPrev == null) {
            root = current;
        } else {
            if (compare(key, currentPrev.key) < 0) {
                currentPrev.left = current;
            } else {
                currentPrev.right = current;
            }
        }

        size++;
        balanceAfterInsertion(current);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != NIL) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                V oldValue = (V) current.value;
                Node m = current;
                Node c;
                if (current.left == NIL) {
                    c = current.right;
                } else if (current.right == NIL) {
                    c = current.left;
                } else {
                    Node successor = current.right;
                    while (successor.left != NIL) successor = successor.left;
                    current.key = successor.key;
                    current.value = successor.value;
                    m = successor;
                    c = m.right;
                }
                if (m.parent == null) {
                    root = c;
                } else {
                    if (isLeftChild(m)) {
                        m.parent.left = c;
                    } else {
                        m.parent.right = c;
                    }
                }
                c.parent = m.parent;

                if (c.color == RED) {
                    c.color = BLACK;
                } else if (m.color != RED) {
                    balanceAfterDeletion(c);
                }

                size--;
                return oldValue;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != NIL) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return (V) current.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != NIL) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return preOrder(root, value);
    }

    private boolean preOrder(Node n, Object value) {
        if (n == NIL) return false;
        if (Objects.equals(n.value, value)) return true;
        if (preOrder(n.left, value)) return true;
        return preOrder(n.right, value);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap<K, V> newMap = new MyRbMap<>();
        collectHeadMap(root, toKey, newMap);
        return newMap;
    }

    @SuppressWarnings("unchecked")
    private void collectHeadMap(Node n, K toKey, MyRbMap<K, V> map) {
        if (n == NIL) return;
        collectHeadMap(n.left, toKey, map);
        if (compare(n.key, toKey) < 0) {
            map.put((K) n.key, (V) n.value);
        } else return;
        collectHeadMap(n.right, toKey, map);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap<K, V> newMap = new MyRbMap<>();
        collectTailMap(root, fromKey, newMap);
        return newMap;
    }

    @SuppressWarnings("unchecked")
    private void collectTailMap(Node n, K fromKey, MyRbMap<K, V> map) {
        if (n == NIL) return;
        collectTailMap(n.right, fromKey, map);
        if (compare(n.key, fromKey) >= 0) {
            map.put((K) n.key, (V) n.value);
        } else return;
        collectTailMap(n.left, fromKey, map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public K firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node firstNode = root;
        while (firstNode.left != NIL) firstNode = firstNode.left;
        return (K) firstNode.key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node lastNode = root;
        while (lastNode.right != NIL) lastNode = lastNode.right;
        return (K) lastNode.key;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
