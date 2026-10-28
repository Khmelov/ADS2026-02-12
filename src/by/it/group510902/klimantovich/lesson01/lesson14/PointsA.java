package by.it.group510902.klimantovich.lesson01.lesson14;
import java.util.*;
public class PointsA {
    //внутренний класс для реализации DSU (Системы непересекающихся множеств)
    static class DSU {
        int[] parent;
        int[] size;
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; //изначально каждый элемент сам себе родитель
                size[i] = 1;   // размер каждого множества = 1
            }
        }
        // найти корень элемента с сокращением пути
        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            //рекурсивно находим корень и сразу прикрепляем к нему i
            return parent[i] = find(parent[i]);
        }
        //объединить два множества эвристика по размеру
        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                //присоединяем меньшее дерево к большему
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
        if (!scanner.hasNext()) return;
        int D = scanner.nextInt();
        int N = scanner.nextInt();
        // считываем точки
        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }
        DSU dsu = new DSU(N);
        // проверяем все пары точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                //Вычисляем евклидово расстояние
                double dist = Math.sqrt(
                        Math.pow(points[i][0] - points[j][0], 2) +
                                Math.pow(points[i][1] - points[j][1], 2) +
                                Math.pow(points[i][2] - points[j][2], 2)
                );
                //если расстояние строго меньше D, объединяем
                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }
        //собираем размеры кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            // если элемент является корнем, значит, это отдельный кластер
            if (dsu.find(i) == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }
        // Сортируем по убыванию сначала большие кластеры
        clusterSizes.sort((a, b) -> b - a);
        // выводим через пробел
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}