package com.mcgowan.timetable.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

public class StudentIdEditTextPreference extends EditTextPreference {
    static private final int DEFAULT_MINIMUM_ID_LENGTH = 9;
    private int mMinLength;


    public StudentIdEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StudentIdEditTextPreference,
                0, 0);
        try {
            mMinLength = a.getInteger(R.styleable.StudentIdEditTextPreference_minLength,
                    DEFAULT_MINIMUM_ID_LENGTH);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        EditText editText = getEditText();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Boolean showButton = !(s.length() < mMinLength);
                    button.setEnabled(showButton);
                }
            }
        });
    }
}
