package com.fletamuto.sptb.data;


public class SMSParseItem extends BaseItem{
	private static final long serialVersionUID = -431858797456300474L;
	
	private String mNumber;
	private int mTypeId = -1;
	private int mCardId = -1;
	private int mAmountRow ;
	private int mAmountStartPosition;
	private String mAmountEndText;
	private int mInstallmentRow;
	private int mInstallmentStartPosition;
	private String mInstallmentEndText;
	private int mShopRow;
	private int mShopStartPosition;
	private String mShopEndText;
	private String mParseSource;
	
	public void setTypeId(int typeId) {
		this.mTypeId = typeId;
	}
	
	public int getTypeId() {
		return mTypeId;
	}
	
	public void setCardId(int cardId) {
		this.mCardId = cardId;
	}
	
	public int getCardId() {
		return mCardId;
	}
	
	public void setAmountRow(int amountRow) {
		this.mAmountRow = amountRow;
	}
	
	public int getAmountRow() {
		return mAmountRow;
	}
	
	public void setAmountStartPosition(int amountStartPosition) {
		this.mAmountStartPosition = amountStartPosition;
	}
	
	public int getAmountStartPosition() {
		return mAmountStartPosition;
	}
	
	public void setAmountEndText(String amountEndText) {
		this.mAmountEndText = amountEndText;
	}
	
	public String getAmountEndText() {
		return mAmountEndText;
	}
	
	public void setInstallmentRow(int installmentRow) {
		this.mInstallmentRow = installmentRow;
	}
	
	public int getInstallmentRow() {
		return mInstallmentRow;
	}
	
	public void setInstallmentStartPosition(int installmentStartPosition) {
		this.mInstallmentStartPosition = installmentStartPosition;
	}
	
	public int getInstallmentStartPosition() {
		return mInstallmentStartPosition;
	}
	
	public void setInstallmentEndText(String installmentEndText) {
		this.mInstallmentEndText = installmentEndText;
	}
	
	public String getInstallmentEndText() {
		return mInstallmentEndText;
	}
	
	public void setShopRow(int shopRow) {
		this.mShopRow = shopRow;
	}
	
	public int getShopRow() {
		return mShopRow;
	}
	
	public void setShopStartPosition(int shopStartPosition) {
		this.mShopStartPosition = shopStartPosition;
	}
	
	public int getShopStartPosition() {
		return mShopStartPosition;
	}
	
	public void setShopEndText(String shopEndText) {
		this.mShopEndText = shopEndText;
	}
	
	public String getShopEndText() {
		return mShopEndText;
	}
	
	public void setParseSource(String parseSource) {
		this.mParseSource = parseSource;
	}
	
	public String getParseSource() {
		return mParseSource;
	}

	public void setNumber(String number) {
		this.mNumber = number;
	}

	public String getNumber() {
		return mNumber;
	}
}
