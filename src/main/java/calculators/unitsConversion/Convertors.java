package calculators.unitsConversion;

public class Convertors {
    private Convertors(){}

    // 长度单位名称数组
    public static final String[] LENGTH_UNITS = {
            "毫米(mm)", "厘米(cm)", "米(m)", "千米(km)",
            "英寸(in)", "英尺(ft)", "英里(mi)",
            "分(fen)", "寸(cun)", "尺(chi)", "里(li)",
            "海里(n mi)", "码(yd)"
    };

    // 面积单位名称数组
    public static final String[] AREA_UNITS = {
            "平方毫米(mm²)", "平方厘米(cm²)", "平方米(m²)",
            "公顷(ha)", "平方千米(km²)", "亩(mu)",
            "平方英寸(in²)", "平方英尺(ft²)", "平方英里(mi²)",
            "平方码(yd²)", "英亩(acre)", "公亩(a)"
    };

    // 体积单位名称数组
    public static final String[] VOLUME_UNITS = {
            "毫升(ml)", "升(L)", "立方米(m³)",
            "加仑(gal)", "液量盎司(oz)"
    };

    // 质量单位名称数组
    public static final String[] MASS_UNITS = {
            "克(g)", "千克(kg)", "吨(t)",
            "两(liang)", "斤(jin)", "磅(lb)", "盎司(oz)"
    };

    // 进制单位名称数组
    public static final String[] NUM_SYSTEM_UNITS = {
            "二进制", "八进制", "十进制", "十六进制"
    };

    // 速度单位名称数组
    public static final String[] SPEED_UNITS = {
            "米/秒(m/s)", "千米/小时(km/h)", "英里/小时(mph)", "节(knots)"
    };

    // 温度单位名称数组
    public static final String[] TEMPERATURE_UNITS = {
            "摄氏度(C)", "华氏度(F)", "开尔文(K)"
    };

    // 数据存储单位数组
    public static final String[] STORAGE_UNITS = {
            "位(bit)", "字节(int)", "千字节(KB)",
            "兆字节(MB)", "吉字节(GB)", "太字节(TB)", "拍字节(PB)"
    };

    // 颜色码名称数组
    public static final String[] COLOR_CODES = {
            "RGB", "CMYK", "HEX"
    };

    //换算方法1：长度换算
    public static double[] length(int choose, double num) {

        //将选择的单位转化为标准单位
        double std = AuxFunctions.getLengthStd(choose, num);

        //计算结果
        return new double[]{std*1e3, std*1e2, std, std/1e3, std*39.37, std*3.281, std/1609.347, std*300, std*30, std*3, std/500, std/0.9144, std/1852};
    }

    //换算方法2：面积换算
    public static double[] area(int choose, double num) {

        //将选择的单位转化为标准单位
        double std = AuxFunctions.getAreaStd(choose, num);

        //计算结果
        return new double[]{std*1e6, std*1e4, std, std*100, std/1e6, std/(2000.0 / 3.0), std*1550, std*10.7639, std/2589988.1, std*1.196, std/4046.94, std/1e4};
    }

    //换算方法3：体积换算
    public static double[] volume(int choose, double num) {

        //将选择的单位转化为标准单位
        double std = AuxFunctions.getVolumeStd(choose, num);

        //计算结果
        return new double[]{std*1e6, std*1e3, std, std*264.172, std*33818.06};
    }

    //换算方法4：质量换算
    public static double[] mass(int choose, double num) {
        //将选择的单位转化为标准单位
        double std = AuxFunctions.getMassStd(choose, num);

        //计算结果
        return new double[]{std*1e3, std, std/1e3, std*20, std*2, std*2.2046, std*35.274};
    }

    //换算方法5：进制换算
    public static String[] numSystem(int choose, String num) {
        //将选择的进制转化为标准进制
        int std = AuxFunctions.getNumSystemStd(choose, num);

        //计算结果
        return new String[]{Integer.toBinaryString(std), Integer.toOctalString(std), Integer.toString(std), Integer.toHexString(std)};
    }

    //换算方法6：速度换算
    public static double[] speed(int choose, double num) {
        //将选择的单位转化为标准单位
        double std = AuxFunctions.getSpeedStd(choose, num);

        //计算结果
        return new double[]{std, std*3.6, std/0.44704, std/0.514444};
    }

    //换算方法7：温度换算
    public static double[] temperature(int choose, double num) {
        //将选择的单位转化为标准单位
        double std = AuxFunctions.getTemperatureStd(choose, num);

        //计算结果
        return new double[]{std, std*1.8+32, std+273.15};
    }

    //换算方法8：数据存储单位换算
    public static double[] storage(int choose, double num) {
        //将选择的单位转化为标准单位
        double std = AuxFunctions.getStorageStd(choose, num);

        //计算结果
        double[] result = new double[STORAGE_UNITS.length];
        result[0] = std * 8;
        for (int i = 1; i < result.length; i++) {
            result[i] = std / Math.pow(1024, i - 1);
        }
        return result;
    }

    // 换算方法9：颜色码转换函数
    public static String RGB2HEX(int[] rgb) {
        StringBuilder hex = new StringBuilder("#");
        for (int i : rgb) {
            if (i == -1) continue;
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }
    public static int[] HEX2RGB(String hex) {
        int[] rgb = new int[3];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = Integer.parseInt(hex.substring(2*i, 2*(i+1)), 16);
        }
        return rgb;
    }
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
    public static int[] CMYK2RGB(int[] cmyk) {
        double c = cmyk[0] / 100.0;
        double m = cmyk[1] / 100.0;
        double y = cmyk[2] / 100.0;
        double k = cmyk[3] / 100.0;

        double r = 255 * (1 - c) * (1 - k);
        double g = 255 * (1 - m) * (1 - k);
        double b = 255 * (1 - y) * (1 - k);

        return new int[] {(int) r, (int) g, (int) b};
    }
}
