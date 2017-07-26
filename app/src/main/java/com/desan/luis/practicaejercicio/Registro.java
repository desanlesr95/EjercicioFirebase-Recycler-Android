package com.desan.luis.practicaejercicio;

/**
 * Created by luis on 27/06/17.
 */

public class Registro {
    private int id;
    private String imagen;
    private String nombre;
    private String numero;
    private String direccion;

    public Registro(int id, String imagen, String nombre, String numero, String direccion) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.numero = numero;
        this.direccion = direccion;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
