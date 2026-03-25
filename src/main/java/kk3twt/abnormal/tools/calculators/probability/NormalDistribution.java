package kk3twt.abnormal.tools.calculators.probability;

public class NormalDistribution {

    private static final double PI = 3.141592653589793;
    private static final double E = 2.718281828459045;

    private final double mu;
    private final double sigma;

    public NormalDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    public double normal(double x) {
        double exp = -Math.pow(x - mu, 2) / (2 * sigma * sigma);
        return (1 / (Math.sqrt(2 * PI) * sigma)) * Math.pow(E, exp);     // 返回正态分布值
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

    public static void main(String[] args) {
        NormalDistribution nb = new NormalDistribution(0,1);
        System.out.println(nb.calculate(-4));
    }
}