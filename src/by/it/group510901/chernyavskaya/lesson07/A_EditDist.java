package by.it.group510901.chernyavskaya.lesson07;

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

    // Принимает две строки и их текущие индексы
    private int editDistRecursive(String one, String two, int i, int j) {
        //первая строка закончилась (i == 0)
        // Осталось вставить все оставшиеся символы из второй строки
        if (i == 0) {
            return j;
        }
        // если вторая строка закончилась
        // Осталось удалить все оставшиеся символы из первой строки
        if (j == 0) {
            return i;
        }

        // Проверяем, равны ли текущие символы
        // one.charAt(i - 1) - последний символ текущей подстроки
        // two.charAt(j - 1) - последний символ текущей подстроки
        // Если символы равны, то замена не нужна (cost = 0)
        // Если символы разные, то нужна операция замены (cost = 1)
        int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
        // Удаляем последний символ первой строки
        // Уменьшаем индекс i на 1
        // Добавляем 1 операцию удаления
        int delete = editDistRecursive(one, two, i - 1, j) + 1;
        // Вставляем последний символ второй строки в первую
        // Уменьшаем индекс j на 1
        // Добавляем 1 операцию вставки
        int insert = editDistRecursive(one, two, i, j - 1) + 1;
        // Если символы равны: просто копируем
        // Если символы разные: нужно заменить символ
        // Уменьшаем оба индекса на 1
        // Добавляем cost (0 если копирование, 1 если замена)
        int replace = editDistRecursive(one, two, i - 1, j - 1) + cost;

        // выбирает наименьшее из двух чисел
        return Math.min(Math.min(delete, insert), replace);
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Запускаем рекурсивную функцию
        // Передаем строки и их полные длины
        int result = editDistRecursive(one, two, one.length(), two.length());

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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