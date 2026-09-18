package by.it.group510901.kaleeva.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Массив для мемоизации: сохраняем уже вычисленные расстояния
    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        // Инициализируем массив для запоминания результатов
        // Размер: (длина one + 1) на (длина two + 1)
        memo = new int[one.length() + 1][two.length() + 1];

        // Заполняем массив специальным значением -1, означающим,
        // что значение для этой пары ещё не вычислено
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        // Запускаем рекурсивную функцию
        return editDist(one.length(), two.length(), one, two);
    }

    // Рекурсивная функция вычисления расстояния Левенштейна
    // i - длина текущего префикса первой строки
    // j - длина текущего префикса второй строки
    private int editDist(int i, int j, String one, String two) {
        // Если результат уже посчитан, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Базовый случай 1: первая строка пуста
        // Нужно вставить все символы второй строки
        if (i == 0) {
            memo[i][j] = j;
            return j;
        }

        // Базовый случай 2: вторая строка пуста
        // Нужно удалить все символы первой строки
        if (j == 0) {
            memo[i][j] = i;
            return i;
        }

        // Рекурсивный случай: смотрим на последние символы
        // Получаем последние символы
        char ch1 = one.charAt(i - 1);
        char ch2 = two.charAt(j - 1);

        // Если символы совпадают, ничего делать не нужно
        // Просто переходим к предыдущим символам
        if (ch1 == ch2) {
            memo[i][j] = editDist(i - 1, j - 1, one, two);
            return memo[i][j];
        }

        // Если символы разные, пробуем три операции и выбираем минимальную:

        // 1. Удаление: удаляем символ из первой строки
        int deleteOp = editDist(i - 1, j, one, two) + 1;

        // 2. Вставка: вставляем символ во вторую строку
        int insertOp = editDist(i, j - 1, one, two) + 1;

        // 3. Замена: заменяем символ в первой строке на символ из второй
        int replaceOp = editDist(i - 1, j - 1, one, two) + 1;

        // Берём минимальную стоимость
        int result = Math.min(deleteOp, Math.min(insertOp, replaceOp));

        // Запоминаем результат
        memo[i][j] = result;
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}