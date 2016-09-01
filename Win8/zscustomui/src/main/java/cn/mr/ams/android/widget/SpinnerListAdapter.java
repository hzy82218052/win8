package cn.mr.ams.android.widget;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.mr.ams.android.R;

public class SpinnerListAdapter extends BaseAdapter{

	Context context;
	List<String> listStr;
	int itemHeight;
	Drawable itemDrawable;
	int itemTextColor;

	public SpinnerListAdapter(Context cx, List<String> list, int height, Drawable drawable, int color){
		this.context = cx;
		this.listStr = list;
		this.itemHeight = height;
		this.itemDrawable = drawable;
		this.itemTextColor = color;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listStr.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listStr.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (null == convertView) {
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_spinner_item, null);
			convertView.setMinimumHeight(itemHeight);
//			convertView.setBackground(itemDrawable);
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_spinner_item);
			holder.tvItem.setTextColor(itemTextColor);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		holder.tvItem.setText(listStr.get(position));

		return convertView;
	}

	class HolderView {
		TextView tvItem;
	}

}
