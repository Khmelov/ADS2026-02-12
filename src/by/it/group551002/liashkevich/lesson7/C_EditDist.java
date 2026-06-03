package by.it.group551002.liashkevich.lesson7;

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
        String result = "";
        int M = one.length();
        int N = two.length();
        int[][] D = new int[M + 1][N + 1];
        for (int j = 0; j <= N; j++)
            D[0][j] = j;
        for (int i = 0; i <= N; i++)
            D[i][0] = i;
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                int replaceCost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                D[i][j] = Math.min(D[i][j-1]+1, Math.min(D[i-1][j] + 1, D[i-1][j-1] + replaceCost));
            }
        }
        StringBuilder ops = new StringBuilder();
        int i = M;
        int j = N;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1) && D[i][j] == D[i-1][j-1]) {
                ops.append('#').append(',');
                i--;
                j--;
            }
            else if (i > 0 && j > 0 && D[i][j] == D[i-1][j-1] + 1) {
                ops.append(two.charAt(j-1)).append('~').append(',');
                i--;
                j--;
            }
            else if (i > 0 && D[i][j] == D[i-1][j] + 1) {
                ops.append(one.charAt(i-1)).append('-').append(',');
                i--;
            }
            else if (j > 0 && D[i][j] == D[i][j-1] + 1) {
                ops.append(two.charAt(j-1)).append('+').append(',');
                j--;
            }
        }
        ops = ops.reverse();
        ops.delete(0, 1);
        ops.append(',');
        result = ops.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}