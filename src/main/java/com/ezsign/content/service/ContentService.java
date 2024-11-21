package com.ezsign.content.service;

import java.util.List;

import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.vo.FileVO;
import com.jarvis.pdf.vo.PageVO;

public interface ContentService {

	public static final int BUFFER_SIZE = 1024 * 9;

	public static final int COMPLETED = 0;
	public static final int NOT_FOUND = 100;
	public static final int ERR_READ_FILE = 200;
	public static final int ERR_READ_CLASS = 201;
	public static final int ERR_READ_CABINET = 202;
	public static final int ERR_STORAGE_FULL = 300;
	public static final int ERR_MAKE_DIR = 301;
	public static final int ERR_WRITE_FILE = 900;
	public static final int ERR_DELETE_FILE = 901;
	public static final int ERR_DELETE_CONTENT = 902;

	public ContentVO getContent(ContentVO vo)  throws Exception;

	public int transContent(ContentVO vo) throws Exception;

	public int deleteContent(List<ContentVO> list) throws Exception;

	public List<PageVO> getPDFToImage(ContentVO vo, List<FileVO> fileList)  throws Exception;

}
