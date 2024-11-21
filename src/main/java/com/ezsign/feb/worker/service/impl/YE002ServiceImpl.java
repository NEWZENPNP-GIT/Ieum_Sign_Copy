package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.service.YE002Service;
import com.ezsign.feb.worker.vo.YE002VO;

@Service("ye002Service")
public class YE002ServiceImpl implements YE002Service {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ye002DAO")
	private YE002DAO ye002DAO;
	
	@Resource(name="ye003DAO")
	private YE003DAO ye003DAO;
	
	@Resource(name="empService")
	private EmpService empService;

	// 연말정산_주/종근무지 전체 조회
	@Override
	public List<YE002VO> getYE002List(YE002VO vo) throws Exception {
		List<YE002VO> list = ye002DAO.getYE002List(vo);
		
		return list;
	}
	
	// 연말정산_주/종근무지 갯수
	@Override
	public int getYE002ListCount(YE002VO vo) throws Exception {
		return ye002DAO.getYE002ListCount(vo);
	}

	// 연말정산_주/종근무지 조회
	@Override
	public YE002VO getYE002(YE002VO vo) throws Exception {
		return ye002DAO.getYE002(vo);		
	}

	// 연말정산_종근무지 저장
	@Override
	public void saveYE002(YE000VO vo) throws Exception {
		
		if(vo.getDbMode().equals("C")) {
			
			// 종(전)근무지 급여항목 등록
			ye003DAO.insYE003(vo);

			// 종(전)근무지 등록
			ye002DAO.insYE002(vo);
		} else if(vo.getDbMode().equals("U")) {
			// 종(전)근무지 수정
			ye002DAO.updYE002(vo);
			
			// 종(전)근무지 급여항목 수정
			ye003DAO.updYE003(vo);
		} else if(vo.getDbMode().equals("D")) {

			// 종(전)근무지 삭제
			ye002DAO.delYE002(vo);
			
			// 종(전)근무지 급여항목 삭제
			ye003DAO.delYE003(vo);
		}
	}

	// 연말정산_종근무지 입력
	@Override
	public int insYE002(YE000VO vo) throws Exception {
		int result = 0;
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setEmpNo(vo.getEmpNo());
		
		EmpVO empResultVO = empService.getEmpNo(empVO);
		if (empResultVO == null) return 0;
		
		vo.set사용자ID(empResultVO.getUserId());
		
		// 종(전)근무지 급여항목 등록
		
		ye003DAO.insYE003(vo);

		// 종(전)근무지 등록
		// 종(전)근무지
		vo.set근무지구분("2");
		ye002DAO.insYE002(vo);
		result++;
		
		return result;
	}

	// 연말정산_주/종근무지 수정
	@Override
	public int updYE002(YE000VO vo) throws Exception {
		int result = 0;
		
		// 종(전)근무지 수정
		ye002DAO.updYE002(vo);
		
		// 종(전)근무지 급여항목 수정
		ye003DAO.updYE003(vo);
		result++;
		
		return result;
	}

	// 연말정산_주/종근무지 삭제
	@Override
	public int delYE002(YE000VO vo) throws Exception {
		int result = 0;
		
		YE002VO ye002VO = new YE002VO();
		ye002VO.set계약ID(vo.get계약ID());
		ye002VO.set사용자ID(vo.get사용자ID());
		ye002VO.set일련번호(vo.get일련번호());
		
		YE002VO resultVO = new YE002VO();
		resultVO = ye002DAO.getYE002(ye002VO);
		
		// 종(전)근무지 삭제
		ye002DAO.delYE002(vo);
		
		// 종(전)근무지 급여항목 삭제
		vo.set원천명세ID(resultVO.get원천명세ID());
		ye003DAO.delYE003(vo);
		result++;
		
		return result;
	}


	// 연말정산_주/종근무지 갯수
	@Override
	public int getYE002ChkEdit(YE002VO vo) throws Exception {
		return ye002DAO.getYE002ChkEdit(vo);
	}
}
