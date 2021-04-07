package com.hf.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    private EditText etMaxBuff, edTop, etBottom;
    private Button btnSave;
    private RadioGroup radioGroup0, radioGroup, radioGroup2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etMaxBuff = findViewById(R.id.et_maxbuff);
        edTop = findViewById(R.id.et_top);
        etBottom = findViewById(R.id.et_bottom);
        btnSave = findViewById(R.id.btn_save);

        radioGroup0 = (RadioGroup) findViewById(R.id.radioGroup0);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        if (ConsData.Reconnect) {
            ((RadioButton) radioGroup0.findViewById(R.id.rb_reconnect_open)).setChecked(true);
            ((RadioButton) radioGroup0.findViewById(R.id.rb_reconnect_close)).setChecked(false);
        } else {
            ((RadioButton) radioGroup0.findViewById(R.id.rb_reconnect_open)).setChecked(false);
            ((RadioButton) radioGroup0.findViewById(R.id.rb_reconnect_close)).setChecked(true);
        }
        if (ConsData.UseCache) {
            ((RadioButton) radioGroup.findViewById(R.id.rb_open)).setChecked(true);
            ((RadioButton) radioGroup.findViewById(R.id.rb_close)).setChecked(false);
        } else {
            ((RadioButton) radioGroup.findViewById(R.id.rb_open)).setChecked(false);
            ((RadioButton) radioGroup.findViewById(R.id.rb_close)).setChecked(true);
        }
        switch (ConsData.musicType) {
            case Traffic:
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_traffic)).setChecked(true);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_ugc)).setChecked(false);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_k)).setChecked(false);
                break;
            case UGC:
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_traffic)).setChecked(false);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_ugc)).setChecked(true);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_k)).setChecked(false);
                break;
            case K:
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_traffic)).setChecked(false);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_ugc)).setChecked(false);
                ((RadioButton) radioGroup2.findViewById(R.id.rb_type_k)).setChecked(true);
                break;
        }


        etMaxBuff.setText(ConsData.MaxBufferSize / 1024 + "");
        edTop.setText(ConsData.marginTop + "");
        etBottom.setText(ConsData.marginBottom + "");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxBuff = etMaxBuff.getText().toString().trim();
                String top = edTop.getText().toString().trim();
                String bottom = etBottom.getText().toString().trim();
                if (!isInteger(maxBuff) || !isInteger(top) || !isInteger(bottom)) {
                    Toast.makeText(EditActivity.this, "必须是数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                int iBuff = Integer.parseInt(maxBuff);
                if (iBuff < 200){
                    Toast.makeText(EditActivity.this, "缓冲不能小于200", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (iBuff > 1000){
                    Toast.makeText(EditActivity.this, "缓冲不能大于1000", Toast.LENGTH_SHORT).show();
                    return;
                }
                int iTop = Integer.parseInt(top);
                int iBottom = Integer.parseInt(bottom);

                if (iTop < 0){
                    Toast.makeText(EditActivity.this, "拖拽范围必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (iBottom < 0){
                    Toast.makeText(EditActivity.this, "拖拽范围必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }

                ConsData.MaxBufferSize = iBuff * 1024;
                ConsData.marginTop = Integer.parseInt(top);
                ConsData.marginBottom = Integer.parseInt(bottom);
                Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_open) {
                    ConsData.UseCache = true;
                } else {
                    ConsData.UseCache = false;
                }
            }
        });
        radioGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_reconnect_open) {
                    ConsData.Reconnect = true;
                } else {
                    ConsData.Reconnect = false;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_type_traffic) {
                    ConsData.musicType = MusicType.Traffic;
                } else if (checkedId == R.id.rb_type_ugc) {
                    ConsData.musicType = MusicType.UGC;
                } else if (checkedId == R.id.rb_type_k) {
                    ConsData.musicType = MusicType.K;
                }
            }
        });
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
