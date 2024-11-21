package com.ezsign.window;

public class CodeUtils {

    public static final String 국세청PDF = "0";
    public static final String 국세청 = "1";

    public static boolean is국세청(String 자료구분코드) {
        return 국세청PDF.equals(자료구분코드) || 국세청.equals(자료구분코드);
    }
}
