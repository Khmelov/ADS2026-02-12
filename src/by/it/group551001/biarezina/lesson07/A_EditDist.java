package by.it.group551001.biarezina.lesson07;

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

    // Массив для хранения уже вычисленных значений
    private int[][] D;
    private String s1;
    private String s2;

    private int diff(char a, char b) {
        return (a == b) ? 0 : 1;
    }

    private int editDistTD(int i, int j) {
        if (D[i][j] != -1) {
            return D[i][j];
        }

        if (i == 0) {
            D[i][j] = j;
        } else if (j == 0) {
            D[i][j] = i;
        } else {
            int ins = editDistTD(i, j - 1) + 1;
            int del = editDistTD(i - 1, j) + 1;
            // В строках индексы начинаются с 0, поэтому используем i-1 и j-1
            int sub = editDistTD(i - 1, j - 1) + diff(s1.charAt(i - 1), s2.charAt(j - 1)); // Замена

            // Находим минимум из трех вариантов
            D[i][j] = Math.min(Math.min(ins, del), sub);
        }

        return D[i][j];
    }


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        this.s1 = one;
        this.s2 = two;
        int n = one.length();
        int m = two.length();

        // Инициализация массива D[0...n][0...m] значением -1 (вместо бесконечности)
        D = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                D[i][j] = -1;
            }
        }

        // Вызов рекурсивной функции для полных длин строк
        int result = editDistTD(n, m);

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
