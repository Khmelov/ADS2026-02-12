package by.it.group510901.halavachou.lesson07;

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

        int m = one.length();
        int n = two.length();

        // Создаем матрицу DP размером (m+1) x (n+1)
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первой строки и первого столбца
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // стоимость удаления всех символов из первой строки
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // стоимость вставки всех символов во вторую строку
        }

        // Заполнение матрицы DP
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы одинаковые, стоимость операции равна 0
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Выбираем минимальную стоимость из трех операций:
                // 1. Замена/совпадение символа
                // 2. Удаление символа из первой строки
                // 3. Вставка символа во вторую строку
                dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + cost, // замена или совпадение
                        Math.min(
                                dp[i - 1][j] + 1,    // удаление
                                dp[i][j - 1] + 1     // вставка
                        )
                );
            }
        }

        int result = dp[m][n];
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
