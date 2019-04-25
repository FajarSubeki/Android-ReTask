package com.example.re_task.model;


import java.io.Serializable;

import io.realm.RealmObject;

public class TugasModel extends RealmObject implements Serializable {

    private String id_user;
    private String id_tugas;
    private String nama_tugas;
    private String ket_tugas;
    private String tgl_target_tugas;
    private String tgl_pengumpulan_tugas;
    private String tgl_selesai_tugas;
    private String status;

    public TugasModel() {
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_tugas() {
        return id_tugas;
    }

    public void setId_tugas(String id_tugas) {
        this.id_tugas = id_tugas;
    }

    public String getNama_tugas() {
        return nama_tugas;
    }

    public void setNama_tugas(String nama_tugas) {
        this.nama_tugas = nama_tugas;
    }

    public String getKet_tugas() {
        return ket_tugas;
    }

    public void setKet_tugas(String ket_tugas) {
        this.ket_tugas = ket_tugas;
    }


    public String getTgl_pengumpulan_tugas() {
        return tgl_pengumpulan_tugas;
    }

    public void setTgl_pengumpulan_tugas(String tgl_pengumpulan_tugas) {
        this.tgl_pengumpulan_tugas = tgl_pengumpulan_tugas;
    }

    public String getTgl_target_tugas() {
        return tgl_target_tugas;
    }

    public void setTgl_target_tugas(String tgl_target_tugas) {
        this.tgl_target_tugas = tgl_target_tugas;
    }

    public String getTgl_selesai_tugas() {
        return tgl_selesai_tugas;
    }

    public void setTgl_selesai_tugas(String tgl_selesai_tugas) {
        this.tgl_selesai_tugas = tgl_selesai_tugas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return " "+id_user+"\n" +
                " "+nama_tugas+"\n" +
                " "+ket_tugas+"\n" +
                " "+tgl_pengumpulan_tugas+"\n" +
                " "+status+"\n" +
                " "+tgl_target_tugas;
    }

    public TugasModel(String id_user, String nama_tugas, String ket_tugas, String tgl_target_tugas, String tgl_pengumpulan_tugas, String status) {
        this.id_user = id_user;
        this.nama_tugas = nama_tugas;
        this.ket_tugas = ket_tugas;
        this.tgl_target_tugas = tgl_target_tugas;
        this.tgl_pengumpulan_tugas = tgl_pengumpulan_tugas;
        this.status = status;
    }


}
