package com.desan.luis.practicaejercicio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Actualizar extends AppCompatActivity {
    @BindView(R.id.update)
    Button button;
    @BindView(R.id.updatenom)
    EditText nom;

    @BindView(R.id.updatedireccion)
    EditText dir;

    @BindView(R.id.updatetelefono)
    EditText tel;

    @BindView(R.id.updateimagen)
    EditText img;

    Bundle bundle;
    int position;
    RecyclerView view;
    DatabaseReference base;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        bundle=getIntent().getExtras();
        position=bundle.getInt("posicion");
        Log.i("position:->",position+"");
        ButterKnife.bind(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        base=firebaseDatabase.getReference("Practica");
        base.child("Contacto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    DataSnapshot reg=iterator.next();
                    if(i==position) {
                        String imagen = reg.child("imagen").getValue().toString();
                        String nombre = reg.child("nombre").getValue().toString();
                        String telefono = reg.child("numero").getValue().toString();
                        String direccion = reg.child("direccion").getValue().toString();
                        if(!imagen.equals("default")){
                            img.setText(imagen);
                        }
                        nom.setText(nombre);
                        tel.setText(telefono);
                        dir.setText(direccion);
                        Log.i("valores-->",reg.toString());
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @OnClick(R.id.update)
    public void actualizar(){
        final String imagen = img.getText().toString();
        final String nombre = nom.getText().toString();
        final String telefono = tel.getText().toString();
        final String direccion = dir.getText().toString();

        base.child("Contacto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                Log.i("iterator-->",iterator.toString());
                while (iterator.hasNext()){
                    DataSnapshot reg=iterator.next();
                    if(i==position) {
                        reg.child("nombre").getRef().setValue(nombre);
                        reg.child("direccion").getRef().setValue(direccion);
                        reg.child("numero").getRef().setValue(telefono);
                        if (imagen.equals("")){
                            reg.child("imagen").getRef().setValue("default");
                        }
                        else{
                            reg.child("imagen").getRef().setValue(imagen);
                        }

                        break;
                    }
                    i++;
                }
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error! ",databaseError.getMessage().toString());
            }
        });

    }
}
