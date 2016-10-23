package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class CalculatorActivity extends Activity {
    private enum Operation {ADD, SUB, MUL, DIV}

    private String number, textError;
    TextView window;
    double oper1, oper2;
    private boolean isAnswer;
    Operation operation;

    static final String NUMBER = "number";
    static final String OPERATOR1 = "oper1";
    static final String OPERATOR2 = "oper2";
    static final String OPERATION = "operation";
    static final String ISANSWER = "isAnswer";
    static final String TEXTERROR = "textError";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        window = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            number = savedInstanceState.getString(NUMBER);
            oper1 = savedInstanceState.getDouble(OPERATOR1);
            oper2 = savedInstanceState.getDouble(OPERATOR2);
            operation = (Operation) savedInstanceState.getSerializable(OPERATION);
            isAnswer = savedInstanceState.getBoolean(ISANSWER);
            textError = savedInstanceState.getString(TEXTERROR);
            if (textError.isEmpty()) {
                window.setText(number);
            } else {
                window.setText(textError);
            }
        } else {
            reset();
        }
    }

    public void onClickDigit(View view) {
        Button btn = (Button) view;
        if (number.equals("Error") && operation == null) {
            reset();
        }
        if (!number.equals("0")) {
            number += btn.getText().toString();
        } else if (btn.getId() != R.id.d0) {
            number = btn.getText().toString();
        }
        if (operation == null) {
            if (isAnswer) {
                reset();
                number = btn.getText().toString();
            }
            oper1 = Double.parseDouble(number);
        } else {
            oper2 = Double.parseDouble(number);
        }
        window.setText(number);
    }

    public void onClickSign(View view) {
        Button btn = (Button) view;
        if (oper2 == 0 && operation == Operation.DIV || oper2 != 0) {
            ResultAction();
        }
        if (number.equals("Error")) {
            textError = "Error: please, enter the number!";
            window.setText(textError);
            return;
        }
        switch (btn.getId()) {
            case R.id.add:
                operation = Operation.ADD;
                number = "0";
                break;
            case R.id.sub:
                operation = Operation.SUB;
                number = "0";
                break;
            case R.id.mul:
                operation = Operation.MUL;
                number = "0";
                break;
            case R.id.div:
                operation = Operation.DIV;
                number = "0";
                break;
        }
    }

    public void onClickEquivalence(View view) {
        if (!number.equals("Error")) {
            ResultAction();
        } else {
            textError = "Error: please, enter expression!";
            window.setText(textError);
        }
    }

    public void onClickClear(View view) {
        reset();
    }

    public void ResultAction() {
        double answer = oper1;
        boolean ERROR = false;
        switch (operation) {
            case ADD:
                answer = oper1 + oper2;
                break;
            case SUB:
                answer = oper1 - oper2;
                break;
            case MUL:
                answer = oper1 * oper2;
                break;
            case DIV:
                if (Math.abs(oper2) < 0.0000000001) {
                    ERROR = true;
                    reset();
                    number = "Error";
                    textError = "Error: divide by zero!";
                    window.setText(textError);
                } else {
                    answer = oper1 / oper2;
                }
                break;
        }
        if (!number.equals("Error")) {
            reset();
            number = String.valueOf(answer);
            window.setText(number);
            oper1 = answer;
            isAnswer = true;
        }
    }

    private void reset() {
        oper1 = 0;
        oper2 = 0;
        operation = null;
        number = "0";
        isAnswer = false;
        textError = "";
        window.setText(number);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString(NUMBER, number);
        saveInstanceState.putDouble(OPERATOR1, oper1);
        saveInstanceState.putDouble(OPERATOR2, oper2);
        saveInstanceState.putSerializable(OPERATION, operation);
        saveInstanceState.putBoolean(ISANSWER, isAnswer);
        saveInstanceState.putString(TEXTERROR, textError);
    }

}
