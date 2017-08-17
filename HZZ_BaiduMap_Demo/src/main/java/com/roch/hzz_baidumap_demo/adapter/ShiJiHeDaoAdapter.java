package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.ShiJiHeDao;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 河道资料列表List适配器
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 14:04
 */
public class ShiJiHeDaoAdapter extends MyBaseAdapter<ShiJiHeDao> {

    private boolean isXiaoWeiShuiTt=false;

    public ShiJiHeDaoAdapter(Context mContext, List<ShiJiHeDao> mData, boolean isXiaoWeiShuiTt) {
        super(mContext, mData);
        this.isXiaoWeiShuiTt=isXiaoWeiShuiTt;
    }

    @Override
    public View getConvertView(Context mContext) {
        if(isXiaoWeiShuiTt){
            return View.inflate(mContext, R.layout.adapter_xwst_hedao, null);
        }else {
            return View.inflate(mContext, R.layout.adapter_shiji_hedao, null);
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(ShiJiHeDao data, BaseViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if(isXiaoWeiShuiTt){
            holder.tvWentiType.setText(data.getRiverName()); // 名称
            holder.tvWentiDetail.setText(data.getAdnm()); // 行政区
            holder.tvStartTime.setText(data.getRiverLength()+"km"); // 长度
            holder.tvEndTime.setText(data.getLocation()); // 位置
            holder.tvHeduanMingcheng.setText(data.getMk()); // 污染状况
        }else {
            holder.tvLocationName.setText(data.getRiverName());    // 河道名称
            holder.tvHeduanMingcheng.setText(data.getRiverAlias()); // 河段名称
            holder.tvWentiType.setText(data.getAdnm()); // 行政区划
            holder.tvWentiDetail.setText(data.getRiverLength()+"km"); // 河道长度
            holder.tvStartTime.setText(data.getRiverStart()); // 起点
            holder.tvEndTime.setText(data.getRiverEnd()); // 终点
        }
    }

    public void onRefsh(List<ShiJiHeDao> shiJiHeDaos) {
        mData.clear();
        mData.addAll(shiJiHeDaos);
        notifyDataSetChanged();
    }

    public void addList(List<ShiJiHeDao> shiJiHeDaos) {
        mData.addAll(shiJiHeDaos);
        notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {

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
