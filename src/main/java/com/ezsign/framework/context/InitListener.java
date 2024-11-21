package com.ezsign.framework.context;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.ezsign.framework.util.MultipartFileUtil;

public class InitListener implements ServletContextListener {

	/** 
	 * @Description : System property 정보를 설정
	 * @param event
	 *
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		try{
			ClassPathResource classRes = new ClassPathResource("/egovframework/egovProps/globals.properties"); 
			Properties props = PropertiesLoaderUtils.loadProperties(classRes);
			
			 for (String propName : props.stringPropertyNames()){
				 if (System.getProperty(propName) == null){
					 if(props.getProperty(propName) != null){
						 System.setProperty(propName, props.getProperty(propName));
					 }
		         }

			 }
			 
		}catch(IOException iex){
			iex.printStackTrace();
		}
																																																																																																														
	      
		/*String configFile = null;
		
		configFile = MultipartFileUtil.getSystemPath() + "/egovframework/log4j2.xml";
		JoranConfigurator configurator = new JoranConfigurator();*/
//		DOMConfigurator configurator = new DOMConfigurator();
//		configurator.doConfigure(configFile, LogManager.getLoggerRepository());
	}

	/** 
	 * @Description :
	 * @param event
	 *
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		// Do nothing
	}
	
}
