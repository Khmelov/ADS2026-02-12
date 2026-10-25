package by.it.group510902.vinnichuk.lesson07;

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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        int n = one.length();
        int m = two.length();

        // создаю таблицу для мемоизации
        int[][] memo = new int[n + 1][m + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        int result = getDist(one, two, n, m, memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // рекурсивный подсчет расстояния
    private int getDist(String s1, String s2, int i, int j, int[][] memo) {
        // базовые случаи
        if (i == 0) return j;
        if (j == 0) return i;

        // возвращаю готовый ответ
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // если символы совпали
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return memo[i][j] = getDist(s1, s2, i - 1, j - 1, memo);
        }

        // считаю варианты операций
        int ins = getDist(s1, s2, i, j - 1, memo) + 1;
        int del = getDist(s1, s2, i - 1, j, memo) + 1;
        int rep = getDist(s1, s2, i - 1, j - 1, memo) + 1;

        return memo[i][j] = Math.min(ins, Math.min(del, rep));
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
