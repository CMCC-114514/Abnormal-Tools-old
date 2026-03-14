package calculators.date;

/**
 * 自定义的简单日期类，用于替代Java标准库中的日期类。
 * 仅包含年、月、日三个整数字段，提供基本的get/set和显示方法。
 *
 * @author CMCC-114514
 */
public class Date {

    /** 年份 */
    int year;
    /** 月份（1-12） */
    int month;
    /** 日期（1-31） */
    int day;

    /**
     * 带参数的构造方法
     * @param year  年份
     * @param month 月份
     * @param day   日期
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}