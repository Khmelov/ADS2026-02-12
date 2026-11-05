package by.it.group551002.liashkevich.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap {

    static class TreeNode {
        Object value;
        Object key;
        TreeNode parent;
        TreeNode leftChild;
        TreeNode rightChild;

        TreeNode(Object key, Object value) {
            this.key =  key;
            this.value = value;
            this.parent = null;
            this.leftChild = null;
            this.rightChild = null;
        }

    }

    TreeNode Root = null;
    int size = 0;

    private void inorderTraversal(TreeNode P, StringBuilder sb) {
        if (P == null)
            return;
        inorderTraversal(P.leftChild, sb);
        sb.append(P.key).append("=").append(P.value).append(", ");
        inorderTraversal(P.rightChild, sb);
    }

    private void headTraversal(TreeNode P, Object toKey, MySplayMap result) {
        if (P == null)
            return;
        headTraversal(P.leftChild, toKey, result);
        if (((Comparable<Object>)P.key).compareTo(toKey) >= 0)
            return;
        result.put(P.key, P.value);
        headTraversal(P.rightChild, toKey, result);
    }

    private void headTraversalBack(TreeNode P, Object fromKey, MySplayMap result) {
        if (P == null)
            return;
        headTraversalBack(P.rightChild, fromKey, result);
        if (((Comparable<Object>)P.key).compareTo(fromKey) < 0)
            return;
        result.put(P.key, P.value);
        headTraversalBack(P.leftChild, fromKey, result);
    }

    private TreeNode r_rotation(TreeNode P, TreeNode L) {
        TreeNode G = P.parent;
        P.leftChild = L.rightChild;
        if (L.rightChild != null)
            L.rightChild.parent = P;
        L.rightChild = P;
        L.parent = G;
        P.parent = L;
        if (G == null)
            this.Root = L;
        else if (G.leftChild == P)
            G.leftChild = L;
        else
            G.rightChild = L;
        return L;
    }

    private TreeNode l_rotation(TreeNode P, TreeNode R) {
        TreeNode G = P.parent;
        P.rightChild = R.leftChild;
        if (R.leftChild != null)
            R.leftChild.parent = P;
        R.leftChild = P;
        R.parent = G;
        P.parent = R;
        if (G == null)
            this.Root = R;
        else if (G.rightChild == P)
            G.rightChild = R;
        else
            G.leftChild = R;
        return R;
    }

    private TreeNode rl_rotation(TreeNode P, TreeNode R) {
        P.rightChild  = r_rotation(P.rightChild, R);
        P = l_rotation(P, P.rightChild);
        return P;
    }

    private TreeNode lr_rotation(TreeNode P, TreeNode L) {
        P.leftChild = l_rotation(P.leftChild, L);
        P = r_rotation(P, P.leftChild);
        return P;
    }

    private TreeNode findNode(Object key) {
        TreeNode current = this.Root;
        TreeNode last = null;
        while (current != null) {
            last = current;
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0)
                current = current.rightChild;
            else {
                balanceTree(current);
                return current;
            }
        }
        if (last != null)
            balanceTree(last);
        return null;
    }

    private void balanceTree(TreeNode S) {
        while (S.parent != null) {
            TreeNode P = S.parent;
            TreeNode G = P.parent;
            if (G == null) {
                if (S == P.leftChild)
                    r_rotation(P, S);
                else
                    l_rotation(P, S);
            }
            else if (S == P.leftChild && P == G.leftChild) {
                r_rotation(G, P);
                r_rotation(P, S);
            }
            else if (S == P.rightChild && P == G.rightChild) {
                l_rotation(G, P);
                l_rotation(P, S);
            }
            else if (S == P.rightChild && P == G.leftChild)
                lr_rotation(G, S);
            else
                rl_rotation(G, S);
        }
    }

    @Override
    public String toString() {
        if (this.Root == null)
            return "{}";
        StringBuilder result = new StringBuilder("{");
        this.inorderTraversal(Root, result);
        return result.delete(result.length() - 2, result.length()).append("}").toString();
    }

    @Override
    public Object put(Object key, Object value) {
        if (this.Root == null) {
            this.Root = new TreeNode(key, value);
            ++size;
            return null;
        }
        TreeNode parent = null;
        TreeNode current = this.Root;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0)
                current = current.rightChild;
            else {
                Object toReturn = current.value;
                current.value = value;
                balanceTree(current);
                return toReturn;
            }
        }
        TreeNode newNode = new TreeNode(key, value);
        newNode.parent = parent;
        if (cmp < 0)
            parent.leftChild = newNode;
        else
            parent.rightChild = newNode;
        ++size;
        balanceTree(newNode);
        return null;
    }

    @Override
    public Object remove(Object key) {
        TreeNode toRemove = findNode(key);
        if (toRemove == null)
            return null;
        Object toReturn = toRemove.value;
        TreeNode left = toRemove.leftChild;
        TreeNode right = toRemove.rightChild;
        if (left != null)
            left.parent = null;
        if (right != null)
            right.parent = null;
        toRemove.leftChild = null;
        toRemove.rightChild = null;
        if (left == null)
            this.Root = right;
        else {
            this.Root = left;
            while (left.rightChild != null)
                left = left.rightChild;
            balanceTree(left);
            this.Root.rightChild = right;
            if (right != null)
                right.parent = this.Root;
        }
        --size;
        return toReturn;
    }

    @Override
    public Object get(Object key) {
        TreeNode toReturn = findNode(key);
        return (toReturn == null) ? null : toReturn.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    private boolean recursiveContainsValue(TreeNode N, Object value) {
        if (N == null)
            return false;
        return Objects.equals(value, N.value) || recursiveContainsValue(N.leftChild, value) || recursiveContainsValue(N.rightChild, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return recursiveContainsValue(this.Root, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.Root = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.Root == null;
    }

    @Override
    public NavigableMap headMap(Object toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headTraversal(this.Root, toKey, result);
        return result;
    }

    @Override
    public NavigableMap tailMap(Object fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headTraversalBack(this.Root, fromKey, result);
        return result;
    }

    @Override
    public Object firstKey() {
        TreeNode current = this.Root;
        TreeNode toReturn = null;
        while (current != null) {
            toReturn = current;
            current = current.leftChild;
        }
        return toReturn != null ? toReturn.key : null;
    }

    @Override
    public Object lastKey() {
        TreeNode current = this.Root;
        TreeNode toReturn = null;
        while (current != null) {
            toReturn = current;
            current = current.rightChild;
        }
        return toReturn != null ? toReturn.key : null;
    }

    @Override
    public Object lowerKey(Object key) {
        TreeNode current = this.Root;
        TreeNode result = null;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp > 0) {
                result = current;
                current = current.rightChild;
            }
            else {
                current = current.leftChild;
            }
        }
        if (result != null)
            balanceTree(result);
        return result != null ? result.key : null;
    }

    @Override
    public Object floorKey(Object key) {
        TreeNode current = this.Root;
        TreeNode result = null;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0) {
                result = current;
                current = current.rightChild;
            }
            else {
                result = current;
                break;
            }
        }
        if (result != null)
            balanceTree(result);
        return result != null ? result.key : null;
    }

    @Override
    public Object ceilingKey(Object key) {
        TreeNode current = this.Root;
        TreeNode result = null;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0) {
                result = current;
                current = current.leftChild;
            }
            else if (cmp > 0) {
                current = current.rightChild;
            }
            else {
                result = current;
                break;
            }
        }
        if (result != null)
            balanceTree(result);
        return result != null ? result.key : null;
    }

    @Override
    public Object higherKey(Object key) {
        TreeNode current = this.Root;
        TreeNode result = null;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0) {
                result = current;
                current = current.leftChild;
            }
            else {
                current = current.rightChild;
            }
        }
        if (result != null)
            balanceTree(result);
        return result != null ? result.key : null;
    }




    @Override
    public Entry lowerEntry(Object key) {
        return null;
    }

    @Override
    public Entry floorEntry(Object key) {
        return null;
    }

    @Override
    public Entry ceilingEntry(Object key) {
        return null;
    }

    @Override
    public Entry higherEntry(Object key) {
        return null;
    }

    @Override
    public Entry firstEntry() {
        return null;
    }

    @Override
    public Entry lastEntry() {
        return null;
    }

    @Override
    public Entry pollFirstEntry() {
        return null;
    }

    @Override
    public Entry pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap descendingMap() {
        return null;
    }

    @Override
    public NavigableSet navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
        return null;
    }


    @Override
    public Comparator comparator() {
        return null;
    }

    @Override
    public SortedMap subMap(Object fromKey, Object toKey) {
        return null;
    }

    @Override
    public SortedMap headMap(Object toKey) {
        MySplayMap result = new MySplayMap();
        headTraversal(this.Root, toKey, result);
        return result;
    }

    @Override
    public SortedMap tailMap(Object fromKey) {
        MySplayMap result = new MySplayMap();
        headTraversalBack(this.Root, fromKey, result);
        return result;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public Set keySet() {
        return Set.of();
    }

    @Override
    public Collection values() {
        return List.of();
    }

    @Override
    public Set<Entry> entrySet() {
        return Set.of();
    }
}
