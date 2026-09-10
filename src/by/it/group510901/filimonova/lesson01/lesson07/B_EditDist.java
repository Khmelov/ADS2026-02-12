package by.it.group510901.filimonova.lesson01.lesson07;

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

        // Оптимизация: используем только две строки вместо полной матрицы
        // Это уменьшает использование памяти с O(m*n) до O(min(m,n))

        // Убеждаемся, что m <= n для оптимального использования памяти
        if (m > n) {
            // Меняем местами строки, чтобы меньшая была первой
            String temp = one;
            one = two;
            two = temp;
            int tempLen = m;
            m = n;
            n = tempLen;
        }

        // Создаем два массива для текущей и предыдущей строки
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // Инициализация первой строки (преобразование пустой строки в префикс one)
        for (int i = 0; i <= m; i++) {
            prev[i] = i;
        }

        // Итеративно заполняем таблицу
        for (int j = 1; j <= n; j++) {
            // Первый элемент текущей строки (преобразование префикса two длины j в пустую строку)
            curr[0] = j;

            char twoChar = two.charAt(j - 1);

            for (int i = 1; i <= m; i++) {
                char oneChar = one.charAt(i - 1);

                // Стоимость замены (0 если символы равны, 1 если разные)
                int cost = (oneChar == twoChar) ? 0 : 1;

                // Минимум из трех операций:
                // 1. Удаление: prev[i] + 1
                // 2. Вставка: curr[i-1] + 1
                // 3. Замена: prev[i-1] + cost
                curr[i] = Math.min(
                        Math.min(prev[i] + 1,      // удаление
                                curr[i-1] + 1),   // вставка
                        prev[i-1] + cost           // замена
                );
            }

            // Меняем местами массивы для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        // Результат находится в prev[m] (последняя строка)
        int result = prev[m];

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