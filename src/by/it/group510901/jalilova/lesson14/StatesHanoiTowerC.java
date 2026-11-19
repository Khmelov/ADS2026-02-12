package by.it.group510901.jalilova.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // DSU с эвристиками: по размеру поддерева и сжатием путей
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Эвристика 2: сокращение пути (Path Compression)
        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            parent[i] = find(parent[i]);
            return parent[i];
        }

        // Эвристика 1: объединение по размеру поддерева (Union by Size)
        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
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

    // Текущие высоты пирамид: heights[0] - A, heights[1] - B, heights[2] - C
    static int[] heights;
    // Номер первого шага, на котором была достигнута определенная максимальная высота
    static int[] firstStepWithHeight;
    // Счетчик выполненного шага (от 0 до 2^N - 2)
    static int stepIndex;
    static DSU dsu;

    // Рекурсивное решение задачи Ханойских башен
    static void solveHanoi(int count, int from, int to, int aux) {
        if (count == 0) {
            return;
        }

        // 1. Переносим N-1 колец с исходного стержня на вспомогательный
        solveHanoi(count - 1, from, aux, to);

        // 2. Переносим самое большое кольцо с исходного стержня на целевой
        heights[from]--;
        heights[to]++;

        // Находим наибольшую высоту среди трех пирамид на текущем шаге
        int maxH = heights[0];
        if (heights[1] > maxH) maxH = heights[1];
        if (heights[2] > maxH) maxH = heights[2];


        // Группируем шаги с одинаковой максимальной высотой в DSU
        if (firstStepWithHeight[maxH] == -1) {
            // Если такая максимальная высота встретилась впервые, запоминаем индекс этого шага
            firstStepWithHeight[maxH] = stepIndex;
        } else {
            // Иначе объединяем текущий шаг с первым шагом, имевшим такую же высоту
            dsu.union(firstStepWithHeight[maxH], stepIndex);
        }
        stepIndex++;

        // 3. Переносим N-1 колец со вспомогательного стержня на целевой
        solveHanoi(count - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        if (n <= 0) return;

        // Общее число шагов без учета стартового состояния равно 2^N - 1
        int totalSteps = (1 << n) - 1;
        dsu = new DSU(totalSteps);

        // Инициализируем высоты: на стержне A (0) лежат все N колец
        heights = new int[]{n, 0, 0};
        stepIndex = 0;

        // Массив для хранения первого шага с каждой возможной высотой (от 0 до N)
        firstStepWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepWithHeight[i] = -1;
        }

        // Запускаем перенос колец со стержня A (0) на стержень B (1), используя C (2)
        solveHanoi(n, 0, 1, 2);

        // Собираем размеры всех кластеров без использования коллекций (максимум N кластеров)
        int[] resultSizes = new int[n + 1];
        int clusterCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                resultSizes[clusterCount++] = dsu.size[i];
            }
        }

        // Сортируем массив размеров по возрастанию (пузырьковая сортировка)
        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = 0; j < clusterCount - i - 1; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int temp = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = temp;
                }
            }
        }

        // Выводим результат через пробел
        for (int i = 0; i < clusterCount; i++) {
            System.out.print(resultSizes[i] + (i == clusterCount - 1 ? "" : " "));
        }
        System.out.println();
    }
}