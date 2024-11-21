package com.ezsign.popup.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExcelUploadVO {
	
	private String mapId;
	private String mapName;
    private String columnId;
    private String id;
    private String fileId;
    private String excelType;
    private String columnType;

    @ApiModelProperty(hidden = true)
    private String grpCommCode;
    @ApiModelProperty(hidden = true)
    private String codeValue;

    private String required;
    private String columnName;
    private String displayName;
    
    @ApiModelProperty(hidden = true)
    private String bizId;
    private int mappingOrder;
    private int defaultOrder;
    
    private List<String> codes;
}
