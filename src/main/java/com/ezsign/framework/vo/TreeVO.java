package com.ezsign.framework.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TreeVO {

	private String label;
	private String itemId;
	private String itemLv;
	private String parentId;
	private String url;
	
	private List<TreeVO> children = new ArrayList<TreeVO>();
	
}
