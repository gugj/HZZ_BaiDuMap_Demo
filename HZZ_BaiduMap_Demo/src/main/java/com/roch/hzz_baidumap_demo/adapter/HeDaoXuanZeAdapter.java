package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.HeDao;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/12/012 15:36
 */
public class HeDaoXuanZeAdapter extends MyBaseAdapter<HeDao> {

    private int layoutType=1;

    /**
     * @param layoutType 布局view类型：1.内容布局居中显示---选择，2.带进入图标的布局
     */
    public HeDaoXuanZeAdapter(Context mContext, List<HeDao> mData,int layoutType) {
        super(mContext, mData);
        this.layoutType=layoutType;
    }

    @Override
    public View getConvertView(Context mContext) {
        if(layoutType==1){
            return View.inflate(mContext, R.layout.adapter_hedao_xuanze_center, null);
        }else {
            return View.inflate(mContext, R.layout.adapter_hedao_xuanze, null);
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(HeDao data, BaseViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.tvHedaoName.setText(data.getRiverName());
    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.tv_hedao_name)
        TextView tvHedaoName;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }
}
