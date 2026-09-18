package by.it.group551003.tarasau.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        ArrayList<Operation> operations = getEditScript(one, two);

        String str = operations.toString();

        str = String.copyValueOf(str.toCharArray(),1,str.length()-2);

        StringBuilder str2 = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ')
                str2.append(str.charAt(i));
        }
        str2.append(',');

        String result = str2.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static ArrayList<Operation> getEditScript(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        int[][] memo = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            memo[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            memo[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    memo[i][j] = memo[i - 1][j - 1];
                } else {
                    memo[i][j] = 1 + Math.min(Math.min(memo[i - 1][j], memo[i][j - 1]), memo[i - 1][j - 1]);
                }
            }
        }

        // restore string
        ArrayList<Operation> operations = new ArrayList<>();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && s1.charAt(i - 1) == s2.charAt(j - 1)) {
                // symbols matching - we're copying
                operations.add(new Operation('#', s1.charAt(i - 1)));
                i--;
                j--;
            }
            else if (i > 0 && j > 0 && memo[i][j] == memo[i - 1][j - 1] + 1) {
                // change
                operations.add(new Operation('~', s2.charAt(j - 1)));
                i--;
                j--;
            }
            else if (i > 0 && memo[i][j] == memo[i - 1][j] + 1) {
                // delete symbol from s1
                operations.add(new Operation('-', s1.charAt(i - 1)));
                i--;
            }
            else if (j > 0 && memo[i][j] == memo[i][j - 1] + 1) {
                // insert into s1 from s2
                operations.add(new Operation('+', s2.charAt(j - 1)));
                j--;
            }
        }

        // reverse cuz we went back to front
        Collections.reverse(operations);

        return operations;
    }


    static class Operation {
        char type;  // '+', '-', '~', '#'
        char symbol;

        Operation(char type, char symbol) {
            this.type = type;
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            if (this.type == '#') {
                return type + "";
            } else {
                return type + "" + symbol;
            }

        }
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