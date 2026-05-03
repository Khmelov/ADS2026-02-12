package by.it.group551002.brutski.lesson07;

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
    int diff(char a, char b) {
        if (a == b)
            return 0;

        return 1;
    }

    int min(int a, int b, int c) {
        if (a >= b && b >= c || b >= a && a >= c)
            return c;

        if (c > a && a > b || a > c && c > b)
            return b;

        return a;
    }

    int editDist(int[][] d, String s1, String s2, int i, int j) {
        if (d[i][j] != -1)
            return d[i][j];

        if (i == 0)
            d[i][j] = j;
        else if (j == 0)
            d[i][j] = i;
        else {
            int ins = editDist(d, s1, s2, i, j - 1) + 1;
            int del = editDist(d, s1, s2,i - 1, j) + 1;
            int sub = editDist(d, s1, s2, i - 1, j - 1) + diff(s1.charAt(i - 1), s2.charAt(j - 1));
            d[i][j] = min(ins, del, sub);
        }

        return d[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = one.length(), m = two.length();

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < m + 1; j++)
                d[i][j] = -1;
        }

        int result = editDist(d, one, two, n, m);
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
