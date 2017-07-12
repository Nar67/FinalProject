package com.tg.narcis.finalproject.fragments;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tg.narcis.finalproject.R;

import java.io.IOException;

public class MediaPlayer extends Fragment {

    View rootView;
    Button play, pause, stop;
    android.media.MediaPlayer mediaPlayer = new android.media.MediaPlayer();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_media_player, container, false);

        play = (Button) rootView.findViewById(R.id.play);
        pause = (Button) rootView.findViewById(R.id.pause);
        mediaPlayer = android.media.MediaPlayer.create(getActivity(), R.raw.agario);
        stop = (Button) rootView.findViewById(R.id.stop);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
        pause.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        }));
        stop.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setHasOptionsMenu(false);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }
}
