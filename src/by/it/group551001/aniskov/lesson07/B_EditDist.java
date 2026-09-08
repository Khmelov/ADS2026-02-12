package by.it.group551001.aniskov.lesson07;

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

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dist = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dist[i][0] = i;
        }
        for (int i = 0; i <= m; i++) {
            dist[0][i] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dist[i][j] = dist[i - 1][j - 1];
                } else {
                    dist[i][j] = 1 + Math.min(dist[i - 1][j], Math.min(dist[i][j - 1], dist[i - 1][j - 1]));
                }
            }
        }

        return dist[n][m];
    }
}