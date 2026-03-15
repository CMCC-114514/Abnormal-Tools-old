package kk3twt.abnormal.tools.calculators.date;

/**
 * 日期相关计算类，提供天数转日期、日期推算、日期间隔计算功能。
 * 所有方法均为静态，不可实例化。
 *
 * @author CMCC-114514
 */
public class Calculators {
    private Calculators(){}

    /**
     * 将天数转换为从 startYear 年1月1日开始的日期（以年、月、日表示）。
     * <p>
     * 默认从1月1日开始计算，例如输入365天且第一年为平年，则结果为1年0个月0天（即下一年的1月1日）。
     * 算法：先减整年，再减整月，剩余为日。
     *
     * @param numOfDays 要转换的天数（非负）
     * @param startYear 起始年份
     * @return 表示年、月、日的 Date 对象（年表示经过的整年数，月、日为剩余部分）
     */
    public static Date Conversion(int numOfDays, int startYear) {
        // 如果numOfDays大于365，算整年份
        int year = 0;
        while (numOfDays >= 365) {
            int numOfYear = AuxFunctions.yearCheck(startYear) ? 366 : 365;
            numOfDays -= numOfYear;
            startYear++;
            year++;
        }

        // 再算月数，需要考虑大小月日期不同
        int month = 0;  // 表示几个月（月数-1）
        while (numOfDays > 30) {
            // 大小月判定
            numOfDays -= AuxFunctions.getDayOfMonth(month + 1, startYear);
            // 计算一次月数加一
            month++;
        }

        // 输出并返回日期
        return new Date(year, month, numOfDays);
    }

    /**
     * 日期推算：给定起始日期，加上或减去指定天数，返回新日期。
     * <p>
     * 支持正数（向后推算）和负数（向前推算）。算法会先将日期调整到整年起点，再进行加减，最后处理溢出。
     *
     * @param date      起始日期（会被修改，调用前建议传入副本）
     * @param numOfDays 要推算的天数，大于0向后，小于0向前
     * @return 推算后的新日期（与传入的 date 是同一个对象，已被修改）
     */
    public static Date Calculation(Date date, int numOfDays) {
        // 向前推算：输入的天数小于0
        if (numOfDays < 0) {
            // 天数调正
            numOfDays = -numOfDays;

            // 预处理1：将日期调整为整年
            // 将日期调整至当前年份的12月31日
            int dayOfYear = AuxFunctions.yearCheck(date.year) ? 366 : 365;  // 一年的天数
            int dayCount = AuxFunctions.getDayCount(date);   // 当前年份已经过去的天数
            int dayLeft = dayOfYear - dayCount;  // 当前年份剩余的天数
            numOfDays += dayLeft;     // 天数加上剩余天数，表示调整至12月31日
            date.month = 12;    // 调整日期
            date.day = 31;

            // 如果天数大于365，将天数减到小于365
            while (numOfDays >= 365) {
                dayOfYear = AuxFunctions.yearCheck(date.year) ? 366 : 365;
                numOfDays -= dayOfYear;
                date.year--;
            }

            // 然后对days进行年份换算
            Date addend = Conversion(numOfDays, date.year);
            // 日期直接相减
            date.year -= addend.year;
            date.month -= addend.month;
            date.day -= addend.day;

            // 向后推算：输入的天数大于0
        } else {
            // 预处理：将日期调整为整年
            // 将日期调整至下一年的1月1日
            while (numOfDays >= 365) {
                // 第一步：将days减去当前年份的剩余天数
                int dayOfYear = AuxFunctions.yearCheck(date.year) ? 366 : 365;  // 一年的天数
                int dayCount = AuxFunctions.getDayCount(date);   // 当前年份已经过去的天数
                int dayLeft = dayOfYear - dayCount;  // 当前年份剩余的天数
                numOfDays -= dayLeft;   // 将天数减去当前年份剩余的天数，表示跳过这一年
                // 第二步：将日期重置到下年的1月1日
                date.year++;
                date.month = date.day = 1;
            }

            // 然后对days进行年份换算
            Date addend = Conversion(numOfDays, date.year);
            // 日期直接相加
            date.year += addend.year;
            date.month += addend.month;
            date.day += addend.day;
        }

        // 处理日期溢出
        AuxFunctions.adjustDate(date);

        // 返回日期
        return date;
    }

    /**
     * 计算两个日期之间的间隔天数（起始日期必须早于或等于结束日期）。
     * <p>
     * 算法：将起始日期逐步调整到与结束日期同年同月同日，累加中间经过的天数。
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 间隔天数，如果起始日期晚于结束日期则返回 -1 表示错误
     */
    public static int Interval(Date startDate, Date endDate) {
        int dateInterval = 0;

        // 检查起始日期是否晚于结束日期
        if (startDate.year > endDate.year ||
                (startDate.year == endDate.year && startDate.month > endDate.month) ||
                (startDate.year == endDate.year && startDate.month == endDate.month
                        && startDate.day > endDate.day)) {
            return -1;
        }

        // 将起始日期调到和结束日期相同
        // 调整日
        // 如果结束日期的天数大于起始日期，就将整月天数减去两日期之间间隔的天数
        if (startDate.day > endDate.day) {
            dateInterval += AuxFunctions.getDayOfMonth(startDate.month, startDate.year)
                    - (startDate.day - endDate.day);
            startDate.month++;
        } else {
            dateInterval += endDate.day - startDate.day;
        }
        startDate.day = endDate.day;

        // 调整月份，与调整日类似
        if (startDate.month > endDate.month) {
            dateInterval += AuxFunctions.yearCheck(startDate.year + 1) ? 366 : 365
                    - AuxFunctions.getDayCount(endDate, startDate);
            startDate.year++;
        } else {
            dateInterval += AuxFunctions.getDayCount(startDate, endDate);
        }
        startDate.month += endDate.month;

        // 最后计算整年的天数
        for (int i = startDate.year; i < endDate.year; i++) {
            dateInterval += AuxFunctions.yearCheck(i) ? 366 : 365;
        }

        return dateInterval;
    }
}