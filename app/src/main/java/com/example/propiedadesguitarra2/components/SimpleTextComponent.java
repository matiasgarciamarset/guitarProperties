package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.function.Consumer;

public class SimpleTextComponent {
    private EditText field;
    private boolean decimal = true;
    private Consumer<Float> onChangeMethod;

    public SimpleTextComponent(EditText field) {
        this.field = field;

        field.addTextChangedListener(onSimpleFieldChange());
    }

    public SimpleTextComponent(EditText field, Boolean decimal) {
        this.field = field;
        this.decimal = decimal;

        field.addTextChangedListener(onSimpleFieldChange());
    }

    public void update(Float number) {
        if (number != null) {
            field.setText(decimal ? number.toString() : String.valueOf(number.intValue()));
        } else {
            field.setText("0");
        }
    }

    public void onChange(Consumer<Float> method) {
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
                try {
                    onChangeMethod.accept(Float.parseFloat(s.toString()));
                } catch (Exception e) {
                    System.out.println("No se puede convertir valor a float: " + s.toString());
                }
            }
        };
    }
}
