package com.ezsign.mobile.service;

import java.util.List;

import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.framework.vo.SessionVO;

public interface MobileContractService {
	
	
	/**
	 * 모바일 전자근로계약 저장
	 * 
	 * @param vo
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public ContractPersonVO saveElectronicContract(ContractPersonVO paramVO) throws Exception;
	
	
	/**
	 * 전자 PDF 파일 생성
	 * 
	 * @param vo
	 * @param loginVO
	 * @param actCd
	 * @return
	 * @throws Exception
	 */
	public List<ContentVO> generateContractPDF(ContractPersonVO vo) throws Exception;
	
	
	/**
	 * 전자문서 전송
	 * 
	 * @param list
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public ContractPersonVO sendContractPerson(ContractPersonVO vo) throws Exception;
	
	/**
	 * 전자근로계약 삭제
	 * 
	 * @param jArray
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public int deleteContractData(ContractPersonVO vo) throws Exception;
}
