package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.activity.BaseActivity;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;
import com.roch.hzz_baidumap_demo.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 巡河记录ListView的适配器
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 14:04
 */
public class XunHeJiLuAdapter extends MyBaseAdapter<XunHeJiLu> {

    private Drawable wuTuDrawable;

    public XunHeJiLuAdapter(Context mContext, List<XunHeJiLu> mData) {
        super(mContext, mData);
        wuTuDrawable = mContext.getResources().getDrawable(R.drawable.wutu);
    }

    @Override
    public View getConvertView(Context mContext) {
        return View.inflate(mContext, R.layout.adapter_xunhe_jilu, null);
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(XunHeJiLu data, BaseViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;

        // 图片处理
        ViewGroup.LayoutParams lParams = holder.iv_xunhe_jilu.getLayoutParams();
        lParams.width = Common.Width / 5;
        lParams.height = Common.Width / 5;
        holder.iv_xunhe_jilu.setLayoutParams(lParams);
        if(StringUtil.isNotEmpty(data.getImglist()) && data.getImglist().size()>0 && StringUtil.isNotEmpty(data.getImglist().get(0).getFilePath())){
            LogUtil.println("拼接之后图片的路径为：==" + URLs.IMAGE_URL + data.getImglist().get(0).getFilePath());
            Glide.with(mContext)
                    .load(URLs.IMAGE_URL+data.getImglist().get(0).getFilePath())
                    .into(holder.iv_xunhe_jilu);
        }else {
            holder.iv_xunhe_jilu.setImageDrawable(wuTuDrawable);
        }

        holder.tvLocationName.setText(data.getAdnm());  // 行政区划
        holder.tvHeduanMingcheng.setText(data.getRiverName()); // 河道名称
        holder.tvWentiType.setText(data.getQtype()); // 问题类型
        holder.tvWentiDetail.setText(data.getDetail()); // 问题描述
//        holder.tvChuliZhangtai.setText(data.getStatus());
//        holder.tvChuliJieguo.setText(data.getNt());
//        holder.tvStartTime.setText(data.getCkTime_begin());
//        holder.tvEndTime.setText(data.getCkTime_end());
    }

    /**
     * 下拉刷新时或者筛选条件过之后---添加数据
     * @param xunHeJiLus
     */
    public void onRefsh(List<XunHeJiLu> xunHeJiLus) {
        mData.clear();
        if(xunHeJiLus.size()<=0){
            ((BaseActivity)mContext).ShowToast("暂无数据");
        }else {
            mData.addAll(xunHeJiLus);
        }
        notifyDataSetChanged();
    }

    /**
     * 上拉加载时---追加数据
     * @param xunHeJiLus
     */
    public void addList(List<XunHeJiLu> xunHeJiLus) {
        if(xunHeJiLus.size()>0){
            mData.addAll(xunHeJiLus);
            notifyDataSetChanged();
        }else {
            ((BaseActivity)mContext).ShowToast("没有更多数据了");
        }
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_location_name)
        TextView tvLocationName;
        @BindView(R.id.tv_heduan_mingcheng)
        TextView tvHeduanMingcheng;
        @BindView(R.id.tv_wenti_type)
        TextView tvWentiType;
        @BindView(R.id.tv_wenti_detail)
        TextView tvWentiDetail;
        @BindView(R.id.tv_chuli_zhangtai)
        TextView tvChuliZhangtai;
        @BindView(R.id.tv_chuli_jieguo)
        TextView tvChuliJieguo;
        @BindView(R.id.tv_start_time)
        TextView tvStartTime;
        @BindView(R.id.tv_end_time)
        TextView tvEndTime;
        @BindView(R.id.iv_xunhe_jilu)
        CircleImageView iv_xunhe_jilu;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

}
