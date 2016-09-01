package cn.mr.ams.android.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mr.ams.android.R;

/**
 *@author zhangshuo
 *@date 2013-4-25 下午5:04:49
 *@version
 * description:
 */
public class PopListWithTitleAdapter extends BaseAdapter{

	Context context;
	List<String> listStr;


	public PopListWithTitleAdapter(Context cx, List<String> list){
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
		convertView = LayoutInflater.from(context).inflate(R.layout.pop_list_item_with_title, null);
		ImageView iv = (ImageView) convertView.findViewById(R.id.iv_pop_item_with_title);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_pop_list_item_with_title);

		if(!listStr.get(position).equals("经纬度采集")){
			iv.setVisibility(View.INVISIBLE);
		}

		tv.setText(listStr.get(position));
		return convertView;
	}

}

