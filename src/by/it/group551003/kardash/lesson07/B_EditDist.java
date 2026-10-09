package by.it.group551003.kardash.lesson07;

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
*/

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        // dp[i][j] = минимальное расстояние между one[0..i) и two[0..j)
        int[][] dp = new int[m + 1][n + 1];

        // Базовые случаи: преобразование пустой строки в префикс длины k требует k операций
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // удалить i символов из one
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // вставить j символов в one
        }

        // Заполнение таблицы ДП
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают — операция не нужна
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Выбираем минимальную стоимость из трёх операций:
                    int delete = dp[i - 1][j] + 1;        // удалить символ из one
                    int insert = dp[i][j - 1] + 1;         // вставить символ в one
                    int substitute = dp[i - 1][j - 1] + 1; // заменить символ
                    dp[i][j] = Math.min(delete, Math.min(insert, substitute));
                }
            }
        }

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