package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;
import com.roch.hzz_baidumap_demo.utils.LogUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 17:14
 */
public class Gv_Photo_Adapter extends MyBaseAdapter {

    public Gv_Photo_Adapter(Context mContext, List mData) {
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
        Photo photo= (Photo) data;

        if("0".equals(photo.getId())){ //加载res资源文件图片
            holder.ivPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER); // 按比例缩放图片，居中显示
            LogUtil.println("Glide加载res资源文件图片===" + photo.getUrl());

//            holder.ivPhoto.setImageResource(Integer.parseInt(photo.getUrl()));
            Glide.with(mContext)
                    .load(Integer.parseInt(photo.getUrl()))
                    .into(holder.ivPhoto);
        }else if("1".equals(photo.getId())){ //加载本地照片图片
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP); // 宽高全部显示，占满View
            LogUtil.println("Glide加载本地照片图片===" + photo.getUrl());

            Glide.with(mContext)
                    .load(new File(photo.getUrl()))
                    .into(holder.ivPhoto);
        } else if("2".equals(photo.getId())){ //加载拍照后的照片
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP); // 宽高全部显示，占满View
            LogUtil.println("Glide加载拍照后的照片==="+photo.getUrl());

            Glide.with(mContext)
                    .load(photo.getUrl())
                    .into(holder.ivPhoto);
        }

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
