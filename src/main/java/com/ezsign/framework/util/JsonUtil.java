package com.ezsign.framework.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class JsonUtil {
	
	/**
	 * Description  : json 스트링값을 map에 매핑한다.
	 * @Method Name : setJsonString
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map setJsonString(String jsonStr) {
		Map classMap = new HashMap();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map resultMap = (Map)JSONObject.toBean(jsonObject,java.util.HashMap.class,classMap);		
		return resultMap;
	}
}
