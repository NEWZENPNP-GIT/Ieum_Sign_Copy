package com.ezsign.window.vo;

import com.ezsign.feb.worker.vo.YE001VO;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class Family {

    public String code;
    public String name;
    public String 나이;
    public String 장애인;

	public static List<Family> getIDList(List<YE001VO> list) {
        List<Family> result = new ArrayList<>();
        for (YE001VO item : list) {
            Family family = new Family();
            family.code = item.get부양가족ID();
            family.name = item.get성명();
            family.나이 = item.get나이();
            family.장애인 = item.get장애인();

            result.add(family);
        }

        return result;
    }
}
