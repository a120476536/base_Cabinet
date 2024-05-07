package com.linji.mylibrary.utils;

import java.util.List;

public class ListUtil {
    //方法一：使用list中的containsAll方法，此方法是判断list2是否是list的子集，即list2包含于list
    public static boolean compareByContainsAll(List list, List list2) {
        boolean flag = false;
        if (list.size() == list2.size()) {
            if (list.containsAll(list2)) {
                flag = true;
            }
        }
        return flag;
    }

    public static String splitList(List<String> list) {
        return splitList(list, "、");
    }

    public static String splitList(List<String> list, String split) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                stringBuffer.append(list.get(i) + split);
            } else {
                stringBuffer.append(list.get(i));
            }
        }
        return stringBuffer.toString();
    }

    public static int convertNum(int num) {
        int checkNum = 1;
        if (num > 10) {
            checkNum = num % 10 == 0 ? num / 10 : (num / 10) + 1;
        }
        return checkNum;
    }
}
