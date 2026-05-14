package by.it.group510901.dremluk.lesson07;

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
/*даны две строки и нужно посчитать количество операций, чтобы они стали одинаковые. у нас создается */
    /* Главный метод - считает расстояние Левенштейна между двумя строками */
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
/*таблица зависимости от индекса и с помощью формулы перехода мы определяем какая операция будет использоваться*/
        /* Длины строк */
        int m = one.length();  // длина первой строки
        int n = two.length();  // длина второй строки

        /*
            Создаём таблицу для ДП
            dp[i][j] = сколько операций нужно, чтобы превратить
            ПЕРВЫЕ i букв строки one в ПЕРВЫЕ j букв строки two
            +1 нужен для пустых строк
        */
        int[][] dp = new int[m + 1][n + 1];

        /* ШАГ 1: Заполняем границы таблицы */

        /* Первый столбец: превращаем строку one в пустую строку */
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  /* нужно удалить i символов */
        }

        /* Первая строка: превращаем пустую строку в строку two */
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  /* нужно вставить j символов */
        }

        /* ШАГ 2: Заполняем всю таблицу */
        for (int i = 1; i <= m; i++) {        // перебираем буквы первой строки
            for (int j = 1; j <= n; j++) {    // перебираем буквы второй строки

                /* Если буквы одинаковые - ничего не делаем */
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];  /* берём ответ из диагонали */
                }

                /* Если буквы разные - выбираем лучшую операцию */
                else {
                    /* ТРИ ОПЕРАЦИИ (стоимость каждой = 1) */
                    int insert = dp[i][j - 1] + 1;   /* вставка: берём значение слева + 1 */
                    int delete = dp[i - 1][j] + 1;   /* удаление: берём значение сверху + 1 */
                    int replace = dp[i - 1][j - 1] + 1; /* замена: берём диагональ + 1 */

                    /* Берём операцию с МИНИМАЛЬНОЙ стоимостью */
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }

        /* ШАГ 3: Ответ в правом нижнем углу таблицы */
        int result = dp[m][n];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    /* ТЕСТИРОВАНИЕ */
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        /* Выводим результаты для трёх пар строк из файла */
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}