package by.it.group510901.jalilova.lesson07;

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
    // Матрица для хранения уже вычисленных расстояний (мемоизация)
    int[][] m;

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int w = two.length();

        // Инициализируем таблицу мемоизации значением -1
        m = new int[n + 1][w + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= w; j++) {
                m[i][j] = -1;
            }
        }

        // Запускаем рекурсию с конца строк
        return editDistRecursive(one, two, n, w);
    }

    private int editDistRecursive(String s1, String s2, int i, int j) {
        // 1. Базовые случаи (если одна из строк закончилась)
        if (i == 0) return j;
        if (j == 0) return i;

        // 2. Если результат уже есть в таблице — возвращаем его
        if (m[i][j] != -1) {
            return m[i][j];
        }

        // 3. Вычисляем стоимость замены/совпадения
        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        // 4. Рекурсивные вызовы для трех операций
        int res = Math.min(
                Math.min(
                        editDistRecursive(s1, s2, i - 1, j) + 1,    // Удаление
                        editDistRecursive(s1, s2, i, j - 1) + 1     // Вставка
                ),
                editDistRecursive(s1, s2, i - 1, j - 1) + cost  // Замена/совпадение
        );

        // Сохраняем результат в таблицу перед возвратом
        m[i][j] = res;
        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        // Читаем по парам строк из файла
        if (scanner.hasNextLine()) {
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        }
        if (scanner.hasNextLine()) {
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        }
        if (scanner.hasNextLine()) {
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        }
    }
}
