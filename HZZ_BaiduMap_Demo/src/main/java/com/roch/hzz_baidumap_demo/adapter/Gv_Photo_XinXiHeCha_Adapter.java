package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 17:14
 */
public class Gv_Photo_XinXiHeCha_Adapter extends MyBaseAdapter {

    public Gv_Photo_XinXiHeCha_Adapter(Context mContext, List mData) {
        super(mContext, mData);
    }

    @Override
    public View getConvertView(Context mContext) {
        return View.inflate(mContext, R.layout.gridview_photo_item, null);
    }

    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void converItemValue(Object data, BaseViewHolder viewHolder) {
        ViewHolder holder= (ViewHolder) viewHolder;
        XinXiHeChaPhoto photo= (XinXiHeChaPhoto) data;

        // http://192.168.1.152:8080/jeecg/upload/20170710/1499676044183.jpg
        LogUtil.println("Glide加载网络图片时路径：==="+ URLs.IMAGE_URL+photo.getFilePath());
        Glide.with(mContext)
                .load(URLs.IMAGE_URL+photo.getFilePath())
                .into(holder.ivPhoto);

    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }
}
