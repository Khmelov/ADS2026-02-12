package by.it.group551002.rudominskaya.lesson07;

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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();  // длина первой строки
        int n = two.length();  // длина второй строки

        // Создаём таблицу размером (m+1) x (n+1)
        // dp[i][j] = минимальное расстояние между префиксами one[0..i-1] и two[0..j-1]
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первого столбца
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // стоимость = количество удалений
        }

        // Инициализация первой строки
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // стоимость = количество вставок
        }

        // Заполнение таблицы построчно
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Стоимость замены: 0 если символы совпадают, 1 если разные
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Вычисляем минимум из трёх возможных операций:
                // 1. Удаление символа из первой строки: dp[i-1][j] + 1
                // 2. Вставка символа во вторую строку: dp[i][j-1] + 1
                // 3. Замена (или копирование) символа: dp[i-1][j-1] + cost
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,      // удаление
                                dp[i][j - 1] + 1),     // вставка
                        dp[i - 1][j - 1] + cost         // замена или копирование
                );
            }
        }

        // Результат находится в правом нижнем углу таблицы
        int result = dp[m][n];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}