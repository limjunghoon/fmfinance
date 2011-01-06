package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;


/**
 * �ڻ� �� �ݵ�
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
	 * �ݵ� DB���̺� ���̵�
	 */
	private int mFundID = -1;
	
	/**
	 * ��� ���԰���
	 */
	private long mMeanPrice = 0;
	
	/**
	 * �ݵ� ���� ��¥
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * �Ǹ�ó
	 */
	private String mStore;
	
	/**
	 * �ݵ� ����
	 */
	private int mKind= KIND_INTERNAL_SAVING;
	
	public AssetsFundItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	//	setRepeatMonthly(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	//	getRepeat().setType(Repeat.MONTHLY);
	}
	
	/**
	 * �ݵ� ���̵� ����
	 * @param mDepositID �ݵ� ���̵�
	 */
	public void setFundID(int fundID) {
		this.mFundID = fundID;
	}

	/**
	 * �ݵ� ���̵� ��´�.
	 * @return �ݵ� ���̵�
	 */
	public int getFundID() {
		return mFundID;
	}

	/**
	 * ��հ��� �����Ѵ�.
	 * @param meanPrice
	 */
	public void setMeanPrice(long meanPrice) {
		this.mMeanPrice = meanPrice;
	}

	/**
	 * ��հ��� ��´�.
	 * @return
	 */
	public long getMeanPrice() {
		return mMeanPrice;
	}

	/**
	 * �Ǹ�ó�� �����Ѵ�.
	 * @param storeCompany
	 */
	public void setStore(String store) {
		this.mStore = store;
	}

	/**
	 * �Ǹ�ó�� ��´�.
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
	 * �ݵ� ���⳯¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 * �ݵ� ���⳯¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 * �ݵ� ���⳯¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpriyDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
}
