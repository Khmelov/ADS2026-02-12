package by.it.group551002.voroshko.lesson07;

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

        int result = 0;

        int[][] matrix = new int[one.length() + 1][two.length() + 1];

        for (int n = 0; n <= one.length(); n++)
            for (int m = 0; m <= two.length(); m++)
                matrix[n][m] = -1;

        result = levsLen(matrix, one, two, one.length(), two.length());

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int levsLen(int[][] matrix, String one, String two, int oneIdx, int twoIdx) {
        if (oneIdx == 0)
            return twoIdx;

        if (twoIdx == 0)
            return oneIdx;

        if (matrix[oneIdx][twoIdx] != -1)
            return matrix[oneIdx][twoIdx];

        if (one.charAt(oneIdx - 1) == two.charAt(twoIdx - 1)) {
            matrix[oneIdx][twoIdx] = levsLen(matrix, one, two, oneIdx - 1, twoIdx - 1);
        } else {
            int ins = levsLen(matrix, one, two, oneIdx, twoIdx - 1);
            int del = levsLen(matrix, one, two, oneIdx - 1, twoIdx);
            int repl = levsLen(matrix, one, two, oneIdx - 1, twoIdx - 1);
            matrix[oneIdx][twoIdx] = 1 + min(ins, del, repl);
        }

        return matrix[oneIdx][twoIdx];
    }

    int min (int a, int b, int c) {
        if (a < b && a < c)
            return a;
        else if (b < a && b < c)
            return b;
        else
            return c;
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
