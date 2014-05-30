package com.covito.kit.utility.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <一句话功能简述><br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-18]
 */
public class BeanDescription {
	
	Class<?> beanClass;
	Map<String, BeanProperty> propertyMap;
	boolean initFlag;
	
	private final Log log=LogFactory.getLog(getClass());

	public Class<?> getBeanClass() {
		return this.beanClass;
	}

	BeanDescription(Class<?> clazz) {
		this.beanClass = clazz;
	}

	public Map<String, BeanProperty> getPropertyMap() {
		checkInitialized();
		return this.propertyMap;
	}

	void checkInitialized() {
		if (!this.initFlag)
			synchronized (this) {
				if (!this.initFlag) {
					initialize();
					this.initFlag = true;
				}
			}
	}

	void initialize() {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.beanClass);
			this.propertyMap = new HashMap<String, BeanProperty>();
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			Method writeMethod;
			for (int i = 0; (pds != null) && (i < pds.length); i++) {
				PropertyDescriptor pd = pds[i];
				Method readMethod = BeanManager.getPublicMethod(pd
						.getReadMethod());
				writeMethod = BeanManager.getPublicMethod(pd.getWriteMethod());
				if ((readMethod == null) && (writeMethod == null)) {
					continue;
				}
				BeanProperty property = new BeanProperty(readMethod,
						writeMethod);
				this.propertyMap.put(pd.getName(), property);
			}
			for (Field f : this.beanClass.getFields()) {
				if (!Modifier.isPublic(f.getModifiers())) {
					continue;
				}
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				if (Modifier.isFinal(f.getModifiers())) {
					continue;
				}
				BeanProperty property = new BeanProperty(f);
				if (!this.propertyMap.containsKey(property.getName()))
					this.propertyMap.put(f.getName(), property);
			}
		} catch (IntrospectionException exc) {
			log.warn("" + ",Bean=" + this.beanClass.getName() + ":"
					+ exc.getMessage());
		}
	}

	public BeanProperty getProperty(String name) {
		checkInitialized();
		BeanProperty bip = (BeanProperty) this.propertyMap.get(name);
		if (bip == null) {
			bip = (BeanProperty) this.propertyMap.get(name.substring(0, 1)
					.toLowerCase()
					+ name.substring(1));
		}
		return bip;
	}
}
