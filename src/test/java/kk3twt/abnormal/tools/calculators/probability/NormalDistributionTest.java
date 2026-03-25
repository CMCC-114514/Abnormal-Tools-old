package kk3twt.abnormal.tools.calculators.probability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalDistributionTest {

    private static final double DELTA = 1e-4; // 允许的误差范围

    @Test
    void testConstructorAndGetters() {
        NormalDistribution nd = new NormalDistribution(0.0, 1.0);
        // 没有直接提供 getter，但可以通过计算验证
        assertEquals(0.5, nd.calculate(0.0), DELTA); // 间接验证
    }

    @Test
    void testNormalWithStandardNormal() {
        NormalDistribution std = new NormalDistribution(0.0, 1.0);
        // 标准正态分布密度函数值（查表或已知值）
        assertEquals(0.3989422804, std.normal(0.0), DELTA);
        assertEquals(0.2419707245, std.normal(1.0), DELTA);
        assertEquals(0.2419707245, std.normal(-1.0), DELTA);
        assertEquals(0.0539909665, std.normal(2.0), DELTA);
    }

    @Test
    void testNormalWithNonStandardParameters() {
        NormalDistribution nd = new NormalDistribution(5.0, 2.0);
        // 手动计算标准化值后使用标准正态密度
        // 例如 x=5.0 => z=0.0 => density = 0.39894
        assertEquals(0.3989422804 / 2.0, nd.normal(5.0), DELTA);
        // x=7.0 => z=(7-5)/2=1.0 => density = 0.24197 / 2
        assertEquals(0.2419707245 / 2.0, nd.normal(7.0), DELTA);
        // x=3.0 => z=-1.0 => 同样
        assertEquals(0.2419707245 / 2.0, nd.normal(3.0), DELTA);
    }

    @Test
    void testCalculateCdfForStandardNormal() {
        NormalDistribution std = new NormalDistribution(0.0, 1.0);
        // 已知累积概率（四舍五入到小数点后4位）
        assertEquals(0.5, std.calculate(0.0), DELTA);
        assertEquals(0.841344746, std.calculate(1.0), DELTA);
        assertEquals(0.158655254, std.calculate(-1.0), DELTA);
        assertEquals(0.977249868, std.calculate(2.0), DELTA);
        assertEquals(0.022750132, std.calculate(-2.0), DELTA);
    }

    @Test
    void testCalculateCdfForNonStandard() {
        NormalDistribution nd = new NormalDistribution(10.0, 3.0);
        // 转换为标准正态：x=10 => z=0 => 0.5
        assertEquals(0.5, nd.calculate(10.0), DELTA);
        // x=13 => z=1 => 0.84134
        assertEquals(0.841344746, nd.calculate(13.0), DELTA);
        // x=7 => z=-1 => 0.15866
        assertEquals(0.158655254, nd.calculate(7.0), DELTA);
    }

    @Test
    void testCalculateCdfAtExtremePoints() {
        NormalDistribution std = new NormalDistribution(0.0, 1.0);
        // 下限为 -4，积分到 -4 应接近 0
        double lower = std.calculate(-4.0);
        assertTrue(lower > 0 && lower < 1e-5);
        // 积分到 +4 应接近 1
        double upper = std.calculate(4.0);
        assertTrue(upper > 0.9999 && upper < 1.0);
    }

    @Test
    void testCalculateCdfWithDifferentSigma() {
        // sigma 很小时，分布集中，计算应稳定
        NormalDistribution narrow = new NormalDistribution(0.0, 0.1);
        assertEquals(0.5, narrow.calculate(0.0), DELTA);
        assertEquals(0.841344746, narrow.calculate(0.1), DELTA); // 1 sigma
        assertEquals(0.022750132, narrow.calculate(-0.2), DELTA); // -2 sigma

        // sigma 很大时，分布平坦
        NormalDistribution wide = new NormalDistribution(0.0, 10.0);
        assertEquals(0.5, wide.calculate(0.0), DELTA);
        assertEquals(0.539827837, wide.calculate(1.0), 1e-4); // 近似
    }

    @Test
    void testConstructorWithZeroSigma() {
        // sigma=0 会导致除以零，但 Java 中 1/0.0 得到 Infinity，不会抛异常
        // 但后续计算可能产生 NaN 或 Infinity，此处仅确保不抛异常
        assertDoesNotThrow(() -> new NormalDistribution(0.0, 0.0));
        NormalDistribution nd = new NormalDistribution(0.0, 0.0);
        // 对于 x != mu，normal 返回 0 (因为 Math.pow(E, -Infinity/2)=0)
        assertEquals(0.0, nd.normal(1.0), DELTA);
        // 对于 x == mu，z = (0-0)/0 = NaN，导致 normal 返回 NaN
        assertTrue(Double.isNaN(nd.normal(0.0)));
        // calculate 方法会因积分区间为 NaN 导致结果 NaN
        assertTrue(Double.isNaN(nd.calculate(0.0)));
    }

//    @Test
//    void testCalculateWithNaN() {
//        NormalDistribution nd = new NormalDistribution(0.0, 1.0);
//        assertTrue(Double.isNaN(nd.calculate(Double.NaN)));
//    }

//    @Test
//    void testCalculateWithInfinity() {
//        NormalDistribution nd = new NormalDistribution(0.0, 1.0);
//        assertEquals(0.0, nd.calculate(Double.NEGATIVE_INFINITY), DELTA);
//        assertEquals(1.0, nd.calculate(Double.POSITIVE_INFINITY), DELTA);
//    }
}