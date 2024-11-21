package com.ezsign.framework.context;


import java.io.File;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;

public class ServletStartListener implements ServletContextListener {

	public ServletStartListener() throws Exception {
		System.out.println("#### Servlet IEUMSIGN Start ####");
    }
	
	public void contextInitialized(ServletContextEvent event){
		System.out.println("#### Init IEUMSIGN ServletContext ####");
		
		try {

			jobSchedule job = new jobSchedule();
			
			Timer timer = new Timer();
			Calendar date = Calendar.getInstance();
			date.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			date.set(Calendar.AM_PM, Calendar.AM);
			date.set(Calendar.HOUR, 03);
			date.set(Calendar.MINUTE, 29);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			
			timer.scheduleAtFixedRate(job, date.getTime(), 1000 * 60 * 60 * 24 * 7);
			/*
			// JOB 스케쥴 구동
			Timer timer = new Timer();
			
			timer.schedule(j, 60000, 60000);
			*/
		} catch (Exception e) {
			e.printStackTrace();
			 System.out.println("#### Init FEB ServletContext Create Not Failed ####");
		}
		 
	}
	 
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("#### Stop FEB ServletContext ####");
	}

	
}

class jobSchedule extends TimerTask {
	@Override
	public void run()
	{
		tempFileDelete();
	}
	
	private static void tempFileDelete() {
		// Temp폴더 삭제
		System.out.println("was  path => "+MultipartFileUtil.getSystemPath());
		
		String filePath = MultipartFileUtil.getSystemTempPath();
		System.out.println(filePath);
		
		FileUtil.deleteAllFiles(filePath, true);
	}
	
}
