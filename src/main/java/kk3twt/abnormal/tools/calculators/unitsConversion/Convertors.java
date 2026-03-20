package kk3twt.abnormal.tools.calculators.unitsConversion;

/**
 * 单位换算核心类。
 * 提供各种单位之间的换算方法，以及预定义的常用单位名称数组。
 * 所有方法均为静态，禁止实例化。
 */
public class Convertors {

    /** 私有构造方法，防止外部实例化。 */
    private Convertors() {}

    /** 长度单位名称数组（按标准顺序排列，与 {@link AuxFunctions#getLengthStd} 的 choose 参数对应） */
    public static final String[] LENGTH_UNITS = {
            "毫米(mm)", "厘米(cm)", "米(m)", "千米(km)",
            "英寸(in)", "英尺(ft)", "英里(mi)",
            "分(fen)", "寸(cun)", "尺(chi)", "里(li)",
            "海里(n mi)", "码(yd)"
    };

    /** 面积单位名称数组（与 {@link AuxFunctions#getAreaStd} 的 choose 参数对应） */
    public static final String[] AREA_UNITS = {
            "平方毫米(mm²)", "平方厘米(cm²)", "平方米(m²)",
            "公顷(ha)", "平方千米(km²)", "亩(mu)",
            "平方英寸(in²)", "平方英尺(ft²)", "平方英里(mi²)",
            "平方码(yd²)", "英亩(acre)", "公亩(a)"
    };

    /** 体积单位名称数组（与 {@link AuxFunctions#getVolumeStd} 的 choose 参数对应） */
    public static final String[] VOLUME_UNITS = {
            "毫升(ml)", "升(L)", "立方米(m³)",
            "加仑(gal)", "液量盎司(oz)"
    };

    /** 质量单位名称数组（与 {@link AuxFunctions#getMassStd} 的 choose 参数对应） */
    public static final String[] MASS_UNITS = {
            "克(g)", "千克(kg)", "吨(t)",
            "两(liang)", "斤(jin)", "磅(lb)", "盎司(oz)"
    };

    /** 进制单位名称数组（与 {@link AuxFunctions#getNumSystemStd} 的 choose 参数对应） */
    public static final String[] NUM_SYSTEM_UNITS = {
            "二进制", "八进制", "十进制", "十六进制"
    };

    /** 速度单位名称数组（与 {@link AuxFunctions#getSpeedStd} 的 choose 参数对应） */
    public static final String[] SPEED_UNITS = {
            "米/秒(m/s)", "千米/小时(km/h)", "英里/小时(mph)", "节(knots)"
    };

    /** 温度单位名称数组（与 {@link AuxFunctions#getTemperatureStd} 的 choose 参数对应） */
    public static final String[] TEMPERATURE_UNITS = {
            "摄氏度(C)", "华氏度(F)", "开尔文(K)"
    };

    /** 数据存储单位名称数组（与 {@link AuxFunctions#getStorageStd} 的 choose 参数对应） */
    public static final String[] STORAGE_UNITS = {
            "位(bit)", "字节(int)", "千字节(KB)",
            "兆字节(MB)", "吉字节(GB)", "太字节(TB)", "拍字节(PB)"
    };

    /** 颜色编码类型名称数组（用于颜色码转换） */
    public static final String[] COLOR_CODES = {
            "RGB", "CMYK", "HEX"
    };

    /**
     * 长度换算：将给定单位的值转换为所有其他长度单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #LENGTH_UNITS}）
     * @param num    待转换的数值
     * @return 包含所有单位换算结果的 double 数组，顺序与 {@link #LENGTH_UNITS} 一致
     */
    public static double[] length(int choose, double num) {
        // 将输入单位转换为标准单位（米）
        double std = AuxFunctions.getLengthStd(choose, num);

        // 计算结果数组：依次为毫米、厘米、米、千米、英寸、英尺、英里、分、寸、尺、里、海里、码
        return new double[]{
                std * 1e3, std * 1e2, std, std / 1e3,
                std * 39.37, std * 3.281, std / 1609.347,
                std * 300, std * 30, std * 3, std / 500,
                std / 0.9144, std / 1852
        };
    }

    /**
     * 面积换算：将给定单位的值转换为所有其他面积单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #AREA_UNITS}）
     * @param num    待转换的数值
     * @return 包含所有单位换算结果的 double 数组，顺序与 {@link #AREA_UNITS} 一致
     */
    public static double[] area(int choose, double num) {
        // 将输入单位转换为标准单位（平方米）
        double std = AuxFunctions.getAreaStd(choose, num);

        // 计算结果数组：依次为平方毫米、平方厘米、平方米、公顷、平方千米、亩、平方英寸、平方英尺、平方英里、平方码、英亩、公亩
        return new double[]{
                std * 1e6, std * 1e4, std, std * 100,
                std / 1e6, std / (2000.0 / 3.0),
                std * 1550, std * 10.7639, std / 2589988.1,
                std * 1.196, std / 4046.94, std / 1e4
        };
    }

    /**
     * 体积换算：将给定单位的值转换为所有其他体积单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #VOLUME_UNITS}）
     * @param num    待转换的数值
     * @return 包含所有单位换算结果的 double 数组，顺序与 {@link #VOLUME_UNITS} 一致
     */
    public static double[] volume(int choose, double num) {
        // 将输入单位转换为标准单位（立方米）
        double std = AuxFunctions.getVolumeStd(choose, num);

        // 计算结果数组：毫升、升、立方米、加仑、盎司
        return new double[]{
                std * 1e6, std * 1e3, std,
                std * 264.172, std * 33818.06
        };
    }

    /**
     * 质量换算：将给定单位的值转换为所有其他质量单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #MASS_UNITS}）
     * @param num    待转换的数值
     * @return 包含所有单位换算结果的 double 数组，顺序与 {@link #MASS_UNITS} 一致
     */
    public static double[] mass(int choose, double num) {
        // 将输入单位转换为标准单位（千克）
        double std = AuxFunctions.getMassStd(choose, num);

        // 计算结果数组：克、千克、吨、两、斤、磅、盎司
        return new double[]{
                std * 1e3, std, std / 1e3,
                std * 20, std * 2, std * 2.2046, std * 35.274
        };
    }

    /**
     * 进制换算：将给定进制的数字字符串转换为所有其他进制表示。
     *
     * @param choose 输入进制的索引+1（对应 {@link #NUM_SYSTEM_UNITS}）
     * @param num    待转换的数字字符串（需符合输入进制的格式）
     * @return 包含二进制、八进制、十进制、十六进制字符串的数组
     */
    public static String[] numSystem(int choose, String num) {
        // 将输入进制转换为十进制整数
        int std = AuxFunctions.getNumSystemStd(choose, num);

        // 转换为各进制字符串
        return new String[]{
                Integer.toBinaryString(std),
                Integer.toOctalString(std),
                Integer.toString(std),
                Integer.toHexString(std)
        };
    }

    /**
     * 速度换算：将给定单位的值转换为所有其他速度单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #SPEED_UNITS}）
     * @param num    待转换的数值
     * @return 包含所有单位换算结果的 double 数组，顺序与 {@link #SPEED_UNITS} 一致
     */
    public static double[] speed(int choose, double num) {
        // 将输入单位转换为标准单位（米/秒）
        double std = AuxFunctions.getSpeedStd(choose, num);

        // 计算结果数组：米/秒、千米/小时、英里/小时、节
        return new double[]{
                std, std * 3.6,
                std / 0.44704, std / 0.514444
        };
    }

    /**
     * 温度换算：将给定单位的值转换为所有其他温度单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #TEMPERATURE_UNITS}）
     * @param num    待转换的数值
     * @return 包含摄氏度、华氏度、开尔文结果的 double 数组
     */
    public static double[] temperature(int choose, double num) {
        // 将输入单位转换为标准单位（摄氏度）
        double std = AuxFunctions.getTemperatureStd(choose, num);

        // 计算结果数组：摄氏度、华氏度、开尔文
        return new double[]{
                std, std * 1.8 + 32, std + 273.15
        };
    }

    /**
     * 数据存储单位换算：将给定单位的值转换为所有其他存储单位。
     *
     * @param choose 输入单位的索引+1（对应 {@link #STORAGE_UNITS}）
     * @param num    待转换的数值
     * @return 包含 bit、字节、KB、MB、GB、TB、PB 结果的 double 数组
     */
    public static double[] storage(int choose, double num) {
        // 将输入单位转换为标准单位（字节）
        double std = AuxFunctions.getStorageStd(choose, num);

        double[] result = new double[STORAGE_UNITS.length];
        result[0] = std * 8;          // bit
        for (int i = 1; i < result.length; i++) {
            result[i] = std / Math.pow(1024, i - 1); // 字节、KB、MB、GB、TB、PB
        }
        return result;
    }

    // 颜色码转换相关方法 --------------------------------------------------

    /**
     * 将 RGB 颜色值转换为 HEX 字符串。
     *
     * @param rgb RGB 三个分量的整数数组（每个分量 0-255）
     * @return 格式如 "#RRGGBB" 的十六进制字符串
     */
    public static String RGB2HEX(int[] rgb) {
        StringBuilder hex = new StringBuilder("#");
        for (int i : rgb) {
            if (i == -1) continue;
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }

    /**
     * 将 HEX 颜色字符串转换为 RGB 整数数组。
     *
     * @param hex 格式如 "RRGGBB" 的十六进制字符串（可包含或不包含前导 #）
     * @return RGB 三个分量的整数数组（0-255）
     */
    public static int[] HEX2RGB(String hex) {
        int[] rgb = new int[3];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = Integer.parseInt(hex.substring(2 * i, 2 * (i + 1)), 16);
        }
        return rgb;
    }

    /**
     * 将 RGB 颜色值转换为 CMYK 整数数组。
     *
     * @param rgb RGB 三个分量的整数数组（每个分量 0-255）
     * @return CMYK 四个分量的整数数组（每个分量 0-100，代表百分比）
     */
    public static int[] RGB2CMYK(int[] rgb) {
        double r = rgb[0] / 255.0;
        double g = rgb[1] / 255.0;
        double b = rgb[2] / 255.0;

        double k = 1 - Math.max(Math.max(r, g), b);
        return new int[]{
                (int) (((1 - r - k) / (1 - k)) * 100),
                (int) (((1 - g - k) / (1 - k)) * 100),
                (int) (((1 - b - k) / (1 - k)) * 100),
                (int) (k * 100)
        };
    }

    /**
     * 将 CMYK 颜色值转换为 RGB 整数数组。
     *
     * @param cmyk CMYK 四个分量的整数数组（每个分量 0-100，代表百分比）
     * @return RGB 三个分量的整数数组（0-255）
     */
    public static int[] CMYK2RGB(int[] cmyk) {
        double c = cmyk[0] / 100.0;
        double m = cmyk[1] / 100.0;
        double y = cmyk[2] / 100.0;
        double k = cmyk[3] / 100.0;

        double r = 255 * (1 - c) * (1 - k);
        double g = 255 * (1 - m) * (1 - k);
        double b = 255 * (1 - y) * (1 - k);

        return new int[]{(int) r, (int) g, (int) b};
    }
}