package com.ezsign.contract.vo;

import lombok.Data;

@Data
public class ContractPersonNewFormVO {

	private String serviceId;
	private String bizId;
	private String fileId;
	private String formDataId;
	private String formDataName;
	private String formDataType;	
	private String formDataDefaultValue;
	private String insDate;
	
	private String metaId;
	
}
