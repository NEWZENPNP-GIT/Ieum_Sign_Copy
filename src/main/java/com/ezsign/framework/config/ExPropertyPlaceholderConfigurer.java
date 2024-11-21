package com.ezsign.framework.config;

/**
 * @Class Name  : com.nexcos.framework.config.ExPropertyPlaceholderConfigurer
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

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class ExPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements BeanNameAware, BeanFactoryAware {

	/** 
	 * @Description : Properties 위치를 설정한다.
	 * @param locations Properties 위치
	 *
	 * @see org.springframework.core.io.support.PropertiesLoaderSupport#setLocations(org.springframework.core.io.Resource[])
	 */
	public void setLocations(Resource[] locations) {
		ClassPathResource[] classRes = new ClassPathResource[locations.length];
				
		try{
			for(int i=0; i<classRes.length; i++) {	 			
				classRes[i] = new ClassPathResource("/properties/" + locations[i].getFilename());				
				Properties props = PropertiesLoaderUtils.loadProperties(classRes[i]);								
			}		
		}catch(IOException iex){
			iex.printStackTrace();
		}
		
		super.setLocations(classRes);
	}
	
	
}
