package calculators.probability;

public class Calculators {
    private Calculators(){}

    // 二项分布
    public static double[] binomial(int n, double p) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] = calculators.factorial.Calculators.Combination(i, n) * Math.pow(p, i) * Math.pow((1 - p), (n - i));
        }

        return result;
    }

    // 超几何分布
    public static double[] hypergeometric(int n, int M, int N) {
        double[] result = new double[n + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] =
                    calculators.factorial.Calculators.Combination(i, M) * calculators.factorial.Calculators.Combination((n - i), (N - M)) /
                            (calculators.factorial.Calculators.Combination(n, N) * 1.0);
        }

        return result;
    }

    // 泊松分布
    public static double[] poisson(int k, int lambda) {
        double e = 2.7182818285;
        double[] result = new double[k + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] = Math.pow(lambda, i) * Math.pow(e, -lambda) / calculators.factorial.Calculators.Factorial(i);
        }

        return result;
    }

    // 几何分布
    public static double[] geometric(int k, int r, double p) {
        double[] result = new double[k];

        for (int i = 0; i < result.length; i++) {
            result[i] = calculators.factorial.Calculators.Combination(r - 1, k - 1) * Math.pow(p, r) * Math.pow((1 - p), (k - r));
        }

        return result;
    }
}
