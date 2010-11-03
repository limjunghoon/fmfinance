package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCardLayout extends Activity {
	public static final int ACT_ADD_CARD = MsgDef.ActRequest.ACT_ADD_CARD;
	public static final int ACT_CARD_INPUT_SELECT = MsgDef.ActRequest.ACT_CARD_INPUT_SELECT;
	
	private ArrayList<CardItem> mArrCard;
	protected CategoryItemAdapter mAdapterCard;
	private int mSelectedInstallmentPlan = -1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
 //       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_card);
        setAddButtonListener();
        getCardItems();
        setAdapterList();
    }
	
	protected void getCardItems() {
		mArrCard = DBMgr.getCardItems();
    }
	
	protected void setAdapterList() {
    	if (mArrCard == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVCard);
    	mAdapterCard = new CategoryItemAdapter(this, R.layout.text_list, mArrCard);
    	listCard.setAdapter(mAdapterCard);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				CardItem card = (CardItem)view.getTag();
				
				if (card.getType() == CardItem.CREDIT_CARD) {
					SelectedInstallmentPlan(card);
				}
				else {
					Intent intent = new Intent();
					intent.putExtra(MsgDef.ExtraNames.CARD_ID, card.getID());
					setResult(RESULT_OK, intent);
					finish();
				}
				
				
			}
		});
    }
	

	/**
	 *  할부 방법을 선택한다.
	 */
	private void SelectedInstallmentPlan(final CardItem card) {
		
		
		new AlertDialog.Builder(SelectCardLayout.this)
	    .setTitle("할부선택")
	    .setSingleChoiceItems(R.array.select_installment_plan, 0, 
	      new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	mSelectedInstallmentPlan = whichButton;
	        	card.getID();
	        }
	      })
	      .setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        	if (mSelectedInstallmentPlan == -1)
	        		mSelectedInstallmentPlan = 0;
        	
	        	Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CARD_ID, card.getID());
				intent.putExtra(MsgDef.ExtraNames.INSTALLMENT_PLAN, mSelectedInstallmentPlan);
				
				setResult(RESULT_OK, intent);
				finish();
	        }
	      })
	     .create().show();
	}

	private void setAddButtonListener() {
		Button btnAdd = (Button)findViewById(R.id.BtnCardAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(SelectCardLayout.this, SelectInputCardLayout.class);		
				startActivityForResult(intent, ACT_ADD_CARD);
			}
		});
	}  
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_CARD) {
			if (resultCode == RESULT_OK) {
				int CardID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
				if (CardID == -1) return;
				
				CardItem Card = DBMgr.getCardItem(CardID);
				if (Card == null) return;
				mAdapterCard.add(Card);
				mAdapterCard.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public class CategoryItemAdapter extends ArrayAdapter<CardItem> {
    	int mResource;
    	LayoutInflater mInflater;

		public CategoryItemAdapter(Context context, int resource,
				 List<CardItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardItem item = (CardItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			TextView tvCard = (TextView)convertView.findViewById(R.id.TVListItem);
			tvCard.setText(String.format("%s : %s", item.getCompenyName().getName(), item.getNumber()));
			
			convertView.setTag(item);
			
			return convertView;
		}
    }
}