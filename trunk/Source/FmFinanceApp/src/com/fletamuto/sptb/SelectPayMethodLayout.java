package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fletamuto.sptb.data.PaymentMethod;

public class SelectPayMethodLayout extends SelectGridBaseLayout {
	protected ArrayList<PaymentMethod> mArrCategory = null;
	PaymentMethodButtonAdpter mAdapterCategory;
	
	private class PaymentMethodButtonAdpter extends ArrayAdapter<PaymentMethod> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public PaymentMethodButtonAdpter(Context context, int resource,
				 List<PaymentMethod> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			PaymentMethod paymentMethod = (PaymentMethod)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
//			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
//			button.setText(category.getName());
			//button.setOnClickListener(categoryListener);
			//button.setTag(category);
			
			return convertView;
		}
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAdaper() {
		// TODO Auto-generated method stub
		
	}
}
