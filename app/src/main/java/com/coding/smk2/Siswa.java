package com.coding.smk2;

public class Siswa {
    private String id;
    private String nama;
    private String alamat;
    private String nohp;

    public Siswa(String id, String nama, String alamat, String nohp){
        this.setId(id);
        this.setNama(nama);
        this.setAlamat(alamat);
        this.setNohp(nohp);
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


    public String getAlamat() { return alamat; }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}