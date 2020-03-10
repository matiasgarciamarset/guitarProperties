package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.function.Consumer;

public class SimpleTextComponent {
    private EditText field;
    private boolean decimal;
    private Float from, to;

    private Consumer<Float> onChangeMethod;
    private Consumer<Float> onFocusChangeMethod;
    private Float previousValue = null;

    public SimpleTextComponent setView(EditText field) {
        this.field = field;

        field.addTextChangedListener(onSimpleFieldChange());
        field.setOnFocusChangeListener((v, hasFocus) -> {
            // Cuando se saca el foco, cambio el numero si corresponde
            if (!hasFocus) {
                Float number = Float.parseFloat(SimpleTextComponent.this.field.getText().toString());
                boolean limit = false;
                if (from != null && number < from) {
                    limit = true;
                    number = from;
                }
                if (to != null && number > to) {
                    limit = true;
                    number = to;
                }
                if (limit) {
                    SimpleTextComponent.this.field.setText(
                            decimal ? String.format("%f", number) : String.valueOf(number.intValue())
                    );
                }
                if (onFocusChangeMethod != null)
                    onFocusChangeMethod.accept(number);
            }
        });
        return this;
    }

    public SimpleTextComponent(Boolean decimal, Float from, Float to) {
        this.decimal = decimal;
        this.from = from;
        this.to = to;
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

    public void onFocusChange(Consumer<Float> method) {
        this.onFocusChangeMethod = method;
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
                        // No se procesa el valor si se va de margenes
                        if (from != null && number < from) {
                            return;
                        }
                        if (to != null && number > to) {
                            return;
                        }
                        if (onChangeMethod != null)
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
