package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.propiedadesguitarra2.NumberConverter;

import java.util.function.BiConsumer;

public class SimpleTextComponent {
    private EditText field;
    private BiConsumer<Integer, Integer> onChangeMethod;

    public SimpleTextComponent(EditText field) {
        this.field = field;

        field.addTextChangedListener(onSimpleFieldChange());
    }

    public void update(String number) {
        field.setText(NumberConverter.desserialize(number));
    }

    public void onChange(BiConsumer<Integer, Integer> method) {
        this.onChangeMethod = method;
    }

    private TextWatcher onSimpleFieldChange() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equals("-")) {
                    onChangeMethod.accept(0, 0);
                    return;
                }
                String[] split = s.toString().split("\\.");
                Integer coef;
                Integer exp;
                if (split.length == 1) {
                    coef = Integer.parseInt(split[0]);
                    exp = 0;
                } else {
                    coef = Integer.parseInt(split[0].concat(split[1]));
                    exp = split[1].length() * -1;
                }
                onChangeMethod.accept(coef, exp);
            }
        };
    }
}
