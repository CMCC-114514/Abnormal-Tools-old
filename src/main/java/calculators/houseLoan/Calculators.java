package calculators.houseLoan;

public class Calculators {
    public static final String[] PATTERNS = {"等额本息", "等额本金"};

    // 等额本息还款:返回每个月还款额
    public static double interest(double principal, double rate, int totalMonth) {
        double up = principal * rate * Math.pow((1+rate), totalMonth);   // 分母
        double down = Math.pow((1+rate), totalMonth) - 1;                // 分子
        return up / down;
    }

    // 等额本金还款:返回每个月还款额
    public static double[] capital(double principal, double rate, int totalMonth) {
        double avePrincipal = principal / totalMonth;
        double aveInterest;

        double[] result = new double[totalMonth];
        for (int i = 0; i < result.length; i++) {
            aveInterest = principal - avePrincipal * i;
            result[i] = avePrincipal + aveInterest * rate;
        }
        return result;
    }
}
