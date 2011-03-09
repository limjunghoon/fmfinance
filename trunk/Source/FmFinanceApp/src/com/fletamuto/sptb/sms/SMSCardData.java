package com.fletamuto.sptb.sms;

public class SMSCardData {
	private int id, typeId, cardId, amountStartRow, installmentStartRow, shopStartRow;
	private String number = "", amountEndText = "", installmentEndText = "", shopEndText = "", parseSource = "";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getAmountStartRow() {
		return amountStartRow;
	}
	public void setAmountStartRow(int amountStartRow) {
		this.amountStartRow = amountStartRow;
	}
	public int getInstallmentStartRow() {
		return installmentStartRow;
	}
	public void setInstallmentStartRow(int installmentStartRow) {
		this.installmentStartRow = installmentStartRow;
	}
	public int getShopStartRow() {
		return shopStartRow;
	}
	public void setShopStartRow(int shopStartRow) {
		this.shopStartRow = shopStartRow;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAmountEndText() {
		return amountEndText;
	}
	public void setAmountEndText(String amountEndText) {
		this.amountEndText = amountEndText;
	}
	public String getInstallmentEndText() {
		return installmentEndText;
	}
	public void setInstallmentEndText(String installmentEndText) {
		this.installmentEndText = installmentEndText;
	}
	public String getShopEndText() {
		return shopEndText;
	}
	public void setShopEndText(String shopEndText) {
		this.shopEndText = shopEndText;
	}
	public String getParseSource() {
		return parseSource;
	}
	public void setParseSource(String parseSource) {
		this.parseSource = parseSource;
	}
}