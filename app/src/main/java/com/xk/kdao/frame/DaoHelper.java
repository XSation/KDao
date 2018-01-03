package com.xk.kdao.frame;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * TODO:兼容外置卡读写的运行时权限
 * Created by xuekai on 2018/1/2.
 */

public class DaoHelper {

    private volatile static DaoHelper instance = null;
    private  SQLiteDatabase sqLiteDatabase;

    private DaoHelper() {
        String path = Environment.getExternalStorageDirectory() + "/kdao.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    public static DaoHelper getInstance() {
        if (instance == null) {
            synchronized (DaoHelper.class) {
                if (instance == null) {
                    instance = new DaoHelper();
                }
            }
        }
        return instance;
    }

    /**
     * TODO:在这里可以把dao缓存到map中，保证只new一次
     * @param clazz
     * @param <T>
     * @return
     */
    public synchronized <T> BaseDao<T> getDaoHelper(Class<T> clazz) {
        BaseDao<T> tBaseDao = new BaseDao<>(sqLiteDatabase,clazz);
        return tBaseDao;
    }
}
