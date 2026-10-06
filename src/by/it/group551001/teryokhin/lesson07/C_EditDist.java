package by.it.group551001.teryokhin.lesson07;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        int n = one.length();
        int m = two.length();
        int[][] dp = new int[n + 1][m + 1];
        char[][] action = new char[n + 1][m + 1];
        for(int[] line : dp) Arrays.fill(line, (int)1e9);
        dp[0][0] = 0;
        for(int i = 0;i <= n;i++)
        {
            for(int j = 0;j <= m;j++)
            {
                if(i != 0)
                {
                    if(dp[i - 1][j] + 1 < dp[i][j])
                    {
                        dp[i][j] = dp[i - 1][j] + 1;
                        action[i][j] = '-';
                    }
                }
                if(j != 0)
                {
                    if(dp[i][j - 1] + 1 < dp[i][j])
                    {
                        dp[i][j] = dp[i][j - 1] + 1;
                        action[i][j] = '+';
                    }
                }
                if(i != 0 && j != 0)
                {
                    Boolean differ = one.charAt(i - 1) != two.charAt(j - 1);
                    if(dp[i - 1][j - 1] + (differ ? 1 : 0) < dp[i][j])
                    {
                        dp[i][j] = dp[i - 1][j - 1] + (differ ? 1 : 0);
                        action[i][j] = differ ? '~' : '#';
                    }
                }
            }
        }
        String result = "";
        int i = n, j = m;
        while(i != 0 || j != 0)
        {
            result += ',';
            char prev_action = action[i][j];

            if(action[i][j] == '-')
            {
                result += one.charAt(i - 1);
                i--;
            }
            else if(action[i][j] == '+')
            {
                result += two.charAt(j - 1);
                j--;
            }
            else if(action[i][j] == '~')
            {
                result += two.charAt(j - 1);
                i--;
                j--;
            }
            else if(action[i][j] == '#')
            {
                i--;
                j--;
            }

            result += prev_action;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return new StringBuilder(result).reverse().toString();
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