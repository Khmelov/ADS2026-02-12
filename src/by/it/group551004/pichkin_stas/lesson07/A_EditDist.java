package by.it.group551004.pichkin_stas.lesson07;

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



    int isCharEqual(Character a, Character b) {
        return a.equals(b) ? 0 : 1;
    }

    int dist(int i, int j, String one, String two) {
        if (i == 0) return j;
        if (j == 0) return i;
        int insert = dist(i, j - 1, one, two) + 1;
        int delete = dist(i - 1, j, one, two) + 1;
        int sub = dist(i - 1, j - 1, one, two) + isCharEqual(one.charAt(i - 1), two.charAt(j - 1));
        return Math.min(insert, Math.min(delete, sub));
    }
    int getDistanceEdinting(String one, String two) {
        return dist(one.length(), two.length(), one, two);
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