package com.fletamuto.sptb.db;

/**
 * 아이템의 값을 정의.
 * @author yongbban
 * @version 1.0.0.0
 */
final class DBDef {
	/**
	 * DB 정보
	 * @author yongbban
	 *
	 */
	public static final class DBInfo {
		public static final String DB_NAME = "fmfinance.db";
		public static final int DB_VERSION = 1;
	}
	
	/**
	 * 에러값 정의
	 * @author yongbban
	 *
	 */
	public static final class ValidError {
		public static final int SUCCESS = 0;
		public static final int NULL_ERR = 1;
		public static final int MAIN_CATEGORY_INVAlID = 2;
		public static final int SUB_CATEGORY_INVAlID = 3;
	}
}
