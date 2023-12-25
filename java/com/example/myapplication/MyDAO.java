package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDAO {
    MyOpenHelper openhelper;
    SQLiteDatabase database;
    Context mycontext;

    // 构造函数，接收上下文
    public MyDAO(Context context) {
        mycontext = context;
    }

    // 连接到数据库
    public void connectDB() {
        // 使用MyOpenHelper创建或打开数据库
        openhelper = new MyOpenHelper(mycontext, "myDB", null, 1);
        database = openhelper.getWritableDatabase();

        // 检查Person表中是否已经有数据
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM Person", null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // 如果表为空，则插入初始数据
                myInsert("张三", 18, "男");
                myInsert("李四", 20, "女");
                myInsert("王五", 22, "男");
                myInsert("赵六", 19, "女");
                myInsert("钱七", 21, "男");
                myInsert("孙八", 23, "女");
                myInsert("周九", 20, "男");
                myInsert("吴十", 18, "女");
                myInsert("郑十一", 22, "男");
                myInsert("王十二", 19, "女");
            }
            cursor.close();
        }
    }

    // 插入数据到Person表
    public void myInsert(String name, int age, String gender) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("gender", gender);
        // 插入数据
        database.insert("Person", null, values);
    }

    // 从Person表删除数据
    public void myDelete(int id) {
        // 根据id删除记录
        database.delete("Person", "id=?", new String[]{String.valueOf(id)});
    }

    // 更新Person表中的数据
    public void myUpdate(int id, String newName, int newAge, String newGender) {
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("age", newAge);
        values.put("gender", newGender);
        // 根据id更新记录
        database.update("Person", values, "id=?", new String[]{String.valueOf(id)});
    }

    // 获取Person表中所有的联系人姓名
    @SuppressLint("Range")
    public List<String> getAllContactNames() {
        List<String> names = new ArrayList<>();
        // 查询Person表中的name列
        Cursor cursor = database.query("Person", new String[]{"name"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            // 添加每个联系人的名字到列表
            names.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        return names;
    }
}