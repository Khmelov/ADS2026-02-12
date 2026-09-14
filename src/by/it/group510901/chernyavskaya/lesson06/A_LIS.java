package by.it.group510901.chernyavskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Создаем массив dp, где dp[i] - длина наибольшей возрастающей
        // подпоследовательности (НВП), заканчивающейся на элементе m[i]
        int[] dp = new int[n];

        // Инициализируем: каждый элемент сам по себе образует
        // подпоследовательность длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Основной алгоритм динамического программирования
        // Для каждого i (текущий элемент) просматриваем все предыдущие элементы j
        // Если m[j] < m[i] (элемент j может быть перед i в возрастающей последовательности)
        // и dp[j] + 1 > dp[i] (нашли лучший вариант), то обновляем dp[i]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем, можно ли добавить элемент i к последовательности,
                // заканчивающейся на j (нужно, чтобы m[j] был меньше m[i])
                if (m[j] < m[i]) {
                    // Длина новой подпоследовательности = длина подпоследовательности,
                    // заканчивающейся на j, плюс 1 (добавляем текущий элемент i)
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                    }
                }
            }
        }

        // Находим максимальное значение в массиве dp
        // Это и будет длина наибольшей возрастающей подпоследовательности
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}