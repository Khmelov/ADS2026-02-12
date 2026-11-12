package by.it.group510902.vishnevskaya.lesson14;

import java.util.*;

public class PointsA {

    // Класс DSU (Disjoint Set Union) с эвристикой по размеру
    static class DSU {
        private final int[] parent;
        private final int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // сжатие путей
            }
            return parent[x];
        }

        public void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            // объединяем меньшее дерево с большим
            if (size[ra] < size[rb]) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    // Точка в 3D-пространстве
    static class Point {
        final long x, y, z;

        Point(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Квадрат расстояния (используем long, чтобы избежать переполнения)
        long distSq(Point other) {
            long dx = x - other.x;
            long dy = y - other.y;
            long dz = z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextLong()) {
            return;
        }
        long D = scanner.nextLong();
        int N = scanner.nextInt();

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            long x = scanner.nextLong();
            long y = scanner.nextLong();
            long z = scanner.nextLong();
            points[i] = new Point(x, y, z);
        }

        // Сравниваем квадраты расстояний, чтобы избежать извлечения корня
        long Dsq = D * D;

        DSU dsu = new DSU(N);

        // Объединяем все пары точек, расстояние между которыми < D
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].distSq(points[j]) < Dsq) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort(Collections.reverseOrder());


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);

        scanner.close();
    }
}