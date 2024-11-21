package com.ezsign.popup.vo;

import java.util.List;

import lombok.Data;

@Data
public class ExcelUploadRequest<T> {
	
    public String id;
    public List<T> data;
}
