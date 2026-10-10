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
        int m = one.length();
        int n = two.length();

        // Создаем таблицу DP
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первого столбца
        // Чтобы из строки one получить пустую строку, нужно удалить все i символов
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // Инициализация первой строки
        // нужно вставить все j символов
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        // Заполняем таблицу DP построчно
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Проверяем, равны ли текущие символы
                // Если равны, то стоимость замены = 0, иначе = 1
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                // Удаление символа из первой строки
                int delete = dp[i - 1][j] + 1;
                //Вставка символа во вторую строку
                int insert = dp[i][j - 1] + 1;

                // Замена символа (если нужно) или ничего не делаем (если символы равны)
                int replace = dp[i - 1][j - 1] + cost;
                // Берем минимальное значение из трех операций
                dp[i][j] = Math.min(Math.min(delete, insert), replace);
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