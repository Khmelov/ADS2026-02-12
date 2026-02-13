package by.it.group551002.shinkevich.lesson01;

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
		long[] s = new long[m * 6 + 2];

		s[0] = 0;
		s[1] = 1;

		for (int i = 2; i < m * 6 + 2; i++) {
			s[i] = (s[i - 1] + s[i - 2]) % m;

			if (s[i] == 1 && s[i - 1] == 0) {
				long period = i - 1;
				int index = (int) (n % period);
				return s[index];
			}
		}

		return s[(int) n];
    }


}

