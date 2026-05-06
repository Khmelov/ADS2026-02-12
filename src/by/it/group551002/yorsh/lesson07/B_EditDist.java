package by.it.group551002.yorsh.lesson07;

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
        int m = one.length(); // длина первой строки
        int n = two.length(); // длина второй строки

        // dp[i][j] – редакционное расстояние между первыми i символами one и j символами two
        int[][] dp = new int[m + 1][n + 1];

        // Базовый случай: если вторая строка пустая, нужно удалить все символы первой
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // Базовый случай: если первая строка пустая, нужно вставить все символы второй
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполнение таблицы динамического программирования (bottom-up)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, стоимость замены 0, иначе 1
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Три возможных операции:
                int delete  = dp[i - 1][j] + 1;       // удалить символ из one
                int insert  = dp[i][j - 1] + 1;       // вставить символ в two
                int replace = dp[i - 1][j - 1] + cost; // заменить или оставить без изменений

                // Выбираем минимальную стоимость
                dp[i][j] = Math.min(delete, Math.min(insert, replace));
            }
        }

        // Ответ – расстояние для полных строк
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