package com.example.propiedadesguitarra2.ui.propiedades.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.function.BiConsumer;

import static com.example.propiedadesguitarra2.StateManager.convertToNumbers;
import static com.example.propiedadesguitarra2.StateManager.prettyPrint;

public class NumberComponent {
    private EditText numeroEditBox;
    private SeekBar numeroBar;
    private NumberPicker exponenteNumberPicker;
    private TextView numeroYexponenteTextView;

    private BiConsumer<Double, Integer> onChange = null;

    public NumberComponent(EditText numeroEditBox, NumberPicker exponentNumberPicker, SeekBar numeroBar, TextView vista) {
        this.numeroEditBox = numeroEditBox;
        this.exponenteNumberPicker = exponentNumberPicker;
        this.numeroBar = numeroBar;
        this.numeroYexponenteTextView = vista;


        exponenteNumberPicker.setWrapSelectorWheel(true);
        final int minValue = -20;
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
                    onChange.accept(Double.parseDouble(NumberComponent.this.numeroEditBox.getText().toString()), exponenteNumberPicker.getValue() - 20);
            }
        });

        this.numeroEditBox.setOnClickListener(v -> this.numeroBar.setProgress(500));

        exponenteNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            this.numeroBar.setProgress(500);
            display(this.numeroEditBox, exponenteNumberPicker);
            if (onChange != null && NumberComponent.this.numeroEditBox.getText().length() > 0)
                onChange.accept(Double.parseDouble(this.numeroEditBox.getText().toString()), exponenteNumberPicker.getValue() - 20);
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

    public void onChange(BiConsumer<Double, Integer> method) {
        this.onChange = method;
    }

    public void update(String value) {
        Pair<Double, Integer> parsed = convertToNumbers(value);
        numeroEditBox.setText(parsed.first.toString().split("\\.")[0]); // Elimino el punto del Double
        exponenteNumberPicker.setValue(parsed.second + 20);
        numeroYexponenteTextView.setText("");
    }

    private void display(EditText friccNum, NumberPicker friccDec) {
        if (friccNum.getText()!= null && friccNum.getText().length() > 0) {
            Double value = Double.parseDouble(friccNum.getText().toString());
            int dec = friccDec.getValue() - 20;
            numeroYexponenteTextView.setText(prettyPrint(value, dec));
        }
    }
}
