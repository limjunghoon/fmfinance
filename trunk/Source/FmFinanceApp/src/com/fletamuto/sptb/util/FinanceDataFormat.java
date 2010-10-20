package com.fletamuto.sptb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinanceDataFormat {
	public static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getFormat(Date date) {
		return DATA_FORMAT.format(date);
	}
}
