package com.example.propiedadesguitarra2.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.propiedadesguitarra2.converters.NumberConverter;
import com.example.propiedadesguitarra2.model.Pair;

import java.util.function.BiConsumer;

public class NumberComponent {
    private EditText numeroEditBox;
    private SeekBar numeroBar;
    private NumberPicker exponenteNumberPicker;
    private TextView numeroYexponenteTextView;
    private int minValue;

    private BiConsumer<Integer, Integer> onChange = null;

    public NumberComponent(EditText numeroEditBox, NumberPicker exponentNumberPicker, SeekBar numeroBar, TextView vista) {
        this.numeroEditBox = numeroEditBox;
        this.exponenteNumberPicker = exponentNumberPicker;
        this.numeroBar = numeroBar;
        this.numeroYexponenteTextView = vista;


        exponenteNumberPicker.setWrapSelectorWheel(true);
        minValue = -20;
        final int maxValue = 20;
        exponenteNumberPicker.setMinValue(0);
        exponenteNumberPicker.setMaxValue(maxValue - minValue);
        exponenteNumberPicker.setValue(maxValue);
        exponenteNumberPicker.setFormatter(index -> Integer.toString(index + minValue));


        this.numeroEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                display(NumberComponent.this.numeroEditBox, exponenteNumberPicker);
                if (onChange != null && NumberComponent.this.numeroEditBox.getText().length() > 0 &&
                        !NumberComponent.this.numeroEditBox.getText().equals("-"))
                    onChange.accept(Integer.parseInt(NumberComponent.this.numeroEditBox.getText().toString()), exponenteNumberPicker.getValue() + minValue);
            }
        });

        this.numeroEditBox.setOnClickListener(v -> this.numeroBar.setProgress(500));

        exponenteNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            this.numeroBar.setProgress(500);
            display(this.numeroEditBox, exponenteNumberPicker);
            if (onChange != null && NumberComponent.this.numeroEditBox.getText().length() > 0)
                onChange.accept(Integer.parseInt(this.numeroEditBox.getText().toString()), exponenteNumberPicker.getValue() + minValue);
        });

        this.numeroBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private Integer value;
            private int prev_value = 500;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value += (progress > prev_value? 10 : (progress == prev_value ? 0 :-10));
                NumberComponent.this.numeroEditBox.setText(value.toString());
                prev_value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                value = Integer.parseInt(NumberComponent.this.numeroEditBox.getText().toString());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void onChange(BiConsumer<Integer, Integer> method) {
        this.onChange = method;
    }

    public void update(Pair<Integer, Integer> value) {
        numeroEditBox.setText(value.first.toString());
        exponenteNumberPicker.setValue(value.second - minValue);
        numeroYexponenteTextView.setText(NumberConverter.prettyPrint(value));
    }

    private void display(EditText friccNum, NumberPicker friccDec) {
        if (friccNum.getText()!= null && friccNum.getText().length() > 0) {
            Integer value = Integer.parseInt(friccNum.getText().toString());
            int dec = friccDec.getValue() + minValue;
            numeroYexponenteTextView.setText(NumberConverter.prettyPrint(Pair.create(value, dec)));
        }
    }
}
