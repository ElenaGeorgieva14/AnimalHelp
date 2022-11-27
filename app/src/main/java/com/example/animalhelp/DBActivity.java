package com.example.animalhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBActivity extends AppCompatActivity {
    protected interface OnQuerySuccess{
        public void OnSuccess();
    }

    protected interface OnSelectSuccess{
        public void OnElementSelected(
                String ID, String Name, String Phone, String Email, String Address, String Problem
        );
    }

    protected boolean validateText(String text,String regex){
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            return true;
        }
        return false;
    }

    protected void validation(EditText editEmail, EditText editPhone) throws Exception {
        if(!validateText(editEmail.getText().toString(), "^[\\w\\.\\-]+@[\\w\\.\\-]+.+[\\w]+$")){
            throw new Exception("Invalid E-mail! Try again!");
        }
        if(!validateText(editPhone.getText().toString(), "^(\\+|00)?\\d+(\\/\\d+)?$")){
            throw new Exception("Invalid Phone! Try again!");
        }
    }
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    protected void SelectSQL(String selectQuery, String[] args, OnSelectSuccess success)
            throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase( getFilesDir().getPath()+"/AnimalsHelp.db",null);
        Cursor cursor = db.rawQuery(selectQuery,args);
        while(cursor.moveToNext()){
            @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String Name = cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range") String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
            @SuppressLint("Range") String Email = cursor.getString(cursor.getColumnIndex("Email"));
            @SuppressLint("Range") String Address= cursor.getString(cursor.getColumnIndex("Address"));
            @SuppressLint("Range") String Problem= cursor.getString(cursor.getColumnIndex("Problem"));
            success.OnElementSelected(ID,Name,Phone,Email,Address,Problem);
        }
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase( getFilesDir().getPath()+"/AnimalsHelp.db",null);
        if(args != null){
            db.execSQL(SQL,args);
        }else{
            db.execSQL(SQL);
        }
        db.close();
        success.OnSuccess();
    }

    protected void initDB()
            throws Exception{
        ExecSQL(
                "CREATE TABLE if not exists ANIMALS( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "Phone text not null, " +
                        "Email text not null, " +
                        "Address text not null, " +
                        "Problem text not null, " +
                        "unique(Id,Address) "+
                        ")",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "Database was created successfully",
                        Toast.LENGTH_LONG).show()
        );
    }
}
