package com.ezsign.framework.util;

public class CRNValidator {

	private final static int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

    public final static boolean isValid(String regNum) {

        if (!isNumeric(regNum) || regNum.length() != 10)
            return false;

        int sum = 0;
        int j = -1;
        for (int i = 0; i < 9; i++) {
            j = Character.getNumericValue(regNum.charAt(i));
            sum += j * LOGIC_NUM[i];
        }

        sum += (int) (Character.getNumericValue(regNum.charAt(8)) * 5 /10);

        int checkNum = (10 - sum % 10) % 10 ;
        return (checkNum == Character.getNumericValue(regNum.charAt(9)));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
