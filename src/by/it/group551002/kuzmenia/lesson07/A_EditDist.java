package by.it.group551002.kuzmenia.lesson07;

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
    private int[][] cache;

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        cache = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++)
                cache[i][j] = -1;

        return calculate(m, n, one, two);
    }

    int calculate(int i, int j, String S1, String S2)
    {
        if (cache[i][j] != -1)
            return cache[i][j];

        int result;

        if (i == 0 && j == 0)
        {
            return 0;
        }
        else if (j == 0 && i > 0)
        {
            return i;
        }
        else if (i == 0 && j > 0)
        {
            return j;
        }
        else
        {
            int a = calculate(i, j - 1, S1, S2) + 1; // вставка
            int b = calculate(i - 1, j, S1, S2) + 1; // удаление

            int m = S1.charAt(i - 1) == S2.charAt(j - 1) ? 0 : 1;
            int c = calculate(i - 1, j - 1, S1, S2) + m;

            result = Math.min(a, Math.min(b, c));
        }

        cache[i][j] = result;
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
