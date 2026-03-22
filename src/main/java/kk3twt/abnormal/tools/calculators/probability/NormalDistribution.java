package kk3twt.abnormal.tools.calculators.probability;

public class NormalDistribution {

    private static final double PI = 3.1415926535;
    private static final double E = 2.7182818285;

    private double mu;
    private double sigma;

    public NormalDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    public double normal(double x) {
        double z = (x - mu) / sigma;        // 标准化
        return (1 / Math.sqrt(2 * PI)) * Math.pow(E, -z*z / 2);     // 返回标准正态分布值
    }
    
    private double integral(double a, double x, double tolerance, int maxDepth) {
        double mid = (a + x) / 2;
        double s = simpson(a, x);
        double s_left = simpson(a, mid);
        double s_right = simpson(mid, x);

        if (maxDepth <= 0 || Math.abs(s_left + s_right - s) < 15 * tolerance) {
            return s_left + s_right + (s_left + s_right - s) / 15;
        }

        return integral(a, mid, tolerance / 2, maxDepth - 1) + integral(mid, x, tolerance / 2, maxDepth - 1);
    }

    private double simpson(double a, double b) {
        return (b - a) / 6 * (normal(a) + 4 * normal((a + b) / 2) + normal(b));
    }

    public double calculate(double x) {
        double lowerLimit = mu - 4 * sigma;
        return integral(lowerLimit, x, 1e-7, 50);
    }
}