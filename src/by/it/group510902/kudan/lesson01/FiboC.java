package by.it.group510902.kudan.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        java.util.ArrayList<Long> list = new java.util.ArrayList<>();
        list.add(0L);
        list.add(1L);

        for(int i = 2; i < m * 6; i++) {
            list.add((list.get(i - 1) + list.get(i - 2)) % m);

            if(list.get(i) == 1 && list.get(i - 1) == 0) {
                list.remove(list.size() - 1);
                list.remove(list.size() - 1);
                break;
            }
        }

        int period = list.size();
        return list.get((int) (n % period));
    }


}

