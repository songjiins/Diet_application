package com.example.eat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edt1, edt2, edt3, edt4, edt5, edt6, edt7, edt8, edt9;
    private Button btn1;
    private TextView textView5;
    private EatData dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText 연결
        edt1 = findViewById(R.id.edt1); // 식사 유형 (아침, 점심, 저녁)
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        edt5 = findViewById(R.id.edt5);
        edt6 = findViewById(R.id.edt6);
        edt7 = findViewById(R.id.edt7);
        edt8 = findViewById(R.id.edt8);
        edt9 = findViewById(R.id.edt9);

        // Button 및 TextView 연결
        btn1 = findViewById(R.id.btn1);
        textView5 = findViewById(R.id.textView5);

        // DBHelper 초기화
        dbHelper = new EatData(this);

        // 버튼 클릭 이벤트
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealType = edt1.getText().toString().trim();
                if (mealType.isEmpty()) {
                    Toast.makeText(MainActivity.this, "아침, 점심, 저녁 중 하나를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // EditText 값 저장
                saveData(mealType, edt2, edt3);
                saveData(mealType, edt4, edt5);
                saveData(mealType, edt6, edt7);
                saveData(mealType, edt8, edt9);

                // 입력 필드 초기화
                clear();

                // 데이터 표시
                showData();
            }
        });
    }

    private void saveData(String mealType, EditText foodEditText, EditText calorieEditText) {
        String food = foodEditText.getText().toString().trim();
        String calorieStr = calorieEditText.getText().toString().trim();

        if (!food.isEmpty() && !calorieStr.isEmpty()) {
            try {
                int calories = Integer.parseInt(calorieStr);
                dbHelper.insertData(mealType, food, calories);
            } catch (NumberFormatException e) {
                calorieEditText.setError("숫자를 입력해주세요");
            }
        }
    }

    private void showData() {
        // 현재 저장된 데이터를 불러오기
        StringBuilder stringBuilder = new StringBuilder();
        int dailyTotalCalories = 0;

        // 아침, 점심, 저녁 데이터를 각각 출력
        String[] mealTypes = {"아침", "점심", "저녁"};
        for (String mealType : mealTypes) {
            Cursor cursor = dbHelper.getMealData(mealType);
            stringBuilder.append("[").append(mealType).append("]\n");

            int mealTotalCalories = 0;
            while (cursor.moveToNext()) {
                String food = cursor.getString(cursor.getColumnIndex("food"));
                int calories = cursor.getInt(cursor.getColumnIndex("calories"));
                mealTotalCalories += calories;

                // 음식 이름과 칼로리 출력
                stringBuilder.append(food).append(" ").append(calories).append("칼로리\n");
            }
            cursor.close();

            // 각 식사별 총 칼로리 출력
            if (mealTotalCalories > 0) {
                stringBuilder.append("총 칼로리: ").append(mealTotalCalories).append("칼로리\n\n");
            }
            dailyTotalCalories += mealTotalCalories; // 하루 총 칼로리 계산
        }

        // 하루 총 칼로리 출력
        stringBuilder.append("오늘 하루 총 먹은 칼로리는 ").append(dailyTotalCalories).append("칼로리입니다.");

        // TextView에 출력
        textView5.setText(stringBuilder.toString());
    }

    // 입력 필드 초기화 메서드
    private void clear() {
        EditText[] inputs = {edt1, edt2, edt3, edt4, edt5, edt6, edt7, edt8, edt9};

        for (EditText input : inputs) {
            input.setText("");
        }
    }
}
