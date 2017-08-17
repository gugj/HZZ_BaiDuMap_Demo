package com.roch.hzz_baidumap_demo.dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.adapter.CommonAdapter;
import com.roch.hzz_baidumap_demo.entity.ImageFloder;
import com.roch.hzz_baidumap_demo.photo.ViewHolder;
import java.util.List;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder> {
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
                R.layout.list_dir_item) {
            @Override
            public void convert(ViewHolder helper, ImageFloder item, int position) {
                helper.setText(R.id.id_dir_item_name, item.getName());
                helper.setImageByUrl(R.id.id_dir_item_image,
                        item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
                helper.setVisible(R.id.iv_select, item.isSelected());
            }
        });
    }

    public interface OnImageDirSelected {
        void selected(ImageFloder floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                    for (int i = 0; i < mDatas.size(); i++) {

                        if (i == position) {

                            mDatas.get(i).setSelected(true);

                        } else {

                            mDatas.get(i).setSelected(false);

                        }

                    }
                }
            }
        });
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
        // TODO Auto-generated method stub
    }

}
