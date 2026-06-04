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
        int len1 = one.length();
        int len2 = two.length();
        int[][] m = new int[len1 + 1][len2 + 1];
        int result = 0;

        for (int i = 0; i < len1 + 1; i++) {
            for (int j = 0; j < len2 + 1; j++) {
                m[i][j] = -1;
            }
        }

        result = editDistTD(m, len1, len2, one, two);

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

    static int editDistTD(int[][] m, int i, int j, String one, String two) {
        if (m[i][j] == -1) {
            if (i == 0)
                m[i][j] = j;
            else
                if (j == 0)
                    m[i][j] = i;
            else {
                int ins = editDistTD(m, i, j - 1, one, two) + 1;
                int del = editDistTD(m, i - 1, j, one, two) + 1;
                int sub = editDistTD(m,i - 1, j - 1, one, two) + diff(one.charAt(i - 1), two.charAt(j - 1));
                m[i][j] = find_min(ins, del, sub);
            }
        }

        return m[i][j];
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
