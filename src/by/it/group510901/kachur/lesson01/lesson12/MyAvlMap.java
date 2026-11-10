
package by.it.group510901.kachur.lesson01.lesson12;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс, представляющий узел АВЛ-дерева
    private class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height; // Высота поддерева для балансировки

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новый узел всегда добавляется как лист с высотой 1
        }
    }

    private Node root; // Корень дерева
    private int size = 0; // Количество элементов в мапе

    // --- Вспомогательные методы для работы с АВЛ-деревом ---


    private int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }


    private int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }


    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Производим поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Возвращаем новый корень
        return x;
    }


    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Производим поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Возвращаем новый корень
        return y;
    }

    // --- Реализация обязательных методов Map ---

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node current = root;
        while (current != null) {
            if (k < current.key) {
                current = current.left;
            } else if (k > current.key) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        // Проверяем, существует ли уже такой ключ, чтобы вернуть старое значение (требование Map.put)
        String oldValue = get(key);

        root = insertRec(root, key, value);

        // Если старого значения не было, значит размер увеличился
        if (oldValue == null) {
            size++;
        }
        return oldValue;
    }


    private Node insertRec(Node node, int key, String value) {
        // 1. Стандартная вставка в дерево поиска
        if (node == null)
            return new Node(key, value);

        if (key < node.key) {
            node.left = insertRec(node.left, key, value);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, value);
        } else {
            node.value = value; // Ключ уже есть, просто обновляем значение
            return node;
        }

        // 2. Обновляем высоту текущего узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // 3. Получаем фактор баланса, чтобы проверить, потерял ли узел баланс
        int balance = getBalance(node);

        // Если узел несбалансирован, то есть 4 случая:

        // Left Left Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;

        String oldValue = get(k);
        if (oldValue != null) {
            root = deleteRec(root, k);
            size--;
        }
        return oldValue;
    }

    // Поиск узла с минимальным значением (используется при удалении)
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }


    private Node deleteRec(Node root, int key) {
        if (root == null)
            return null;

        if (key < root.key)
            root.left = deleteRec(root.left, key);
        else if (key > root.key)
            root.right = deleteRec(root.right, key);
        else {
            // Узел с одним или без детей
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    root = null;
                } else
                    root = temp;
            } else {
                // Узел с двумя детьми: получаем inorder-преемника (минимальный в правом поддереве)
                Node temp = minValueNode(root.right);

                root.key = temp.key;
                root.value = temp.value;

                root.right = deleteRec(root.right, temp.key);
            }
        }

        if (root == null)
            return null;

        // Обновляем высоту
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Проверяем баланс
        int balance = getBalance(root);

        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }


    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);

            // Если в StringBuilder уже что-то есть кроме открывающей скобки, добавляем разделитель
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);

            inOrderToString(node.right, sb);
        }
    }

    // --- Заглушки для остальных методов интерфейса Map (чтобы код компилировался) ---
    @Override
    public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}