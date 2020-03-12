package com.linlif.linechart;

/**
 * Created by linlif on 2020-03-12
 */


import java.util.Collection;
import java.util.List;

public class ListUtil {
    public ListUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> T getLastItem(List<T> list) {
        return list == null ? null : getItem(list, size(list) - 1);
    }

    public static <T> T getItem(List<T> list, int index) {
        return checkIndex(list, index) ? list.get(index) : null;
    }

    public static boolean checkIndex(List<?> list, int index) {
        return index >= 0 && index < size(list);
    }

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

}
