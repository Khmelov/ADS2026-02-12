package by.it.group510901.jalilova.lesson14;

import java.util.*;

public class PointsA {

    // Вспомогательный класс для хранения координат точки в 3D
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    // Структура данных DSU (Система непересекающихся множеств)
    static class DSU {
        int[] parent; // Хранит родителя для каждой точки
        int[] size;   // Хранит размер дерева (количество точек в кластере)

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждая точка сама себе лидер
                size[i] = 1;   // Размер каждого начального кластера равен 1
            }
        }

        // Поиск корня кластера со сжатием путей
        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            // Переподвешиваем текущую точку напрямую к главному лидеру для ускорения
            parent[i] = find(parent[i]);
            return parent[i];
        }

        // Объединение двух кластеров с эвристикой по размеру
        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            // Если точки уже в одном кластере, ничего не делаем
            if (rootI != rootJ) {
                // Меньшее дерево подвешиваем к корню большего дерева
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble()) return;

        // Считываем допустимое расстояние D и количество точек N
        double d = scanner.nextDouble();
        int n = scanner.nextInt();

        double dSquared = d * d;

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }

        //Создаем общее DSU
        DSU dsu = new DSU(n);

        // Перебираем все уникальные пары точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисляем квадрат евклидова расстояния между точками i и j
                long dx = points[i].x - points[j].x;
                long dy = points[i].y - points[j].y;
                long dz = points[i].z - points[j].z;
                long distSquared = dx * dx + dy * dy + dz * dz;

                // Проверяем строгое неравенство [0, D)
                if (distSquared < dSquared) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры всех сформированных кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // Если точка является корнем своего дерева, берем размер этого дерева
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        // Выводим результат через пробел
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}
