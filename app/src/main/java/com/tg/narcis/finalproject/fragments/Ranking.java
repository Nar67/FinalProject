package com.tg.narcis.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tg.narcis.finalproject.MyCustomAdapter;
import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.User;
import com.tg.narcis.finalproject.database.DataBaseHelper;

import java.util.List;

public class Ranking extends Fragment {

    View rootview;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    MyCustomAdapter contactsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_ranking, container, false);
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



        return rootview;
    }

    private MyCustomAdapter initAdapterData () {
        List<User> users = getUsers();
        return new MyCustomAdapter(getActivity(),users  );
    }

    private List<User> getUsers() {
        List<User> users;
        users = DataBaseHelper.getInstance(getActivity()).queryAllUsers();
        return users;
    }

}
