package by.it.group551003.sysoev.lesson07;

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

    // рекурсивная функция с мемоизацией
    private int rec(String one, String two, int i, int j, int[][] memo) {
        // если одна строка закончилась, нужно удалить все символы другой
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

        // если уже считали, возвращаем сохранённое значение
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // если символы совпадают, рекурсивно считаем для предыдущих символов
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = rec(one, two, i - 1, j - 1, memo);
            return memo[i][j];
        }

        // иначе считаем три операции:
        // 1. замена символа
        int replace = rec(one, two, i - 1, j - 1, memo) + 1;
        // 2. удаление символа из первой строки
        int delete = rec(one, two, i - 1, j, memo) + 1;
        // 3. вставка символа во вторую строку
        int insert = rec(one, two, i, j - 1, memo) + 1;

        // берём минимальное из трёх
        memo[i][j] = Math.min(replace, Math.min(delete, insert));
        return memo[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = one.length();
        int m = two.length();

        // создаём таблицу для мемоизации, заполняем -1 (означает, что значение ещё не вычислено)
        int[][] memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        int result = rec(one, two, n, m, memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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