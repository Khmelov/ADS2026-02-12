package by.it.group551004.sheev.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    // Узел АВЛ-дерева: хранит ключ, значение, детей и высоту поддерева
    private static class Node {
        int key;
        String value;
        Node left, right;
        int height; // высота поддерева с корнем в этом узле (лист = 1)
        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root; // корень дерева
    private int size;  // количество пар ключ-значение

    // Высота поддерева; null-узел имеет высоту 0
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    // Баланс-фактор = высота левого - высота правого.
    // Для АВЛ допустимы только -1, 0, +1.
    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Пересчитываем высоту узла после изменения детей
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    // Правый поворот (малый). Случай LL.
    private Node rotateRight(Node n) {
        Node t1 = n.left;
        Node t2 = t1.right;
        t1.right = n;
        n.left = t2;
        updateHeight(n);   // сначала потомок, потом новый корень
        updateHeight(t1);
        return t1;
    }

    // Левый поворот (малый). Случай RR.
    private Node rotateLeft(Node n) {
        Node t1 = n.right;
        Node t2 = t1.left;
        t1.left = n;
        n.right = t2;
        updateHeight(n);
        updateHeight(t1);
        return t1;
    }

    // Восстанавливает баланс поддерева после вставки/удаления.
    // Различает 4 случая: LL, LR, RR, RL.
    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            // Перевес слева
            if (balanceFactor(n.left) < 0) {
                // LR: сначала левый поворот у левого ребёнка
                n.left = rotateLeft(n.left);
            }
            // LL или LR после подворота — правый поворот
            return rotateRight(n);
        }
        if (bf < -1) {
            // Перевес справа
            if (balanceFactor(n.right) > 0) {
                // RL: сначала правый поворот у правого ребёнка
                n.right = rotateRight(n.right);
            }
            // RR или RL после подворота — левый поворот
            return rotateLeft(n);
        }
        return n; // баланс в норме
    }

    // Итеративный поиск узла по ключу — O(log n) в сбалансированном дереве
    private Node findNode(int key) {
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                curr = curr.left;
            } else if (key > curr.key) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }

    // Рекурсивная вставка. Возвращает новый корень поддерева.
    private Node insert(Node n, int key, String value) {
        if (n == null) {
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else if (key > n.key) {
            n.right = insert(n.right, key, value);
        } else {
            return n; // ключ уже есть — не трогаем (обновление делает put)
        }
        return balance(n); // на обратном пути балансируем
    }

    // Симметричный обход (in-order) — даёт ключи в возрастающем порядке
    private void inorder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    // Минимальный узел в поддереве — самый левый
    private Node findMin(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    // Рекурсивное удаление. Возвращает новый корень поддерева.
    private Node delete(Node n, int key) {
        if (n == null) {
            return null;
        }
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            // Нашли узел
            if (n.left == null) {
                return n.right; // 0 или 1 ребёнок справа
            }
            if (n.right == null) {
                return n.left;  // 1 ребёнок слева
            }
            // Два ребёнка: заменяем ключ/значение минимальным из правого поддерева
            Node min = findMin(n.right);
            n.key = min.key;
            n.value = min.value;
            // И удаляем этот минимальный узел из правого поддерева
            n.right = delete(n.right, min.key);
        }
        return balance(n); // балансируем на обратном пути
    }

    public String put(Integer key, String value) {
        Node existing = findNode(key);
        if (existing != null) {
            // Ключ уже есть — обновляем значение и возвращаем старое
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null; // по контракту Map.put: null, если ключа не было
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    public boolean containsKey(Object key) {
        return key instanceof Integer && findNode((Integer) key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    public void clear() {
        // Просто обнуляем корень — всё дерево соберёт GC
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    public String toString() {
        // In-order обход даёт ключи в отсортированном виде — как у TreeMap
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) {
            // Убираем лишнюю ", " после последнего элемента
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
}