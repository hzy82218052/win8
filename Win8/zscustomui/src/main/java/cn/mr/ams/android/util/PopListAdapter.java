package cn.mr.ams.android.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.mr.ams.android.R;

/**
 *@author zhangshuo
 *@date 2013-4-25 上午10:44:27
 *@version
 * description:
 */
public class PopListAdapter extends BaseAdapter{

	Context context;
	List<String> listStr;


	public PopListAdapter(Context cx, List<String> list){
		this.context = cx;
		this.listStr = list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.pop_list_item, null);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_pop_list_item);
		tv.setText(listStr.get(position));
		return convertView;
	}

}

