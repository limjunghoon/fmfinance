package com.fletamuto.sptb;

import java.util.ArrayList;

import com.fletamuto.sptb.data.BookMarkItemData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookMarkAdapter extends ArrayAdapter<BookMarkItemData> {
	Context context;
	int layoutResouceId;
	ArrayList<BookMarkItemData> bookMarkItemDatas;
	
	public BookMarkAdapter(Context context, int textViewResourceId,
			ArrayList<BookMarkItemData> bookMarkItemDatas) {
		super(context, textViewResourceId, bookMarkItemDatas);
		this.context = context;
		this.layoutResouceId = textViewResourceId;
		this.bookMarkItemDatas = bookMarkItemDatas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookMarkViewHolder viewHolder;
		if(convertView == null) {
			convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutResouceId, null);
			
			viewHolder = new BookMarkViewHolder();
			
			viewHolder.icon = (ImageView)convertView.findViewById(R.id.BookMarkItemIcon);
			viewHolder.title = (TextView)convertView.findViewById(R.id.BookMarkItemTitle);
			viewHolder.category = (TextView)convertView.findViewById(R.id.BookMarkItemCategory);
			viewHolder.deleteImage = (ImageView)convertView.findViewById(R.id.BookMarkItemDelete);
			viewHolder.method = (TextView)convertView.findViewById(R.id.BookMarkItemMethod);
			viewHolder.amount = (TextView)convertView.findViewById(R.id.BookMarkItemAmount);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (BookMarkViewHolder)convertView.getTag();
		}
		
		viewHolder.icon.setImageResource(bookMarkItemDatas.get(position).iconResource);
		viewHolder.title.setText(bookMarkItemDatas.get(position).memo);
		viewHolder.category.setText(bookMarkItemDatas.get(position).category);
		viewHolder.method.setText(bookMarkItemDatas.get(position).method);
		if(InputExpenseLayout.editableList) {
			viewHolder.deleteImage.setVisibility(View.VISIBLE);
		} else {
			viewHolder.deleteImage.setVisibility(View.INVISIBLE);
			//viewHolder.deleteImage.setVisibility(View.GONE);
		}
		viewHolder.amount.setText(bookMarkItemDatas.get(position).amount);

		return convertView;
	}
	
	class BookMarkViewHolder {
		public ImageView icon;
		public TextView title;
		public TextView category;
		public ImageView deleteImage;
		public TextView method;
		public TextView amount;
	}
}
