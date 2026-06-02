package by.it.group510901.chernyavskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность
*/

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
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

        // Создаем массив dp, где dp[i] - длина наибольшей кратной подпоследовательности,
        // заканчивающейся на элементе m[i]
        // В кратной подпоследовательности каждый следующий элемент должен делиться на предыдущий
        int[] dp = new int[n];

        // Инициализация: каждый элемент сам по себе образует подпоследовательность длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Основной алгоритм динамического программирования
        // Для каждого элемента i (текущий) просматриваем все предыдущие элементы j
        // Проверяем, делится ли текущий элемент m[i] на предыдущий m[j]
        // Если делится (m[i] % m[j] == 0), то можем продолжить кратную подпоследовательность
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Условие кратности: текущий элемент должен делиться на предыдущий
                // В отличие от задачи A_LIS, здесь не "меньше", а "делится"
                if (m[i] % m[j] == 0) {
                    // Если m[j] является делителем m[i], то можем добавить m[i]
                    // к подпоследовательности, заканчивающейся на m[j]
                    // Новая длина = длина подпоследовательности для j + 1
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                    }
                }
            }
        }

        // Находим максимальную длину кратной подпоследовательности
        // Это максимальное значение в массиве dp
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