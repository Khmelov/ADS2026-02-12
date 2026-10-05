package by.it.group551003.parkhaniuk.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.lang.Math;

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

        int n = one.length() + 1;
        int m = two.length() + 1;

        int[][] memo = new int[n][m];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                memo[i][j] = -1;

        return countDist(one, two, memo, n - 1, m - 1);

    }

    public static int countDist(String str1, String str2, int[][] matrix, int i, int j)
    {
        if (i == 0)
            return j;
        if (j == 0)
            return i;

        if (matrix[i][j] != -1)
            return matrix[i][j];

        int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

        matrix[i][j] = Math.min(countDist(str1, str2, matrix, i - 1, j) + 1, Math.min(countDist(str1, str2, matrix, i, j - 1) + 1, countDist(str1, str2, matrix, i - 1, j - 1) + cost));

        return matrix[i][j];
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
