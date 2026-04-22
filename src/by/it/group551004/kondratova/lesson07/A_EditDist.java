package by.it.group551004.kondratova.lesson07;

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
        Вычисляет минимальное количество операций
    (вставка, удаление, замена), необходимых для превращения одной строки в другую.
Входные данные — две строки one и two.
Вызов рекурсивной функции — editDistRecursive(one, two, длина one, длина two).
Базовый случай 1 — если первая строка пуста (i == 0), то нужно вставить все символы второй строки. Возвращается j.
Базовый случай 2 — если вторая строка пуста (j == 0), то нужно удалить все символы первой строки. Возвращается i.
Совпадение символов — если последние символы строк совпадают (one.charAt(i-1) == two.charAt(j-1)), то ничего делать не нужно.
Рекурсивно вызывается функция для строк без последних символов.
Если символы разные — вычисляются три варианта:
Вставка — добавляем символ из второй строки в первую (+1 операция), рекурсивно обрабатываем оставшуюся часть второй строки.
Удаление — удаляем символ из первой строки (+1 операция), рекурсивно обрабатываем оставшуюся часть первой строки.
Замена — заменяем символ первой строки на символ второй (+1 операция), рекурсивно обрабатываем оставшиеся части обеих строк.
Выбор минимума — из трёх вариантов выбирается наименьшее количество операций.
Возврат результата — полученное минимальное расстояние возвращается наверх.
*/

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        return editDistRecursive(one, two, one.length(), two.length());
    }

    private int editDistRecursive(String one, String two, int i, int j) {
        // Базовые случаи
        if (i == 0) {
            return j; // нужно вставить j символов
        }
        if (j == 0) {
            return i; // нужно удалить i символов
        }

        // Если символы совпадают, рекурсивно обрабатываем остаток
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            return editDistRecursive(one, two, i - 1, j - 1);
        }

        // Иначе берём минимум из трёх операций:
        // 1. Вставка (insert) - обрабатываем j-1
        // 2. Удаление (delete) - обрабатываем i-1
        // 3. Замена (replace) - обрабатываем i-1 и j-1
        int insert = editDistRecursive(one, two, i, j - 1) + 1;
        int delete = editDistRecursive(one, two, i - 1, j) + 1;
        int replace = editDistRecursive(one, two, i - 1, j - 1) + 1;

        return Math.min(Math.min(insert, delete), replace);
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