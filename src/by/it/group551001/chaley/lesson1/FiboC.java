package by.it.group551001.chaley.lesson1;

import java.util.ArrayList;
import java.util.List;

public class FiboC {

    private long startTime = System.currentTimeMillis();
    private List<Integer> primes = new ArrayList<>();
    private List<Integer> powers = new ArrayList<>();
    private int pi;

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
        factorise(m);
        countPi();
        int ans = nthFibMod((int)(n % pi), m);
        return ans;
    }

    private void factorise(int m){
        primes.clear();
        powers.clear();
        int lpn = 0;
        if(m%2 == 0){
            primes.add(2);
            powers.add(0);
            lpn = primes.size()-1;
        }
        while (m % 2 == 0) {
            int lp = powers.get(lpn);
            powers.set(lpn, lp+1);
            m /= 2;
        }
        if(m%3 == 0){
            primes.add(3);
            powers.add(0);
            lpn = primes.size()-1;
        }
        while(m%3 == 0){
            int lp = powers.get(lpn);
            powers.set(lpn, lp+1);
            m /= 3;
        }

        for (int i = 5; i <= Math.sqrt(m); i += 6) {
            if(m%i == 0){
                primes.add(i);
                powers.add(0);
                lpn = primes.size()-1;
            }
            while(m%i == 0){
                int lp = powers.get(lpn);
                powers.set(lpn, lp+1);
                m /= i;
            }
            if(m%(i+2) == 0){
                primes.add(i+2);
                powers.add(0);
                lpn = primes.size()-1;
            }
            while(m%(i+2) == 0){
                int lp = powers.get(lpn);
                powers.set(lpn, lp+1);
                m /= i+2;
            }
        }
        if (m > 2) {
            primes.add(m);
            powers.add(1);
        }
    }

    private int gcd(int a, int b){
        while(b != 0){
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    private int lcm(int a, int b){
        return a / gcd(a, b) * b;
    }

    private int piOfPrime(int p){
        if (p == 2) return 3;
        if (p == 5) return 20;
        if((p+1)%5==0 || (p-1)%5==0) return p-1;
        if((p+2)%5==0 || (p-2)%5==0) return 2*(p+1);
        return 0;
    }
    private void countPi(){
        int p = primes.getFirst();
        int k = powers.getFirst();
        pi = (int)Math.pow(p, k-1)*piOfPrime(p);
        for(int i=1; i < primes.size(); i++){
            p = primes.get(i);
            k = powers.get(i);
            pi = lcm(pi, (int)Math.pow(p, k-1)*piOfPrime(p));
        }
    }
    private int nthFibMod(int n, int m){
        if(n==0 || n==1) return 1;
        int fib[] = new int[n+1];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= n; i++)
            fib[i] = (fib[i-1]+(fib[i-2])) % m;
        return fib[n];
    }
}