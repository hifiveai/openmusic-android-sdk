package com.hf.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    private EditText etMaxBuff, edTop, etBottom;
    private Button btnSave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etMaxBuff = findViewById(R.id.et_maxbuff);
        edTop = findViewById(R.id.et_top);
        etBottom = findViewById(R.id.et_bottom);
        btnSave = findViewById(R.id.btn_save);

        etMaxBuff.setText(ConsData.MaxBufferSize / 1024 + "");
        edTop.setText(ConsData.marginTop + "");
        etBottom.setText(ConsData.marginBottom + "");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxBuff = etMaxBuff.getText().toString().trim();
                String top = edTop.getText().toString().trim();
                String bottom = etBottom.getText().toString().trim();
                if (!isInteger(maxBuff) || !isInteger(top) || !isInteger(bottom)){
                    Toast.makeText(EditActivity.this, "必须是数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                ConsData.MaxBufferSize = Integer.parseInt(maxBuff) * 1024;
                ConsData.marginTop = Integer.parseInt(top);
                ConsData.marginBottom = Integer.parseInt(bottom);
                Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
