package by.it.group551002.sikorski.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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

    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        memo = new int[n + 1][m + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return editDistance(one, two, n, m);
    }

    private int editDistance(String s1, String s2, int i, int j) {
        // Базовые случаи: если одна из строк закончилась
        if (i == 0) return j;
        if (j == 0) return i;

        // Если значение уже вычислено, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int res;
        // Если символы одинаковые, стоимость перехода 0
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            res = editDistance(s1, s2, i - 1, j - 1);
        } else {
            // Иначе выбираем минимум из трех операций + 1 (плата за замену/вставку/удаление)
            int replace = editDistance(s1, s2, i - 1, j - 1);
            int insert = editDistance(s1, s2, i, j - 1);
            int delete = editDistance(s1, s2, i - 1, j);

            res = Math.min(Math.min(replace, insert), delete) + 1;
        }

        // Сохраняем результат в таблицу перед возвратом
        memo[i][j] = res;
        return res;
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
