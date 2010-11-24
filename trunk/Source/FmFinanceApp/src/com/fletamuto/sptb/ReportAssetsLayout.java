package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;

public class ReportAssetsLayout extends ReportBaseLayout {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getItemsFromDB(AssetsItem.TYPE) == false) {
        	return;
        }
        
 //       setListAdapter(R.layout.report_list_assets);
    }
    
	@Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputAssetsLayout.class, item.getID());
	}
//    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	startEditInputActivity(InputAssetsLayout.class, item.getID());
//    	super.onListItemClick(l, v, position, id);
//    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	AssetsItem item = (AssetsItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText(item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("±Ý¾× : %,d¿ø", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText(item.getTitle());
		String categoryText = String.format(item.getCategory().getName());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAssetsDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_assets;
	}



}