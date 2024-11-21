package com.ezsign.contract.service;

import java.util.List;

import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.vo.*;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.pdf.vo.PageVO;

public interface ContractService {

	//근로게약서 원본PDF파일
	String SERVICE_ID = "171010134547001";	// 근로계약서 서비스ID
	String MONTH_FILE_ID = "10000000000000000000000000000002"; // 근로계약서 원본 콘텐츠ID
	String DAY_FILE_ID = "10000000000000000000000000000004"; // 일용직근로계약서 원본 콘텐츠ID
	String TIME_FILE_ID = "10000000000000000000000000000005"; // 알바근로계약서 원본 콘텐츠ID
	String SAVE_CLASS_ID = "171016170512003";	// 작성된 근로계약서 분류체계ID
	String UPLOAD_CLASS_ID = "171016170512004";	// 사용자 등록요청 근로계약서 양식파일
	long   MAX_FILE_SIZE = 5 * 1024 * 1024; // 계약서 업로드 파일 사이즈 제한

	String MOBILE_CONTRACT_TEMP_PDF_ID = "10000000000000000000000000000010";			// 모바일 근로계약서 기본폼 ID
	String MOBILE_CONTRACT_TEMP_XLS_ID = "10000000000000000000000000000011";			// 모바일 근로계약서 기본폼 ID

	ContractPersonVO sendContractExcel(String bizId, String xlsPath, String fileId,String webSocketKey) throws Exception;

	List<ContentVO> createContractPDF(ContractPersonVO vo, SessionVO loginVO) throws Exception;

	List<ContractPersonVO> createContractPDFList(ContractPersonVO vo, SessionVO loginVO, String webSocketKey) throws Exception;

	ContractPersonVO uploadContractFile(ContractPersonVO vo, FileVO fileVO, SessionVO loginVO) throws Exception;

	ContractPersonVO getContractNonce(ContractPersonVO vo)  throws Exception;

	ContractPersonVO getContractView(ContractPersonVO vo, SessionVO loginVO)  throws Exception;

	List<ContractFormVO> getContractFormList(ContractFormVO vo)  throws Exception;

	List<ContractPersonVO> getContractPersonList(ContractPersonVO vo) throws Exception;

	int getContractPersonListCount(ContractPersonVO vo) throws Exception;

	List<ContractPersonLogVO> getContractPersonLogList(ContractPersonLogVO vo) throws Exception;

	List<ContractPersonNewVO> getContractPersonNewList(ContractPersonNewVO vo)  throws Exception;

	List<ContractPersonNewVO> getContractTransformList(ContractPersonNewVO vo)  throws Exception;

	ContractPersonNewVO getContractTransformListCount(ContractPersonNewVO vo)  throws Exception;

	ContractPersonNewVO getContractTransformStatusListCount(ContractPersonNewVO vo)  throws Exception;

	int updContractForm(ContractFormVO vo) throws Exception;

	ContractPersonNewVO delContractPersonNew(ContractPersonNewVO vo) throws Exception;

	ContractPersonNewVO contractNewUpload(ContractPersonNewVO vo, FileVO fileVO) throws Exception;

	ContractPersonNewVO contractNewUpdateSign(ContractPersonNewVO vo) throws Exception;

	ContractPersonNewVO contractNewUpdateStatus(ContractPersonNewVO vo) throws Exception;

	ContractPersonNewVO contractNewUpdate(String bizId, String fileId, FileVO pdffileVO, FileVO xlsfileVO, FileVO alterfileVO) throws Exception;

	int downloadContractZip(ContractPersonVO vo) throws Exception;

	int delContractPersonTemp(ContractPersonVO vo, String webSocketKey) throws Exception;

	int delContractPerson(ContractPersonVO vo, SessionVO loginVO) throws Exception;

	List<ContentVO> generateContractPDF(ContractPersonVO vo, SessionVO loginVO) throws Exception;

	ContractPersonVO setContractSignHashDataMulti(List<ContractPersonVO> list, SessionVO loginVO, String webSocketKey) throws Exception;

	ContractPersonVO sendContractPersonMulti(List<ContractPersonVO> list, SessionVO loginVO, String webSocketKey) throws Exception;

	boolean sendContractPersonMultiComplete(List<ContractPersonVO> list, SessionVO loginVO) throws Exception;

	List<PageVO> getContractPersonPdfView(ContractPersonVO vo) throws Exception;

	boolean saveContract(ContractPersonVO vo) throws Exception;

	boolean signContract(UserSignDataVO vo) throws Exception;

	boolean signContractCustomer(UserSignDataVO vo) throws Exception;

	boolean rejectContract(UserSignDataVO vo) throws Exception;

	int delContractPersonList(ContractPersonVO vo) throws Exception;

	int downloadContractSelectZip(List<ContractPersonVO> list, SessionVO loginVO) throws Exception;

	int setContractNewData(String bizId) throws Exception;

	ContentVO downContractPdf(ContractPersonVO vo) throws Exception;

	List<ContractPersonNewFormVO> getContractPersonNewFormList(ContractPersonNewFormVO vo)  throws Exception;

	int createContractForm(ContractFormVO vo) throws Exception;

	ContractPersonNewVO getContractFormView(ContractPersonNewVO vo) throws Exception;

	List<ContractPersonVO> getContractPersonUserGrpList(ContractPersonVO vo) throws Exception;

	int getContractPersonUserGrpListCount(ContractPersonVO vo) throws Exception;

	ContractPersonNewVO getContractPersonSign(ContractPersonVO vo) throws Exception;

	int getContractPersonDownloadCount(ContractPersonVO vo) throws Exception;

	int getContractPersonUserGrpDownloadCount(ContractPersonVO vo) throws Exception;

	int getDownloadCount(ContractPersonLogVO vo) throws Exception;

	int delContractAll(List<String> list) throws Exception;

	List<ContractPersonNewFormCommentVO> getContractPersonNewFormCommentList(ContractPersonNewFormCommentVO vo) throws Exception;

	void insContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) throws Exception;

	void delContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) throws Exception;

	void sendContractNewUpdateEmailC(String bizId) throws Exception;

	void sendContractNewUpdateEmailP(String bizId) throws Exception;

	ContractPersonNewVO contractNewFinalUpload(ContractPersonNewVO vo, FileVO fileVO) throws Exception;

	void sendContractNewFormCommentUpdateEmail(String bizId) throws Exception ;

	List<ContractPersonVO> getSelectAuditTrailCertificate(List<ContractPersonVO> list, SessionVO loginVO) throws Exception;

	List<ContractPersonVO> getRemoveContractList(ContractPersonVO vo) throws Exception;

	EmpVO sendNanumhrContractExcel(String bizId, String xlsPath, String metaId, String fileId) throws Exception;

	List<ContractPersonVO> getContIdContractPerson(String multiData, SessionVO loginVO) throws Exception;

	void insContractLog() throws Exception;

	int delContractPersonSelectList(List<ContractPersonVO> vo, SessionVO loginVO) throws Exception;

	void saveContractSetting(ContractSettingVO vo)  throws Exception;

	int downContractDataExcel(String filePath, String fileName, ContractPersonVO vo) throws Exception;

	void sendEmailWithFile(ContractPersonVO vo, String zipFileURL, String zipFileName) throws Exception;

	void sendErrorEmail(ContractPersonVO vo) throws Exception;

	String createContractPreview(ContractFormVO vo) throws Exception;

	ContentVO createContractPreviewPDF(ContractPersonVO vo, SessionVO loginVO, String BizType, String UserType, String CustomerType) throws Exception;

	ContractPersonNewVO contractNewReload(ContractPersonNewVO vo, String message) throws Exception;

	ContractPersonVO getContractPreview(ContractPersonVO vo, SessionVO loginVO)  throws Exception;

	List<ContractFormVO> getContractFormListUpdate(ContractFormVO vo)  throws Exception;

	int delContractPreview(List<ContractPersonVO> vo, SessionVO loginVO) throws Exception;

	void delPreview() throws Exception;
}
