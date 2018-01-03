package com.xk.kdao.frame;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xk.kdao.frame.annotation.TableField;
import com.xk.kdao.frame.annotation.TableFieldModifier;
import com.xk.kdao.frame.annotation.TableName;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by xuekai on 2018/1/2.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqliteDatabase;

    private Class<T> tClass;
    /**
     * 表名
     */
    private String sTbName;
    /**
     * 一个map，key为数据库中的字段名，value为对应的javabean中的field
     */
    private HashMap<String, Field> fieldMaps;

    BaseDao(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        this.sqliteDatabase = sqLiteDatabase;
        this.tClass = tClass;
        if (sqliteDatabase.isOpen()) {
            init();
        }
    }

    private void init() {
        collectField();
        createTable();

    }

    /**
     * 创建fieldMaps
     */
    private void collectField() {
        fieldMaps = new HashMap<>();

        TableName tbName = tClass.getAnnotation(TableName.class);
        if (tbName != null) {
            sTbName = tbName.value();
        } else {
            sTbName = ("tb_" + tClass.getSimpleName()).toLowerCase();
        }
        //首先从表中查，或许表已经创建好了
        Cursor queryColumns = null;
        try {
            queryColumns = sqliteDatabase.query(sTbName, null, null, null, null, null, null, "1");
            for (String s : queryColumns.getColumnNames()) {
                fieldMaps.put(s, null);
            }
        } catch (Exception e) {
            Log.e("BaseDao", "collectField-->数据库中没有该表，去创建");
        } finally {
            if (queryColumns != null) {
                queryColumns.close();
            }
        }


        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            Log.i("BaseDao", "collectField-->" + field.getType().getSimpleName());
        }

        if (fieldMaps.size() > 0) {
            //表已经创建好了，根据表来确定字段
            for (String key : fieldMaps.keySet()) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    TableField tableField = field.getAnnotation(TableField.class);
                    String fieldName;
                    if (tableField != null) {
                        fieldName = tableField.value();
                    } else {
                        fieldName = field.getName();
                    }
                    if (key.equals(fieldName)) {
                        fieldMaps.put(key, field);
                        break;
                    }
                }
            }
        } else {
            //表没有创建好，根据tClass来确定字段，如果要依据javabena去创建数据库，必须用TableField注解
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    String fieldName = tableField.value();
                    fieldMaps.put(fieldName, field);

                }
            }
        }

    }


    /**
     * 创建泛型T对应的table
     */
    private void createTable() {

        Log.i("BaseDao", "createTable-->" + sTbName);

        StringBuffer createSQL = new StringBuffer();
        createSQL.append("create table if not exists ")
                .append(sTbName)
                .append("(");

        for (String key : fieldMaps.keySet()) {
            createSQL.append(key);
            createSQL.append(" ");
            createSQL.append(fieldMaps.get(key).getType().getSimpleName()).append(" ");
            TableFieldModifier tableFieldModifier = fieldMaps.get(key).getAnnotation(TableFieldModifier.class);
            String[] modifiers;
            if (tableFieldModifier != null) {
                modifiers = tableFieldModifier.value();
            } else {
                modifiers = new String[]{"text"};
            }
            for (String modifier : modifiers) {
                createSQL.append(modifier)
                        .append(" ");
            }
            createSQL.append(",");
        }
        if (createSQL.charAt(createSQL.length() - 1) == ',') {
            createSQL.deleteCharAt(createSQL.length() - 1);
        }

        createSQL.append(")");
        try {
            sqliteDatabase.execSQL(createSQL.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long insert(T data) {
        long insert = -1;
        try {
            ContentValues contentValues = new ContentValues();
            for (String key : fieldMaps.keySet()) {
                Field field = fieldMaps.get(key);
                field.setAccessible(true);
                Object value = field.get(data);
                contentValues.put(key, String.valueOf(value));
            }
            insert = sqliteDatabase.insert(sTbName, null, contentValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    public long update(T data, T where) {
        return 0;
    }
}
