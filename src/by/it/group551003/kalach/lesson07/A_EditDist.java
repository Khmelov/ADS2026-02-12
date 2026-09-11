package by.it.group551003.kalach.lesson07;

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
        int n = one.length();
        int m = two.length();

        // Создаем таблицу для запоминания результатов (мемоизация)
        int[][] cache = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                cache[i][j] = -1;
            }
        }

        return getLevenshteinDist(one, two, n, m, cache);
    }

    // Рекурсивный метод с мемоизацией
    private int getLevenshteinDist(String s1, String s2, int i, int j, int[][] cache) {
        if (cache[i][j] != -1) {
            return cache[i][j];
        }

        // Базовые случаи (если одна из строк пустая)
        if (i == 0) return cache[i][j] = j;
        if (j == 0) return cache[i][j] = i;

        // Стоимость замены символа
        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        // Три варианта: удаление, вставка, замена
        int d1 = getLevenshteinDist(s1, s2, i - 1, j, cache) + 1;
        int d2 = getLevenshteinDist(s1, s2, i, j - 1, cache) + 1;
        int d3 = getLevenshteinDist(s1, s2, i - 1, j - 1, cache) + cost;

        // Находим минимум из трех вариантов
        int res = d1;
        if (d2 < res) res = d2;
        if (d3 < res) res = d3;

        return cache[i][j] = res;
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
