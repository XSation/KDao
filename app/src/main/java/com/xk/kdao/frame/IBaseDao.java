package com.xk.kdao.frame;

/**
 * Created by xuekai on 2018/1/2.
 */

public interface IBaseDao<T> {
    long insert(T data);

    long update(T data, T where);
}
