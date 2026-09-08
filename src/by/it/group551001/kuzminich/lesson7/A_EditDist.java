package by.it.group551001.kuzminich.lesson7;

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
    int get_min(int a, int b, int c){
        if (a<b){
            return (a<c) ? a : c;
        }else{
            return (b<c) ? b : c;
        }
    }
    int distance(int i, int j, String one, String two, int[][] D){
        if (i == 0 && j == 0){
            return 0;
        }
        if (j == 0 && i>0){
            D[i][j] = i;
            return i;
        }
        if (i==0 && j>0){
            D[i][j] = j;
            return j;
        }
        if (D[i][j] == -1) {
            int dist = get_min(distance(i, j - 1, one, two, D) + 1, distance(i - 1, j, one, two, D) + 1, distance(i - 1, j - 1, one, two, D) + ((one.charAt(i-1) == two.charAt(j-1) ? 0 : 1)));
            D[i][j] = dist;
            return dist;
        } else{
            return D[i][j];
        }
    }
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int l1 = one.length();
        int l2 = two.length();
        int [][] D = new int [l1+1][l2+1];
        for (int i=0; i<=l1; i++){
            for (int j = 0; j<=l2; j++){
                D[i][j] = -1;
            }
        }
        int result = distance( l1, l2, one, two, D);


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
