package by.it.group551002.litovchenko.lesson07;

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

    int[][] m;

    int getDistanceEdinting(String one, String two) {
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int w = two.length();
        // Создаем матрицу n+1 на w+1 и заполняем её -1 (признак того, что еще не
        // считали)
        m = new int[n + 1][w + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= w; j++) {
                m[i][j] = -1;
            }
        }
        return editDist(one, two, n, w);
    }

    // Рекурсивная функция
    private int editDist(String s1, String s2, int i, int j) {
        // Базовые случаи: если одна из строк пустая
        if (i == 0)
            return j; // нужно вставить j символов
        if (j == 0)
            return i; // нужно удалить i символов

        // Если мы уже считали это значение, просто возвращаем его
        if (m[i][j] != -1)
            return m[i][j];

        int res;
        // Если символы одинаковы, ничего не платим
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            res = editDist(s1, s2, i - 1, j - 1);
        } else {
            // Если разные, выбираем минимум из 3 операций + 1 штраф
            int replace = editDist(s1, s2, i - 1, j - 1); // Замена
            int insert = editDist(s1, s2, i, j - 1); // Вставка
            int delete = editDist(s1, s2, i - 1, j); // Удаление

            res = Math.min(Math.min(replace, insert), delete) + 1;
        }

        m[i][j] = res; // Сохраняем в кэш
        return res;
    }
    // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
