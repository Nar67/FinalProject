package com.tg.narcis.finalproject.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;
import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.User;
import com.tg.narcis.finalproject.database.DataBaseHelper;

import java.util.Random;
import android.os.Handler;
import android.widget.TableRow;
import android.widget.TextView;

public class Memory extends Fragment {


    SharedPreferences sp;
    View rootview;
    CoolImageFlipper flipper;
    private int nrows = 4;
    private int ncols = 4;
    ImageView[][] grid;
    Drawable[][] imgGrid;
    private int clicked = 0;
    ImageView firstFlipped;
    ImageView secondFlipped;
    Drawable firstImg, secondImg;
    boolean[][] won;
    int cardsDone;
    int time = 2000;
    int moves = 0;
    private Runnable mRunnable;
    TextView textMem;

    LinearLayout linear;
    public Memory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
        rootview = inflater.inflate(R.layout.fragment_memory, container, false);
        won = new boolean[ncols][nrows];
        grid = new ImageView[nrows][ncols];
        imgGrid = new Drawable[nrows][ncols];
        cardsDone = 0;
        linear = (LinearLayout) rootview.findViewById(R.id.linear_layout);
        final int[] ids = {0};
        Thread th = new Thread() {
            @Override
            public void run() {
                Log.v("thread", "test");
                randomize();
            }
        };
        th.start();
        final int[] firstClickRow = {0};
        final int[] firstClickCols = { 0 };
        final Handler handl = new Handler();
        for (int rows = 0; rows < nrows; ++rows) {
            if(rows == 0){ //create TextView
                TableRow.LayoutParams tp = new TableRow.LayoutParams();
                textMem = new TextView(getActivity());
                tp.weight = 0.5f;
                tp.height = TableRow.LayoutParams.WRAP_CONTENT;
                tp.width = TableRow.LayoutParams.MATCH_PARENT;
                //textMem.setTextSize(20);
                textMem.setText("Moves: ");
                linear.addView(textMem);
            }
            LinearLayout lin = new LinearLayout(getActivity());
            lin.setId(rows);
            lin.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            lin.setLayoutParams(lp);
            linear.addView(lin);
            for (int cols = 0; cols < ncols; ++cols) {
                final ImageView iv = new ImageView(getActivity());
                iv.setId(ids[0]++);
                LinearLayout.LayoutParams lpiv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lpiv.weight = 1;
                lpiv.setMargins(2, 2, 2, 2);
                iv.setLayoutParams(lpiv);
                if(!(nrows*ncols%2 != 0 && rows == nrows/2 && cols == ncols/2)) {
                    iv.setImageResource(R.drawable.triforce_mem);
                    final int finalRows = rows;
                    final int finalCols = cols;
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            flipper = new CoolImageFlipper(getActivity());
                            flipper.flipImage(imgGrid[finalRows][finalCols], ((ImageView) view) );
                            if(clicked == 0) {
                                firstFlipped = grid[finalRows][finalCols];
                                firstImg = imgGrid[finalRows][finalCols];
                                firstClickRow[0] = finalRows;
                                firstClickCols[0] = finalCols;
                            }
                            if(clicked == 1) {
                                secondFlipped = grid[finalRows][finalCols];
                                secondImg = imgGrid[finalRows][finalCols];
                            }
                            if(clicked < 2)
                                clicked++;
                            if(clicked == 2) {
                                disableClick();
                                if(check(firstImg, secondImg)) {
                                    won[firstClickRow[0]][firstClickCols[0]] = true;
                                    won[finalRows][finalCols] = true;
                                    cardsDone += 2;
                                    Log.v("card", "cards done: " + cardsDone);
                                }
                                workInHandler();
                                handl.postDelayed(mRunnable, time);
                                clicked = 0;
                                moves++;
                                textMem.setText("Moves: " + moves);
                                if(cardsDone >= nrows*ncols-1) {
                                    User user = new User(null,null,"-1");
                                    user = getCurrentUser();
                                    String userScore = user.getScore();
                                    if(userScore == null || userScore.equals("-1") || Integer.parseInt(userScore) > moves) {
                                        int i = DataBaseHelper.getInstance(getActivity()).updateUser(user.getUsername(), user.getPassword(), String.valueOf(moves));
                                        if(i == 0)
                                            Log.v("profile", user.printUser(user));
                                    }
                                    showDialog();
                                }
                            }
                        }
                    });
                }
                grid[rows][cols] = iv;
                lin.addView(iv);

            }
        }
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return rootview;

    }
    public User getCurrentUser() {
        String s = sp.getString("username", "-1");
        User user = DataBaseHelper.getInstance(getActivity()).queryUser(s);
        return user;
    }

    private void showDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Congratulations! You Won!")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restartFragment();
                    }
                })
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memory, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void restartFragment() {
        clicked = 0;
        cardsDone = 0;
        moves = 0;
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(Memory.this)
                .attach(Memory.this)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.two:
                nrows = ncols = 2;
                restartFragment();
                break;
            case R.id.three:
                nrows = ncols = 3;
                restartFragment();
                break;
            case R.id.four:
                nrows = ncols = 4;
                restartFragment();
                break;
            case R.id.five:
                nrows = ncols = 5;
                restartFragment();
                break;
            case R.id.six:
                nrows = ncols = 6;
                restartFragment();
                break;
            case R.id.easy:
                time = 2000;
                restartFragment();
                break;
            case R.id.normal:
                time = 1000;
                restartFragment();
                break;
            case R.id.hard:
                time = 300;
                restartFragment();
                break;
            case R.id.hard_as_fuck:
                time = 10;
                restartFragment();
                break;
            case R.id.restart:
                restartFragment();
                break;
        }
        return true;
    }

    private boolean check(Drawable a, Drawable b) {
        return  a.getConstantState() == b.getConstantState();
    }



    private void disableClick() {
        for(int r = 0; r < nrows; r++ ) {
            for (int c = 0; c < ncols; c++) {
                grid[r][c].setClickable(false);
            }
        }
    }

    private void enableClick() {
        for(int r = 0; r < nrows; r++ ) {
            for (int c = 0; c < ncols; c++) {
                if(!won[r][c])
                    grid[r][c].setClickable(true);
            }
        }
    }

    public void workInHandler() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(check(firstImg, secondImg)) {
                    flipper.flipImage(null, firstFlipped);
                    flipper.flipImage(null, secondFlipped);
                } else {
                flipper.flipImage(getResources().getDrawable(R.drawable.triforce_mem), firstFlipped);
                flipper.flipImage(getResources().getDrawable(R.drawable.triforce_mem), secondFlipped);
                }
                enableClick();
            }
        };
    }



    private void randomize(){
        int[] used;
        used = new int[18];
        int imagesDone = 0;
        int imagesToDo;
        int row, col;
        row = -1;
        col = 0;
        imagesToDo = nrows*ncols - (nrows*ncols%2);
        while(imagesDone != imagesToDo) {
            Log.v("loop", "imagesDone: " + String.valueOf(imagesDone) + " row: " + row + " col: " + col);
            if(nrows*ncols%2 != 0 && row == nrows/2 && col == ncols/2) {
                col++;
            }
            else {
                Random rand = new Random();
                int n = rand.nextInt((nrows * ncols) / 2);
                if (used[n] < 2) {
                    used[n]++;
                    if (col % ncols == 0) {
                        col = 0;
                        row++;
                    }
                    imgGrid[row][col] = getDraw(n);
                    imagesDone++;
                    col++;
                }
            }
        }
    }

    private Drawable getDraw(int i){
        switch (i) {
            case 0:
                return getResources().getDrawable(R.drawable.toon_link_red_mem);
            case 1:
                return getResources().getDrawable(R.drawable.toon_link_purple_mem);
            case 2:
                return getResources().getDrawable(R.drawable.toon_link_green);
            case 3:
                return getResources().getDrawable(R.drawable.toon_link_dark);
            case 4:
                return getResources().getDrawable(R.drawable.goron_logo);
            case 5:
                return getResources().getDrawable(R.drawable.goron_mem);
            case 6:
                return getResources().getDrawable(R.drawable.majoras_mask_mem);
            case 7:
                return getResources().getDrawable(R.drawable.nayru_mem);
            case 8:
                return getResources().getDrawable(R.drawable.skull_kid_mem);
            case 9:
                return getResources().getDrawable(R.drawable.farore_mem);
            case 10:
                return getResources().getDrawable(R.drawable.din_mem);
            case 11:
                return getResources().getDrawable(R.drawable.deku_mem);
            case 12:
                return getResources().getDrawable(R.drawable.maskseller_mem);
            case 13:
                return getResources().getDrawable(R.drawable.zora_mem);
            case 14:
                return getResources().getDrawable(R.drawable.midna);
            case 15:
                return getResources().getDrawable(R.drawable.ganondorf_mem);
            case 16:
                return getResources().getDrawable(R.drawable.fierce_deity_mem);
            case 17:
                return getResources().getDrawable(R.drawable.tael_mem);
            default:
                return getResources().getDrawable(R.drawable.tael_mem);
        }
    }
}
