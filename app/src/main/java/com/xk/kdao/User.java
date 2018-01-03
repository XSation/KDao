package com.xk.kdao;

import com.xk.kdao.frame.annotation.TableField;
import com.xk.kdao.frame.annotation.TableFieldModifier;
import com.xk.kdao.frame.annotation.TableName;

/**
 * 实体类，对应于数据库中的一张表
 * Created by xuekai on 2018/1/2.
 */
@TableName("tb_user7")
public class User {
    @TableField("tb_name")
    @TableFieldModifier({"primary key"})
    private String name;
    @TableField("tb_age")
    private int age;


    @TableField("tb_man")
    private boolean isMan;

    public User(String name, int age, boolean isMan) {
        this.name = name;
        this.age = age;
        this.isMan = isMan;
    }
}
