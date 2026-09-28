package by.it.group510902.shimul.lesson07;

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
import java.util.Arrays;
public class A_EditDist {

    int[][] memo;

    int rec(String a, String b, int i, int j) {
        // если уже считали
        if (memo[i][j] != -1)
            return memo[i][j];

        // если одна строка пустая
        if (i == 0) return memo[i][j] = j;
        if (j == 0) return memo[i][j] = i;

        // если последние символы равны
        if (a.charAt(i - 1) == b.charAt(j - 1))
            return memo[i][j] = rec(a, b, i - 1, j - 1);

        // иначе минимум из 3 операций
        int delete = rec(a, b, i - 1, j);
        int insert = rec(a, b, i, j - 1);
        int replace = rec(a, b, i - 1, j - 1);

        return memo[i][j] = 1 + Math.min(delete, Math.min(insert, replace));
    }

    int getDistanceEdinting(String one, String two) {
        // создаём таблицу для мемоизации
        memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo)
            Arrays.fill(row, -1);

        return rec(one, two, one.length(), two.length());
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
