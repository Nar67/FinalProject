package com.tg.narcis.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.MainActivity;

import static com.tg.narcis.finalproject.R.menu.drawer;
import static java.lang.String.valueOf;

public class Calculator extends Fragment implements View.OnClickListener {

    private static final String TAG = "MainActivty";
    TextView calc_text;
    HorizontalScrollView horizon;
    Button calc_one, calc_two, calc_three, calc_four, calc_five, calc_six, calc_seven, calc_eight, calc_nine, calc_zero;
    Button calc_div, calc_mult, calc_subs, calc_sum, calc_equal, calc_coma, calc_ac, calc_ans, calc_open_par, calc_close_par, calc_del;
    private double result;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_calculator, container, false);


       // Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        calc_text = rootView.findViewById(R.id.text_calc);
        horizon = rootView.findViewById(R.id.horiz);
        horizon.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

        calc_zero = rootView.findViewById(R.id.calc_zero);
        calc_one = rootView.findViewById(R.id.calc_one);
        calc_two = rootView.findViewById(R.id.calc_two);
        calc_three = rootView.findViewById(R.id.calc_three);
        calc_four = rootView.findViewById(R.id.calc_four);
        calc_five = rootView.findViewById(R.id.calc_five);
        calc_six = rootView.findViewById(R.id.calc_six);
        calc_seven = rootView.findViewById(R.id.calc_seven);
        calc_eight = rootView.findViewById(R.id.calc_eight);
        calc_nine = rootView.findViewById(R.id.calc_nine);
        calc_div = rootView.findViewById(R.id.calc_div);
        calc_mult = rootView.findViewById(R.id.calc_mult);
        calc_subs = rootView.findViewById(R.id.calc_subs);
        calc_sum = rootView.findViewById(R.id.calc_sum);
        calc_equal = rootView.findViewById(R.id.calc_equals);
        calc_coma = rootView.findViewById(R.id.calc_coma);
        calc_ac = rootView.findViewById(R.id.calc_AC);
        calc_ans = rootView.findViewById(R.id.calc_ans);
        calc_open_par = rootView.findViewById(R.id.calc_open_par);
        calc_close_par = rootView.findViewById(R.id.calc_close_par);
        calc_del = rootView.findViewById(R.id.calc_delete);
        //setSupportActionBar(toolbar);

        calc_zero.setOnClickListener(this);
        calc_one.setOnClickListener(this);
        calc_two.setOnClickListener(this);
        calc_three.setOnClickListener(this);
        calc_four.setOnClickListener(this);
        calc_five.setOnClickListener(this);
        calc_six.setOnClickListener(this);
        calc_seven.setOnClickListener(this);
        calc_eight.setOnClickListener(this);
        calc_nine.setOnClickListener(this);
        calc_div.setOnClickListener(this);
        calc_mult.setOnClickListener(this);
        calc_subs.setOnClickListener(this);
        calc_sum.setOnClickListener(this);
        calc_equal.setOnClickListener(this);
        calc_coma.setOnClickListener(this);
        calc_ac.setOnClickListener(this);
        calc_ans.setOnClickListener(this);
        calc_open_par.setOnClickListener(this);
        calc_close_par.setOnClickListener(this);
        calc_del.setOnClickListener(this);
        return rootView;

    }


    private void setText(String text){
        //Log.v(TAG, text);
        calc_text.setText(text);
    }

    public String del_last_char(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('x')) x *= parseFactor(); // multiplication
                    else if (eat('÷')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


    @Override
    public void onClick(View view) {
        String showed_text = calc_text.getText().toString();
        horizon.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        switch (view.getId()) {
            case R.id.calc_zero:
                setText(showed_text+"0");
                break;
            case R.id.calc_one:
                setText(showed_text+"1");
                break;
            case R.id.calc_two:
                setText(showed_text+"2");
                break;
            case R.id.calc_three:
                setText(showed_text+"3");
                break;
            case R.id.calc_four:
                setText(showed_text+"4");
                break;
            case R.id.calc_five:
                setText(showed_text+"5");
                break;
            case R.id.calc_six:
                setText(showed_text+"6");
                break;
            case R.id.calc_seven:
                setText(showed_text+"7");
                break;
            case R.id.calc_eight:
                setText(showed_text+"8");
                break;
            case R.id.calc_nine:
                setText(showed_text+"9");
                break;
            case R.id.calc_div:
                setText(showed_text+" ÷ ");
                break;
            case R.id.calc_mult:
                setText(showed_text+" x ");
                break;
            case R.id.calc_subs:
                setText(showed_text+" - ");
                break;
            case R.id.calc_sum:
                setText(showed_text+" + ");
                break;
            case R.id.calc_equals:
                result = eval(showed_text);
                if(Double.isNaN(result)){
                    Log.v(TAG, "Not a number");
                    //TODO mostrar notificació d'error
                    setText("Math Error");
                }
                else if(result - (int) result == 0)
                    setText(valueOf((int) result));
                else
                    setText(valueOf(result));
                break;
            case R.id.calc_coma:
                setText(showed_text+".");
                break;
            case R.id.calc_AC:
                horizon.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                setText("");
                break;
            case R.id.calc_ans:
                if(result - (int) result == 0)
                    setText(showed_text + valueOf((int) result));
                else
                    setText(showed_text + String.valueOf(result));
                break;
            case R.id.calc_open_par:
                setText(showed_text + "(");
                break;
            case R.id.calc_close_par:
                setText(showed_text + ")");
                break;
            case R.id.calc_delete:
                showed_text = del_last_char(showed_text);
                setText(showed_text);
                break;
        }
    }
}
