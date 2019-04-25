package com.example.re_task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.re_task.base.BaseActivity;
import com.example.re_task.helper.PrefManager;
import com.example.re_task.model.TugasModel;
import com.example.re_task.util.DateDifference;
import com.example.re_task.util.DatePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.realm.Realm;

public class AddTaskActivity extends BaseActivity {

    MaterialEditText namatugas, kettugas;
    DatePicker tanggal_target, tanggal;
    Button simpan;
    Realm realm;
    PrefManager prefManager;
    String idUser;

    // variable yang merefers ke Firebase Realtime Database
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        super.setUpActionBar("Tugas Baru");

        init();
    }

    private void init(){
        namatugas = findViewById(R.id.etNamaTugas);
        kettugas = findViewById(R.id.etKetTugas);
        tanggal_target = findViewById(R.id.dpTarget);
        tanggal = findViewById(R.id.dpTanggalSelesai);
        simpan = findViewById(R.id.btnSimpanTugas);
        final String status = "F";
        final String status2 = "N";

        final TugasModel tugas = (TugasModel) getIntent().getSerializableExtra("data");
        if (tugas != null){
            namatugas.setText(tugas.getNama_tugas());
            kettugas.setText(tugas.getKet_tugas());
            tanggal_target.setText(tugas.getTgl_target_tugas());
            tanggal.setText(tugas.getTgl_pengumpulan_tugas());
            simpan.setText("Ubah Tugas");
            super.setUpActionBar("Ubah Tugas");
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddTaskActivity.super.showLoading();
                    tugas.setNama_tugas(namatugas.getText().toString());
                    tugas.setKet_tugas(kettugas.getText().toString());
                    tugas.setTgl_pengumpulan_tugas(tanggal.getText().toString());
                    tugas.setTgl_target_tugas(tanggal_target.getText().toString());
                    tugas.setStatus(status2);
                    updateTugas(tugas);
                }
            });
        }else {
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddTaskActivity.super.showLoading();

                    prefManager = new PrefManager(getApplicationContext());
                    idUser = prefManager.getUserId();

                    if (isEmpty(namatugas.getText().toString())) {
                        Snackbar.make(findViewById(R.id.btnSimpanTugas), "Nama tugas kosong", Snackbar.LENGTH_LONG).show();
                        AddTaskActivity.super.dismissLoading();
                    }else if( isEmpty(tanggal.getText().toString()))
                    {
                        Snackbar.make(findViewById(R.id.btnSimpanTugas), "Tanggal Pengumpulan tugas kosong", Snackbar.LENGTH_LONG).show();
                        AddTaskActivity.super.dismissLoading();
                    }
                    else if((DateDifference.betweenDates(tanggal.getText().toString(), tanggal_target.getText().toString())+1)>1){
                        Snackbar.make(findViewById(R.id.btnSimpanTugas), "Inputan tanggal tidak sesuai", Snackbar.LENGTH_LONG).show();
                        AddTaskActivity.super.dismissLoading();
                    }else{
                        submitTugas(new TugasModel(idUser, namatugas.getText().toString(), kettugas.getText().toString(), tanggal_target.getText().toString(), tanggal.getText().toString(), status2));
                        //process();
                    }

//                        Toast.makeText(getApplicationContext(),"Sesuai", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void process()
    {
        namatugas = findViewById(R.id.etNamaTugas);
        kettugas = findViewById(R.id.etKetTugas);
        tanggal_target = findViewById(R.id.dpTarget);
        tanggal = findViewById(R.id.dpTanggalSelesai);

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        TugasModel data = realm.createObject(TugasModel.class);
        data.setNama_tugas(namatugas.getText().toString());
        data.setKet_tugas(kettugas.getText().toString());
        data.setTgl_target_tugas(tanggal_target.getText().toString());
        data.setTgl_selesai_tugas(tanggal.getText().toString());
        realm.copyFromRealm(data);
        realm.commitTransaction();
        realm.close();
        Log.d("STATUS : ", "Berhasil");
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void reset()
    {
        namatugas.setText("");
        kettugas.setText("");
        tanggal.setText("");
        tanggal_target.setText("");
    }

    private void submitTugas(TugasModel tugasModel){

        databaseReference.child("tugasDb").push().setValue(tugasModel).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reset();
                Snackbar.make(findViewById(R.id.btnSimpanTugas), "Berhasil Menambahkan Tugas", Snackbar.LENGTH_LONG).show();
                AddTaskActivity.super.dismissLoading();
            }
        });

    }

    private void updateTugas(TugasModel tugasModel)
    {
        databaseReference.child("tugasDb")
                .child(tugasModel.getId_tugas())
                .setValue(tugasModel)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.btnSimpanTugas), "Berhasil Mengubah Data", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                        AddTaskActivity.super.dismissLoading();
                    }
                });
    }

    public static Intent getActIntent(Activity activity)
    {
        return new Intent(activity, AddTaskActivity.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
