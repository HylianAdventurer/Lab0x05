import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class MyBigInteger {
    private String Value;

    public static final MyBigInteger ZERO = new MyBigInteger("0");
    public static final MyBigInteger ONE = new MyBigInteger("1");
    public static final MyBigInteger TWO = new MyBigInteger("2");
    public static final MyBigInteger TEN = new MyBigInteger("10");

    public static void main(String[] args) {
        Random rng = new Random();
        Scanner in = new Scanner(System.in);
        int N = 1000;

        int successful = 0;

        System.out.println("Verification Testing...");
        for(int i = 0; i < N; i++) {
            // I only supported positive values in my implementation of Big Integers so I made sure that Y is smaller than X
            BigInteger X, Y;
            do {
                X = Lab0x05.randomBigInt();
                Y = Lab0x05.randomBigInt();
            } while (X.compareTo(Y) < 0);

            MyBigInteger MyX = new MyBigInteger(X.toString()), MyY = new MyBigInteger(Y.toString());

            BigInteger Z1 = X.add(Y), Z2 = X.subtract(Y), Z3 = X.multiply(Y);
            MyBigInteger MyZ1 = MyX.Plus(MyY), MyZ2 = MyX.Minus(MyY), MyZ3 = MyX.Times(MyY);
            if(Z1.toString().equals(MyZ1.Value()) && Z2.toString().equals(MyZ2.Value()) && Z3.toString().equals(MyZ3.Value())) successful++;
        }
        System.out.println(successful + " out of " + N);
    }

    public MyBigInteger() {
        Value = "0";
    }

    public MyBigInteger(String value) {
        // Filters string to only allow positive whole numbers
        StringBuilder sb = new StringBuilder(value);
        for(int i = 0; i < sb.length(); i++)
            if(!Character.isDigit(sb.charAt(i)))
                sb.deleteCharAt(i--);
        Value = value;
        Trim();
    }

    public MyBigInteger(int n) {
        Value = "0".repeat(Math.max(0, n));
    }

    public String Value() {
        return Value;
    }

    public String AbbreviatedValue() {
        return Value.length() < 12 ? Value : Value.substring(0,5) + "..." + Value.substring(Value.length() - 5);
    }

    public MyBigInteger Plus(MyBigInteger x) {
        StringBuilder sb = new StringBuilder();
        int carryover = 0;

        for(int i = 1; i <= Math.max(Value.length()+1,x.Value.length()+1); i++) { // Cycles through all digits in each MyBigInteger
            // The sum of adding corresponding numbers
            int sum =
                    (i <= Value.length() ? Value.charAt(Value.length()-i)-48 : 0) +
                            (i <= x.Value.length() ? x.Value.charAt(x.Value.length()-i)-48 : 0) +
                            carryover;
            carryover = sum / 10; // The amount that needs to be carried over
            sb.insert(0, Integer.toString(sum % 10).charAt(0)); // Saves digit to position
        }

        MyBigInteger result = new MyBigInteger(sb.toString());
        result.Trim();
        return result;
    }

    public MyBigInteger Minus(MyBigInteger x) {
        if(compareTo(x) < 0) throw new RuntimeException("Only absolute values currently supported");
        StringBuilder sb = new StringBuilder();
        int carryover = 0;

        for(int i = 1; i <= Math.max(Value.length()+1,x.Value.length()+1); i++) { // Cycles through all digits in each MyBigInteger
            // the difference between the two integers
            int diff =
                    (i <= Value.length() ? Value.charAt(Value.length()-i)-48 : 0) -
                            (i <= x.Value.length() ? x.Value.charAt(x.Value.length()-i)-48 : 0) +
                            carryover;
            carryover = diff < 0 ? -1 : 0; // whether a -1 needs to be taken from the next spot or not
            sb.insert(0, Integer.toString(diff < 0 ? (10 + diff) % 10 : diff % 10).charAt(0)); //Saves digit to position
        }

        MyBigInteger result = new MyBigInteger(sb.toString());
        result.Trim();
        return result;
    }

    public MyBigInteger Times(MyBigInteger x) {
        StringBuilder sb = new StringBuilder("0".repeat(2 * Math.max(Value.length(), x.Value.length())));
        int carryover = 0;

        for(int i = Value.length()-1; i >= 0; i--) { // Cycles through all digits in first MyBigInteger
            for(int j = x.Value.length()-1; j >= 0; j--) { // Cycles through all digits in second MyBigInteger
                // The product of the two digits along with the existing digit in the place and any necessary carryovers
                int prod = sb.charAt(sb.length() - (Value.length() - i) - (x.Value.length() - j) + 1) - 48 + (Value.charAt(i) - 48) * (x.Value.charAt(j) - 48) + carryover;
                carryover = prod / 10; // The amount to be carried over
                sb.setCharAt(sb.length()-(Value.length()-i)-(x.Value.length()-j)+1,Integer.toString(prod%10).charAt(0));
            }
            for(int j = 0; carryover > 0; j++) { // Handle's any remaining carryovers
                sb.setCharAt(sb.length()-(Value.length()-i)-x.Value.length()-j,Integer.toString(carryover % 10).charAt(0));
                carryover /= 10;
            }
        }

        MyBigInteger result = new MyBigInteger(sb.toString());
        result.Trim();

        return result;
    }

    // Compares two MyBigIntegers to determine which one is greater. The meaning behind the numbers only extends to negative means the first one is smaller, zero means they're equal and positive mean's the first is larger
    public int compareTo(MyBigInteger x) {
        Trim();
        x.Trim();
        if(Value.length() != x.Value.length()) return Value.length() - x.Value.length();
        for(int i = 0; i < Value.length(); i++)
            if(Value.charAt(i) != x.Value.charAt(i))
                return Integer.parseInt(Value.substring(i, i+1)) - Integer.parseInt(x.Value.substring(i,i+1));
        return 0;
    }

    public boolean equals(Object x) { // Checks to make sure values are equal
        return compareTo((MyBigInteger)x) == 0;
    }

    private void Trim() { // Trims MyBigInteger of leading 0's, this helps to stay low on memory and help make other functions easier to implement
        StringBuilder sb = new StringBuilder(Value);
        while(sb.charAt(0) == '0' && sb.length() > 1)
            sb.deleteCharAt(0);
        Value = sb.toString();
    }

    public MyBigInteger clone() { // Makes a clone of this MyBigInteger
        return new MyBigInteger(Value);
    }

    public String toString() { // Overrides toString() to return AbbreviatedValue()
        return AbbreviatedValue();
    }
}
