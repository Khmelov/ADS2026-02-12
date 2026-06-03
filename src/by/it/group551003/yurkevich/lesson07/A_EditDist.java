package by.it.group551003.yurkevich.lesson07;

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

    public static int EDITDISTTD(String A, String B, int i, int j, int[][] D){
        if (D[i][j] != -1){
            return D[i][j];
        }
        if (i == 0){
            D[i][j] = j;
        }
        else if (j == 0){
            D[i][j] = i;
        }
        else{
            int c;
            if (A.charAt(i - 1) == B.charAt(j - 1)){
                c = 0;
            } else{
                c = 1;
            }

            int ins = EDITDISTTD(A, B, i, j - 1, D);
            int del = EDITDISTTD(A, B, i - 1, j, D);
            int sub = EDITDISTTD(A, B, i - 1, j - 1, D);

            D[i][j] = Math.min(ins + 1, Math.min(del + 1, sub + c));
        }
        return D[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        int result = 0;

        int D[][] = new int[one.length() + 1][two.length() + 1];

        for(int i =0; i <= one.length(); i++){
            for(int j =0; j <= two.length(); j++){
                D[i][j] = -1;
            }
        }

        result = EDITDISTTD(one, two, one.length(), two.length(), D);
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
