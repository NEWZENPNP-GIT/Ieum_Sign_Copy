package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;

@Service("ys030Service")
public class YS030ServiceImpl implements YS030Service {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name="ys000DAO")
	private YS000DAO ys000DAO;
	
	@Resource(name="ys030DAO")
	private YS030DAO ys030DAO;

	@Resource(name="ys031DAO")
	private YS031DAO ys031DAO;	
	
	// 연말정산_사업장 조회
	@Override
	public List<YS030VO> getYS030List(YS030VO vo)  throws Exception {
		
		List<YS030VO> list =  ys030DAO.getYS030List(vo);
		
		if(list.size()==0) {

			//조회시 자료가 없는 경우 본점 사업장 입력처리
			YS030VO keyDataVO = new YS030VO();
			keyDataVO.set계약ID(vo.get계약ID());
			keyDataVO.set지점여부("0");
			keyDataVO.setStartPage(0);
			keyDataVO.setEndPage(1);
			List<YS030VO> keylist =  ys030DAO.getYS030List(keyDataVO);
			
			if(keylist.size()==0) {					
				//계약ID기준 기업ID 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.set계약ID(vo.get계약ID());
				List<YS000VO> ys000List = ys000DAO.getYS000List(ys000VO);
				if(ys000List.size()>0) {
					// 기업ID기준 기업정보 조회
					BizVO bizVO = new BizVO();
					bizVO.setBizId(ys000List.get(0).getBizId());
					bizVO.setStartPage(0);
					bizVO.setEndPage(1);
					List<BizVO> bizList = bizDAO.getBizList(bizVO);
					if(bizList.size()>0) {
						BizVO resultBizVO = bizList.get(0);
						// 사업장 본점 자료 입력
						YS030VO masterVO = new YS030VO();
						masterVO.set계약ID(vo.get계약ID());
						masterVO.set지점여부("0");
						masterVO.set사업장명(resultBizVO.getBizName());
						masterVO.set대표자명(resultBizVO.getOwnerName());
						masterVO.set사업자등록번호(resultBizVO.getBusinessNo());
						masterVO.set회사주소1(resultBizVO.getAddr1());
						masterVO.set회사주소2(resultBizVO.getAddr2());
						masterVO.set회사전화1(resultBizVO.getCompanyTelNum());
						masterVO.set회사팩스1(resultBizVO.getCompanyFaxNum());
						masterVO.set종사업자일련번호("0000");
						masterVO.set사용여부("1");
						ys030DAO.insYS030(masterVO);
						list.add(masterVO);
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		}
		return list;
	}

	// 연말정산_사업장 총건수
	@Override
	public int getYS030ListCount(YS030VO vo)  throws Exception {
		return ys030DAO.getYS030ListCount(vo);
	}
	
	
	// 연말정산_사업장  입력
	@Override
	public void insYS030(YS030VO vo) throws Exception {
		ys030DAO.insYS030(vo);
	}

	// 연말정산_사업장 수정
	@Override
	public int updYS030(YS030VO vo) throws Exception {
		
		// 사업장 본점 체크
		if("0".equals(vo.get지점여부())) {
			// 기업정보 수정
			BizVO bizVO = new BizVO();
			bizVO.setBizId(vo.getBizId());
			bizVO.setBizName(vo.get사업장명());
			bizVO.setOwnerName(vo.get대표자명());
			bizVO.setBusinessNo(vo.get사업자등록번호());
			bizVO.setAddr1(vo.get회사주소1());
			bizVO.setAddr2(vo.get회사주소2());
			bizVO.setCompanyTelNum(vo.get회사전화1());
			bizVO.setCompanyFaxNum(vo.get회사팩스1());
			bizVO.setBizType(vo.getBizType());
			bizDAO.updBiz(bizVO);
		}
		return ys030DAO.updYS030(vo);
	}

	// 연말정산_사업장 삭제
	@Override
	public int delYS030(YS030VO vo) throws Exception {
		int result = 0;
		YS031VO ys031VO = new YS031VO();
		ys031VO.set계약ID(vo.get계약ID());
		ys031VO.set사업장ID(vo.get사업장ID());
		List<YS031VO> list = ys031DAO.getYS031List(ys031VO);
		if(list.size()>0) {
			vo.setMessage("부서를 먼저 삭제해주시기 바랍니다.");
			vo.setCode("100");
		} else {
			result= ys030DAO.delYS030(vo);			
		}
		return result;
	}

	// 연말정산_사업장 종사업자 일련번호 자동증가 값
	@Override
	public String getYS030TaxNumber(YS030VO vo)  throws Exception {
		return ys030DAO.getYS030TaxNumber(vo);
	}
}
