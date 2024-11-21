package com.ezsign.feb.master.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExcelMappingVO {

    private int columnId;
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

    private List<String> codes;
}
