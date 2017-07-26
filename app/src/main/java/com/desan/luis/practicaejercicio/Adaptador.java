package com.desan.luis.practicaejercicio;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

/**
 * Created by luis on 27/06/17.
 */

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> implements View.OnClickListener{
    public List<Registro> registros;
    DatabaseReference base;
    Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacto,parent,false);
        context=parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (registros.get(position).getImagen().equals("default")){
            holder.imagen.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(registros.get(position).getImagen()).crossFade().centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.imagen);
        }
        holder.nombre.setText(registros.get(position).getNombre());
        holder.numero.setText(registros.get(position).getNumero());
        holder.direccion.setText(registros.get(position).getDireccion());
        holder.imagen.setId(position);
        holder.imagen.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    public Adaptador(List<Registro> registros){
        this.registros=registros;
    }

    @Override
    public void onClick(final View view) {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    base=firebaseDatabase.getReference("Practica");
    MaterialDialog dialog = new MaterialDialog.Builder(view.getContext())
                .title("Contacto")
                .content("Telefono: "+registros.get(view.getId()).getNumero())
                .negativeText("Eliminar")
                .positiveText("Actualizar")
                .neutralText("Cerrar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //Actualizar de firebase
                        base.child("Contacto").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i=0;
                                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                                Log.i("iterator-->",iterator.toString());
                                while (iterator.hasNext()){
                                    DataSnapshot reg=iterator.next();
                                    if(i==view.getId()) {
                                        //reg.child("nombre").getRef().setValue("NUEVONOMBRE");
                                        break;
                                    }
                                    i++;
                                }
                                Intent intent=new Intent(view.getContext(),Actualizar.class);
                                intent.putExtra("posicion",view.getId());
                                view.getContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        base.child("Contacto").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i=0;
                                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()){
                                    DataSnapshot reg=iterator.next();
                                    if(i==view.getId()) {
                                        reg.child("id").getRef().removeValue();
                                        reg.child("nombre").getRef().removeValue();
                                        reg.child("numero").getRef().removeValue();
                                        reg.child("direccion").getRef().removeValue();
                                        reg.child("imagen").getRef().removeValue();
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
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        notifyDataSetChanged();
                    }
                })
                .positiveColorRes(R.color.update)
                .negativeColorRes(R.color.delete)
                .neutralColorRes(R.color.cerrar)
                .theme(Theme.DARK)
                .maxIconSize(50)
                .titleColorRes(R.color.colorPrimary)
                .titleGravity(GravityEnum.CENTER)
                .show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen;
        public TextView nombre;
        public TextView numero;
        public TextView direccion;
        public ViewHolder(View itemView) {
            super(itemView);
            imagen=(ImageView)itemView.findViewById(R.id.imgContacto);
            nombre=(TextView) itemView.findViewById(R.id.nombre);
            numero=(TextView)itemView.findViewById(R.id.numero);
            direccion=(TextView)itemView.findViewById(R.id.direccion);
        }


    }
}
