package com.tg.narcis.finalproject.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tg.narcis.finalproject.MyCustomAdapter;
import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.User;
import com.tg.narcis.finalproject.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends Fragment {

    View rootview;
    SharedPreferences sp;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    MyCustomAdapter contactsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_ranking, container, false);
        sp = getActivity().getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
        //findViewById del layout activity_main
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.mRecyclerView);
        //LinearLayoutManager necesita el contexto de la Activity.
        //El LayoutManager se encarga de posicionar los items dentro del recyclerview
        //Y de definir la politica de reciclaje de los items no visibles.
        mLinearLayout = new LinearLayoutManager(getActivity());

        //Asignamos el LinearLayoutManager al recycler:
        mRecyclerView.setLayoutManager(mLinearLayout);

        //El adapter se encarga de  adaptar un objeto definido en el c�digo a una vista en xml
        //seg�n la estructura definida.
        //Asignamos nuestro custom Adapter
        contactsAdapter = initAdapterData();

        mRecyclerView.setAdapter(contactsAdapter);


        setHasOptionsMenu(true);
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ranking_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void restartFragment() {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(Ranking.this)
                .attach(Ranking.this)
                .commit();
    }

    public User getCurrentUser() {
        String s = sp.getString("username", "-1");
        User user = DataBaseHelper.getInstance(getActivity()).queryUser(s);
        return user;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.reset){
            User user = getCurrentUser();
            int i = DataBaseHelper.getInstance(getActivity()).updateUser(user.getUsername(), user.getPassword(), "-1");
            Toast.makeText(getActivity(), "User score deleted, you can't delete other users scores tho", Toast.LENGTH_SHORT).show();
            restartFragment();
        }
        return true;
    }

    private MyCustomAdapter initAdapterData () {
        ArrayList<User> users = getUsers();
        return new MyCustomAdapter(getActivity(),users);
    }


    private ArrayList<User> getUsers() {
        ArrayList<User> users;
        users = DataBaseHelper.getInstance(getActivity()).queryAllUsers();
        return users;
    }

}
