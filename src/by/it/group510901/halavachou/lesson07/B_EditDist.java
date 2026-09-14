package by.it.group510901.halavachou.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        // Для итеративного подхода используем два одномерных массива
        // вместо полной матрицы, чтобы сэкономить память
        int[] prev = new int[n + 1]; // предыдущая строка
        int[] curr = new int[n + 1]; // текущая строка

        // Инициализация первой строки
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // Итеративное заполнение
        for (int i = 1; i <= m; i++) {
            curr[0] = i; // первый элемент строки

            for (int j = 1; j <= n; j++) {
                // Стоимость замены: 0 если символы одинаковые, 1 если разные
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Выбираем минимум из трех операций
                curr[j] = Math.min(
                        prev[j - 1] + cost,                          // замена/совпадение
                        Math.min(
                                prev[j] + 1,                              // удаление
                                curr[j - 1] + 1                           // вставка
                        )
                );
            }

            // Меняем массивы местами для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        int result = prev[n];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}