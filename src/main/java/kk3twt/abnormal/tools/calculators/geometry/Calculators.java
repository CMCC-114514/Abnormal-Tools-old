package kk3twt.abnormal.tools.calculators.geometry;

/**
 * 几何图形计算工具类。
 * 提供静态方法用于计算各种平面图形和立体图形的几何属性（如面积、周长、体积等）。
 * 所有方法均假设输入的参数为正数（非负），但方法内部不进行参数校验，调用者需确保输入有效。
 */
public class Calculators {

    /**
     * 私有构造方法，防止外部实例化工具类。
     */
    private Calculators() {
    }

    /**
     * 计算矩形的面积、周长和对角线长度。
     *
     * @param a 矩形的长度
     * @param b 矩形的宽度
     * @return 包含三个 double 值的数组：
     *         <ul>
     *           <li>索引 0：面积</li>
     *           <li>索引 1：周长</li>
     *           <li>索引 2：对角线长度</li>
     *         </ul>
     */
    public static double[] rectangle(double a, double b) {
        double[] results = new double[3];
        results[0] = a * b;               // 面积
        results[1] = 2 * (a + b);         // 周长
        results[2] = Math.sqrt(a * a + b * b); // 对角线长
        return results;
    }

    /**
     * 根据已知条件（半径、周长或面积）计算圆的半径、周长和面积。
     *
     * @param choose 已知条件类型：
     *               <ul>
     *                 <li>1：已知半径</li>
     *                 <li>2：已知周长</li>
     *                 <li>3：已知面积</li>
     *               </ul>
     * @param num    已知条件的数值
     * @return 包含三个 double 值的数组：
     *         <ul>
     *           <li>索引 0：半径</li>
     *           <li>索引 1：周长</li>
     *           <li>索引 2：面积</li>
     *         </ul>
     */
    public static double[] circle(byte choose, double num) {
        // 根据 choose 计算半径 r
        double r = 0;
        switch (choose) {
            case 1 -> r = num;                      // 已知半径
            case 2 -> r = num / (2 * Math.PI);        // 已知周长 -> 半径
            case 3 -> r = Math.sqrt(num / Math.PI); // 已知面积 -> 半径
        }

        double[] results = new double[3];
        results[0] = r;                         // 半径
        results[1] = (Math.PI * 2) * r;         // 周长
        results[2] = Math.PI * r * r;           // 面积
        return results;
    }

    /**
     * 计算三角形面积（底乘高除以二）。
     *
     * @param a 三角形的底边长
     * @param h 三角形的高
     * @return 三角形的面积
     */
    public static double triangle(double a, double h) {
        return a * h / 2;
    }

    /**
     * 计算平行四边形面积（底乘高）。
     *
     * @param a 平行四边形的底边长
     * @param h 平行四边形的高
     * @return 平行四边形的面积
     */
    public static double rhomboid(double a, double h) {
        return a * h;
    }

    /**
     * 计算梯形面积（(上底 + 下底) × 高 ÷ 2）。
     *
     * @param a 梯形的上底长
     * @param b 梯形的下底长
     * @param h 梯形的高
     * @return 梯形的面积
     */
    public static double trapezoid(double a, double b, double h) {
        return (a + b) * h / 2;
    }

    /**
     * 计算圆锥体的表面积和体积。
     *
     * @param r 圆锥底面半径
     * @param h 圆锥的高
     * @return 包含两个 double 值的数组：
     *         <ul>
     *           <li>索引 0：表面积</li>
     *           <li>索引 1：体积</li>
     *         </ul>
     */
    public static double[] cone(double r, double h) {
        double l = Math.sqrt(r * r + h * h); // 母线长

        double[] results = new double[2];
        results[0] = Math.PI * r * r + Math.PI * r * l; // 表面积（底面积 + 侧面积）
        results[1] = Math.PI * r * r * h / 3;          // 体积
        return results;
    }

    /**
     * 根据已知条件（半径、表面积或体积）计算球体的半径、表面积和体积。
     *
     * @param choose 已知条件类型：
     *               <ul>
     *                 <li>1：已知半径</li>
     *                 <li>2：已知表面积</li>
     *                 <li>3：已知体积</li>
     *               </ul>
     * @param num    已知条件的数值
     * @return 包含三个 double 值的数组：
     *         <ul>
     *           <li>索引 0：半径</li>
     *           <li>索引 1：表面积</li>
     *           <li>索引 2：体积</li>
     *         </ul>
     */
    public static double[] sphere(byte choose, double num) {
        // 根据 choose 计算半径 r
        double r = 0;
        switch (choose) {
            case 1 -> r = num;                          // 已知半径
            case 2 -> r = Math.sqrt(num / (4 * Math.PI)); // 已知表面积 -> 半径
            case 3 -> r = Math.pow(3 * num / (4 * Math.PI), 1.0 / 3); // 已知体积 -> 半径
        }

        double[] results = new double[3];
        results[0] = r;                         // 半径
        results[1] = 4 * Math.PI * r * r;       // 表面积
        results[2] = (4.0 / 3.0) * Math.PI * r * r * r; // 体积
        return results;
    }

    /**
     * 计算长方体的对角线长度、表面积和体积。
     *
     * @param a 长方体的长
     * @param b 长方体的宽
     * @param h 长方体的高
     * @return 包含三个 double 值的数组：
     *         <ul>
     *           <li>索引 0：对角线长度</li>
     *           <li>索引 1：表面积</li>
     *           <li>索引 2：体积</li>
     *         </ul>
     */
    public static double[] cuboid(double a, double b, double h) {
        double[] result = new double[3];
        result[0] = Math.sqrt(a * a + b * b + h * h); // 对角线长
        result[1] = 2 * (a * b + a * h + b * h);       // 表面积
        result[2] = a * b * h;                     // 体积
        return result;
    }

    /**
     * 计算圆柱体的表面积和体积。
     *
     * @param r 圆柱底面半径
     * @param h 圆柱的高
     * @return 包含两个 double 值的数组：
     *         <ul>
     *           <li>索引 0：表面积</li>
     *           <li>索引 1：体积</li>
     *         </ul>
     */
    public static double[] cylinder(double r, double h) {
        double[] results = new double[2];
        results[0] = 2 * Math.PI * r * r + 2 * Math.PI * r * h; // 表面积（两个底面积 + 侧面积）
        results[1] = Math.PI * r * r * h;                       // 体积
        return results;
    }
}