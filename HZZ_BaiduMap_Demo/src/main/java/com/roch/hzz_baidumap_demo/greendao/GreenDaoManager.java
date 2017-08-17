package com.roch.hzz_baidumap_demo.greendao;

import com.roch.hzz_baidumap_demo.MyApplication;
import com.roch.hzz_baidumap_demo.entity.DaoMaster;
import com.roch.hzz_baidumap_demo.entity.DaoSession;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/19/019 13:46
 */
public class GreenDaoManager {

    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    private GreenDaoManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(MyApplication.getContext(), "hzz_app-db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
