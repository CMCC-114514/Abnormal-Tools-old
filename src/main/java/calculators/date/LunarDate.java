package calculators.date;

/**
 * 农历日期类，表示一个农历日期，并提供中文干支、生肖、月份、日期的格式化输出。
 * 支持1900年-2099年范围内的农历日期。
 *
 * @author CMCC-114514
 */
public class LunarDate {

    /** 农历年份（如 2023） */
    public int year;
    /** 农历月份（1-12，1表示正月） */
    public int month;
    /** 农历日期（1-30） */
    public int day;
    /** 是否为闰月 */
    public boolean isLeap;


    /** 十天干名称 */
    private static final String[] TIAN_GAN = {
            "甲","乙","丙","丁","戊","己","庚","辛","壬","癸"
    };

    /** 十二地支名称 */
    private static final String[] DI_ZHI = {
            "子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"
    };

    /** 十二生肖名称（与地支对应） */
    private static final String[] SHENG_XIAO = {
            "鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"
    };

    /** 农历月份名称（正月~腊月） */
    private static final String[] MONTH_NAME = {
            "正月","二月","三月","四月","五月","六月",
            "七月","八月","九月","十月","冬月","腊月"
    };

    /** 农历日期名称（初一~三十） */
    private static final String[] DAY_NAME = {
            "初一","初二","初三","初四","初五","初六","初七","初八","初九","初十",
            "十一","十二","十三","十四","十五","十六","十七","十八","十九","二十",
            "廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九","三十"
    };


    /**
     * 构造一个农历日期对象
     * @param year   农历年份
     * @param month  农历月份（1-12，1表示正月）
     * @param day    农历日期（1-30）
     * @param isLeap 是否为闰月
     */
    public LunarDate(int year, int month, int day, boolean isLeap) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.isLeap = isLeap;
    }


    /**
     * 获取农历年的干支表示（如“庚子”）
     * @return 干支字符串
     */
    public String getGanZhiYear() {
        int index = year - 1900 + 36; // 1900 是庚子年
        return TIAN_GAN[index % 10] + DI_ZHI[index % 12];
    }

    /**
     * 获取农历年的生肖（如“鼠”）
     * @return 生肖字符串
     */
    public String getShengXiao() {
        return SHENG_XIAO[(year - 4) % 12];
    }


    /**
     * 获取农历月份的中文名称（如“正月”、“闰四月”）
     * @return 月份名称
     */
    public String getMonthName() {
        return (isLeap ? "闰" : "") + MONTH_NAME[month - 1];
    }

    /**
     * 获取农历日期的中文名称（如“初一”、“十五”）
     * @return 日期名称
     */
    public String getDayName() {
        return DAY_NAME[day - 1];
    }


    /**
     * 返回农历日期的完整中文表示
     * @return 例如：“庚子（鼠）年 正月初一”
     */
    @Override
    public String toString() {
        return getGanZhiYear() + "（" + getShengXiao() + "）年 "
                + getMonthName() + getDayName();
    }
}