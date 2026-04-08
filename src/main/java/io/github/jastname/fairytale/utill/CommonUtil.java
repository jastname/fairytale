package io.github.jastname.fairytale.utill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.jastname.fairytale.collect.service.impl.fairytaleCollectServiceImpl;

public class CommonUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);
	
	public static String idMake(String str) {
		String id;
		Random random = new Random();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", new Locale("ko", "KO"));
			String formattedValue = formatter.format(new Date());
			id = str + formattedValue + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
			return id;
		} catch (RuntimeException e) {
			log.debug("idMake 에러발생");
			return str;
		}
	}
	
}
