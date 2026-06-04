package by.it.group551003.tereshko.lesson07;

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
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        StringBuilder result = new StringBuilder();
        int[][] matrix = new int[one.length() + 1][two.length() + 1];
        matrix[0][0] = 0;
        for (int j = 1; j <= two.length(); j++)
            matrix[0][j] = j;
        for (int i = 1; i <= one.length(); i++)
            matrix[i][0] = i;
        for (int i = 1; i <= one.length(); i++)
            for (int j = 1; j <= two.length(); j++)
                if (one.charAt(i - 1) != two.charAt(j - 1))
                    matrix[i][j] = 1 + min(matrix[i - 1][j], matrix[i][j - 1], matrix[i - 1][j - 1]);
                else
                    matrix[i][j] = matrix[i - 1][j - 1];
        int i = one.length();
        int j = two.length();
        while (i > 0 || j > 0) {
            char c1, c2;
            if (i > 0)
                c1 = one.charAt(i - 1);
            else c1 = 0;

            if (j > 0)
                c2 = two.charAt(j - 1);
            else c2 = 0;
            if (i > 0 && j > 0 && c1 == c2 && matrix[i][j] == matrix[i - 1][j - 1]) {
                result.append(",");
                result.append("#");
                i--;
                j--;
            } else if (i > 0 && j > 0 && matrix[i][j] == matrix[i - 1][j - 1] + 1) {
                result.append("~");
                result.append(c2);
                result.append(",");
                i--;
                j--;
            } else if (i > 0 && matrix[i][j] == matrix[i - 1][j] + 1) {
                result.append("-");
                result.append(c1);
                result.append(",");
                i--;
            } else if (j > 0 && matrix[i][j] == matrix[i][j - 1] + 1) {
                result.append("+");
                result.append(c2);
                result.append(",");
                j--;
            }
        }
        result.reverse();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result.toString();
    }

    int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}