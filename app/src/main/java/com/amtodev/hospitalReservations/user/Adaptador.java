package com.amtodev.hospitalReservations.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Adaptador implements Serializable, Parcelable {

    private String id_vehiculo;
    private String marca;
    private String modelo;
    private String anio;
    private String usuario;

    public Adaptador(){}

    protected Adaptador(Parcel in) {
        id_vehiculo = in.readString();
        marca = in.readString();
        modelo = in.readString();
        anio = in.readString();
        usuario = in.readString();
    }

    public static final Creator<Adaptador> CREATOR = new Creator<Adaptador>() {
        @Override
        public Adaptador createFromParcel(Parcel in) {
            return new Adaptador(in);
        }

        @Override
        public Adaptador[] newArray(int size) {
            return new Adaptador[size];
        }
    };

    public String getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(String id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Adaptador(String id_vehiculo, String marca, String modelo, String anio, String usuario) {
        this.id_vehiculo = id_vehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Adaptador{" +
                "id_vehiculo='" + id_vehiculo + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio='" + anio + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_vehiculo);
        dest.writeString(marca);
        dest.writeString(modelo);
        dest.writeString(anio);
        dest.writeString(usuario);
    }
}
