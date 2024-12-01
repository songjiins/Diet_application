package com.example.eat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EatData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CaloriesDB";
    private static final int DATABASE_VERSION = 2; // 데이터베이스 버전 증가
    private static final String TABLE_NAME = "FoodTable";

    public EatData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mealType TEXT, " + // 식사 유형: 아침, 점심, 저녁
                "food TEXT, " +
                "calories INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 데이터 삽입 메서드
    public void insertData(String mealType, String food, int calories) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + " (mealType, food, calories) VALUES ('" + mealType + "', '" + food + "', " + calories + ");");
        db.close();
    }

    // 특정 식사 유형 데이터 조회
    public Cursor getMealData(String mealType) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE mealType = '" + mealType + "'", null);
    }

    // 특정 식사 유형의 칼로리 합산
    public int getTotalCaloriesByMeal(String mealType) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(calories) AS Total FROM " + TABLE_NAME + " WHERE mealType = '" + mealType + "'", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        cursor.close();
        db.close();
        return total;
    }
}
