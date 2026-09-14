package by.it.group510901.chernyavskaya.lesson07;

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

        // Получаем длины строк
        int m = one.length();  // длина первой строки
        int n = two.length();  // длина второй строки

        // Создаем таблицу DP размером (m+1) x (n+1)
        // dp[i][j] = минимальное расстояние редактирования для первых i символов строки one
        // и первых j символов строки two
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первого столбца (j = 0)
        // Чтобы из строки one получить пустую строку, нужно удалить все i символов
        // Поэтому dp[i][0] = i для всех i от 0 до m
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Инициализация первой строки (i = 0)
        // Чтобы из пустой строки получить строку two, нужно вставить все j символов
        // Поэтому dp[0][j] = j для всех j от 0 до n
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполняем таблицу DP построчно
        // Проходим по всем символам первой строки (от 1 до m)
        for (int i = 1; i <= m; i++) {
            // Проходим по всем символам второй строки (от 1 до n)
            for (int j = 1; j <= n; j++) {
                // Проверяем, равны ли текущие символы
                // Если равны, то стоимость замены = 0, иначе = 1
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Вычисляем три возможные операции:

                // 1. Удаление символа из первой строки
                // dp[i-1][j] - расстояние для строк без последнего символа one
                // +1 - добавляем операцию удаления
                int delete = dp[i - 1][j] + 1;

                // 2. Вставка символа во вторую строку
                // dp[i][j-1] - расстояние для строк без последнего символа two
                // +1 - добавляем операцию вставки
                int insert = dp[i][j - 1] + 1;

                // 3. Замена символа (если нужно) или ничего не делаем (если символы равны)
                // dp[i-1][j-1] - расстояние для строк без последних символов обеих строк
                // + cost - добавляем стоимость замены (0 или 1)
                int replace = dp[i - 1][j - 1] + cost;

                // Берем минимальное значение из трех операций
                // Это и будет минимальное расстояние для подстрок one[0..i-1] и two[0..j-1]
                dp[i][j] = Math.min(Math.min(delete, insert), replace);
            }
        }

        // Результат находится в правом нижнем углу таблицы
        // dp[m][n] - минимальное расстояние редактирования для полных строк one и two
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