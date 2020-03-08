package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.function.Consumer;

public class SimpleTextComponent {
    private EditText field;
    private boolean decimal;
    private Consumer<Float> onChangeMethod;
    private Float previousValue = null;

    public SimpleTextComponent() {
    }

    public SimpleTextComponent setView(EditText field) {
        this.field = field;

        field.addTextChangedListener(onSimpleFieldChange());
        return this;
    }

    public SimpleTextComponent(Boolean decimal) {
        this.decimal = decimal;
    }

    public void update(Float number) {
        if (number != null) {
            field.setText(decimal ? String.format("%f", number) : String.valueOf(number.intValue()));
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
                    Float number = Float.parseFloat(s.toString());
                    if (!number.equals(previousValue)) {
                        onChangeMethod.accept(number);
                        previousValue = number;
                    }
                } catch (Exception e) {
                    System.out.println("No se puede convertir valor a float: " + s.toString());
                }
            }
        };
    }
}
