package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;


/**
 * 자산 중 펀드
 * @author yongbban
 *
 */
public class AssetsFundItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.FUND;
	
	private static final long serialVersionUID = 9044769708788641644L;
	
	public static final int KIND_INTERNAL_SAVING = 0;
	public static final int KIND_INTERNAL_DEFERRED = 1;
	public static final int KIND_EXTERNAL_SAVING = 2;
	public static final int KIND_EXTERNAL_DEFERRED = 3;

	/**
	 * 펀드 DB테이블 아이디
	 */
	private int mFundID = -1;
	
	/**
	 * 평균 구입가격
	 */
	private long mMeanPrice = 0;
	
	/**
	 * 펀드 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 판매처
	 */
	private String mStore;
	
	/**
	 * 펀드 유형
	 */
	private int mKind= KIND_INTERNAL_SAVING;
	
	public AssetsFundItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	//	setRepeatMonthly(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	//	getRepeat().setType(Repeat.MONTHLY);
	}
	
	/**
	 * 펀드 아이디 설정
	 * @param mDepositID 펀드 아이디
	 */
	public void setFundID(int fundID) {
		this.mFundID = fundID;
	}

	/**
	 * 펀드 아이디를 얻는다.
	 * @return 펀드 아이디
	 */
	public int getFundID() {
		return mFundID;
	}

	/**
	 * 편균가를 설정한다.
	 * @param meanPrice
	 */
	public void setMeanPrice(long meanPrice) {
		this.mMeanPrice = meanPrice;
	}

	/**
	 * 편균가를 얻는다.
	 * @return
	 */
	public long getMeanPrice() {
		return mMeanPrice;
	}

	/**
	 * 판매처를 설정한다.
	 * @param storeCompany
	 */
	public void setStore(String store) {
		this.mStore = store;
	}

	/**
	 * 판매처를 얻는다.
	 * @return
	 */
	public String getStore() {
		return mStore;
	}

	public int getExtendType() {
		return EXEND_TYPE;
	}

	public void setKind(int kind) {
		this.mKind = kind;
	}

	public int getKind() {
		return mKind;
	}
	
	/**
	 * 펀드 만기날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 * 펀드 만기날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 * 펀드 만기날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpriyDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
}
