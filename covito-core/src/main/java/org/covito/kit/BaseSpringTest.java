package org.covito.kit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * spring junit 测试基类
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月18日]
 */
public abstract class BaseSpringTest {
	
	public static ApplicationContext cxt;

	public ApplicationContext getContext() {
		if (cxt == null) {
			String [] path=getXmlPath();
			if(getXmlPath()==null||getXmlPath().length==0){
				path=new String[] { "classpath*:applicationContext.xml" };
			}
			cxt = new ClassPathXmlApplicationContext(path);
			return cxt;
		} else {
			return cxt;
		}
	}
	
	protected abstract String[] getXmlPath();

	public Object getBean(String beanName) {
		return getContext().getBean(beanName);
	}

	@Before
	public void init() {
		System.out.println("------------Start to init database context--------------------");
		System.out.println("------------End to init database context--------------------");
	}
	
	@After
	public void destroy() {
		System.out.println("------------Start to destroy database context--------------------");
		System.out.println("------------End to destroy database context--------------------");
	}
	
	@BeforeClass
	public static void global_start(){
		System.out.println("------------Start to global_start--------------------");
		System.out.println("------------end to global_start--------------------");
	}
	
	@AfterClass
	public static void global_end(){
		System.out.println("------------Start to global_end--------------------");
		System.out.println("------------end to global_end--------------------");
	}
	
}
