package by.it.group510902.paleychik.lesson07;

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
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int len1 = one.length();
        int len2 = two.length();

        int[][] table = new int[len1 + 1][len2 + 1];

        // инициализация базы динамики
        for (int i = 0; i <= len1; i++) {
            table[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            table[0][j] = j;
        }

        // заполнение таблицы расстояний
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {

                int replaceCost;

                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    replaceCost = 0;
                } else {
                    replaceCost = 1;
                }

                int delete = table[i - 1][j] + 1;
                int insert = table[i][j - 1] + 1;
                int replace = table[i - 1][j - 1] + replaceCost;

                table[i][j] = Math.min(delete, Math.min(insert, replace));
            }
        }

        // восстановление редакционного предписания
        StringBuilder operations = new StringBuilder();

        int i = len1;
        int j = len2;

        while (i > 0 || j > 0) {

            if (i > 0 && j > 0 &&
                    one.charAt(i - 1) == two.charAt(j - 1) &&
                    table[i][j] == table[i - 1][j - 1]) {

                operations.insert(0, "#,");
                i--;
                j--;
            }

            else if (i > 0 && j > 0 &&
                    table[i][j] == table[i - 1][j - 1] + 1) {

                operations.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }

            else if (i > 0 &&
                    table[i][j] == table[i - 1][j] + 1) {

                operations.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }

            else {

                operations.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        String result = operations.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}