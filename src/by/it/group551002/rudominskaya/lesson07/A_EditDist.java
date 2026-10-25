package by.it.group551002.rudominskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        // Создаём таблицу для мемоизации
        int[][] memo = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                memo[i][j] = -1;
            }
        }

        // Вызываем рекурсивную функцию
        int result = editDistRecursive(one, two, m, n, memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    private int editDistRecursive(String one, String two, int i, int j, int[][] memo) {

        // Базовый случай 1: первая строка пуста
        if (i == 0) {
            return j;  // нужно вставить все символы второй строки
        }

        // Базовый случай 2: вторая строка пуста
        if (j == 0) {
            return i;  // нужно удалить все символы первой строки
        }

        // Если значение уже вычислено, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Если символы совпадают, рекурсивно вызываем для оставшихся символов
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDistRecursive(one, two, i - 1, j - 1, memo);
            return memo[i][j];
        }

        // Иначе вычисляем минимум из трёх операций:
        // 1. Удаление символа из первой строки
        // 2. Вставка символа во вторую строку
        // 3. Замена символа
        int delete = editDistRecursive(one, two, i - 1, j, memo) + 1;     // удаление
        int insert = editDistRecursive(one, two, i, j - 1, memo) + 1;     // вставка
        int replace = editDistRecursive(one, two, i - 1, j - 1, memo) + 1; // замена

        // Запоминаем и возвращаем минимальное значение
        memo[i][j] = Math.min(Math.min(delete, insert), replace);
        return memo[i][j];
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