package by.it.group551002.liashkevich.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map {

    static class TreeNode {
        Object value;
        Object key;
        TreeNode leftChild;
        TreeNode rightChild;
        int height;

        TreeNode(Object key, Object value) {
            this.key =  key;
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
            this.height = 0;
        }
    }

    TreeNode Root = null;
    int size = 0;

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int getHight(TreeNode P) {
        return (P != null) ? P.height : -1;
    }

    private TreeNode r_rotation(TreeNode P, TreeNode L) {
        P.leftChild = L.rightChild;
        L.rightChild = P;
        int leftPHeight = getHight(P.leftChild);
        int rightPHeight = getHight(P.rightChild);
        int leftLHeight = getHight(L.leftChild);
        int righLHeight = getHight(L.rightChild);;
        P.height = max(leftPHeight, rightPHeight) + 1;
        L.height = max(leftLHeight, righLHeight) + 1;
        return L;
    }

    private TreeNode l_rotation(TreeNode P, TreeNode R) {
        P.rightChild = R.leftChild;
        R.leftChild = P;
        int leftPHeight = getHight(P.leftChild);
        int rightPHeight = getHight(P.rightChild);
        int leftRHeight = getHight(R.leftChild);
        int righRHeight = getHight(R.rightChild);
        P.height = max(leftPHeight, rightPHeight) + 1;
        R.height = max(leftRHeight, righRHeight) + 1;
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

    private int getBf(TreeNode P) {
        if (P == null)
            return 0;
        int leftHeight = getHight(P.leftChild);
        int rightHeight = getHight(P.rightChild);
        return leftHeight - rightHeight;
    }

    private TreeNode balanceTree(TreeNode P) {
        P.height = max(getHight(P.leftChild), getHight(P.rightChild)) + 1;
        int bf = getBf(P);
        int bf_left = getBf(P.leftChild);
        int bf_right = getBf(P.rightChild);
        if (bf > 1 && bf_left < 0)
            return lr_rotation(P, P.leftChild.rightChild);
        else if (bf > 1 && bf_left >= 0)
            return r_rotation(P, P.leftChild);
        if (bf < -1 && bf_right > 0)
            return rl_rotation(P, P.rightChild.leftChild);
        else if (bf < -1 && bf_right <= 0)
            return l_rotation(P, P.rightChild);
        return P;
    }

    private void inorderTraversal(TreeNode P, StringBuilder sb) {
        if (P == null)
            return;
        inorderTraversal(P.leftChild, sb);
        sb.append(P.key).append("=").append(P.value).append(", ");
        inorderTraversal(P.rightChild, sb);
    }

    private TreeNode findNode(Object key) {
        TreeNode current = this.Root;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0)
                current = current.rightChild;
            else
                return current;
        }
        return null;
    }

    private TreeNode delete(TreeNode P, Object key) {
        if (P == null)
            return null;
        int cmp = ((Comparable<Object>)key).compareTo(P.key);
        if (cmp < 0)
            P.leftChild = this.delete(P.leftChild, key);
        else if (cmp > 0)
            P.rightChild = this.delete(P.rightChild, key);
        else {
            if (P.leftChild == null)
                return P.rightChild;
            if (P.rightChild == null)
                return P.leftChild;
            TreeNode s = P.rightChild;
            while (s.leftChild != null)
                s = s.leftChild;
            P.key = s.key;
            P.value = s.value;
            P.rightChild = delete(P.rightChild, s.key);
        }
        return balanceTree(P);
    }

    private TreeNode insert(TreeNode P, Object key, Object value) {
        if (P == null)
            return new TreeNode(key, value);
        int cmp = ((Comparable<Object>)key).compareTo(P.key);
        if (cmp < 0)
            P.leftChild = this.insert(P.leftChild, key, value);
        else
            P.rightChild =  this.insert(P.rightChild, key, value);
        return balanceTree(P);
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
        TreeNode toPut = this.findNode(key);
        if (toPut != null) {
            Object toReturn = toPut.value;
            toPut.value = value;
            return toReturn;
        }
        this.Root = insert(this.Root, key, value);
        ++this.size;
        return null;
    }

    @Override
    public Object remove(Object key) {
        TreeNode toReturn = this.findNode(key);
        if (toReturn == null)
            return null;
        Object toDelete = toReturn.value;
        this.Root = delete(Root, key);
        --this.size;
        return toDelete;
    }

    @Override
    public Object get(Object key) {
        TreeNode toReturn = this.findNode(key);
        return (toReturn != null) ? toReturn.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        TreeNode toReturn = this.findNode(key);
        return toReturn != null;
    }

    @Override
    public int size() {
        return this.size;
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
    public boolean containsValue(Object value) {
        return false;
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
