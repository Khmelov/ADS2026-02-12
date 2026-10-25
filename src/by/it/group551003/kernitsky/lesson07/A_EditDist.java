package by.it.group551003.kernitsky.lesson07;

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
        int m = one.length();
        int n = two.length();
        // таблица мемоизации: dp[i][j] для префиксов длины i и j,
        // инициализируем -1 (невычисленное значение)
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return editDistance(one, two, m, n, dp);
    }
    private int editDistance(String s1, String s2, int i, int j, int[][] dp) {
        // если одна из строк пуста, расстояние равно длине другой
        if (i == 0) return j;
        if (j == 0) return i;

        // если значение уже вычислено, возвращаем его
        if (dp[i - 1][j - 1] != -1) {
            return dp[i - 1][j - 1];
        }

        // если последние символы совпадают – без операции
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return dp[i - 1][j - 1] = editDistance(s1, s2, i - 1, j - 1, dp);
        }

        // иначе выбираем минимум из вставки, удаления и замены
        int insert = editDistance(s1, s2, i, j - 1, dp);       // вставка
        int delete = editDistance(s1, s2, i - 1, j, dp);       // удаление
        int replace = editDistance(s1, s2, i - 1, j - 1, dp);  // замена

        int min = insert;
        if (delete < min) min = delete;
        if (replace < min) min = replace;

        return dp[i - 1][j - 1] = 1 + min;
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
