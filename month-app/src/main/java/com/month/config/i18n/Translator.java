package com.month.config.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class Translator {

	private final ResourceBundleMessageSource messageSource;

	public String toLocale(String message, String... args) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(message, args, locale);
	}

}
