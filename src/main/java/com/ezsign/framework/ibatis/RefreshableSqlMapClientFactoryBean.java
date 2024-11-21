package com.ezsign.framework.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;

import egovframework.rte.psl.orm.ibatis.SqlMapClientFactoryBean;


public class RefreshableSqlMapClientFactoryBean extends SqlMapClientFactoryBean implements SqlMapClientRefreshable, DisposableBean {
	
	private static final Log logger = LogFactory
			.getLog(RefreshableSqlMapClientFactoryBean.class);
	
	private SqlMapClient proxy;
	private int interval;
	
	private Timer timer;
	private TimerTask task;
	
	private Resource[] configLocations;
	
	private Resource[] mappingLocations;
	
	/**
	 * 파일 감시 쓰레드가 실행중인지 여부.
	 */
	private boolean running = false;
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();
	
	public void setConfigLocation(Resource configLocation) {
		super.setConfigLocation(configLocation);
		this.configLocations = (configLocation != null ? new Resource[] { configLocation }
				: null);
	}
	
	public void setConfigLocations(Resource[] configLocations) {
		super.setConfigLocations(configLocations);
		this.configLocations = configLocations;
	}
	
	public void setMappingLocations(Resource[] mappingLocations) {
		super.setMappingLocations(mappingLocations);
		this.mappingLocations = mappingLocations;
	}
	
	/**
	 * iBATIS 설정을 다시 읽어들인다.<br /> SqlMapClient 인스턴스 자체를 새로 생성하여 교체한다.
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		/*
		 * WRITE LOCK.
		 */
		w.lock();
		try {
			super.afterPropertiesSet();
			
		} finally {
			w.unlock();
		}
	}
	
	/**
	 * 싱글톤 멤버로 SqlMapClient 원본 대신 프록시로 설정하도록 오버라이드.
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		setRefreshable();
	}
	
	private void setRefreshable() {
		proxy = (SqlMapClient) Proxy.newProxyInstance(SqlMapClient.class
				.getClassLoader(), new Class[] { SqlMapClient.class,
				ExtendedSqlMapClient.class }, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
				return method.invoke(getParentObject(), args);
			}
			
		});
		
		task = new TimerTask() {
			
			private Map map = new HashMap();
			
			public void run() {
				
				if (isModified()) {
					try {
						refresh();
					}
					catch (Exception e) {
						logger.error("caught exception", e);
					}
				}
				
			}
			
			private boolean isModified() {
				boolean retVal = false;
				
				for (int i = 0; i < configLocations.length; i++) {
					Resource configLocation = configLocations[i];
					retVal |= findModifiedResource(configLocation);
				}
				
				if (mappingLocations != null) {
					for (int i = 0; i < mappingLocations.length; i++) {
						Resource mappingLocation = mappingLocations[i];
						retVal |= findModifiedResource(mappingLocation);
					}
				}
				
				return retVal;
			}
			
			private boolean findModifiedResource(Resource resource) {
				boolean retVal = false;
				List modifiedResources = new ArrayList();
				
				try {
					long modified = resource.lastModified();
					
					if (map.containsKey(resource)) {
						long lastModified = ((Long) map.get(resource)).longValue();
						
						if (lastModified != modified) {
							map.put(resource, new Long(modified));
							modifiedResources.add(resource.getDescription());
							retVal = true;
						}
						
					}
					else {
						map.put(resource, new Long(modified));
					}
				}
				catch (IOException e) {
					logger.error("caught exception", e);
				}
				
				if (retVal) {
//					if (logger.isInfoEnabled()) {
//						logger.info("modified files : " + modifiedResources);
//					}
					logger.info("modified files : " + modifiedResources);
				}
				return retVal;
			}
			
		};
		
		timer = new Timer(true);
		resetInterval();
		
		
		List mappingLocationList = extractMappingLocations(configLocations);
		
		if (this.mappingLocations != null) {
			mappingLocationList.addAll(Arrays.asList(this.mappingLocations));
		}
		this.mappingLocations = (Resource[]) mappingLocationList.toArray(new Resource[0]);
	}
	
	private List extractMappingLocations(Resource[] configLocations) {
		List mappingLocationList = new ArrayList();
		SqlMapExtractingSqlMapConfigParser configParser = new SqlMapExtractingSqlMapConfigParser();
		for (int i = 0; i < configLocations.length; i++) {
			try {
				InputStream is = configLocations[i].getInputStream();
				mappingLocationList.addAll(configParser.parse(is));
			}
			catch (IOException ex) {
				logger.warn("Failed to parse config resource: "
						+ configLocations[i], ex.getCause());
			}
		}
		return mappingLocationList;
	}

	private Object getParentObject() {
		/*
		 * READ LOCK.
		 */
		r.lock();
		try {
			return super.getObject();
			
		} finally {
			r.unlock();
		}
	}
	
	public SqlMapClient getObject() {
		return this.proxy;
	}
	
	public Class getObjectType() {
		return (this.proxy != null ? this.proxy.getClass() : SqlMapClient.class);
	}
	
	public boolean isSingleton() {
		return true;
	}
	
	public void setCheckInterval(int ms) {
		interval = ms;
		
		if (timer != null) {
			resetInterval();
		}
	}
	
	private void resetInterval() {
		if (running) {
			timer.cancel();
			running = false;
		}
		if (interval > 0) {
			timer.schedule(task, 0, interval);
			running = true;
		}
	}

	public void destroy() throws Exception {
		timer.cancel();
	}
	
}
