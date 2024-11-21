package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.special.dao.YE402DAO;
import com.ezsign.feb.special.dao.YE403DAO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.worker.dao.YE013DAO;
import com.ezsign.feb.worker.service.YE013Service;
import com.ezsign.feb.worker.vo.YE013VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye013Service")
public class YE013ServiceImpl extends AbstractServiceImpl implements YE013Service {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ye013DAO")
	private YE013DAO ye013DAO;
	
	@Resource(name="ye402DAO")
	private YE402DAO ye402DAO;
	
	@Resource(name="ye403DAO")
	private YE403DAO ye403DAO;
	
	@Resource(name="ye108DAO")
	private YE108DAO ye108DAO;
	
	@Resource(name="empService")
	private EmpService empService;
	
	// 연말정산_공제불가회사지원금 저장
	@Override
	public void saveYE013(List<YE013VO> list) throws Exception {
		
		// 데이터 존재 여부확인 (계약ID, 사용자ID)
		for(int i=0;i<list.size();i++) {
			YE013VO ye013VO = list.get(i);
			System.out.println("계약ID:" + ye013VO.get계약ID());
			System.out.println("사용자ID:" + ye013VO.get사용자ID());

			
			if(ye013VO.getDbMode().equals("C")) {
				if("1".equals(ye013VO.get공제구분코드())) {  // 의료비
					YE402VO ye402VO = new YE402VO();
					
					ye402VO = setYE402(ye013VO);
					
					ye402DAO.insYE402(ye402VO);
				} else if ("2".equals(ye013VO.get공제구분코드())) {  // 교육비
					YE403VO ye403VO = new YE403VO();
					
					ye403VO = setYE403(ye013VO);
					
					ye403DAO.insYE403(ye403VO);
				} else if ("3".equals(ye013VO.get공제구분코드())) {  // 신용카드
					YE108VO ye108VO = new YE108VO();
					
					ye108VO = setYE108(ye013VO);
					
					ye108DAO.insYE108(ye108VO);
				}
				// ye013DAO.insYE013(ye013VO);
			} else if(ye013VO.getDbMode().equals("U")) {
				if("1".equals(ye013VO.get공제구분코드())) {  // 의료비
					YE402VO ye402VO = new YE402VO();
					
					ye402VO = setYE402(ye013VO);
					
					ye402DAO.updYE402(ye402VO);
				} else if ("2".equals(ye013VO.get공제구분코드())) {  // 교육비
					YE403VO ye403VO = new YE403VO();
					
					ye403VO = setYE403(ye013VO);
					
					ye403DAO.updYE403(ye403VO);
				} else if ("3".equals(ye013VO.get공제구분코드())) {  // 신용카드
					YE108VO ye108VO = new YE108VO();
					
					ye108VO = setYE108(ye013VO);
					
					ye108DAO.updYE108(ye108VO);
				}
				// ye013DAO.updYE013(ye013VO);
			} else if(ye013VO.getDbMode().equals("D")) {
				if("1".equals(ye013VO.get공제구분코드())) {  // 의료비
					YE402VO ye402VO = new YE402VO();
					
					ye402VO = setYE402(ye013VO);
					
					ye402DAO.delYE402(ye402VO);
				} else if ("2".equals(ye013VO.get공제구분코드())) {  // 교육비
					YE403VO ye403VO = new YE403VO();
					
					ye403VO = setYE403(ye013VO);
					
					ye403DAO.delYE403(ye403VO);
				} else if ("3".equals(ye013VO.get공제구분코드())) {  // 신용카드
					YE108VO ye108VO = new YE108VO();
					
					ye108VO = setYE108(ye013VO);
					
					ye108DAO.delYE108(ye108VO);
				}
				// ye013DAO.delYE013(ye013VO);
			}
		}
		
	}
	
	// 연말정산_공제불가회사지원금 조회
	@Override
	public List<YE013VO> getYE013List(YE013VO vo) throws Exception {
		List<YE013VO> list = ye013DAO.getYE013List(vo);
		
		return list;
	}
	
	// 연말정산_공제불가회사지원금 갯수
	@Override
	public YE013VO getYE013ListCount(YE013VO vo) throws Exception {
		return ye013DAO.getYE013ListCount(vo);
	}
	
	// 연말정산_공제불가회사지원금 입력
	@Override
	public int insYE013(YE013VO vo) throws Exception {
		int result = 0;
		
		ye013DAO.insYE013(vo);
		result++;
		
		return result;
	}

	// 연말정산_공제불가회사지원금 수정
	@Override
	public int updYE013(YE013VO vo) throws Exception {
		
		return ye013DAO.updYE013(vo);
	}

	// 연말정산_공제불가회사지원금 삭제
	@Override
	public int delYE013(YE013VO vo) throws Exception {
		
		return ye013DAO.delYE013(vo);
		
	}
	
	// 의료비
	public YE402VO setYE402(YE013VO ye013VO) {
		YE402VO ye402VO = new YE402VO();
		ye402VO.set계약ID(ye013VO.get계약ID());
		ye402VO.set사용자ID(ye013VO.get사용자ID());
		ye402VO.set작업자ID(ye013VO.get작업자ID());
		ye402VO.set부양가족ID(ye013VO.get부양가족ID());
		ye402VO.set일련번호(ye013VO.get일련번호());
		ye402VO.set자료구분코드("2");
		ye402VO.set의료비증빙코드(ye013VO.get의료증빙코드());
		ye402VO.set공제종류코드(ye013VO.get공제상세코드());
		ye402VO.set지급처_사업자등록번호(ye013VO.get사업자등록번호());
		ye402VO.set상호(ye013VO.get상호());
		ye402VO.set건수(ye013VO.get건수());
		ye402VO.set의료비유형(ye013VO.get의료비유형());
		ye402VO.set차감금액(0);
		
		if(ye013VO.get공제금액() > 0) {
			ye402VO.set지출액(-ye013VO.get공제금액());			
		} else {
			ye402VO.set지출액(ye013VO.get공제금액());
		}
		return ye402VO;
	}
	
	// 교육비
	public YE403VO setYE403(YE013VO ye013VO) {
		YE403VO ye403VO = new YE403VO();
		
		ye403VO.set계약ID(ye013VO.get계약ID());
		ye403VO.set사용자ID(ye013VO.get사용자ID());
		ye403VO.set작업자ID(ye013VO.get작업자ID());
		ye403VO.set부양가족ID(ye013VO.get부양가족ID());
		ye403VO.set일련번호(ye013VO.get일련번호());
		ye403VO.set자료구분코드("2");
		ye403VO.set공제종류코드(ye013VO.get공제상세코드());
		ye403VO.set공납금_차감금액(0);
		ye403VO.set교복구입비(0);
		ye403VO.set교복구입비_차감금액(0);
		ye403VO.set체험학습비(0);
		ye403VO.set체험학습비_차감금액(0);

		if(ye013VO.get공제금액() > 0 ) {
			ye403VO.set공납금(-ye013VO.get공제금액());
		} else {
			ye403VO.set공납금(ye013VO.get공제금액());
		}
		return ye403VO;
	}
	
	// 신용카드
	public YE108VO setYE108(YE013VO ye013VO) {
		YE108VO ye108VO = new YE108VO();
		
		ye108VO.set계약ID(ye013VO.get계약ID());
		ye108VO.set사용자ID(ye013VO.get사용자ID());
		ye108VO.set작업자ID(ye013VO.get작업자ID());
		ye108VO.set부양가족ID(ye013VO.get부양가족ID());
		ye108VO.set일련번호(ye013VO.get일련번호());
		ye108VO.set자료구분코드("2");
		ye108VO.set기간구분코드("1");
		
		if("1".equals(ye013VO.get공제상세코드())) {
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set신용카드(-ye013VO.get공제금액());
			} else {
				ye108VO.set신용카드(ye013VO.get공제금액());
			}
			ye108VO.set현금영수증(0);
			ye108VO.set직불_선불카드(0);
			ye108VO.set도서공연(0);
			ye108VO.set전통시장(0);
			ye108VO.set대중교통(0);
		} else if("2".equals(ye013VO.get공제상세코드())) {
			ye108VO.set신용카드(0);
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set현금영수증(-ye013VO.get공제금액());
			} else {
				ye108VO.set현금영수증(ye013VO.get공제금액());
			}
			ye108VO.set직불_선불카드(0);
			ye108VO.set도서공연(0);
			ye108VO.set전통시장(0);
			ye108VO.set대중교통(0);
		} else if("3".equals(ye013VO.get공제상세코드())) {
			ye108VO.set신용카드(0);
			ye108VO.set현금영수증(0);
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set직불_선불카드(-ye013VO.get공제금액());
			} else {
				ye108VO.set직불_선불카드(ye013VO.get공제금액());
			}
			ye108VO.set도서공연(0);
			ye108VO.set전통시장(0);
			ye108VO.set대중교통(0);
		} else if("4".equals(ye013VO.get공제상세코드())) {
			ye108VO.set신용카드(0);
			ye108VO.set현금영수증(0);
			ye108VO.set직불_선불카드(0);
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set도서공연(-ye013VO.get공제금액());
			} else {
				ye108VO.set도서공연(ye013VO.get공제금액());
			}
			ye108VO.set전통시장(0);
			ye108VO.set대중교통(0);
		} else if("5".equals(ye013VO.get공제상세코드())) {
			ye108VO.set신용카드(0);
			ye108VO.set현금영수증(0);
			ye108VO.set직불_선불카드(0);
			ye108VO.set도서공연(0);
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set전통시장(-ye013VO.get공제금액());
			} else {
				ye108VO.set전통시장(ye013VO.get공제금액());
			}
			ye108VO.set대중교통(0);
		} else if("6".equals(ye013VO.get공제상세코드())) {
			ye108VO.set신용카드(0);
			ye108VO.set현금영수증(0);
			ye108VO.set직불_선불카드(0);
			ye108VO.set도서공연(0);
			ye108VO.set전통시장(0);
			if(ye013VO.get공제금액() > 0) {
				ye108VO.set대중교통(-ye013VO.get공제금액());
			} else {
				ye108VO.set대중교통(ye013VO.get공제금액());
			}
		}
		
		return ye108VO;
	}
}
