package com.example.user.sqlitelab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;


    // layout object
    EditText mEtName;
    EditText mEtDel;
    Button mBtInsert;
    Button mBtRead;
    Button mBtDel;
    Button mBtUpdate;
    Button mBtReset;

    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);
        // 테이블 생성
        createTable();

        mEtName = (EditText) findViewById(R.id.editText);
        mEtDel = (EditText) findViewById(R.id.editText2);
        mBtInsert = (Button) findViewById(R.id.bt_in);
        mBtRead = (Button) findViewById(R.id.bt_rd);
        mBtDel = (Button) findViewById(R.id.bt_del);
        mBtUpdate = (Button) findViewById(R.id.bt_up);
        mBtReset = (Button) findViewById(R.id.bt_reset);
        ListView mList = (ListView) findViewById(R.id.listView);

        //INSERT 버튼 데이터 저장하기
        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                insertData(name);
            }
        });
        //READ 버튼 리스트 전체 읽어오기
        mBtRead = (Button) findViewById(R.id.bt_rd);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        //DELETE 버튼 입력한 INDEX 필드 데이터 삭제
        mBtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delete = mEtDel.getText().toString();
                Integer index = new Integer(delete);
                removeData(index);
            }
        });

        //UPDATE 버튼 입력한 INDEX 필드 데이터 수정
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                String update = mEtDel.getText().toString();
                Integer index = new Integer(update);
                updateData(index, name);
            }
        });

        //RESET 버튼 현재 Table 지우고 다시 Table 생성
        mBtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTable();
                createTable();
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + " order by id desc;";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.close();

    }

}
