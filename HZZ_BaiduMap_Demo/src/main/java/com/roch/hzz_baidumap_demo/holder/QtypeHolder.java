package com.roch.hzz_baidumap_demo.holder;

import android.view.ViewGroup;
import android.widget.CheckBox;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;

public class QtypeHolder extends BaseRecyclerViewHolder<WenTiLeiXin> {

    private CheckBox cb_qtype;

    public QtypeHolder(ViewGroup parent) {
        super(parent, R.layout.holder_consume);
    }

    @Override
    public void setData(final WenTiLeiXin object) {
        super.setData(object);
        cb_qtype.setText(object.getTypename());
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        cb_qtype = findViewById(R.id.cb_qtype);
    }

    @Override
    public void onItemViewClick(WenTiLeiXin object) {
        super.onItemViewClick(object);
        //点击事件
        LogUtil.println("RecyclerView条目点击事件：==="+object.getTypename());
        if(Common.qtype_select.containsKey(object.getTypecode())){
            Common.qtype_select.remove(object.getTypecode());
        }else {
            Common.qtype_select.put(object.getTypecode(),object.getTypename());
        }

//        Set<String> keySet = Common.qtype_select.keySet();
//        for (String key : keySet) {
//            LogUtil.println("选中的问题类型有：==="+Common.qtype_select.get(key));
//        }
    }
}