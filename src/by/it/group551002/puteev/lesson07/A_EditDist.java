package by.it.group551002.puteev.lesson07;

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


    // Матрица для мемоизации (кеширования)
    int[][] cache;

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        cache = new int[n + 1][m + 1];

        // Инициализируем кеш значениями -1 (означает, что результат еще не вычислен)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                cache[i][j] = -1;
            }
        }

        return recursion(n, m, one, two);
    }

    private int recursion(int i, int j, String s1, String s2) {
        // Базовые случаи: если одна из строк пуста, расстояние равно длине другой
        if (i == 0) return j;
        if (j == 0) return i;

        // Если результат уже есть в кеше, возвращаем его
        if (cache[i][j] != -1) {
            return cache[i][j];
        }

        // Вычисляем стоимость замены (0 если символы равны, 1 если разные)
        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        // Рекурсивные переходы:
        int ins = recursion(i, j - 1, s1, s2) + 1;    // Вставка
        int del = recursion(i - 1, j, s1, s2) + 1;    // Удаление
        int sub = recursion(i - 1, j - 1, s1, s2) + cost; // Замена

        // Выбираем минимум из трех операций и сохраняем в кеш
        int res = Math.min(Math.min(ins, del), sub);
        cache[i][j] = res;

        return res;
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
