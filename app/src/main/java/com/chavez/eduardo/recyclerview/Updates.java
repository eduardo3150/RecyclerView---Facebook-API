package com.chavez.eduardo.recyclerview;

import java.io.Serializable;

/**
 * Created by Eduardo_Chavez on 26/2/2017.
 */

public class Updates implements Serializable {
    private int ID_Update;
    private String updateDetail;
    private double updateProgress;
    private String nombre;

    public Updates(int ID_Update, String updateDetail, double updateProgress, String nombre) {
        this.ID_Update = ID_Update;
        this.updateDetail = updateDetail;
        this.updateProgress = updateProgress;
        this.nombre = nombre;
    }

    public int getID_Update() {
        return ID_Update;
    }

    public void setID_Update(int ID_Update) {
        this.ID_Update = ID_Update;
    }

    public String getUpdateDetail() {
        return updateDetail;
    }

    public void setUpdateDetail(String updateDetail) {
        this.updateDetail = updateDetail;
    }

    public double getUpdateProgress() {
        return updateProgress;
    }

    public void setUpdateProgress(double updateProgress) {
        this.updateProgress = updateProgress;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
