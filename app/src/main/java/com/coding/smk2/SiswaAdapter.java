package com.coding.smk2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SiswaAdapter extends ArrayAdapter<Siswa> {

    public SiswaAdapter(@NonNull Context context,
                        int resource,
                        @NonNull ArrayList<Siswa> objects) {
        super(context, resource, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Siswa siswa = getItem(position);
        View view = convertView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_siswa, null);
        }

        TextView id = view.findViewById(R.id.id);
        TextView nama = view.findViewById(R.id.nama);
        TextView alamat = view.findViewById(R.id.alamat);
        TextView nohp = view.findViewById(R.id.nohp);

        Button editbtn = (Button)view.findViewById(R.id.edit_button);
        Button deletebtn = (Button)view.findViewById(R.id.delete_button);

        id.setText(siswa.getId());
        nama.setText(siswa.getNama());
        alamat.setText(siswa.getAlamat());
        nohp.setText(siswa.getNohp());

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Data");

                final EditText input = new EditText(getContext());
                final EditText inpAlamat = new EditText(getContext());
                final EditText inpNohp = new EditText(getContext());

                final TextView namainp = new TextView(getContext());
                final TextView alamatinp = new TextView(getContext());
                final TextView nohpinp = new TextView(getContext());
                int textsize = 20;

                builder.setView(input);
                builder.setView(inpAlamat);
                builder.setView(inpNohp);
                builder.setView(namainp);
                builder.setView(alamatinp);
                builder.setView(nohpinp);

                input.setHint("Nama");
                inpAlamat.setHint("Alamat");
                inpNohp.setHint("Nomor Telepon");
                inpNohp.setInputType(InputType.TYPE_CLASS_PHONE);

                namainp.setText("Nama :");
                alamatinp.setText("Alamat :");
                nohpinp.setText("No Telepon :");

                namainp.setTextColor(getContext().getColor(R.color.black));
                alamatinp.setTextColor(getContext().getColor(R.color.black));
                nohpinp.setTextColor(getContext().getColor(R.color.black));

                namainp.setTextSize(textsize);
                alamatinp.setTextSize(textsize);
                nohpinp.setTextSize(textsize);

                LinearLayout layoutdialog = new LinearLayout(getContext());
                LinearLayout parentView = new LinearLayout(getContext());
                layoutdialog.setOrientation(LinearLayout.VERTICAL);
                parentView.setOrientation(LinearLayout.VERTICAL);

                layoutdialog.addView(namainp);
                layoutdialog.addView(input);

                layoutdialog.addView(alamatinp);
                layoutdialog.addView(inpAlamat);

                layoutdialog.addView(nohpinp);
                layoutdialog.addView(inpNohp);

                layoutdialog.setPadding(40,10,40,0);

                builder.setView(layoutdialog);

                parentView.addView(layoutdialog);
                builder.setView(parentView);

                input.setText(nama.getText().toString().trim());
                inpAlamat.setText(alamat.getText().toString().trim());
                inpNohp.setText(nohp.getText().toString().trim());

                builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*String editnama = input.getText().toString().trim();
                        String editalamat = inpAlamat.getText().toString().trim();
                        String editnohp = inpNohp.getText().toString().trim();
                        Toast.makeText(getContext(),editnama, Toast.LENGTH_SHORT).show();
                        boolean isUpdate = mydb.updateData(id.getText().toString().trim(), editnama, editalamat, editnohp);
                        if(isUpdate) {
                            notifyDataSetChanged(); //<-- useless piece of shiy
                            Toast.makeText(getContext(),"Data Tersimpan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                        }*/

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> siswaUpdate = new HashMap<>();
                        siswaUpdate.put("nama", input.getText().toString().trim());
                        siswaUpdate.put("alamat", inpAlamat.getText().toString().trim());
                        siswaUpdate.put("nohp", inpNohp.getText().toString().trim());

                        db.collection("siswa").document(id.getText().toString().trim())
                                .set(siswaUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.w("TAG", "data siswa dengan ID = " + id.getText().toString() + "berhasil diubah = " + input.getText().toString() );
                                        Toast.makeText(getContext(),"Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "data siswa gagal ditambahkan", e);
                                        Toast.makeText(getContext(),"Data Gagal Diubah", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Hapus Data?");
                builder.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        /*boolean isDeleted = mydb.deleteData(id.getText().toString().trim());
                        if(isDeleted) {
                            notifyDataSetChanged(); //<-- useless piece of shiy
                            Toast.makeText(getContext(),"Data Terhapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
                        }*/

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("siswa")
                                .document(id.getText().toString().trim())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(),"Data Terhapus", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
                builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }
}