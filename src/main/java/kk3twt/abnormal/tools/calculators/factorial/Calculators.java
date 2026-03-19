package kk3twt.abnormal.tools.calculators.factorial;

/**
 * 阶乘计算静态类
 * 也处理排列数与组合数的计算
 */
public class Calculators {

    /**
     * 阶乘计算
     * 阶乘是指从1到某个正整数n的所有整数相乘的积，记作n!
     *
     * @param num 阶乘数
     * @return 阶乘计算结果
     */
    public static long Factorial(int num) {
        long result = 1;
        for (int i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * 排列数计算 A(m, n)
     * 从n个不同的元素中任取m(m≤n)个元素的所有排列的个数，
     * 叫做从n个不同的元素中取出m(m≤n)个元素的排列数。
     *
     * @param m 取出的元素个数
     * @param n 总元素个数
     * @return 计算结果
     */
    public static long Permutation(int m, int n) {
        long molecule = Factorial(n);                // 分子：n!
        long denominator = Factorial(n - m);     // 分母：（n-m）!
        return molecule / denominator;
    }

    /**
     * 组合数计算 C(m, n)
     * 从n个不同元素中，任取m(m≤n)个元素并成一组，叫做从n个不同元素中取出m个元素的一个组合；
     * 从n个不同元素中取出m(m≤n)个元素的所有组合的个数，叫做从n个不同元素中取出m个元素的组合数。
     *
     * @param m 取出的元素个数
     * @param n 总元素个数
     * @return 计算结果
     */
    public static long Combination(int m, int n) {
        long molecule = Factorial(n);
        long denominator = Factorial(n - m) * Factorial(m);
        return molecule / denominator;
    }
}
