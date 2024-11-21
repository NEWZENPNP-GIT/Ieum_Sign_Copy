package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.service.YE001Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.YW413Request;

@Service("ye001Service")
public class YE001ServiceImpl implements YE001Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;

	@Resource(name = "ye001DAO")
	private YE001DAO ye001DAO;

	@Resource(name = "ye900DAO")
	private YE900DAO ye900DAO;

	@Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
	
	// 연말정산_근로자부양가족 조회
	@Override
	public List<YE001VO> getYE001(YE001VO vo, int userType) throws Exception {
		List<YE001VO> list = ye001DAO.getYE001List(vo);

		for (int i = 0; i < list.size(); i++) {
			YE001VO ye001VO = list.get(i);
			String identityCode = ye001VO.get개인식별번호();

			// 개인식별코드 복호화
			if (StringUtil.isNotNull(identityCode)) {
				ye001VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
			}

			list.set(i, ye001VO);
		}
		return list;
	}

	// 연말정산_근로자부양가족 저장
	@Override
	public void saveYE001(YW413Request body) throws Exception {
		String identityCode = "";

		// 관리자정정사유
		if (body.관리자정정사유 != null) {
			body.관리자정정사유.set사유구분("W413");
			ye900DAO.updYE900Disable(body.관리자정정사유);
			ye900DAO.insYE900(body.관리자정정사유);
		}

		List<YE001VO> list = body.부양가족;

		YE901VO selYE901VO = new YE901VO();
		selYE901VO.set작업자ID(body.관리자정정사유.get작업자ID());
		selYE901VO.set진행현황코드("12");	// 부양가족 관리
		
		String 계약ID = null;
		String 사용자ID = null;
		// 데이터 존재 여부확인 (계약ID, 사용자ID)
		for (int i = 0; i < list.size(); i++) {
			YE001VO ye001VO = list.get(i);
			System.out.println("계약ID:" + ye001VO.get계약ID());
			System.out.println("사용자ID:" + ye001VO.get사용자ID());
			
			계약ID = list.get(0).get계약ID();
			사용자ID = list.get(0).get사용자ID();
			
			// 개인식별코드 암호화
			if (StringUtil.isNotNull(ye001VO.get개인식별번호())) {
				identityCode = SecurityUtil.getinstance().aesEncode(ye001VO.get개인식별번호());
				ye001VO.set개인식별번호(identityCode);
			}

			if (ye001VO.getDbMode().equals("C")) {
				ye001DAO.insYE001(ye001VO);
			} else if (ye001VO.getDbMode().equals("U")) {
				ye001DAO.updYE001(ye001VO);
			} else if (ye001VO.getDbMode().equals("D")) {
				if (!ye001VO.get사용자ID().equals(ye001VO.get부양가족ID())) {
					ye001DAO.delYE001(ye001VO);
				}
			}
		}
		
		selYE901VO.set계약ID(계약ID);
		selYE901VO.set사용자ID(사용자ID);
		
		// 근로자 진행관리현황 등록
		if (StringUtil.isNotNull(계약ID) && StringUtil.isNotNull(사용자ID)) {
			ye901DAO.insYE901(selYE901VO);
		}
	}

	// 연말정산_근로자부양가족 입력
	@Override
	public void insYE001(YE001VO vo) throws Exception {
		String identityCode = "";
		
		// 데이터 존재 여부확인 (계약ID, 사용자ID)
		System.out.println("계약ID:" + vo.get계약ID());
		System.out.println("사용자ID:" + vo.get사용자ID());

		// 개인식별코드 암호화
		if (StringUtil.isNotNull(vo.get개인식별번호())) {
			identityCode = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호());
			vo.set개인식별번호(identityCode);
		}

		ye001DAO.insYE001(vo);
	}

	// 연말정산_근로자부양가족 수정
	@Override
	public int updYE001(YE001VO vo) throws Exception {
		String identityCode = "";
		
		int result = 0;

		// 개인식별코드 암호화
		if (StringUtil.isNotNull(vo.get개인식별번호())) {
			identityCode = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호());
			vo.set개인식별번호(identityCode);
		}

		ye001DAO.updYE001(vo);
		result++;

		return result;
	}

	// 연말정산_근로자부양가족 삭제
	@Override
	public int delYE001(YE001VO vo) throws Exception {
		int result = 0;

		ye001DAO.delYE001(vo);
		result++;

		return result;
	}

	// 연말정산_근로자부양가족 조회
	@Override
	public List<YE001VO> getYE001List(YE001VO vo, int userType) throws Exception {
		List<YE001VO> list = ye001DAO.getYE001List(vo);

		for (int i = 0; i < list.size(); i++) {
			YE001VO ye001VO = list.get(i);
			String identityCode = ye001VO.get개인식별번호();

			// 개인식별코드 복호화
			if (StringUtil.isNotNull(identityCode)) {
				ye001VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
			}

			// 부양가족 조회
			YE001VO ye001SubVO = new YE001VO();
			ye001SubVO.set계약ID(vo.get계약ID());
			ye001SubVO.setBizId(vo.getBizId());
			ye001SubVO.set사용자ID(ye001VO.get사용자ID());
			List<YE001VO> ye001Sublist = ye001DAO.getYE001SubList(ye001SubVO);

			for (int j = 0; j < ye001Sublist.size(); j++) {
				YE001VO subYE001VO = ye001Sublist.get(j);

				identityCode = subYE001VO.get개인식별번호();
				// 개인식별코드 복호화
				if (StringUtil.isNotNull(identityCode)) {
					subYE001VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
				ye001Sublist.set(j, subYE001VO);
			}
			ye001VO.setYe001SubList(ye001Sublist);
			list.set(i, ye001VO);
		}
		return list;
	}

	// 연말전산_근로자부양가족 갯수
	@Override
	public int getYE001ListCount(YE001VO vo) throws Exception {
		return ye001DAO.getYE001ListCount(vo);
	}

	// 연말정산_근로자부양가족 ID 조회
	@Override
	public YE001VO getYE001ID(YE001VO vo) throws Exception {
		return ye001DAO.getYE001ID(vo);
	}
	
	/**
	 * 부양가족  나이정보 일괄 재집계
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int allFamilyAge(YE001VO vo, String 계약년도) throws Exception{
		
		int result = 0;
		
		logger.debug("# 계약 아이디 : " + vo.get계약ID() );

		List<YE001VO> ye001List = ye001DAO.getYE001List(vo);		
		YE001VO upVO = new YE001VO();
		
		for(YE001VO ye001VO : ye001List){
						
			int 나이 = 0;
   			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
   			
   			if(StringUtils.isNotEmpty(개인식별번호)){
   				
	   			switch (개인식별번호.substring(6, 7)) {
	   				case "1":
	   				case "2":
	   				case "5":
	   				case "6":
	   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("19" + 개인식별번호.substring(0, 2));
	   					break;
	   				case "3":
	   				case "4":
	   				case "7":
	   				case "8":
	   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("20" + 개인식별번호.substring(0, 2));
	   					break;
	   				case "9":
	   				case "0":
	   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("18" + 개인식별번호.substring(0, 2));
	   					break;
	   			}
	   			
	   			upVO.set계약ID(ye001VO.get계약ID());
	   			upVO.set사용자ID(ye001VO.get사용자ID());
	   			upVO.set부양가족ID(ye001VO.get부양가족ID());
	   			upVO.set나이(String.valueOf(나이));
	   			
	   			ye001DAO.updYE001(upVO);
	   			result++;
   			}
   			
		}

		return result;
		
	}
}
