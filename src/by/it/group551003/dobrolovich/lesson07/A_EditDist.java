package by.it.group551003.dobrolovich.lesson07;

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
        int n = one.length();
        int m = two.length();
        Integer[][] memo = new Integer[n + 1][m + 1];
        return editDistRecursive(one, two, n, m, memo);
    }

    private int editDistRecursive(String one, String two, int i, int j, Integer[][] memo) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (memo[i][j] != null) return memo[i][j];
        int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
        int deleteOp = editDistRecursive(one, two, i - 1, j, memo) + 1;
        int insertOp = editDistRecursive(one, two, i, j - 1, memo) + 1;
        int replaceOp = editDistRecursive(one, two, i - 1, j - 1, memo) + cost;
        int min = Math.min(deleteOp, Math.min(insertOp, replaceOp));
        memo[i][j] = min;
        return min;
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