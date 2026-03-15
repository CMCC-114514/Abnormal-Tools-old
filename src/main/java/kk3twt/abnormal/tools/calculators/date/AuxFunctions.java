package kk3twt.abnormal.tools.calculators.date;

/**
 * 日期计算辅助函数类，提供闰年判断、月份天数获取、日期调整等通用方法。
 * 所有方法均为静态，不可实例化。
 *
 * @author CMCC-114514
 */
public class AuxFunctions {
    private AuxFunctions(){}

    /**
     * 判断指定年份是否为闰年
     * <p>
     * 闰年规则：能被4整除但不能被100整除，或者能被400整除。
     *
     * @param year 要判断的年份
     * @return true 表示闰年，false 表示平年
     */
    public static boolean yearCheck(int year) {
        // 闰年两种形式：
        // 1.能被4整除的非世纪年
        // 2.能被400整除的世纪年
        return ((year % 100 != 0 && year % 4 == 0) || (year % 100 == 0 && year % 400 == 0));
    }

    /**
     * 返回指定月份在给定年份中的天数
     * <p>
     * 月份范围 1-12，对于二月会根据闰年自动调整。
     *
     * @param month    月份（1-12）
     * @param thisYear 年份（用于判断二月是否闰年）
     * @return 该月的天数，如果月份不合法返回 -1
     */
    public static int getDayOfMonth(int month, int thisYear) {
        return switch (month) {
            // 大月
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            // 小月
            case 4, 6, 9, 11 -> 30;
            // 二月
            case 2 -> yearCheck(thisYear) ? 29 : 28;
            // 输入了错误的月份就输出-1，表示错误
            default -> -1;
        };
    }

    /**
     * 计算指定日期在当年中是第几天（从1月1日算起，但不包含当天）
     * <p>
     * 例如：1月1日返回0，1月2日返回1。
     *
     * @param date 要计算的日期
     * @return 从1月1日到 date 前一天经过的天数
     */
    public static int getDayCount(Date date) {
        int dayPassed = 0;   // 当前年份过去的天数

        // 加上每个月的天数
        for (int i = 1; i < date.month; i++) {
            dayPassed += getDayOfMonth(i, date.year);
        }

        // date.day表示的这一天还没过完，所以要减一
        dayPassed += date.day - 1;
        return dayPassed;
    }

    /**
     * 计算两个同一年月份之间的天数（起始日期到结束日期）
     * <p>
     * 例如：6月1日与9月1日之间相差的天数为6月、7月、8月的整月天数之和（即92天）。
     * 注意：此方法计算的是从 startDate 所在月份的下一个月开始到 endDate 所在月份的前一个月为止的整月天数总和。
     * 通常用于辅助日期间隔计算。
     *
     * @param startDate 起始日期（仅使用其 month 字段，year 使用 endDate 的 year）
     * @param endDate   结束日期
     * @return 整月间隔天数
     */
    public static int getDayCount(Date startDate, Date endDate) {
        int numOfDay = 0;

        // 直接相加整月的天数
        for (int i = startDate.month; i < endDate.month; i++) {
            numOfDay += getDayOfMonth(i, endDate.year);
        }
        return numOfDay;
    }

    /**
     * 处理日期溢出问题，将不合理的日期调整为合法日期
     * <p>
     * 例如将2025年13月32日处理为2026年2月1日。支持月份>12、月份<1、天数<1的情况。
     *
     * @param date 待处理的日期对象（会被修改）
     */
    public static void adjustDate(Date date) {
        // 调整月份溢出到年份
        while (date.month > 12) {
            date.month -= 12;
            date.year++;
        }

        // 调整负数天数（向前借位）
        while (date.day < 1) {
            date.month--;
            if (date.month < 1) {
                date.month = 12;
                date.year--;
            }
            date.day += getDayOfMonth(date.month, date.year);
        }
    }
}