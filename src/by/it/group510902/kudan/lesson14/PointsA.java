package by.it.group510902.kudan.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class PointsA {

    //dsu, parent хранит родителя элемента, size размер поддерева если элемент корень
    private static int[] parent;
    private static int[] size;

    //поднимаемся по родителям пока не дойдем до корня, корень и есть номер кластера
    private static int find(int x) {
        while (parent[x] != x) {
            x = parent[x];
        }
        return x;
    }

    //эвристика по размеру, меньшее дерево подвешиваем к большему, так высота не больше log n
    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double d = scanner.nextDouble();
        int n = scanner.nextInt();

        int[][] p = new int[n][3];
        for (int i = 0; i < n; i++) {
            p[i][0] = scanner.nextInt();
            p[i][1] = scanner.nextInt();
            p[i][2] = scanner.nextInt();
        }

        //сначала каждая точка сама себе кластер
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        //перебираем все пары точек, если расстояние меньше d то объединяем
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = p[i][0] - p[j][0];
                double dy = p[i][1] - p[j][1];
                double dz = p[i][2] - p[j][2];
                if (Math.sqrt(dx * dx + dy * dy + dz * dz) < d) {
                    union(i, j);
                }
            }
        }

        //размеры кластеров лежат в корнях
        int[] res = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                res[count++] = size[i];
            }
        }

        //сортируем и выводим от большего к меньшему, так их сравнивает тест
        Arrays.sort(res, 0, count);
        StringBuilder sb = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            sb.append(res[i]).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}