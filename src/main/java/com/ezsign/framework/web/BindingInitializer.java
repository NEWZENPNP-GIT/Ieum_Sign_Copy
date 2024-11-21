package com.ezsign.framework.web;

/**
 * @Class Name  : com.nexcos.framework.web.BindingInitializer
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자             수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2015. 8. 10.      유지운                 최초생성
 * 
 * @Company : Nexcos. Inc
 * @Author  : 유지운
 * @Date    : 2015. 8. 10. 오후 4:21:02
 * @version : 1.0
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class BindingInitializer implements WebBindingInitializer {

	/** 
	 * @Description : initBinder
	 * @param binder
	 * @param request
	 *
	 * @see org.springframework.web.bind.support.WebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.WebRequest)
	 */
	public void initBinder(WebDataBinder binder, WebRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

}
