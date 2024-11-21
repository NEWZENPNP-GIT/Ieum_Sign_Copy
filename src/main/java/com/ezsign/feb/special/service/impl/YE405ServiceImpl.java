package com.ezsign.feb.special.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.service.YE405Service;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ye405Service")
public class YE405ServiceImpl extends AbstractServiceImpl implements YE405Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;
    // 연말정산_이월기부금 조회
    @Override
    public List<YE405VO> getYE405List(YE405VO vo) throws Exception {
    	
    	// 계약년도 조회
		YS000VO ys000VO = new YS000VO();
		ys000VO.setBizId(vo.getBizId());
		ys000VO.set계약ID(vo.get계약ID());
		List<YS000VO> ys000VOList = ys000DAO.getYS000List(ys000VO);
		String 계약년도 = "";
		
		if(ys000VOList.size()>0) {
			계약년도 = ys000VOList.get(0).getFebYear();
		}
		vo.setFebYear(계약년도);
		
        List<YE405VO> list = ye405DAO.getYE405List(vo);

        return list;
    }

    // 연말정산_이월기부금 갯수
    @Override
    public YE405VO getYE405ListCount(YE405VO vo) throws Exception {
        return ye405DAO.getYE405ListCount(vo);
    }

    // 연말정산_이월기부금 저장
    @Override
    public void saveYE405(List<YE405VO> list) throws Exception {
        for (YE405VO vo : list) {
//            if (vo.getDbMode().equals("C")) {
//                ye405DAO.insYE405(vo);
//            } else if (vo.getDbMode().equals("U")) {
//                ye405DAO.updYE405(vo);
//            } else if (vo.getDbMode().equals("D")) {
//                ye405DAO.delYE405(ye405VO);
//            }
            if ("D".equals(vo.getDbMode())) {
                ye405DAO.updYE405Disable(vo);
            } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                ye405DAO.updYE405Disable(vo);
                ye405DAO.insYE405(vo);
            }
        }
    }

    // 연말정산_이월기부금 입력
    @Override
    public void insYE405(YE405VO vo) throws Exception {
        ye405DAO.insYE405(vo);
    }

    // 연말정산_이월기부금 수정
    @Override
    public int updYE405(YE405VO vo) throws Exception {
        // return ye405DAO.updYE405(vo);
        ye405DAO.updYE405Disable(vo);
        ye405DAO.insYE405(vo);
        return 1;
    }

    // 연말정산_이월기부금 삭제
    // 연말정산_이월기부금 사용여부 '0'
    @Override
    public int delYE405(YE405VO vo) throws Exception {
        // return ye405DAO.delYE405(vo);
        return ye405DAO.updYE405Disable(vo);
    }

    // 연말정산_이월기부금 전체삭제
    @Override
    public int allDelYE405(YE405VO vo) throws Exception {
        return ye405DAO.allDelYE405(vo);
    }
}
