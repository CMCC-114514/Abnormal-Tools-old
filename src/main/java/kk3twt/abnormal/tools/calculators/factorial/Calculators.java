package kk3twt.abnormal.tools.calculators.factorial;

public class Calculators {

    // 阶乘计算
    public static long Factorial(int num) {
        long result = 1;
        for (int i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    // 排列数
    public static long Permutation(int m, int n) {
        long molecule = Factorial(n);                // 分子：n!
        long denominator = Factorial(n - m);     // 分母：（n-m）!
        return molecule / denominator;
    }

    // 组合数
    public static long Combination(int m, int n) {
        long molecule = Factorial(n);
        long denominator = Factorial(n - m) * Factorial(m);
        return molecule / denominator;
    }
}
