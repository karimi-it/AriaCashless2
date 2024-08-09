package com.mcac.common.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Pattern;

public class CreditCardTextWatcher implements TextWatcher {
    static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    EditText editText;

    public CreditCardTextWatcher(EditText editText){
        this.editText = editText;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.w("", "input" + s.toString());

        if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
            String input = s.toString();
            String numbersOnly = keepNumbersOnly(input);
            String code = formatNumbersAsCode(numbersOnly);

            Log.w("", "numbersOnly" + numbersOnly);
            Log.w("", "code" + code);

            editText.removeTextChangedListener(this);
            editText.setText(code);
            // You could also remember the previous position of the cursor
            editText.setSelection(code.length());
            editText.addTextChangedListener(this);

        }
    }
    private String keepNumbersOnly(CharSequence s) {
        return s.toString().replaceAll("[^0-9]", ""); // Should of course be more robust
    }
    private String formatNumbersAsCode(CharSequence s) {
        int groupDigits = 0;
        String tmp = "";
        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                tmp += "-";
                groupDigits = 0;
            }
        }
        return tmp;
    }
}
