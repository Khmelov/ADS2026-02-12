package by.it.group551003.kravtsov.lesson14;

public class PointsA {

    // DSU: система непересекающихся множеств для объединения точек в кластеры
    static class DSU {
        int[] parent;
        int[] size;

        // Конструктор: каждый элемент изначально сам себе корень
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // ищет корень множества + сжатие пути
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // объединяет два множества, меньшее подвешиваем к большему
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }
    }

    // Точка в 3D с методом расстояния до другой точки
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        // Ввод: допустимое расстояние D и число точек N
        int maxDistance = scanner.nextInt();
        int n = scanner.nextInt();

        // Чтение N точек
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }

        // Инициализация DSU — каждая точка пока в своём кластере
        DSU dsu = new DSU(n);

        // Перебор всех пар точек: если расстояние < D — объединяем кластеры
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].distanceTo(points[j]) < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчёт размеров кластеров через массивы (коллекции запрещены)
        int[] clusterSize = new int[n];
        int clusterCount = 0;

        // Для каждого корня считаем, сколько точек ему принадлежит
        int[] count = new int[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (count[root] == 0) {
                clusterSize[clusterCount++] = 0;
            }
            count[root]++;
        }

        // Заполняем массив размеров кластеров
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (count[i] > 0) {
                clusterSize[k++] = count[i];
            }
        }

        // Ручная сортировка по возрастанию
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j < k - 1 - i; j++) {
                if (clusterSize[j] > clusterSize[j + 1]) {
                    int tmp = clusterSize[j];
                    clusterSize[j] = clusterSize[j + 1];
                    clusterSize[j + 1] = tmp;
                }
            }
        }

        // Вывод размеров кластеров по возрастанию
        for (int i = 0; i < k; i++) {
            System.out.print(clusterSize[i] + " ");
        }
    }
}