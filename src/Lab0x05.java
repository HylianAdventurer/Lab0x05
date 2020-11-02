import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Lab0x05 {

    private static Random rng = new Random();

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println(" 1: Big Arithmetic Algorithms");
            System.out.println(" 2: Fibonacci Algorithms");
            System.out.println(" 3: MyBigInteger Verification");
            System.out.println("-1: Quit\n");
            System.out.print("Command: ");
            switch(in.nextInt()) {
                case 1:
                    BigAlgorithms();
                    break;
                case 2:
                    FibAlgorithms();
                    break;
                case 3:
                    MyBigInteger.main(args);
                    break;
                case -1:
                    System.out.println("Shutting down...");
                    return;
                default:
                    System.out.println("Command not recognized...");
            }
        }
    }

    private static void BigAlgorithms() throws FileNotFoundException {
        System.out.println("Big Arithmetic Algorithms");
        PrintWriter pw;
        long startTime;
        long time = 0;
        long previousTime = 0;
        long timeout = 60000000000L; // 1 Secs in Nanoseconds
        long overtime = 0;
        long repetitions = 0;

        startTime = getCpuTime();
        for(repetitions = 0; time < timeout && repetitions < 1000; repetitions++)
            time = getCpuTime() - startTime;
        overtime = time / 1000;

        System.out.println("Plus");
        pw = new PrintWriter(new File("Plus.csv"));
        pw.println("N,X1,X2,Time");
        for(int N = 1; previousTime < timeout || repetitions == 1; N *= 2) {
            MyBigInteger X1 = randomMyBigInt(N), X2 = randomMyBigInt(N);
            startTime = getCpuTime();
            for(repetitions = 1; time < timeout && repetitions <= 1000; repetitions++) {
                X1.Plus(X2);
                time = getCpuTime() - startTime;
            }
            time = time / repetitions;
            pw.println(N + "," + X1 + "," + X2 + "," + time);
            previousTime = time;
        }
        pw.close();
        System.out.println("Done");

        previousTime = 0;
        time = 0;

        System.out.println("Times");
        pw = new PrintWriter(new File("Times.csv"));
        pw.println("N,X1,X2,Time");
        for(int N = 1; previousTime < timeout || repetitions == 1; N *= 2) {
            MyBigInteger X1 = randomMyBigInt(N), X2 = randomMyBigInt(N);
            startTime = getCpuTime();
            for(repetitions = 1; time < timeout && repetitions <= 1000; repetitions++) {
                X1.Times(X2);
                time = getCpuTime() - startTime;
            }
            time = time / repetitions;
            pw.println(N + "," + X1 + "," + X2 + "," + time);
            previousTime = time;
        }
        pw.close();
        System.out.println("Done");
    }

    private static void FibAlgorithms() throws FileNotFoundException {
        System.out.println("Fibonacci Algorithms");
        PrintWriter pw;
        long startTime;
        long time = 0;
        long previousTimes[] = new long[10];
        boolean continuing[] = new boolean[10];
        long timeout = 5000000000L; // 1 Secs in Nanoseconds
        long overhead = 0;
        long repetitions = 0;
        MyBigInteger X, factor;

        startTime = getCpuTime();
        for(repetitions = 0; time < timeout && repetitions < 1000; repetitions++)
            time = getCpuTime() - startTime;
        overhead = time / 1000;

        System.out.println("fibLoopBig");
        pw = new PrintWriter(new File("fibLoopBig.csv"));
        X = MyBigInteger.ONE.clone();
        factor = MyBigInteger.ONE.clone();
        for(int i = 0; i < 10; i++) continuing[i] = true;
        pw.println("N,X,fib(X),Time");
        for(int N = 1; previousTimes[0] < timeout || repetitions == 1; N++) {
            for(int i = 1; i < 10; i++) {
                if(continuing[i]) {
                    MyBigInteger fib = MyBigInteger.ZERO.clone();
                    startTime = getCpuTime();
                    for (repetitions = 1; time < timeout && repetitions <= 1000; repetitions++) {
                        fib = fibLoopBig(X);
                        time = getCpuTime() - startTime;
                    }
                    time = (time - overhead * repetitions) / repetitions;

                    pw.println(N + "," + X + "," + fib + "," + time);

                    previousTimes[i] = time;

                    X = X.Plus(factor);
                }
            }
            factor = factor.Times(MyBigInteger.TEN);
        }
        pw.close();

        for(int i = 0; i < 10; i++) {
            previousTimes[i] = 0;
            continuing[i] = true;
        }

        System.out.println("fibMatrixBig");
        pw = new PrintWriter(new File("fibMatrixBig.csv"));
        X = MyBigInteger.ONE.clone();
        factor = MyBigInteger.ONE.clone();
        for(int i = 0; i < 10; i++) continuing[i] = true;
        pw.println("N,X,fib(X),Time");
        for(int N = 1; previousTimes[0] < timeout || repetitions == 1; N++) {
            for(int i = 1; i < 10; i++) {
                if(continuing[i]) {
                    MyBigInteger fib = MyBigInteger.ZERO.clone();
                    startTime = getCpuTime();
                    for (repetitions = 1; time < timeout && repetitions <= 1000; repetitions++) {
                        fib = fibMatrixBig(X);
                        time = getCpuTime() - startTime;
                    }
                    time = (time - overhead * repetitions) / repetitions;

                    pw.println(N + "," + X + "," + fib + "," + time);

                    if(time > timeout) continuing[i] = false;
                    previousTimes[i] = time;

                    X = X.Plus(factor);
                }
            }
            factor = factor.Times(MyBigInteger.TEN);
        }
        pw.close();
    }

    private static int N(int x) {
        return (int) Math.ceil(Math.log(x+1));
    }

    public static MyBigInteger randomMyBigInt(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++)
            sb.append(Math.abs(rng.nextInt() % 10));

        return new MyBigInteger(sb.toString());
    }

    // Random BigIntegers found using method from: https://www.tutorialspoint.com/how-to-generate-a-random-biginteger-value-in-java
    public static BigInteger randomBigInt() {
        BigInteger BigMax = new BigInteger("99999999999999999999"), BigMin = new BigInteger("10000000000000000000");
        BigInteger BigDiff = BigMax.subtract(BigMin);
        int bigLen = BigMax.bitLength();

        BigInteger result  = new BigInteger(bigLen,rng);
        if(result.compareTo(BigMin) < 0)
            result = result.add(BigMin);
        if(result.compareTo(BigDiff) >= 0)
            result = result.mod(BigDiff).add(BigMin);
        return result;
    }

    public static MyBigInteger fibLoopBig(MyBigInteger x) {
        MyBigInteger f1 = MyBigInteger.ONE, f2 = MyBigInteger.ONE;

        for(MyBigInteger i = MyBigInteger.TWO; i.compareTo(x) < 0; i = i.Plus(MyBigInteger.ONE)) {
            f2 = f1.Plus(f2);
            f1 = f2.Minus(f1);
        }

        return f2;
    }

    public static MyBigInteger fibMatrixBig(MyBigInteger x) {
        if(x.equals(MyBigInteger.ZERO) || x.equals(MyBigInteger.ONE)) return x;
        int exponent = Integer.parseInt(x.toString()) - 1;
        MyBigInteger[][] squarePow = new MyBigInteger[][] {{MyBigInteger.ONE,MyBigInteger.ONE},{MyBigInteger.ONE,MyBigInteger.ZERO}}, result = new MyBigInteger[][] {{MyBigInteger.ONE,MyBigInteger.ZERO},{MyBigInteger.ZERO,MyBigInteger.ONE}};

        while(exponent > 0) {
            if(exponent % 2 == 1) {
                result = MatrixMultiplication(squarePow, result);
            }
            squarePow = MatrixMultiplication(squarePow, squarePow);
            exponent /= 2;
        }

        return result[0][0];
    }

    private static MyBigInteger[][] MatrixMultiplication(MyBigInteger[][] Matrix1, MyBigInteger[][] Matrix2) {
        Matrix2 = new MyBigInteger[][] {
                {Matrix1[0][0].Times(Matrix2[0][0]).Plus(Matrix1[0][1].Times(Matrix2[1][0])), Matrix1[0][0].Times(Matrix2[0][1]).Plus(Matrix1[0][1].Times(Matrix2[1][1]))},
                {Matrix1[1][0].Times(Matrix2[0][0]).Plus(Matrix1[1][1].Times(Matrix2[1][0])), Matrix1[1][0].Times(Matrix2[0][1]).Plus(Matrix1[1][1].Times(Matrix2[1][1]))}
        };
        return Matrix2;
    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}
