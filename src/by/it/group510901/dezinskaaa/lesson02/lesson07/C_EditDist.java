package by.it.group510901.dezinskaaa.lesson02.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки
*/

public class C_EditDist {

    // Основной метод, возвращающий строку с редакционным предписанием
    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Длина первой строки
        int m = one.length();
        // Длина второй строки
        int n = two.length();

         int[][] dp = new int[m + 1][n + 1];

          for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
         for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

         for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j] + 1,
                            Math.min(dp[i][j - 1] + 1,
                                    dp[i - 1][j - 1] + 1));
                }
            }
        }

        List<String> operations = new ArrayList<>();
        int i = m, j = n;

         while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) {
                operations.add("#");
                i--;
                j--;
            }
             else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                operations.add("~" + two.charAt(j - 1));
                i--;
                j--;
            }
             else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                operations.add("-" + one.charAt(i - 1));
                i--;
            }
             else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                operations.add("+" + two.charAt(j - 1));
                j--;
            }
        }

         Collections.reverse(operations);

         StringBuilder resultBuilder = new StringBuilder();
        for (String op : operations) {
            resultBuilder.append(op).append(",");
        }
        String result = resultBuilder.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result; // возвращаем строку с редакционным предписанием
    }

    // Точка входа в программу для тестирования
    public static void main(String[] args) throws FileNotFoundException {
        // Открываем поток чтения из файла dataABC.txt, находящегося в ресурсах
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();          // создаём объект
        Scanner scanner = new Scanner(stream);           // создаём сканер для построчного чтения
        // Для трёх пар строк, указанных в файле, выводим редакционное предписание
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}