package by.it.group551003.kalvinkovskaya.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!
        int len1 = one.length();
        int len2 = two.length();
        int[][] m = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++)
            m[i][0] = i;
        for (int j = 0; j <= len2; j++)
            m[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int d = diff(one.charAt(i - 1), two.charAt(j - 1));
                m[i][j] = find_min(m[i - 1][j] + 1, m[i][j - 1] + 1, m[i - 1][j - 1] + d);
            }
        }

        int result = m[len1][len2];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    static int diff(char a, char b) {
        if (a == b)
            return 0;
        else
            return 1;
    }

    static int find_min(int a, int b, int c) {
        int res = 0;
        if (a < b) {
            if (a < c)
                res = a;
            else
                res = c;
        }
        else
        if (c < b)
            res = c;
        else
            res = b;

        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}