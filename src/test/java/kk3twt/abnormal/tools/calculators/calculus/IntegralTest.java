package kk3twt.abnormal.tools.calculators.calculus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 积分计算类 Integral 的单元测试。
 * 测试一次函数、二次函数、三角函数、指数/对数函数的数值积分，
 * 以及边界情况（区间为零、对数函数定义域等）。
 */
class IntegralTest {

    private static final double EPSILON = 1e-6;  // 允许的误差范围
    private static final double TOLERANCE = 1e-8; // 积分精度
    private static final int MAX_DEPTH = 50;      // 最大递归深度

    // 1. 测试一次函数 y = m*x + n
    @Test
    void testLinearFunction() {
        // y = 2x + 1, 区间 [0, 1] 解析解 = 2
        Integral integral = new Integral(2.0, 1.0);
        double result = integral.calculate(0.0, 1.0, 0, TOLERANCE, MAX_DEPTH);
        assertEquals(2.0, result, EPSILON, "一次函数积分结果偏差过大");
    }

    // 2. 测试二次函数 y = a*x² + b*x + c
    @Test
    void testQuadraticFunction() {
        // y = x², 区间 [0, 1] 解析解 = 1/3 ≈ 0.3333333
        Integral integral = new Integral(1.0, 0.0, 0.0);
        double result = integral.calculate(0.0, 1.0, 1, TOLERANCE, MAX_DEPTH);
        assertEquals(1.0 / 3.0, result, EPSILON, "二次函数积分结果偏差过大");
    }

    // 3. 测试正弦函数 y = A*sin(ω*x + φ)
    @Test
    void testSineFunction() {
        // y = sin(x), 区间 [0, π] 解析解 = 2
        Integral integral = new Integral(1.0, 1.0, 0.0, false);
        double result = integral.calculate(0.0, Math.PI, 2, TOLERANCE, MAX_DEPTH);
        assertEquals(2.0, result, EPSILON, "正弦函数积分结果偏差过大");
    }

    // 4. 测试余弦函数 y = A*cos(ω*x + φ)  (通过 isCos 标志)
    @Test
    void testCosineFunction() {
        // y = cos(x), 区间 [0, π/2] 解析解 = 1
        Integral integral = new Integral(1.0, 1.0, 0.0, true);
        double result = integral.calculate(0.0, Math.PI / 2, 2, TOLERANCE, MAX_DEPTH);
        assertEquals(1.0, result, EPSILON, "余弦函数积分结果偏差过大");
    }

    // 5. 测试指数函数 y = C*e^(m*x + n) + k
    @Test
    void testExponentialFunction() {
        // y = e^x, 区间 [0, 1] 解析解 = e - 1 ≈ 1.718281828
        Integral integral = new Integral(1.0, 1.0, 0.0, 0.0, false);
        double result = integral.calculate(0.0, 1.0, 3, TOLERANCE, MAX_DEPTH);
        assertEquals(Math.E - 1, result, EPSILON, "指数函数积分结果偏差过大");
    }

    // 6. 测试对数函数 y = C*ln(m*x + n) + k
    @Test
    void testLogarithmicFunction() {
        // y = ln(x), 区间 [1, e] 解析解 = 1
        Integral integral = new Integral(1.0, 1.0, 0.0, 0.0, true);
        double result = integral.calculate(1.0, Math.E, 3, TOLERANCE, MAX_DEPTH);
        assertEquals(1.0, result, EPSILON, "对数函数积分结果偏差过大");
    }

    // 7. 边界情况：积分区间长度为零
    @Test
    void testZeroLengthInterval() {
        Integral integral = new Integral(2.0, 1.0);
        double result = integral.calculate(1.0, 1.0, 0, TOLERANCE, MAX_DEPTH);
        assertEquals(0.0, result, EPSILON, "区间为零时应返回 0");
    }

    // 8. 对数函数定义域测试：传入使真数非正的区间，应返回 NaN
    @Test
    void testLogarithmDomainError() {
        // y = ln(x), 区间 [-1, 1] 包含非正数，应返回 NaN
        Integral integral = new Integral(1.0, 1.0, 0.0, 0.0, true);
        double result = integral.calculate(-1.0, 1.0, 3, TOLERANCE, MAX_DEPTH);
        assertTrue(Double.isNaN(result), "对数函数在定义域外应返回 NaN");
    }

    // 9. 大区间测试：一次函数在大区间上的积分，验证算法稳定性
    @Test
    void testLargeInterval() {
        // y = x, 区间 [0, 1000] 解析解 = 500000
        Integral integral = new Integral(1.0, 0.0);
        double result = integral.calculate(0.0, 1000.0, 0, TOLERANCE, MAX_DEPTH);
        assertEquals(500000.0, result, 1e-4, "大区间积分结果偏差过大");
    }

    // 10. 极低精度测试：验证算法在低精度下仍能返回合理值
    @Test
    void testLowPrecision() {
        Integral integral = new Integral(1.0, 1.0, 0.0, 0.0, false);
        // 精度 1e-2，最大递归深度 5
        double result = integral.calculate(0.0, 1.0, 3, 1e-2, 5);
        // 实际值 e-1 ≈ 1.718，允许误差 1e-1
        assertEquals(Math.E - 1, result, 1e-1, "低精度积分结果应接近真值");
    }
}