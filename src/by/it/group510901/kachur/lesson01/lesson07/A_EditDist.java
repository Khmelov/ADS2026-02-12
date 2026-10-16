package by.it.group510901.kachur.lesson01.lesson07;

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

        int[][] memo = new int[one.length() + 1][two.length() + 1];

        // Заполняем таблицу значением -1 (означает "еще не вычислено")
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        // Запускаем рекурсивное вычисление
        return levenshtein(one, two, one.length(), two.length(), memo);
    }

    private int levenshtein(String one, String two, int i, int j, int[][] memo) {
        //  ШАГ 1: Проверяем, не вычисляли ли мы уже это значение
        if (memo[i][j] != -1) {
            return memo[i][j];  // уже есть → просто возвращаем
        }

        // ШАГ 2: Базовые случаи

        // Если первая строка пуста (i == 0)
        if (i == 0) {
            // Нужно вставить все j символов из второй строки
            memo[i][j] = j;
            return memo[i][j];
        }

        // Если вторая строка пуста (j == 0)
        if (j == 0) {
            // Нужно удалить все i символов из первой строки
            memo[i][j] = i;
            return memo[i][j];
        }

        //  ШАГ 3: Сравниваем последние символы

        // Получаем последние символы (индексы i-1 и j-1, т.к. строки с 0)
        char charOne = one.charAt(i - 1);
        char charTwo = two.charAt(j - 1);

        // Если символы одинаковые
        if (charOne == charTwo) {
            // Ничего не делаем, просто отрезаем оба символа и идем дальше
            memo[i][j] = levenshtein(one, two, i - 1, j - 1, memo);
            return memo[i][j];
        }

        //  ШАГ 4: Символы разные — пробуем 3 операции

        //   удаляем символ из первой строки
        //    Уменьшаем i на 1
        //    Платим 1 за операцию
        int delete = levenshtein(one, two, i - 1, j, memo) + 1;

        // 2.  удаляем символ из второй строки (вставляем в первую)
        //    Уменьшаем j на 1 (отрезаем последний символ two)
        //    Платим 1 за операцию
        int insert = levenshtein(one, two, i, j - 1, memo) + 1;

        // 3.  заменяем символ one на символ two
        //    Уменьшаем i и j на 1 (отрезаем оба символа)
        //    Платим 1 за операцию
        int replace = levenshtein(one, two, i - 1, j - 1, memo) + 1;

        // Выбираем минимальную стоимость из трёх
        memo[i][j] = Math.min(delete, Math.min(insert, replace));

        return memo[i][j];
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
