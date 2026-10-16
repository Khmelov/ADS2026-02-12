package by.it.group510902.paleychik.lesson07;

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

        int len1 = one.length();
        int len2 = two.length();

        int[][] memory = new int[len1 + 1][len2 + 1];

        // заполняем массив значением -1
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                memory[i][j] = -1;
            }
        }

        int result = calculateDistance(one, two, len1, len2, memory);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int calculateDistance(String one, String two, int i, int j, int[][] memory) {

        // если значение уже вычислено
        if (memory[i][j] != -1) {
            return memory[i][j];
        }

        // если первая строка пустая
        if (i == 0) {
            return j;
        }

        // если вторая строка пустая
        if (j == 0) {
            return i;
        }

        // определяем стоимость замены
        int diff;

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            diff = 0;
        } else {
            diff = 1;
        }

        // удаление символа
        int delete = calculateDistance(one, two, i - 1, j, memory) + 1;

        // вставка символа
        int insert = calculateDistance(one, two, i, j - 1, memory) + 1;

        // замена символа
        int replace = calculateDistance(one, two, i - 1, j - 1, memory) + diff;

        // выбираем минимальный вариант
        int answer = Math.min(delete, Math.min(insert, replace));

        // сохраняем результат
        memory[i][j] = answer;

        return answer;
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