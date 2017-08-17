package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.activity.BaseActivity;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
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
 * 信息核查的ListView的适配器
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 14:04
 */
public class XinXiHeChaAdapter extends MyBaseAdapter<XinXiHeCha> {

    private Drawable wuTuDrawable;

    public XinXiHeChaAdapter(Context mContext, List<XinXiHeCha> mData) {
        super(mContext, mData);
        wuTuDrawable = mContext.getResources().getDrawable(R.drawable.wutu);
    }

    @Override
    public View getConvertView(Context mContext) {
        return View.inflate(mContext, R.layout.adapter_xinxi_hecha, null);
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(XinXiHeCha data, BaseViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;

        // 图片处理
        ViewGroup.LayoutParams lParams = holder.iv_xinxi_hecha.getLayoutParams();
        lParams.width = Common.Width / 5;
        lParams.height = Common.Width / 5;
        holder.iv_xinxi_hecha.setLayoutParams(lParams);
        if(StringUtil.isNotEmpty(data.getReportRvinfoWxAndImgEntity()) && data.getReportRvinfoWxAndImgEntity().getImglist().size()>0 && StringUtil.isNotEmpty(data.getReportRvinfoWxAndImgEntity().getImglist().get(0).getFilePath())){
            LogUtil.println("拼接之后图片的路径为：=="+URLs.IMAGE_URL+data.getReportRvinfoWxAndImgEntity().getImglist().get(0).getFilePath());
            Glide.with(mContext)
                    .load(URLs.IMAGE_URL + data.getReportRvinfoWxAndImgEntity().getImglist().get(0).getFilePath())
                    .into(holder.iv_xinxi_hecha);
        }else {
            holder.iv_xinxi_hecha.setImageDrawable(wuTuDrawable);
        }

        holder.tvHeduanMingcheng.setText(data.getReportRvinfoWxAndImgEntity().getRvName()); // 河道名称
        holder.tv_shangbao_name.setText(data.getReportRvinfoWxAndImgEntity().getStimestr()); // 上报时间
        holder.tvWentiType.setText(data.getReportRvinfoWxAndImgEntity().getDetail()); // 问题描述
        if("0".equals(data.getInfoStatus())){
            holder.tvWentiDetail.setText("未确认"); // 确认状态
        }else if ("1".equals(data.getInfoStatus())){
            holder.tvWentiDetail.setText("已确认"); // 确认状态
        }
//        holder.tvChuliZhangtai.setText(data.getInfoStatus());
//        holder.tvChuliJieguo.setText(data.getStatustxt());
    }

    /**
     * 下拉刷新时或者筛选条件过之后---添加数据
     * @param xunHeJiLus
     */
    public void onRefsh(List<XinXiHeCha> xunHeJiLus) {
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
    public void addList(List<XinXiHeCha> xunHeJiLus) {
        if(xunHeJiLus.size()>0){
            mData.addAll(xunHeJiLus);
            notifyDataSetChanged();
        }else {
            ((BaseActivity)mContext).ShowToast("没有更多数据了");
        }
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_heduan_mingcheng)
        TextView tvHeduanMingcheng;
        @BindView(R.id.tv_shangbao_name)
        TextView tv_shangbao_name;
        @BindView(R.id.tv_wenti_type)
        TextView tvWentiType;
        @BindView(R.id.tv_wenti_detail)
        TextView tvWentiDetail;
        @BindView(R.id.tv_chuli_zhangtai)
        TextView tvChuliZhangtai;
        @BindView(R.id.tv_chuli_jieguo)
        TextView tvChuliJieguo;
        @BindView(R.id.iv_xinxi_hecha)
        CircleImageView iv_xinxi_hecha;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

}
