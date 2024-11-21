package com.ezsign.window.vo;

import com.ezsign.code.vo.CodeVO;

import java.util.ArrayList;
import java.util.List;

public class Code {

    public String code;
    public String name;

	public static List<Code> getCodeList(List<CodeVO> list) {
        List<Code> result = new ArrayList<>();
        for (CodeVO item : list) {
            Code code = new Code();
            code.code = item.getCommCode();
            code.name = item.getCommName();

            result.add(code);
        }

        return result;
    }

    public static List<Code> getCodeList(List<CodeVO> list, int start, int end) {
        List<Code> result = new ArrayList<>();
        for (CodeVO item : list) {
            try {
                int codeNo = Integer.parseInt(item.getCommCode());
                if (codeNo < start || codeNo > end) {
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            Code code = new Code();
            code.code = item.getCommCode();
            code.name = item.getCommName();

            result.add(code);
        }

        return result;
    }
}
