package com.example.lavisha.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_BOOK_VIEW = "book_view";

    //Book columns
    public static final String UNIQUE_ID = "u_id";
    public static final String BOOK_NAME = "book_name";
    public static final String BOOK_COUNT = "book_count";

    // Database Information
    static final String DB_NAME = "Book.db";

    // Creating table query
    private static final String CREATE_BOOK = "create table " + TABLE_BOOK_VIEW + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + BOOK_NAME + " TEXT, " +
            "" + BOOK_COUNT + " TEXT);";


    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_BOOK);
        onCreate(db);
    }

    public boolean checkBookExist(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean bookExist = false;
        Cursor c = db.query(TABLE_BOOK_VIEW, null, BOOK_NAME + " = ?", new String[]{bookId}, null, null, null);
        if (c == null) return bookExist;

        while (c.moveToNext()) {
            bookExist = true;
        }
        c.close();

        return bookExist;
    }

    //INSERT Book ONLY SINGLE
    public void insertBookCount(String mBook) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (checkBookExist(String.valueOf(mBook))) {
            updateBookCount(String.valueOf(mBook));
        } else {

            ContentValues contentValue = new ContentValues();
            contentValue.put(BOOK_NAME, mBook);
            contentValue.put(BOOK_COUNT, "1");
            db.insert(TABLE_BOOK_VIEW, null, contentValue);
        }
    }

    public int updateBookCount(String bookId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        int count = getBookCount(bookId) + 1;
        contentValue.put(BOOK_COUNT, "" + count);

        int i = db.update(TABLE_BOOK_VIEW, contentValue,
                BOOK_NAME + " = ?", new String[]{bookId});

        return i;
    }

    public int getBookCount(String mId) {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_BOOK_VIEW + " WHERE " +
                    BOOK_NAME + "=?", new String[]{mId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        count = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BOOK_COUNT)));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}

