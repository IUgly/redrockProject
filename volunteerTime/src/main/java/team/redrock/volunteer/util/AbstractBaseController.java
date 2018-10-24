package team.redrock.volunteer.util;

import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import java.util.Locale;

public abstract class AbstractBaseController {
	/**
	 *  自动注入此资源对象
	 */
	@Resource
	private MessageSource messageSource;
	public String getMessage(String key, String... args) {
		return this.messageSource.getMessage(key, args, Locale.getDefault()); 
	}
}
