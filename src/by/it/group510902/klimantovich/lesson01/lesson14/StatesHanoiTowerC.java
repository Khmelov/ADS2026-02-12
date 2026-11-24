package by.it.group510902.klimantovich.lesson01.lesson14;
import java.util.Scanner;
public class StatesHanoiTowerC {
    // наша реализация поиск в глубину без использования коллекций
    //т.к. максимальное количество уникальных состояний может быть большим, мы используем массивы.
    static int[] parent;
    static int[] size;
    static int[] height;
    //функция для нахождения корня с сокращением пути
    static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }
    //функция объединения по размеру
    static void union(int i, int j) {
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
    //массив для хранения текущего положения дисков
    static int[] diskPos;
    static int totalSteps = 0;
    static int n;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        n = scanner.nextInt();
        // кол-во шагов для N дисков = 2^N - 1
        totalSteps = (1 << n) - 1;
        //инициализируем обход в глубину для каждого шага
        parent = new int[totalSteps];
        size = new int[totalSteps];
        height = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        diskPos = new int[n + 1]; //диски от 1 до N
        for (int i = 1; i <= n; i++) {
            diskPos[i] = 1; //все диски изначально на стержне A (1)
        }
        //запускаем генерацию шагов. stepCounter будет считать номер шага.
        int[] stepCounter = {0};
        hanoiWithHeight(n, 1, 2, 3, stepCounter);
        // Группируем шаги по максимальной высоте пирамид (высота не больше N).
        int[] firstStepWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) firstStepWithHeight[i] = -1;
        // Теперь у нас есть массив height максимальная высота пирамиды на этом шаге объединяем шаги с одинаковой высотой
        for (int i = 1; i <= n; i++) firstStepWithHeight[i] = -1;
        for (int i = 0; i < totalSteps; i++) {
            int h = height[i];
            if (firstStepWithHeight[h] == -1) {
                firstStepWithHeight[h] = i;
            } else {
                union(firstStepWithHeight[h], i);
            }
        }
        //считаем размеры групп
        int[] groupSizes = new int[totalSteps];
        int groupCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (find(i) == i) {
                groupSizes[groupCount++] = size[i];
            }
        }
        //сортируем размеры по возрастанию (пузырьком, т.к. коллекции нельзя)
        for (int i = 0; i < groupCount - 1; i++) {
            for (int j = 0; j < groupCount - i - 1; j++) {
                if (groupSizes[j] > groupSizes[j + 1]) {
                    int temp = groupSizes[j];
                    groupSizes[j] = groupSizes[j + 1];
                    groupSizes[j + 1] = temp;
                }
            }
        }
        //выводим результат
        for (int i = 0; i < groupCount; i++) {
            System.out.print(groupSizes[i]);
            if (i < groupCount - 1) System.out.print(" ");
        }
        System.out.println();
    }
    // генерация шагов башни с записью высот
    static void hanoiWithHeight(int n, int from, int to, int aux, int[] stepCounter) {
        if (n == 1) {
            //перемещаем диск 1
            diskPos[1] = to;
            //вычисляем максимальную высоту пирамид кол-во дисков на каждом стержне
            int[] heights = new int[4];
            for (int i = 1; i <= n; i++) {
            }
            //нужно использовать глобальную N
            int maxH = 0;
            int countA = 0, countB = 0, countC = 0;
            for (int i = 1; i <= StatesHanoiTowerC.n; i++) {
                if (diskPos[i] == 1) countA++;
                else if (diskPos[i] == 2) countB++;
                else countC++;
            }
            maxH = Math.max(countA, Math.max(countB, countC));
            height[stepCounter[0]++] = maxH;
            return;
        }
        hanoiWithHeight(n - 1, from, aux, to, stepCounter);
        // перемещаем диск n
        diskPos[n] = to;
        int maxH = 0;
        int countA = 0, countB = 0, countC = 0;
        for (int i = 1; i <= StatesHanoiTowerC.n; i++) {
            if (diskPos[i] == 1) countA++;
            else if (diskPos[i] == 2) countB++;
            else countC++;
        }
        maxH = Math.max(countA, Math.max(countB, countC));
        height[stepCounter[0]++] = maxH;
        hanoiWithHeight(n - 1, aux, to, from, stepCounter);
    }
}