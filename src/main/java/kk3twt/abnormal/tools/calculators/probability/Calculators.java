package kk3twt.abnormal.tools.calculators.probability;

public class Calculators {
    private Calculators(){}

    // 二项分布
    public static double[] binomial(int n, double p) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] = kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(i, n) * Math.pow(p, i) * Math.pow((1 - p), (n - i));
        }

        return result;
    }

    // 超几何分布
    public static double[] hypergeometry(int n, int M, int N) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] =
                    kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(i, M) * kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination((n - i), (N - M)) /
                            (kk3twt.abnormal.tools.calculators.factorial.Calculators.Combination(n, N) * 1.0);
        }

        return result;
    }

    // 泊松分布
    public static double poisson(int k, int lambda) {
        double e = 2.7182818285;
        return Math.pow(lambda, k) * Math.pow(e, -lambda) / kk3twt.abnormal.tools.calculators.factorial.Calculators.Factorial(k);
    }

    // 几何分布
    public static double geometry(int k, double p) {
        return p * Math.pow((1 - p), (k - 1));
    }
}
