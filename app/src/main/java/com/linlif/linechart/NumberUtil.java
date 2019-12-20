package com.linlif.linechart;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字工具类
 */
public class NumberUtil {

    /**
     * 将整型字符串转成整型数值
     *
     * @param intStr
     * @param defaultValue
     * @return
     */
    public static int parseInt(String intStr, int defaultValue) {

        if (intStr == null || intStr.length() == 0)
            return defaultValue;

        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    /**
     * 将long字符串转成数值
     *
     * @param longStr
     * @param defaultValue
     * @return
     */
    public static long parseLong(String longStr, long defaultValue) {

        if (longStr == null || longStr.length() == 0)
            return defaultValue;

        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    /**
     * 将float字符串转成数值
     *
     * @param floatStr
     * @param defaultValue
     * @return
     */
    public static float parseFloat(String floatStr, float defaultValue) {

        if (floatStr == null || floatStr.length() == 0)
            return defaultValue;

        try {
            return Float.parseFloat(floatStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    /**
     * 将double字符串转成数值
     *
     * @param doubleStr
     * @param defaultValue
     * @return
     */
    public static double parseDouble(String doubleStr, long defaultValue) {

        if (doubleStr == null || doubleStr.length() == 0)
            return defaultValue;

        try {
            return Double.parseDouble(doubleStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    /**
     * 格式化 金币格式
     *
     * @param price
     * @return
     */
    public static String getPriceFormat(double price) {
        NumberFormat numberFormat = new DecimalFormat("###.#");
        return numberFormat.format(price);
    }

    /**
     * 对字符串型数值进行加1或减1操作
     * 保证返回的数值大于等于0
     *
     * @param intStr
     * @param isAdd
     * @return
     */
    public static String addOrMinus1(String intStr, boolean isAdd) {

        int value = parseInt(intStr, 0);
        value = isAdd ? value + 1 : value - 1;
        if (value < 0)
            value = 0;

        return String.valueOf(value);
    }

    public static int addOrMinus1(int value, boolean isAdd) {

        if (isAdd)
            value += 1;
        else
            value -= 1;

        if (value < 0)
            value = 0;

        return value;
    }

    /**
     * 对小于10的数值补高位补0操作
     *
     * @param num
     * @return
     */
    public static String addZeroLessThan10(int num) {

        return num >= 0 && num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * 格式化数据，保留一位小数 如果小数点后为0则去除
     *
     * @param value
     * @return
     */
    public static String formartPointOneZero(String value) {

        return formartPointOneZero(parseFloat(value, 0));
    }

    public static String formartPointOneZero(float value) {

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(value);
    }

    public static String formartPointTwoZero(float value) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    public static String formatPointNoZero(String value) {

        return formatPointNoZero(parseFloat(value, 0));
    }

    public static String formatPointNoZero(float value) {

        DecimalFormat decimalFormat = new DecimalFormat();
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        BigDecimal decimal = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        return decimalFormat.format(decimal);
    }

    /**
     * 过滤整数float小数点后的0
     * @return eg: 2.0 -> 2
     */
    public static String floatFilterZoneAfterPoint(float value) {
        return new DecimalFormat( "#0.##").format(value);
    }
}
