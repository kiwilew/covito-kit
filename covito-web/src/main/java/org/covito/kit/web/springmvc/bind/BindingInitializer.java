package org.covito.kit.web.springmvc.bind;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;



/**
 * 一句话功能简述
 * <p>功能详细描述</p>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-5-14]
 */
public class BindingInitializer implements WebBindingInitializer{

	
	
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
		
	}


}
