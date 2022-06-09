package com.coding.smk2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListSiswaFragment extends Fragment {


    SiswaAdapter siswaAdapter;
    ListView listSiswa;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.list_siswa_fragment,
                container, false);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*mydb = new DBHelper(getActivity());
        ArrayList arrayList = mydb.getAllSiswa();
         */


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listSiswa = view.findViewById(R.id.list_siswa);
        ArrayList<Siswa> arrayList = new ArrayList<>();

        db.collection("siswa")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Siswa siswa = new Siswa(document.getId(),
                                        document.getString("nama"),
                                        document.getString("alamat"),
                                        document.getString("nohp"));
                                arrayList.add(siswa);
                                Log.e("aaaaa", siswa.getNama());
                            }

                            siswaAdapter = new SiswaAdapter(getContext(),0, arrayList);
                            listSiswa.setAdapter(siswaAdapter);
                            siswaAdapter.notifyDataSetChanged();

                        } else {
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });


        db.collection("siswa").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Listening", "Listen failed.", error);
                    return;
                }

                if (value != null) {
                    Log.d("Listening", "Current data: " + value.getQuery());
                    refreshList();
                } else {
                    Log.d("Listening", "Current data: null");
                }
            }
        });


    }

    public void refreshList() {
        /*ArrayList array_list = mydb.getAllSiswa();
        siswaAdapter.clear();
        siswaAdapter.addAll(array_list);
        siswaAdapter.notifyDataSetChanged();*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Siswa> arrayList = new ArrayList<>();

        db.collection("siswa")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Siswa siswa = new Siswa(document.getId(),
                                        document.getString("nama"),
                                        document.getString("alamat"),
                                        document.getString("nohp"));
                                arrayList.add(siswa);
                            }

                            siswaAdapter.clear();
                            siswaAdapter.addAll(arrayList);
                            siswaAdapter.notifyDataSetChanged();
                            Log.w("TAG", "refreshing table");
                        } else {
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}