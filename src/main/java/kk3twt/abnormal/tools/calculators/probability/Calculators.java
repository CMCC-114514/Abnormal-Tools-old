package kk3twt.abnormal.tools.calculators.probability;

/**
 * 概率分布计算工具类。
 * 提供四种常见离散概率分布的计算：二项分布、超几何分布、泊松分布、几何分布。
 * 方法中调用了阶乘工具类 {@code kk3twt.abnormal.tools.calculators.factorial.Calculators}。
 */
public class Calculators {

    /**
     * 私有构造方法，防止外部实例化工具类。
     */
    private Calculators() {}

    /**
     * 计算二项分布的概率质量函数。
     * 对于 X ~ B(n, p)，返回数组 result[i] = P(X = i) (i = 0..n)。
     *
     * @param n 试验总次数（非负整数）
     * @param p 每次试验中事件发生的概率 (0 <= p <= 1)
     * @return 长度为 n+1 的 double 数组，result[i] 对应 P(X = i)
     */
    public static double[] binomial(int n, double p) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            // P(X = i) = C(n, i) * p^i * (1-p)^(n-i)
            result[i] = kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(i, n)
                    * Math.pow(p, i)
                    * Math.pow((1 - p), (n - i));
        }

        return result;
    }

    /**
     * 计算超几何分布的概率质量函数。
     * 对于 X ~ H(n, M, N)，返回数组 result[i] = P(X = i) (i = 0..n)。
     *
     * @param n 抽取的物件数（不放回）
     * @param M 总体中指定种类的物件个数
     * @param N 总体物件总数
     * @return 长度为 n+1 的 double 数组，result[i] 对应 P(X = i)
     */
    public static double[] hypergeometry(int n, int M, int N) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            // P(X = i) = C(M, i) * C(N-M, n-i) / C(N, n)
            result[i] =
                    kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(i, M)
                            * kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination((n - i), (N - M))
                            / (kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(n, N) * 1.0);
        }

        return result;
    }

    /**
     * 计算泊松分布的概率质量函数。
     * P(X = k) = (λ^k * e^{-λ}) / k!
     *
     * @param k      随机变量取值（非负整数）
     * @param lambda 泊松分布的参数 λ（均值）
     * @return P(X = k) 的概率值
     */
    public static double poisson(int k, int lambda) {
        double e = 2.7182818285; // 自然常数 e 的近似值
        return Math.pow(lambda, k) * Math.pow(e, -lambda)
                / kk3twt.abnormal.tools.calculators.factorial.Calculators.Factorial(k);
    }

    /**
     * 计算几何分布的概率质量函数。
     * P(X = k) = p * (1-p)^(k-1)，其中 k 为首次成功所需的试验次数。
     *
     * @param k 首次成功时的试验次数（k >= 1）
     * @param p 每次试验中事件发生的概率 (0 < p <= 1)
     * @return P(X = k) 的概率值
     */
    public static double geometry(int k, double p) {
        return p * Math.pow((1 - p), (k - 1));
    }
}