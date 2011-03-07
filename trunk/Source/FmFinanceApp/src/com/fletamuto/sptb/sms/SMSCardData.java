package com.fletamuto.sptb.sms;

public class SMSCardData {
	private int id, type_id, company_id, amountStartRow, installmentStartRow, shopStartRow;
	private String amountEndText = "", installmentEndText = "", shopEndText = "", parseSource = "";
	public int getAmountStartRow() {
		return amountStartRow;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
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