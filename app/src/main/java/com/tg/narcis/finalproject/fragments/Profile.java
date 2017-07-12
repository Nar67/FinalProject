package com.tg.narcis.finalproject.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.User;
import com.tg.narcis.finalproject.database.DataBaseHelper;

import java.io.IOException;

public class Profile extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS = 2;
    SharedPreferences sp;
    private boolean canWeRead = false;
    Uri selectedImage;
    private Activity activity = getActivity();

    View rootView;
    ImageView profilePic;
    TextView usern, score, address;
    String addressString;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = getActivity().getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
        profilePic = (ImageView) rootView.findViewById(R.id.profile_image);
        usern = (TextView) rootView.findViewById(R.id.text_username);
        score = (TextView) rootView.findViewById(R.id.text_score);
        address = (TextView) rootView.findViewById(R.id.text_adress);
        profilePic.setImageResource(R.drawable.neutral_face);

        User user = getCurrentUser();
        Log.v("profile", user.printUser(user));
        usern.setText("Username: " + user.getUsername());
        String scor = user.getScore();
        if(scor == null) {
            score.setText("You have not played memory yet! (bugged no sé como arreglarlo)");
        }else score.setText("Max score: " + scor);
        address.setText("Address: (click to edit your address)");

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.checkReadExternalStoragePermissions(getActivity(),MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                Intent pickAnImage = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickAnImage.setType("image/*");

                startActivityForResult(pickAnImage, 2);
                Glide
                        .with(getActivity())
                        .load(selectedImage)
                        .placeholder(R.drawable.neutral_face)
                        .error(R.drawable.neutral_face)
                        .into(profilePic);
            }
        });


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog();
            }
        });




        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return rootView;
    }

    private void editDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Write your address here please:");

// Set up the input
        final EditText input = new EditText(getActivity());
        //input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addressString = input.getText().toString();
                address.setText("Address: " + addressString);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public User getCurrentUser() {
        String s = sp.getString("username", "-1");
        User user = DataBaseHelper.getInstance(getActivity()).queryUser(s);
        return user;
    }

    private boolean canWeRead(){
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED;
    }

    private void loadImageFromString (String imagePath){
        if(imagePath != null){
            Uri imageUri = Uri.parse(imagePath);
            loadImageFromUri(imageUri);
        }
    }

    private void loadImageFromUri(Uri imageUri) {
        try {
            profilePic.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent getContentIntent() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT <19){
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        return intent;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Como en este caso los 3 intents hacen lo mismo, si el estado es correcto recogemos el resultado
        //Aún así comprobamos los request code. Hay que tener total control de lo que hace nuestra app.
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode >= 1 && requestCode <= 3){
                //Líneas extras debido al usar action get content:
                data.getData();
                selectedImage = data.getData();
                String selectedImagePath = selectedImage.toString();

                if(canWeRead && requestCode == 2){
                    Log.v("PICK","Selected image uri" + selectedImage);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("imagePath",selectedImagePath );
                    editor.apply();
                }
                loadImageFromUri(selectedImage);
            }
        }else{
            Log.v("Result","Something happened");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    canWeRead = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canWeRead = false;
                }
                return;
            }
            case  MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    canWeRead = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canWeRead = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



}
