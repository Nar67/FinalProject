package com.tg.narcis.finalproject.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tg.narcis.finalproject.R;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static java.lang.String.valueOf;

public class Calculator extends Fragment implements View.OnClickListener {

    View rootView;

    private static final String TAG = "MainActivty";
    TextView calc_text;
    HorizontalScrollView horizon;
    Button calc_one, calc_two, calc_three, calc_four, calc_five, calc_six, calc_seven, calc_eight, calc_nine, calc_zero;
    Button calc_div, calc_mult, calc_subs, calc_sum, calc_equal, calc_coma, calc_ac, calc_ans, calc_open_par, calc_close_par, calc_del;
    Button calc_sin, calc_cos, calc_tan, calc_pow, calc_sqrt;
    private double result;
    boolean notif_toast;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        rootView = inflater.inflate(R.layout.activity_calculator, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


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

        if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            calc_sin = rootView.findViewById(R.id.calc_sin);
            calc_cos = rootView.findViewById(R.id.calc_cos);
            calc_tan = rootView.findViewById(R.id.calc_tan);
            calc_pow = rootView.findViewById(R.id.calc_pow);
            calc_sqrt = rootView.findViewById(R.id.calc_sqrt);

            calc_sin.setOnClickListener(this);
            calc_cos.setOnClickListener(this);
            calc_tan.setOnClickListener(this);
            calc_pow.setOnClickListener(this);
            calc_sqrt.setOnClickListener(this);
        }

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

        setHasOptionsMenu(true);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            String s = savedInstanceState.getString("text_calc");
            Log.v("save", s);
            setText(s);
            result = savedInstanceState.getDouble("result");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calc, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notif_toast:
                notif_toast();
                break;
            case R.id.notif_state:
                notif_state();
                break;
            case R.id.call:
                Intent inte = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                startActivity(inte);
                break;
        }

        return true;
    }

    private void notific () {
        int mId = 1;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity().getApplicationContext())
                        .setSmallIcon(R.drawable.ic_sentiment_neutral_black_24dp)
                        .setContentTitle("Math Error")
                        .setContentText("Ets un patán?");
        mNotificationManager.notify(mId, mBuilder.build());
    }


    private void notif_toast(){
        notif_toast = true;
    }

    private void notif_state(){
        notif_toast = false;
    }

    private void notification () {
        if(notif_toast)
            Toast.makeText(getActivity(), "Math Error", Toast.LENGTH_SHORT).show();
        else
            notific();
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

    public double eval(final String str) {
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
                if (pos < str.length()) {
                    notification();
                    //throw new RuntimeException("Unexpected: " + (char) ch);
                }
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

                double x = 0;
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
                    else {
                        notification();
                        //throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    notification();
                    //throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


    @Override
    public void onClick(View view) {
        String showed_text = calc_text.getText().toString();
        ViewTreeObserver vto = horizon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                horizon.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                horizon.scrollTo(calc_text.getWidth(), 0);
            }
        });
       // horizon.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
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
                if(Double.isNaN(result) || Double.isInfinite(result)){
                    notification();
                    setText("");
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
            case R.id.calc_sin:
                if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                    setText(showed_text+ "sin");
                break;
            case R.id.calc_cos:
                if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                    setText(showed_text+ "cos");
                break;
            case R.id.calc_tan:
                if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                    setText(showed_text+ "tan");
                break;
            case R.id.calc_pow:
                if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                    setText(showed_text+ "^");
                break;
            case R.id.calc_sqrt:
                if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                    setText(showed_text+ "sqrt");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text_calc", calc_text.getText().toString());
        outState.putDouble("result", result);
    }


}
