package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/21/021 15:24
 */
public class GV_Home_Adapter extends MyBaseAdapter {

    public GV_Home_Adapter(Context mContext, List<Photo> mData) {
        super(mContext, mData);
    }

    /**
     * 1.根据mContext获取Adapter的条目布局View
     * @param mContext
     * @return
     */
    @Override
    public View getConvertView(Context mContext) {
        return LayoutInflater.from(mContext).inflate(R.layout.gv_home_item, null);
    }

    /**
     * 2.根据Adapter的条目布局View获取相应的ViewHolder
     * @param convertView
     * @return
     */
    @Override
    protected BaseViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    /**
     * 3.在getView中最后给条目中的控件进行一一赋值
     * @param data
     * @param viewHolder
     */
    @Override
    protected void converItemValue(Object data, BaseViewHolder viewHolder) {
        ViewHolder holder= (ViewHolder) viewHolder;
        Photo photo= (Photo) data;
        if("0".equals(photo.getId())){ //加载的是res资源目录文件----0
            Glide.with(mContext)
                    .load(Integer.parseInt(photo.getUrl()))
                    .into(holder.iv_image_bg);
        }
    }

    public class ViewHolder extends BaseViewHolder{

        public ImageView iv_image_bg;
        public TextView tv_name;

        public ViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void initFindView(View rootView) {
            this.iv_image_bg = (ImageView) rootView.findViewById(R.id.iv_image_bg);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        }
    }

}
