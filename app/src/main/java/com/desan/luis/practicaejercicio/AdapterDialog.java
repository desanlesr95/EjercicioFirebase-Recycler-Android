package com.desan.luis.practicaejercicio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterDialog extends DialogFragment {
    @BindView(R.id.nuevonombre)
    EditText nombre;
    @BindView(R.id.nuevonumero)
    EditText numero;
    @BindView(R.id.nuevodireccion)
    EditText dir;
    View view;
    DatabaseReference base;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view=getActivity().getLayoutInflater().inflate(R.layout.dialog,null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setView(view);
        dialog.setTitle("Nuevo Contacto");
        dialog.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Mandar los datos a firebase
                Random id=new Random();
                Log.i("nombre->>",nombre.getText().toString());
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                base=firebaseDatabase.getReference("Practica");
                Registro contacto=new Registro(id.nextInt(),"default",nombre.getText().toString(),numero.getText().toString(),dir.getText().toString());
                base.child("Contacto").push().setValue(contacto);

            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return dialog.create();
    }
}
