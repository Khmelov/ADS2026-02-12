package by.it.group510901.sinyak.lesson07;

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
        int n = two.length();
        int m = one.length();
        int[][] arr = new int[m + 1][n + 1];

        for (int[] row : arr) {
            java.util.Arrays.fill(row, -1);
        }
        return  rec(one, two, m, n, arr);
    }

    private int rec(String one, String two, int i, int j, int[][] arr) {
        // Базовые случаи
        if (i == 0) { return j; }
        if (j == 0) { return i; }
        if (arr[i][j] != -1) {
            return arr[i][j];
        }

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            arr[i][j] = rec(one, two, i - 1, j - 1, arr);
        }
        else {
            arr[i][j] = 1 + Math.min(rec(one, two, i - 1, j - 1, arr), Math.min(rec(one, two, i - 1, j, arr), rec(one, two, i, j - 1, arr)));
        }
        int result = arr[i][j];
        return result;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
