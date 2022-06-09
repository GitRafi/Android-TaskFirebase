package com.coding.smk2;

public class Siswa {
    private String id;
    private String nama;

    public Siswa(String id, String nama){
        this.setId(id);
        this.setNama(nama);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}