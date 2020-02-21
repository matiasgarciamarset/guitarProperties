package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.propiedadesguitarra2.converters.NumberConverter;
import com.example.propiedadesguitarra2.model.Pair;

import java.util.function.Consumer;

public class NumberComponent {
    private EditText numeroEditBox;
    private SeekBar numeroBar;
    private EditText factor;
    private Float realFactor;

    private Consumer<Float> onChange = null;

    public NumberComponent(EditText numeroEditBox, EditText factor, SeekBar numeroBar) {
        this.numeroEditBox = numeroEditBox;
        this.factor = factor;
        this.numeroBar = numeroBar;
        this.realFactor = getValueOrCero(factor.getText().toString());

        numeroBar.setMax(100);

        this.numeroEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (onChange != null && NumberComponent.this.numeroEditBox.getText().length() > 0 &&
                        !NumberComponent.this.numeroEditBox.getText().equals("-"))
                    onChange.accept(Float.parseFloat(NumberComponent.this.numeroEditBox.getText().toString()));
            }
        });

        this.numeroEditBox.setOnClickListener(v -> this.numeroBar.setProgress(500));

        this.factor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                NumberComponent.this.numeroBar.setProgress(50);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                NumberComponent.this.realFactor = getValueOrCero(s.toString());
            }
        });

        this.numeroBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private Float value;
            private int prev_value = 50;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value += (progress > prev_value? realFactor : (progress == prev_value ? 0 :-realFactor));
                NumberComponent.this.numeroEditBox.setText(value.toString());
                prev_value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                String number = NumberComponent.this.numeroEditBox.getText().toString();
                if (number != null && number.length() > 1) {
                    try {
                        value = Float.parseFloat(number);
                    } catch(NumberFormatException e) {
                        NumberComponent.this.realFactor = 1f;
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private Float getValueOrCero(String value) {
        if (value != null && value.length() > 0) {
            try {
                return Float.parseFloat(value);
            } catch(Exception e) {
                System.out.println("No se puede convertir el numero a float: " + value);
            }
        }
        return 0f;
    }

    public void onChange(Consumer<Float> method) {
        this.onChange = method;
    }

    public void update(Float value) {
        numeroEditBox.setText(value.toString());
        NumberComponent.this.realFactor = 1f;
        factor.setText("1");
    }
}
