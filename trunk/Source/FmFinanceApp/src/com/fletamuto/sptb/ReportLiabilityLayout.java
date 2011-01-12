package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;

public class ReportLiabilityLayout extends ReportSeparationLayout {
	private long mTotalAmount = 0L;
	public static final int ACT_ADD_LIABLITY = MsgDef.ActRequest.ACT_ADD_LIABLITY;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle("��ä ���");
    	
    	super.setTitleBtn();
    }
    
    @Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	
    	Intent intent = new Intent(this, StateLiabilityDefaultLayout.class);
    	intent.putExtra(MsgDef.ExtraNames.ITEM, item);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_VIEW);
	}
    
    protected void setListViewText(FinanceItem item, View convertView) {
    	int extendType = item.getExtendType();
    	
    	if (extendType == ItemDef.ExtendLiablility.LOAN) {
    		setListViewTextLoan((LiabilityLoanItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendLiablility.CASH_SERVICE) {
    		setListViewTextCashService((LiabilityCashServiceItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendLiablility.PERSON_LOAN) {
    		setListViewTextPersonLoan((LiabilityPersonLoanItem)item, convertView);
		}
    	else {
    		setListViewTextDefault(item, convertView);
    	}
	}
    
    private void setListViewTextLoan(LiabilityLoanItem loan, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(loan.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(loan.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d��", loan.getAmount()));
		
//    	if (loan.getCompany().getID() != -1) {
//    		((TextView)convertView.findViewById(R.id.TVLiabilityLoanCompany)).setText("ȸ�� : " + loan.getCompany().getName());
//    	}
//    	((TextView)convertView.findViewById(R.id.TVLiabilityLoanTitle)).setText("���� : " + loan.getTitle());
//		((TextView)convertView.findViewById(R.id.TVLiabilityLoanDate)).setText("���� ������¥ : " + loan.getCreateDateString());			
//		((TextView)convertView.findViewById(R.id.TVLiabilityLoanAmount)).setText(String.format("����ݾ� : %,d��", loan.getAmount()));
//		((TextView)convertView.findViewById(R.id.TVLiabilityLoanRemain)).setText(String.format("�����ݾ� : %,d��", 0));
	}
    
    private void setListViewTextPersonLoan(LiabilityPersonLoanItem personLoan, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(personLoan.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(personLoan.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d��", personLoan.getAmount()));
//    	if (personLoan.getLoanPeopleName().length() != 0) {
//    		((TextView)convertView.findViewById(R.id.TVLiabilityPersonalLoanPerson)).setText("������� : " + personLoan.getLoanPeopleName());
//    	}
//    	((TextView)convertView.findViewById(R.id.TVLiabilityPersonalLoanTitle)).setText("���� : " + personLoan.getTitle());
//		((TextView)convertView.findViewById(R.id.TVLiabilityPersonalLoanDate)).setText("������¥ : " + personLoan.getCreateDateString());			
//		((TextView)convertView.findViewById(R.id.TVLiabilityPersonalLoanAmount)).setText(String.format("�ݾ� : %,d��", personLoan.getAmount()));
	}
    
    private void setListViewTextCashService(LiabilityCashServiceItem cashService, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(cashService.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(cashService.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d��", cashService.getAmount()));
//    	if (cashService.getCard().getID() != -1) {
//    		((TextView)convertView.findViewById(R.id.TVLiabilityCashServiceCardName)).setText("ī�� : " + cashService.getCard().getCompenyName().getName());
//    	}
//    	((TextView)convertView.findViewById(R.id.TVLiabilityCashServiceTitle)).setText("���� : " + cashService.getTitle());
//		((TextView)convertView.findViewById(R.id.TVLiabilityCashServiceDate)).setText("��¥ : " + cashService.getCreateDateString());			
//		((TextView)convertView.findViewById(R.id.TVLiabilityCashServiceAmount)).setText(String.format("����ݾ� : %,d��", cashService.getAmount()));
	}

	private void setListViewTextDefault(FinanceItem item, View convertView) {
		((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(item.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(item.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d��", item.getAmount()));
//    	((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText("���� : " + item.getTitle());
//		((TextView)convertView.findViewById(R.id.TVLiabilityReportListDate)).setText("��¥ : " + item.getCreateDateString());			
//		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
////		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText("�з� : " + item.getCategory().getName());
//		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setVisibility(View.GONE);
	}

	protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
		btnDelete.setVisibility(View.GONE);
    }

	@Override
	protected int getItemType() {
		return LiabilityItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		Intent intent = new Intent(this, SelectCategoryLiabilityLayout.class);
		startActivityForResult(intent, ACT_ADD_LIABLITY);
	}
	
	protected void updateListItem() {
		int itemSize = mItems.size();
		int itemCategoryID = -1;
		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mItems.get(index);
//			if (item.getCategory().getID() != itemCategoryID) {
//				itemCategoryID = item.getCategory().getID();
//				LiabilityItem separator = new LiabilityItem();
//				separator.setSeparatorTitle(item.getCategory().getName());
//				mListItems.add(separator);
//			}
			
			mTotalAmount += item.getAmount();
			mListItems.add(item);
		}
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_assets_section;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_LIABLITY) {
			if (resultCode == RESULT_OK) {
				getData();
		        setAdapterList();
		        updateChildView();
    		}
    	}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected int getLayoutResources(FinanceItem item) {
		if (item.getExtendType() == ItemDef.ExtendLiablility.LOAN) {
			return R.layout.report_list_liability_loan;
		}
		else if (item.getExtendType() == ItemDef.ExtendLiablility.CASH_SERVICE) {
			return R.layout.report_list_liability_cash_service;
		}
		else if (item.getExtendType() == ItemDef.ExtendLiablility.PERSON_LOAN) {
			return R.layout.report_list_liability_personal_loan;
		}
		else {
			return getAdapterResource();
		}
	}
}