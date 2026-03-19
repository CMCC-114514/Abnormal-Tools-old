package kk3twt.abnormal.tools.calculators.unitsConversion;

/**
 * 单位换算辅助工具类。
 * 提供将各种单位转换为标准单位（如米、平方米、千克等）的静态方法。
 * 所有方法均为私有构造，禁止实例化。
 */
public class AuxFunctions {

    /**
     * 私有构造方法，防止外部实例化。
     */
    private AuxFunctions() {}

    /**
     * 将面积单位转换为标准单位平方米。
     *
     * @param choose 面积单位类型（1~12，对应 {@link Convertors#AREA_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的平方米值
     */
    public static double getAreaStd(int choose, double num) {
        double std = 0;    // 标准单位（平方米）
        switch (choose) {               // 将对应单位转换成平方米
            case 1 -> std = num / 1e6;              // 平方毫米
            case 2 -> std = num / 1e4;              // 平方厘米
            case 3 -> std = num;                    // 平方米
            case 4 -> std = num / 100;              // 平方分米
            case 5 -> std = num * 1e6;              // 平方公里
            case 6 -> std = num * (2000.0 / 3.0);   // 市亩
            case 7 -> std = num / 1550;             // 平方英寸
            case 8 -> std = num / 10.7639;          // 平方英尺
            case 9 -> std = num * 2589988.1;        // 平方英里
            case 10 -> std = num / 1.196;           // 平方码
            case 11 -> std = num * 4046.94;         // 英亩
            case 12 -> std = num * 1e4;             // 公顷
        }
        return std;
    }

    /**
     * 将长度单位转换为标准单位米。
     *
     * @param choose 长度单位类型（1~13，对应 {@link Convertors#LENGTH_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的米值
     */
    public static double getLengthStd(int choose, double num) {
        double std = 0;    // 标准单位（米）
        switch (choose) {               // 将对应单位转换成米
            case 1 -> std = num / 1e3;      // 毫米
            case 2 -> std = num / 1e2;      // 厘米
            case 3 -> std = num;            // 米
            case 4 -> std = num * 1e3;      // 千米
            case 5 -> std = num / 39.37;    // 英寸
            case 6 -> std = num / 3.281;    // 英尺
            case 7 -> std = num * 1609.347; // 英里
            case 8 -> std = num / 300;      // 分
            case 9 -> std = num / 30;       // 寸
            case 10 -> std = num / 3;       // 尺
            case 11 -> std = num * 500;     // 里
            case 12 -> std = num * 0.9144;  // 码
            case 13 -> std = num * 1852;    // 海里
        }
        return std;
    }

    /**
     * 将体积单位转换为标准单位立方米。
     *
     * @param choose 体积单位类型（1~5，对应 {@link Convertors#VOLUME_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的立方米值
     */
    public static double getVolumeStd(int choose, double num) {
        double std = 0;    // 标准单位（立方米）
        switch (choose) {               // 将对应单位转换成立方米
            case 1 -> std = num / 1e6;          // 毫升
            case 2 -> std = num / 1e3;          // 升
            case 3 -> std = num;                // 立方米
            case 4 -> std = num / 264.172;      // 加仑
            case 5 -> std = num / 33818.06;     // 盎司
        }
        return std;
    }

    /**
     * 将质量单位转换为标准单位千克。
     *
     * @param choose 质量单位类型（1~7，对应 {@link Convertors#MASS_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的千克值
     */
    public static double getMassStd(int choose, double num) {
        double std = 0;    // 标准单位（千克）
        switch (choose) {               // 将对应单位转换成千克
            case 1 -> std = num / 1e3;          // 克
            case 2 -> std = num;                // 千克
            case 3 -> std = num * 1e3;          // 吨
            case 4 -> std = num / 20;           // 两
            case 5 -> std = num / 2;            // 斤
            case 6 -> std = num / 2.2046;       // 磅
            case 7 -> std = num / 35.274;       // 盎司
        }
        return std;
    }

    /**
     * 将给定进制的数字字符串转换为十进制整数。
     *
     * @param choose 进制类型（1~4，对应 {@link Convertors#NUM_SYSTEM_UNITS} 中的索引+1）
     * @param num    待转换的数字字符串（需符合所选进制的格式）
     * @return 转换后的十进制整数值
     */
    public static int getNumSystemStd(int choose, String num) {
        int std = 0;
        switch (choose) {
            case 1 -> std = Integer.parseInt(num, 2);   // 二进制
            case 2 -> std = Integer.parseInt(num, 8);   // 八进制
            case 3 -> std = Integer.parseInt(num);      // 十进制
            case 4 -> std = Integer.parseInt(num, 16);  // 十六进制
        }
        return std;
    }

    /**
     * 将速度单位转换为标准单位米/秒。
     *
     * @param choose 速度单位类型（1~4，对应 {@link Convertors#SPEED_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的米/秒值
     */
    public static double getSpeedStd(int choose, double num) {
        double std = 0;
        switch (choose) {
            case 1 -> std = num;               // 米/秒
            case 2 -> std = num / 3.6;         // 千米/小时
            case 3 -> std = num * 0.44704;     // 英里/小时
            case 4 -> std = num * 0.514444;    // 节
        }
        return std;
    }

    /**
     * 将温度单位转换为标准单位摄氏度。
     *
     * @param choose 温度单位类型（1~3，对应 {@link Convertors#TEMPERATURE_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的摄氏度值
     */
    public static double getTemperatureStd(int choose, double num) {
        double std = 0;
        switch (choose) {
            case 1 -> std = num;                // 摄氏度
            case 2 -> std = (num - 32) / 1.8;   // 华氏度
            case 3 -> std = num - 273.15;       // 开尔文
        }
        return std;
    }

    /**
     * 将存储空间单位转换为标准单位字节。
     *
     * @param choose 存储单位类型（1~7，对应 {@link Convertors#STORAGE_UNITS} 中的索引+1）
     * @param num    待转换的数值
     * @return 转换后的字节数
     */
    public static double getStorageStd(int choose, double num) {
        double std;
        if (choose == 1) {
            std = num / 8;    // bit 转换为字节
        } else {
            // 其他单位：字节、KB、MB、GB、TB、PB，乘以 1024^(choose-2)
            std = num * Math.pow(1024, choose - 2);
        }
        return std;
    }
}