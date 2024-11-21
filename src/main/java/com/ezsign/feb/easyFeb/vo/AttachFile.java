package com.ezsign.feb.easyFeb.vo;

import org.springframework.web.multipart.MultipartFile;

import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.worker.vo.YE020VO;

import lombok.Data;

@Data
public class AttachFile {

	private String 공제구분코드;
	private String bizId;
	private String 계약ID;
	private String 사용자ID;
	private String dbMode;
	private String reg;

	private YE020VO 첨부파일;
	
	private YE401VO 보험료;
	private YE402VO 의료비;
	private YE403VO 교육비;
	private YE108VO 신용카드;
	private YE404VO 기부금;
	private YE105VO 월세액공제;

}
