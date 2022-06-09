package com.coding.smk2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InsertSiswaFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_fragment, container, false);
        return view;
    }

    Button btnSave;
    EditText editTextNama;
    EditText editTextId;
    EditText editTextAlamat;
    EditText editTextNohp;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnSave = view.findViewById(R.id.btn_save);
        editTextNama = view.findViewById(R.id.etext_nama);
        editTextId = view.findViewById(R.id.text_id);
        editTextAlamat = view.findViewById(R.id.etext_alamat);
        editTextNohp = view.findViewById(R.id.etext_nohp);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextNama.getText().toString()) || TextUtils.isEmpty(editTextId.getText().toString()) || TextUtils.isEmpty(editTextAlamat.getText().toString()) || TextUtils.isEmpty(editTextNohp.getText().toString())) {
                    if(TextUtils.isEmpty(editTextNama.getText().toString())) {
                        editTextNama.setError("Nama Kosong");
                    }
                    if(TextUtils.isEmpty(editTextId.getText().toString())){
                        editTextId.setError("ID Kosong");
                    }
                    if(TextUtils.isEmpty(editTextAlamat.getText().toString())){
                        editTextAlamat.setError("Alamat Kosong");
                    }
                    if(TextUtils.isEmpty(editTextNohp.getText().toString())){
                        editTextNohp.setError("Nomor Telepon Kosong");
                    }

                } else {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String id = db.collection("siswa").document().getId();
                    Map<String, Object> siswa = new HashMap<>();
                    siswa.put("nama", editTextNama.getText().toString().trim());
                    siswa.put("alamat", editTextAlamat.getText().toString().trim());
                    siswa.put("nohp", editTextNohp.getText().toString().trim());

                    DocumentReference theDocument = db.collection("siswa").document(editTextId.getText().toString().trim());

                    theDocument.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (!documentSnapshot.exists() || documentSnapshot == null) {
                                        theDocument.set(siswa)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.w("TAG", "data siswa dengan ID = " + editTextId.getText().toString() + "berhasil ditambahkan");
                                                        Toast.makeText(getContext(),"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "data siswa gagal ditambahkan", e);
                                                        Toast.makeText(getContext(),"Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Log.w("TAG", "data siswa sudah ada");
                                        Toast.makeText(getContext(),"ID Sudah digunakan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "gagal memvalidasi data", e);
                                    Toast.makeText(getContext(),"Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                                }
                            });



//                    mydb.insertSiswa(editTextNama.getText().toString().trim(), editTextId.getText().toString().trim(), editTextAlamat.getText().toString().trim(), editTextNohp.getText().toString().trim());
//
//                    Log.w("nama", editTextNama.getText().toString().trim());
//                    Log.w("id", editTextId.getText().toString().trim());
//                    Log.w("alamat", editTextAlamat.getText().toString().trim());
//                    Log.w("nohp", editTextNohp.getText().toString().trim());

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    ListSiswaFragment fragm = (ListSiswaFragment)
                            fm.findFragmentById(R.id.fragment_list_siswa);
                    fragm.refreshList();
                }

            }
        });
    }

}