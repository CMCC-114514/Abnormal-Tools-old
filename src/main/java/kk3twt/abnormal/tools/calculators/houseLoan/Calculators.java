package kk3twt.abnormal.tools.calculators.houseLoan;

/**
 * 房贷计算工具类。
 * 提供两种常见的还款方式计算：等额本息和等额本金。
 * 所有方法均假设输入的参数为正数，调用者需确保输入有效。
 */
public class Calculators {

    /**
     * 两种还款方式的名称，用于界面下拉框显示。
     */
    public static final String[] PATTERNS = {"等额本息", "等额本金"};

    /**
     * 等额本息还款方式计算每月还款额。
     * 公式：每月还款额 = [本金 × 月利率 × (1+月利率)^还款月数] / [(1+月利率)^还款月数 - 1]
     *
     * @param principal  贷款本金（单位：元）
     * @param rate       月利率（例如年利率4.9%除以12后为0.0040833...）
     * @param totalMonth 还款总月数
     * @return 每月应还款额（单位：元）
     */
    public static double interest(double principal, double rate, int totalMonth) {
        double up = principal * rate * Math.pow((1 + rate), totalMonth);   // 分子部分
        double down = Math.pow((1 + rate), totalMonth) - 1;                // 分母部分
        return up / down;
    }

    /**
     * 等额本金还款方式计算每月还款额。
     * 每月还款额 = (贷款本金 / 还款月数) + (剩余本金 × 月利率)
     *
     * @param principal  贷款本金（单位：元）
     * @param rate       月利率
     * @param totalMonth 还款总月数
     * @return 长度为 totalMonth 的 double 数组，每个元素对应第 i+1 个月的还款额（单位：元）
     */
    public static double[] capital(double principal, double rate, int totalMonth) {
        double avePrincipal = principal / totalMonth;          // 每月固定偿还的本金
        double[] result = new double[totalMonth];

        for (int i = 0; i < result.length; i++) {
            // 剩余本金 = 总本金 - 已归还本金总额
            double remainingPrincipal = principal - avePrincipal * i;
            // 当月还款额 = 固定本金 + 剩余本金 × 月利率
            result[i] = avePrincipal + remainingPrincipal * rate;
        }
        return result;
    }
}