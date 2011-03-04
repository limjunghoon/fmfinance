package com.fletamuto.sptb.file;

import java.util.ArrayList;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

public class CardData {
	private static final String STRING_COMPANY = "카드사";
	private static final String STRING_TITLE = "이름";
	private static final String STRING_NUMBER = "번호";
	private static final String STRING_ACCOUNT_COMPANY = "결제은행";
	private static final String STRING_ACCOUNT = "결제계좌";
	private static final String STRING_MEMO = "메모";
	
	public static CreditCardData getCreditCardData;
	public static CheckCardData getCheckCardData;
	public static PrepaidCardData getPrepaidCardData;
	
	@SuppressWarnings("static-access")
	public CardData() {
		this.getCreditCardData = new CreditCardData();
		this.getCheckCardData = new CheckCardData();
		this.getPrepaidCardData = new PrepaidCardData();
	}
	
	class CreditCardData extends BaseCardData {
		
		static final String STRING_NAME = "신용카드";
		static final String STRING_COMPANY = CardData.STRING_COMPANY;
		static final String STRING_TITLE = CardData.STRING_TITLE;
		static final String STRING_NUMBER = CardData.STRING_NUMBER;
		static final String STRING_ACCOUNT_COMPANY = CardData.STRING_ACCOUNT_COMPANY;
		static final String STRING_ACCOUNT = CardData.STRING_ACCOUNT;
		static final String STRING_MEMO = CardData.STRING_MEMO;
		static final String STRING_BILLING_DATE = "결제일";
		static final String STRING_BILLING_PERIOD = "청구기간";
		
		protected CreditCardData() {
			super();
		}
		
		protected ArrayList<CreditCardData> getCreditCardDatas() {
			return getDatas();
		}
		
		private ArrayList<CreditCardData> getDatas() {
			ArrayList<CardItem> cardItems = DBMgr.getCardItems(CardItem.CREDIT_CARD);
			ArrayList<CreditCardData> creditCardDatas = new ArrayList<CreditCardData>();
			
			for(int i = 0, size = cardItems.size(); i < size; i++) {
				AccountItem accountItem = DBMgr.getAccountItem(cardItems.get(i).getAccount().getID());
				CreditCardData creditCardData = new CreditCardData();
				
				creditCardData.setTitle(cardItems.get(i).getName());
				creditCardData.setCompany(cardItems.get(i).getCompenyName().getName());
				creditCardData.setNumber(cardItems.get(i).getNumber());
				creditCardData.setAccountCompany(accountItem.getCompany().getName());
				creditCardData.setAccount(accountItem.getNumber());
				creditCardData.setBillingDate(cardItems.get(i).getSettlementDay() + "일");
				creditCardData.setBllingPeriod("월");
				creditCardData.setMemo(cardItems.get(i).getMemo());
				
				creditCardDatas.add(creditCardData);
			}
			
			return creditCardDatas;
		}
	}
	class CheckCardData extends BaseCardData {
		static final String STRING_NAME = "체크카드";
		static final String STRING_COMPANY = CardData.STRING_COMPANY;
		static final String STRING_TITLE = CardData.STRING_TITLE;
		static final String STRING_NUMBER = CardData.STRING_NUMBER;
		static final String STRING_ACCOUNT_COMPANY = CardData.STRING_ACCOUNT_COMPANY;
		static final String STRING_ACCOUNT = CardData.STRING_ACCOUNT;
		static final String STRING_MEMO = CardData.STRING_MEMO;
		
		protected CheckCardData() {
			super();
		}
		
		public ArrayList<CheckCardData> getCheckCardDatas() {
			return getDatas();
		}
		
		private ArrayList<CheckCardData> getDatas() {
			ArrayList<CardItem> cardItems = DBMgr.getCardItems(CardItem.CHECK_CARD);
			ArrayList<CheckCardData> checkCardDatas = new ArrayList<CheckCardData>();
			
			for(int i = 0, size = cardItems.size(); i < size; i++) {
				AccountItem accountItem = DBMgr.getAccountItem(cardItems.get(i).getAccount().getID());
				CheckCardData checkCardData = new CheckCardData();
				
				checkCardData.setTitle(cardItems.get(i).getName());
				checkCardData.setCompany(cardItems.get(i).getCompenyName().getName());
				checkCardData.setNumber(cardItems.get(i).getNumber());
				checkCardData.setAccountCompany(accountItem.getCompany().getName());
				checkCardData.setAccount(accountItem.getNumber());
				checkCardData.setMemo(cardItems.get(i).getMemo());
				
				checkCardDatas.add(checkCardData);
			}
			
			return checkCardDatas;
		}
	}
	class PrepaidCardData extends BaseCardData {
		static final String STRING_NAME = "선불카드";
		static final String STRING_COMPANY = CardData.STRING_COMPANY;
		static final String STRING_TITLE = CardData.STRING_TITLE;
		static final String STRING_NUMBER = CardData.STRING_NUMBER;
		static final String STRING_MEMO = CardData.STRING_MEMO;
		static final String STRING_BALANCE = "잔액";
		
		protected PrepaidCardData() {
			super();
		}
		
		public ArrayList<PrepaidCardData> getPrepaidCardDatas() {
			return getDatas();
		}
		
		private ArrayList<PrepaidCardData> getDatas() {
			ArrayList<CardItem> cardItems = DBMgr.getCardItems(CardItem.PREPAID_CARD);
			ArrayList<PrepaidCardData> PrepaidCardDatas = new ArrayList<PrepaidCardData>();
			
			for(int i = 0, size = cardItems.size(); i < size; i++) {
				PrepaidCardData PrepaidCardData = new PrepaidCardData();
				
				PrepaidCardData.setTitle(cardItems.get(i).getName());
				PrepaidCardData.setCompany(cardItems.get(i).getCompenyName().getName());
				PrepaidCardData.setNumber(cardItems.get(i).getNumber());
				PrepaidCardData.setBalance(cardItems.get(i).getBalance());
				PrepaidCardData.setMemo(cardItems.get(i).getMemo());
				
				PrepaidCardDatas.add(PrepaidCardData);
			}
			
			return PrepaidCardDatas;
		}
	}
	
	private class BaseCardData {
		private String company;
		private String title;
		private String number;
		private String accountCompany;
		private String account;
		private String billingDate;
		private String bllingPeriod;
		private long balance;
		private String memo;
		
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getAccountCompany() {
			return accountCompany;
		}
		public void setAccountCompany(String accountCompany) {
			this.accountCompany = accountCompany;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getBillingDate() {
			return billingDate;
		}
		public void setBillingDate(String billingDate) {
			this.billingDate = billingDate;
		}
		public String getBllingPeriod() {
			return bllingPeriod;
		}
		public void setBllingPeriod(String bllingPeriod) {
			this.bllingPeriod = bllingPeriod;
		}
		public long getBalance() {
			return balance;
		}
		public void setBalance(long balance) {
			this.balance = balance;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
	}
}
