package com.desan.luis.practicaejercicio;

import android.*;
import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.add)
    FloatingActionButton button;

    @BindView(R.id.lista)
    RecyclerView view;
    DatabaseReference base;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<Registro> regis;
    boolean actualizar=false;
    @BindView(R.id.principal)
    SwipeRefreshLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        regis=new ArrayList<>();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/*
             ...
             */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        base=firebaseDatabase.getReference("Practica");

        base.child("Contacto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                regis.clear();
                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    DataSnapshot reg=iterator.next();
                    Log.i("Actualizando--->",reg.toString());
                   try {
                       int id = Integer.parseInt(reg.child("id").getValue().toString());
                       String imagen = reg.child("imagen").getValue().toString();
                       String nombre = reg.child("nombre").getValue().toString();
                       String telefono = reg.child("numero").getValue().toString();
                       String direccion = reg.child("direccion").getValue().toString();
                       regis.add(new Registro(id, imagen, nombre, telefono, direccion));
                   }catch (Exception e){}
                }
                Log.i("registros-->",regis.size()+"");
                view.setHasFixedSize(true);
                manager=new LinearLayoutManager(getApplicationContext());
                view.setLayoutManager(manager);
                adapter=new Adaptador(regis);
                view.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error-->",databaseError.getMessage().toString());
            }
        });

    }
    @OnClick(R.id.add)
    public void agregar(){
        DialogFragment dialogFragment=new AdapterDialog();
        dialogFragment.show(getFragmentManager(),"");
    }
}
