package kk3twt.abnormal.tools.calculators.calculus;

/**
 * 积分计算类
 * 进行初等函数的定积分运算
 * 使用梯形法进行数值运算
 */
public class Integral {

    public static String[] FUNCTION_TYPE = {
            "一次函数", "二次函数", "正弦/余弦函数", "指数/对数函数"
    };

    /**
     * 构造函数1：一次函数构造
     * @param m 斜率
     * @param n y轴截距
     */
    public Integral(double m, double n) {
        this.m = m;
        this.n = n;
    }
    private double m = 0;
    private double n = 0;

    /**
     * 返回一次函数 y = kx + b 的值
     *
     * @param x 自变量值
     * @return 函数值
     */
    private double linear(double x) {
        return m * x + n;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 构造函数2：二次函数构造
     * @param a 二次项参数
     * @param b 一次项参数
     * @param c 常数
     */
    public Integral(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    private double a = 0;
    private double b = 0;
    private double c = 0;

    /**
     * 返回二次函数 y = ax^2 + bx + c 的值
     * @param x 自变量值
     * @return 函数值
     */
    private double quadratic(double x) {
        return a * Math.pow(x, 2) + b * x + c;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 构造函数3：三角函数构造
     * 使用弧度制
     * @param A 振幅
     * @param omega 角速度
     * @param phi 初相位
     * @param isCos 是否为 cos 函数
     */
    public Integral(double A, double omega, double phi, boolean isCos) {
        this.A = A;
        this.omega = omega;
        this.phi = phi;

        double PI = 3.1415926535;
        if (isCos)
            this.phi += PI / 2;  // cos 函数处理，相位相差 pi/2
    }
    private double A = 0;
    private double omega = 0;
    private double phi = 0;

    /**
     * 返回正弦函数 y = Asin(wx + phi) 的值
     * @param x 自变量值
     * @return 函数值
     */
    private double sine(double x) {
        return A * Math.sin(omega * x + phi);
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 构造函数4：e 指数函数或对数函数构造
     * @param C 振幅
     * @param m 指数参数1
     * @param n 指数参数2
     * @param k 常数
     */
    public Integral(double C, double m, double n, double k, boolean isLn) {
        this.C = C;
        this.m = m;
        this.n = n;
        this.k = k;
        this.isLn = isLn;
    }
    private double C = 0;
//    private double m = 0;
//    private double n = 0;
    private double k = 0;
    private boolean isLn = false;

    /**
     * 返回 e 指数函数 y = Ce^(mx+n) + k 或对数函数 y = Cln(mx+n) + k 的值
     * @param x 自变量值
     * @return 函数值
     */
    private double exponential(double x) {
        return isLn ? C * Math.log(linear(x)) + k : C * Math.exp(linear(x)) + k;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 选择函数类型
     * @param x 自变量值
     * @param functionType 函数类型
     * @return 函数值
     */
    private double f(double x, int functionType) {
        double result = 0;
        switch (functionType) {
            case 0 -> result = linear(x);
            case 1 -> result = quadratic(x);
            case 2 -> result = sine(x);
            case 3 -> result = exponential(x);
        }

        return result;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 定积分计算方法
     * 使用辛普森法进行积分
     * 需要自行输入精度和递归深度
     *
     * @param a 积分上限
     * @param b 积分下限
     * @param i 函数类型
     * @param tolerance 计算精度
     * @param maxDepth 最大递归深度
     * @return 积分结果
     */
    public double calculate(double a, double b, int i, double tolerance, int maxDepth) {
        double mid = (a + b) / 2;
        double s = simpson(a, b, i);
        double s_left = simpson(a, mid, i);
        double s_right = simpson(mid, b, i);

        if (maxDepth <= 0 || Math.abs(s_left + s_right - s) < 15 * tolerance) {
            return s_left + s_right + (s_left + s_right - s) / 15;
        }

        return calculate(a, mid, i, tolerance / 2, maxDepth - 1) + calculate(mid, b, i, tolerance / 2, maxDepth - 1);
    }

    /**
     * 辅助方法：使用辛普森公式计算区间的积分
     *
     * @param a 区间下界
     * @param b 区间上界
     * @param i 函数类型
     * @return 积分结果
     */
    private double simpson(double a, double b, int i) {
        return (b - a) / 6 * (f(a, i) + 4 * f((a + b) / 2, i) + f(b, i));
    }
}
