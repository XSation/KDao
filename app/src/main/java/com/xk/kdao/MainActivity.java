package com.xk.kdao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xk.kdao.frame.BaseDao;
import com.xk.kdao.frame.DaoHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        daoHelper.insert()
    }


    public void btn1(View v) {
        BaseDao<User> daoHelper = DaoHelper.getInstance().getDaoHelper(User.class);
        long xuekaiii = daoHelper.insert(new User("xuekaiii", 10,true));
        long asdf = daoHelper.insert(new User("adfa", 12,false));
        Log.i("MainActivity","btn1-->"+xuekaiii);
        Log.i("MainActivity","btn1-->"+asdf);
    }
}
