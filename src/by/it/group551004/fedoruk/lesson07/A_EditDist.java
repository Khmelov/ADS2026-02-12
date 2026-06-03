package by.it.group551004.fedoruk.lesson07;

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


    int[][] memo; // массив для хранения результатов

    int getDistanceEdinting(String one, String two) {
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n1 = one.length();
        int n2 = two.length();

        memo = new int[n1 + 1][n2 + 1];
        for (int i = 0; i <= n1; i++) {
            for (int j = 0; j <= n2; j++) {
                memo[i][j] = -1;
            }
        }

        // Вызываем рекурсивную вспомогательную функцию
        int result = editDistanceRecursive(one, two, n1, n2);

        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Вспомогательная рекурсивная функция
    private int editDistanceRecursive(String s1, String s2, int i, int j) {

        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        int delete = editDistanceRecursive(s1, s2, i - 1, j) + 1;
        int insert = editDistanceRecursive(s1, s2, i, j - 1) + 1;
        int substitute = editDistanceRecursive(s1, s2, i - 1, j - 1) + cost;

        // Сохраняем в память и возвращаем результат
        memo[i][j] = Math.min(delete, Math.min(insert, substitute));
        return memo[i][j];
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
