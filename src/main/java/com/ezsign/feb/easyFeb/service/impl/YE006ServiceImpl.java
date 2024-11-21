package com.ezsign.feb.easyFeb.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.easyFeb.dao.YE006DAO;
import com.ezsign.feb.easyFeb.service.YE006Service;
import com.ezsign.feb.easyFeb.vo.YE006VO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.service.YE020Service;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.vo.FileVO;

@SuppressWarnings("NonAsciiCharacters")
@Service("ye006Service")
public class YE006ServiceImpl implements YE006Service{

	Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ye006DAO")
    private YE006DAO ye006DAO;
    
    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "ye020Service")
    private YE020Service ye020Service;
    
	@Override
	public YE006VO getYE006(Map<String, String> params) {

		YE002VO ye002VO = ye006DAO.getYE002(params);
		
		if(ye002VO == null || StringUtils.isBlank(ye002VO.get원천명세ID()) ){
			throw new RuntimeException("데이터가 존재하지 않습니다.");
		}
		
		params.put("원천명세ID", ye002VO.get원천명세ID());
		return ye006DAO.getYE006(params);
	}

	@Override
	public int saveYE006(YE006VO vo, HttpServletRequest request, String bizId) {

		Map params = new HashMap();
		params.put("사용자ID", vo.get사용자ID());
		params.put("계약ID", vo.get계약ID());
		params.put("bizId", vo.getBizId());

		YE002VO ye002VO = ye006DAO.getYE002(params);
		
		if(ye002VO == null || StringUtils.isBlank(ye002VO.get원천명세ID()) ){
			throw new RuntimeException("데이터가 존재하지 않습니다.");
		}
		
		params.put("원천명세ID", ye002VO.get원천명세ID());
		vo.set원천명세ID(ye002VO.get원천명세ID());
		
		try{
			// 파일생성
			String szMonthPath  = bizId;
			String szSavePath = MultipartFileUtil.getSystemTempPath() + szMonthPath;
			List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
			
			int total = resultFileList.size();

			if(total == 1){
				for(int i=0;i<resultFileList.size();i++) {
					FileVO fileVO = resultFileList.get(i);
					String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

//					attachFile.setBizId(bizId);
//					attachFile.set사용자ID(사용자ID);
					
					YE020VO ye020VO = new YE020VO();
					
					ye020VO.set계약ID(vo.get계약ID());
					ye020VO.set사용자ID(vo.get사용자ID());
					ye020VO.set작업자ID(vo.get사용자ID());
					ye020VO.set공제구분코드("M000");
					ye020VO.set공제구분상세코드("D002");
					
					System.out.println("fileVO : " + fileVO);
					File file = new File(filePath);
					System.out.println("filePath : " + filePath);
					if (!file.exists()) {
						System.out.println("[insYE020Ctrl] 파일이 존재하지 않습니다.");
						//message = "파일이 존재하지 않습니다.";
						throw new RuntimeException();
					} else {
						
						if(StringUtils.isBlank(vo.get일련번호())){
							ye020Service.insYE020(ye020VO, fileVO);	
						}else{
							ye020Service.delYE020(ye020VO);
							ye020Service.insYE020(ye020VO, fileVO);
						}
						vo.set추가제출서류번호(ye020VO.get일련번호());
					}

					boolean deleteResult = FileUtil.deleteFile(filePath);
					if(!deleteResult) {
						System.out.println("임시 폴더에 파일이 삭제 되지 않았습니다.");
					}
				}
			}else if(total > 1){
				throw new RuntimeException("파일을 저장할 수 없습니다.");
			}
			
		}catch (Exception ex) {
			throw new RuntimeException("파일을 저장하지 못했습니다.");
		}

		int result = 0;
		int count = ye006DAO.getYE006Count(params);
		if(count > 0){
			ye006DAO.updYE006(vo);
			result++;
		}else{
			ye006DAO.insYE006(vo);
			result++;
		}

		return result;
	}

	@Override
	public void saveYE006List(List<YE006VO> ye006VOList) {

		Map params = null;
		if(ye006VOList != null && ye006VOList.size() > 0){
			for(int i = 0; i < ye006VOList.size(); i++){
				
				YE006VO ye006VO = ye006VOList.get(i);
System.out.println("ye006VO: " + ye006VO);
				EmpVO empVo = new EmpVO();
				empVo.setEmpNo(ye006VO.get사번());
				empVo.setBizId(ye006VO.getBizId());
				empVo = empDAO.getEmpNo(empVo);
				
				params = new HashMap();
				params.put("계약ID", ye006VO.get계약ID());
				params.put("사용자ID", empVo.getUserId());
				YE002VO ye002VO = ye006DAO.getYE002(params);
				
				ye006VO.set사용자ID(empVo.getUserId());
				ye006VO.set원천명세ID(ye002VO.get원천명세ID());
				
				params.put("원천명세ID", ye002VO.get원천명세ID());
				int count = ye006DAO.getYE006Count(params);
				
				if(count > 0){
					ye006DAO.updYE006Admin(ye006VO);
				}else{
					ye006DAO.insYE006(ye006VO);
				}
			}
		}
	}
	
	@Override
	public List<YE006VO> getYE006List(Map params) {
		List list = ye006DAO.getYE006List(params);
		return list;
	}
	
	public int getYE006ListCount(Map params){
		int count = ye006DAO.getYE006ListCount(params);
		return count;
	}
	
	@Override
	public int delYE006(Map<String, String> params) {

		return 0;
	}

	@Override
	public YE006VO getYE006Sum(YE006VO ye006VO) {
		return (YE006VO) ye006DAO.getYE006Sum(ye006VO);
	}

}
