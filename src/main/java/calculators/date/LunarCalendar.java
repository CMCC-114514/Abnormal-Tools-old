package calculators.date;

/**
 * 农历转换工具类，提供公历转农历的功能。
 * 支持1900年至2099年范围内的公历日期转换为农历日期。
 * 农历数据来源于内置的LUNAR_INFO数组，每个元素为一个32位整数，编码了该年的农历信息。
 *
 * @author CMCC-114514
 */
public class LunarCalendar {

    /**
     * 农历数据表（1900-2099）
     * 每个int表示一年的农历信息，按位编码：
     * 低5位（0-4）：春节离元旦的天数减1（如果第6位为0，则还需加31）
     * 第5位（bit5）：春节离元旦的天数是否需要加31的标志（0表示不加，1表示加）
     * 第6-19位（bit6-bit19）：表示该年12或13个月的大小月情况，1表示30天，0表示29天
     * 第20-23位（bit20-bit23）：闰月月份，0表示无闰月
     * 具体格式参考农历算法资料。
     */
    private static final int[] LUNAR_INFO = {
            0x04AE53, 0x0A5748, 0x5526BD, 0x0D2650, 0x0D9544, 0x46AAB9, 0x056A4D, 0x09AD42, 0x24AEB6, 0x04AE4A,/*1901-1910*/
            0x6A4DBE, 0x0A4D52, 0x0D2546, 0x5D52BA, 0x0B544E, 0x0D6A43, 0x296D37, 0x095B4B, 0x749BC1, 0x049754,/*1911-1920*/
            0x0A4B48, 0x5B25BC, 0x06A550, 0x06D445, 0x4ADAB8, 0x02B64D, 0x095742, 0x2497B7, 0x04974A, 0x664B3E,/*1921-1930*/
            0x0D4A51, 0x0EA546, 0x56D4BA, 0x05AD4E, 0x02B644, 0x393738, 0x092E4B, 0x7C96BF, 0x0C9553, 0x0D4A48,/*1931-1940*/
            0x6DA53B, 0x0B554F, 0x056A45, 0x4AADB9, 0x025D4D, 0x092D42, 0x2C95B6, 0x0A954A, 0x7B4ABD, 0x06CA51,/*1941-1950*/
            0x0B5546, 0x555ABB, 0x04DA4E, 0x0A5B43, 0x352BB8, 0x052B4C, 0x8A953F, 0x0E9552, 0x06AA48, 0x6AD53C,/*1951-1960*/
            0x0AB54F, 0x04B645, 0x4A5739, 0x0A574D, 0x052642, 0x3E9335, 0x0D9549, 0x75AABE, 0x056A51, 0x096D46,/*1961-1970*/
            0x54AEBB, 0x04AD4F, 0x0A4D43, 0x4D26B7, 0x0D254B, 0x8D52BF, 0x0B5452, 0x0B6A47, 0x696D3C, 0x095B50,/*1971-1980*/
            0x049B45, 0x4A4BB9, 0x0A4B4D, 0xAB25C2, 0x06A554, 0x06D449, 0x6ADA3D, 0x0AB651, 0x093746, 0x5497BB,/*1981-1990*/
            0x04974F, 0x064B44, 0x36A537, 0x0EA54A, 0x86B2BF, 0x05AC53, 0x0AB647, 0x5936BC, 0x092E50, 0x0C9645,/*1991-2000*/
            0x4D4AB8, 0x0D4A4C, 0x0DA541, 0x25AAB6, 0x056A49, 0x7AADBD, 0x025D52, 0x092D47, 0x5C95BA, 0x0A954E,/*2001-2010*/
            0x0B4A43, 0x4B5537, 0x0AD54A, 0x955ABF, 0x04BA53, 0x0A5B48, 0x652BBC, 0x052B50, 0x0A9345, 0x474AB9,/*2011-2020*/
            0x06AA4C, 0x0AD541, 0x24DAB6, 0x04B64A, 0x69573D, 0x0A4E51, 0x0D2646, 0x5E933A, 0x0D534D, 0x05AA43,/*2021-2030*/
            0x36B537, 0x096D4B, 0xB4AEBF, 0x04AD53, 0x0A4D48, 0x6D25BC, 0x0D254F, 0x0D5244, 0x5DAA38, 0x0B5A4C,/*2031-2040*/
            0x056D41, 0x24ADB6, 0x049B4A, 0x7A4BBE, 0x0A4B51, 0x0AA546, 0x5B52BA, 0x06D24E, 0x0ADA42, 0x355B37,/*2041-2050*/
            0x09374B, 0x8497C1, 0x049753, 0x064B48, 0x66A53C, 0x0EA54F, 0x06B244, 0x4AB638, 0x0AAE4C, 0x092E42,/*2051-2060*/
            0x3C9735, 0x0C9649, 0x7D4ABD, 0x0D4A51, 0x0DA545, 0x55AABA, 0x056A4E, 0x0A6D43, 0x452EB7, 0x052D4B,/*2061-2070*/
            0x8A95BF, 0x0A9553, 0x0B4A47, 0x6B553B, 0x0AD54F, 0x055A45, 0x4A5D38, 0x0A5B4C, 0x052B42, 0x3A93B6,/*2071-2080*/
            0x069349, 0x7729BD, 0x06AA51, 0x0AD546, 0x54DABA, 0x04B64E, 0x0A5743, 0x452738, 0x0D264A, 0x8E933E,/*2081-2090*/
            0x0D5252, 0x0DAA47, 0x66B53B, 0x056D4F, 0x04AE45, 0x4A4EB9, 0x0A4D4C, 0x0D1541, 0x2D92B5          /*2091-2099*/
    };

    /** 每月累积天数表（平年，不考虑闰年） */
    private static final int[] MONTH_ADD = {
            0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334
    };

    /** 临时存储计算出的农历日（低位6位）、月（中间6位）、闰月标志（高位） */
    private static int lunarCalendarDay;

    /**
     * 核心农历转换算法，计算给定公历日期对应的农历信息，并将结果存入 lunarCalendarDay 静态变量。
     * 算法步骤：
     * 1. 获取该年的春节距元旦的天数 spring_NY
     * 2. 计算公历日期距元旦的天数 sun_NY
     * 3. 根据 sun_NY 与 spring_NY 的比较，判断是在春节前还是后，分别处理。
     * 4. 通过循环减去月份天数，确定农历月份和日期，并判断是否闰月。
     *
     * @param date 公历日期
     * @return true 表示当前农历月为闰月，false 表示非闰月
     */
    private static boolean getLunarCalendar(Date date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();

        /*
            spring_NY 表示春节距离当年元旦的天数
            sun_NY 表示阳历日距离当年元旦的天数
         */
        int spring_NY, sun_NY;

        // 从LUNAR_INFO中解析春节距元旦的天数
        // 第5位（bit5）为1表示需要加31天，否则不加；低5位为春节距离元旦的天数减1
        if (((LUNAR_INFO[year - 1901] & 0x0060) >> 5) == 1)
            spring_NY = (LUNAR_INFO[year - 1901] & 0x001F) - 1;
        else
            spring_NY = (LUNAR_INFO[year - 1901] & 0x001f) - 1 + 31;

        // 公历日期距元旦的天数（1月1日为0）
        sun_NY = MONTH_ADD[month - 1] + day - 1;
        // 如果是闰年且月份大于2月，需加一天（因为MONTH_ADD是平年累积）
        if (!(year % 4 == 0) && (month > 2))
            sun_NY++;

        /*
            staticDayCount 表示当前农历月份的天数（29天或30天）
            index 表示从哪个月开始计算（月份序号，1表示正月）
            flag 用于处理闰月标记
         */
        int staticDayCount, index;
        boolean flag;

        // 判断阳历日在春节之前还是春节之后
        if (sun_NY >= spring_NY) {  // 阳历日在春节后（含春节当天）
            sun_NY -= spring_NY;
            month = 1;
            index = 1;
            flag = false;

            // 获取第一个月（正月）的天数，从LUNAR_INFO的高位（bit19开始）依次表示各月大小
            if ((LUNAR_INFO[year - 1901] & (0x80000 >> (0))) == 0)
                staticDayCount = 29;
            else
                staticDayCount = 30;

            // 逐月减去天数，直到剩余天数小于当前月天数
            while (sun_NY >= staticDayCount) {
                sun_NY -= staticDayCount;
                index++;

                // 判断当前月是否为闰月
                if (month == ((LUNAR_INFO[year - 1901] & 0xF00000) >> 20)) {
                    flag = !flag;
                    if (!flag)
                        month++;
                } else {
                    month++;
                }

                // 获取下个月的天数
                if ((LUNAR_INFO[year - 1901] & (0x80000 >> (index - 1))) != 0)
                    staticDayCount = 29;
                else
                    staticDayCount = 30;
            }

            day = sun_NY + 1;
        } else {    // 阳历日在春节前（属于上一农历年）
            spring_NY -= sun_NY;
            year--;
            month = 12;

            // 确定上一年的总月数（可能有闰月，所以可能是13）
            if (((LUNAR_INFO[year - 1901] & 0xF00000) >> 20) == 0)
                index = 12;
            else
                index = 13;

            flag = false;

            // 从最后一个月开始向前减去天数
            if ((LUNAR_INFO[year - 1901] & (0x80000 >> (index - 1))) == 0)
                staticDayCount = 29;
            else
                staticDayCount = 30;

            while (spring_NY > staticDayCount) {
                spring_NY -= staticDayCount;
                index--;

                if (!flag)
                    month--;

                if (month == ((LUNAR_INFO[year - 1901] & 0xF00000) >> 20))
                    flag = !flag;

                if ((LUNAR_INFO[year - 1901] & (0x80000 >> (index - 1))) != 0)
                    staticDayCount = 29;
                else
                    staticDayCount = 30;
            }
        }

        // 将计算结果存入静态变量：低位6位存日，中间6位存月，高位（此处仅用于判断闰月）
        lunarCalendarDay |= day;
        lunarCalendarDay |= (month << 6);

        // 返回当前月份是否为闰月
        return month == ((LUNAR_INFO[year - 1901] & 0xF00000) >> 20);
    }

    /**
     * 公历日期转农历日期
     *
     * @param solarDate 公历日期对象（年份需在1900-2099之间）
     * @return 对应的农历日期对象 LunarDate
     */
    public static LunarDate solarToLunar(Date solarDate) {
        boolean isHeap = getLunarCalendar(solarDate);

        return new LunarDate(
                solarDate.getYear(),
                (lunarCalendarDay & 0x3C0) >> 6, // 取出中间6位得到月份
                lunarCalendarDay & 0x3F,          // 取出低6位得到日期
                isHeap
        );
    }
}