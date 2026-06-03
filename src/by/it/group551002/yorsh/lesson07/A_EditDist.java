package by.it.group551002.yorsh.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
*/

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();
        // таблица для мемоизации: memo[i][j] – расстояние Левенштейна для
        // первых i символов one и первых j символов two
        int[][] memo = new int[m + 1][n + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return editDist(one, two, m, n, memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    // рекурсивная функция с мемоизацией (нисходящее ДП)
    private int editDist(String one, String two, int i, int j, int[][] memo) {
        // базовые случаи
        if (i == 0) return j; // все символы two нужно вставить
        if (j == 0) return i; // все символы one нужно удалить

        // если ответ для данной пары префиксов уже вычислен
        if (memo[i][j] != -1) return memo[i][j];

        // если последние символы одинаковы – штраф за замену отсутствует
        int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

        // минимум из трёх операций: удаление, вставка, замена/совпадение
        int delete  = editDist(one, two, i - 1, j, memo) + 1;   // удалить из one
        int insert  = editDist(one, two, i, j - 1, memo) + 1;   // вставить в two
        int replace = editDist(one, two, i - 1, j - 1, memo) + cost;

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