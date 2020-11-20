package com.hifive.sdk.demo.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.hifive.sdk.R;


public class HifiveComfirmDialogFragment extends DialogFragment {
    public static final String ContentTx = "contentTx";
    private OnSureClick sureClick;
    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!= null) {
            Window window = getDialog().getWindow();
            if (window != null && mContext != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.hifive_dialog_comfirm, container);
        initDialog(view);
        return view;
    }
    public interface OnSureClick {
        void sureClick();
    }
    public void setOnSureClick(OnSureClick sureClick) {
        this.sureClick = sureClick;
    }

    private void initDialog(View view) {
        TextView dialog_content = view.findViewById(R.id.dialog_content);
        if (getArguments() != null && getArguments().getString(ContentTx) != null) {
            dialog_content.setText(getArguments().getString(ContentTx));
        }
        TextView dialog_confirm = view.findViewById(R.id.dialog_confirm);
        dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (sureClick != null)
                    sureClick.sureClick();
            }
        });
        TextView  dialog_cancel = view.findViewById(R.id.dialog_cancel);
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
