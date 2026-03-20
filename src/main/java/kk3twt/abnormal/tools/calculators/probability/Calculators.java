package kk3twt.abnormal.tools.calculators.probability;

/**
 * 概率分布计算工具类。
 * 提供四种常见离散概率分布的计算：二项分布、超几何分布、泊松分布、几何分布。
 * 方法中调用了阶乘工具类 {@code kk3twt.abnormal.tools.calculators.factorial.Calculators}。
 */
public class Calculators {

    private static final double E = 2.7182818285;
    private static final double PI = 3.1415926535;

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
     * P(X <= k) = Σ_{i=0}^{k} (λ^i * e^{-λ}) / i!
     *
     * @param k      随机变量取值（非负整数）
     * @param lambda 泊松分布的参数 λ（均值）
     * @return P(X <= k) 的概率值
     */
    public static double poisson(int k, double lambda) {
        double sum = 0;
        for (int i = 0; i <= k; i++) {
            sum += Math.pow(lambda, i) * Math.pow(E, -lambda) / kk3twt.abnormal.tools.calculators.factorial.Calculators.Factorial(i);
        }
        return sum;
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

    /**
     * 计算平均分布的概率函数
     * X ~ U(a, b)，
     * 当 x < a 时 P(X <= x) = 0；
     * 当 x > b 时 P(X <= x) = 1；
     * 当 a <= x <= b 时 P(X <= x) = (x - a) / (b - a)
     * 
     * @param a 分布的下界
     * @param b 分布的上界
     * @param x 随机变量取值
     * @return P(X <= x) 的概率值
     */
    public static double uniform(double a, double b, double x) {
        if (x <= a) {
            return 0;
        } else if (x >= b) {
            return 1;
        } else {
            return (x - a) / (double)(b - a);
        }
    }

    /**
     * 计算指数分布的概率函数
     * X ~ E(lambda)，
     * 当 x < 0 时 P(X <= x) = 0；
     * 当 x >= 0 时 P(X <= x) = 1 - e^(-lambda * x)
     * 
     * @param lambda
     * @param x
     * @return
     */
    public static double exponential(double lambda, double x) {
        if (x <= 0) {
            return 0;
        } else {
            return 1 - Math.pow(E, -lambda * x);
        }
    }

    // public static double normal(double mu, double sigma, double x) {
    //     double z = (x - mu) / sigma;
    //     return 0.5 * (1 + Math.erf(z / Math.sqrt(2)));
    // }
}