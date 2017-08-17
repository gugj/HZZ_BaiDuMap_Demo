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
public class GunXiaHeDaoAdapter extends MyBaseAdapter<HeDao> {

    public GunXiaHeDaoAdapter(Context mContext, List<HeDao> mData) {
        super(mContext, mData);
    }

    @Override
    public View getConvertView(Context mContext) {
        return View.inflate(mContext, R.layout.adapter_guanxia_hedao, null);
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(HeDao data, BaseViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.tvLocationName.setText(data.getRiverName());
        holder.tvHeduanMingcheng.setText(data.getRiverAlias());
        holder.tvWentiType.setText(data.getSuperiorRiver());
        holder.tvWentiDetail.setText(data.getRiverLength()+"km");
        holder.tvStartTime.setText(data.getRiverStart());
        holder.tvEndTime.setText(data.getRiverEnd());
    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.tv_location_name)
        TextView tvLocationName;
        @BindView(R.id.tv_heduan_mingcheng)
        TextView tvHeduanMingcheng;
        @BindView(R.id.tv_wenti_type)
        TextView tvWentiType;
        @BindView(R.id.tv_wenti_detail)
        TextView tvWentiDetail;
        @BindView(R.id.tv_start_time)
        TextView tvStartTime;
        @BindView(R.id.tv_end_time)
        TextView tvEndTime;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }
}
