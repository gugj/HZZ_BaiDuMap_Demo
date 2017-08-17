package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.MapEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 信息核查详情信息适配器
 */
public class XinXiHeChaDetailAdapter extends BaseAdapter {

    Context mContext;
    List<MapEntity> list;

    public XinXiHeChaDetailAdapter(Context mContext, List<MapEntity> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_xinxi_hecha_detail, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MapEntity entity = (MapEntity) getItem(position);
        if (entity != null) {
            holder.tvKey.setText(entity.getKey());
            holder.tvValue.setText(entity.getValue());
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_key)
        TextView tvKey;
        @BindView(R.id.tv_value)
        TextView tvValue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
