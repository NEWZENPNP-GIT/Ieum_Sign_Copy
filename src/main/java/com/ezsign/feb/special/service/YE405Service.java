package com.ezsign.feb.special.service;

import com.ezsign.feb.special.vo.YE405VO;

import java.util.List;

public interface YE405Service {

    List<YE405VO> getYE405List(YE405VO vo) throws Exception;

    YE405VO getYE405ListCount(YE405VO vo) throws Exception;

    void saveYE405(List<YE405VO> list) throws Exception;

    void insYE405(YE405VO vo) throws Exception;

    int updYE405(YE405VO vo) throws Exception;

    int delYE405(YE405VO vo) throws Exception;

    int allDelYE405(YE405VO vo) throws Exception;
}
