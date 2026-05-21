package by.it.group510902.shust.lesson07;

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

    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
// Инициализируем таблицу мемоизации значениями -1
        memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return solve(one, two, one.length(), two.length());
    }

    private int solve(String s1, String s2, int i, int j) {
        // Базовые случаи: если одна из строк пустая, возвращаем длину второй
        if (i == 0) return j;
        if (j == 0) return i;

        // Если значение уже вычислено, берем его из кэша
        if (memo[i][j] != -1) return memo[i][j];

        // Стоимость замены (0, если символы равны, 1, если отличаются)
        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        // Рекурсивно находим минимум из трех операций: удаление, вставка, замена/совпадение
        memo[i][j] = Math.min(
                Math.min(solve(s1, s2, i - 1, j) + 1, solve(s1, s2, i, j - 1) + 1),
                solve(s1, s2, i - 1, j - 1) + cost
        );

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
