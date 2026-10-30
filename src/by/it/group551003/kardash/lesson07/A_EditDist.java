package by.it.group551003.kardash.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк
*/

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Таблица для мемоизации: мемо[i][j] = расстояние между one[0..i) и two[0..j)
        int[][] memo = new int[one.length() + 1][two.length() + 1];

        // Инициализация: -1 означает, что значение ещё не вычислено
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return editDistanceRecursive(one, two, one.length(), two.length(), memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    /**
     * Рекурсивная функция вычисления расстояния Левенштейна с мемоизацией
     * @param one первая строка
     * @param two вторая строка
     * @param m длина префикса первой строки для рассмотрения
     * @param n длина префикса второй строки для рассмотрения
     * @param memo таблица для хранения уже вычисленных результатов
     * @return минимальное количество операций редактирования
     */
    private int editDistanceRecursive(String one, String two, int m, int n, int[][] memo) {
        // Базовые случаи: если одна строка пуста, нужно вставить/удалить все символы другой
        if (m == 0) return n;
        if (n == 0) return m;

        // Если уже вычисляли — возвращаем сохранённый результат
        if (memo[m][n] != -1) return memo[m][n];

        // Если последние символы совпадают — операция не нужна
        if (one.charAt(m - 1) == two.charAt(n - 1)) {
            memo[m][n] = editDistanceRecursive(one, two, m - 1, n - 1, memo);
            return memo[m][n];
        }

        // Три возможные операции:
        int insert = editDistanceRecursive(one, two, m, n - 1, memo) + 1;      // вставка
        int delete = editDistanceRecursive(one, two, m - 1, n, memo) + 1;      // удаление
        int substitute = editDistanceRecursive(one, two, m - 1, n - 1, memo) + 1; // замена

        // Выбираем минимальное количество операций
        memo[m][n] = Math.min(insert, Math.min(delete, substitute));
        return memo[m][n];
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