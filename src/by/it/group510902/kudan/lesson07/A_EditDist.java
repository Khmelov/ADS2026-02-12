package by.it.group510902.kudan.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    private int[][] memo;
    private String s1;
    private String s2;

    private int editDistRecursive(int i, int j) {
        if (memo[i][j] == -1) {
            if (i == 0) {
                memo[i][j] = j;
            } else if (j == 0) {
                memo[i][j] = i;
            } else {
                int ins = editDistRecursive(i, j - 1) + 1;
                int del = editDistRecursive(i - 1, j) + 1;
                int sub = editDistRecursive(i - 1, j - 1) + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1);
                memo[i][j] = Math.min(Math.min(ins, del), sub);
            }
        }
        return memo[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        this.s1 = one;
        this.s2 = two;
        int n = one.length();
        int m = two.length();

        memo = new int[n + 1][m + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        int result = editDistRecursive(n, m);
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