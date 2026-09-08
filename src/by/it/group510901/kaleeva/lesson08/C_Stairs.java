package by.it.group510901.kaleeva.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: подъём по лестнице
- Есть n ступенек, каждая имеет свою стоимость (может быть отрицательной)
- Начинаем с 0-й ступеньки (перед первой)
- За один шаг можно подняться на 1 или на 2 ступеньки
- Нужно добраться до n-й ступеньки, набрав максимальную сумму
*/

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        // Создаём сканер для чтения входных данных
        Scanner scanner = new Scanner(stream);

        // n - количество ступенек
        int n = scanner.nextInt();

        // Массив со стоимостями ступенек
        // Индексация: stairs[0] - первая ступенька, stairs[n-1] - последняя
        int[] stairs = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        // Обрабатываем особые случаи
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return stairs[0];
        }

        // Создаём массив dp для динамического программирования
        // dp[i] - максимальная сумма, которую можно набрать, чтобы ДОЙТИ до i-й ступеньки
        // (включая стоимость самой i-й ступеньки)
        int[] dp = new int[n];

        // Инициализация для первых ступенек
        // До первой ступеньки можно допрыгнуть только с начала
        dp[0] = stairs[0];

        // До второй ступеньки можно допрыгнуть
        // Выбираем максимальный путь
        dp[1] = stairs[0] + stairs[1];  // идём через первую ступеньку

        // Проверяем, может быть выгоднее прыгнуть сразу на вторую ступеньку
        // (если stairs[1] больше, чем stairs[0] + stairs[1])
        if (n > 1 && stairs[1] > dp[1]) {
            dp[1] = stairs[1];
        }

        // Основной цикл: заполняем dp для всех ступенек
        for (int i = 2; i < n; i++) {
            // Берём максимальный из этих двух вариантов
            dp[i] = Math.max(dp[i - 1] + stairs[i], dp[i - 2] + stairs[i]);
        }

        // Результат - максимальная сумма для достижения последней ступеньки
        return dp[n - 1];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}