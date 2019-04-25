package com.example.re_task;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.re_task.adapter.TaskAdapter;
import com.example.re_task.base.BaseActivity;
import com.example.re_task.helper.PrefManager;
import com.example.re_task.model.TugasModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadTaskActivity extends BaseActivity implements TaskAdapter.FirebaseDataListener{

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private ArrayList<TugasModel> daftarTugas;
    TextView text, count;
    ImageView ivnull;
    String iduser;
    RelativeLayout rela, relacount;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_task);

        super.showLoading();
        super.setUpActionBar("Daftar Tugas");
        init();
    }

    private void init()
    {
        prefManager = new PrefManager(getApplicationContext());
        rela = findViewById(R.id.relaReadSwipe);
        text = findViewById(R.id.idText);
        ivnull = findViewById(R.id.ivNull);
        count = findViewById(R.id.tvCount);
        rvView = findViewById(R.id.recyclerReadTask);
        relacount = findViewById(R.id.relaCount);
        rvView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rvView.setLayoutManager(manager);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("tugasDb")
                .orderByChild("status")
                .equalTo("N")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ReadTaskActivity.super.dismissLoading();

                daftarTugas = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){

                    TugasModel tugas = noteDataSnapshot.getValue(TugasModel.class);
                    tugas.setId_tugas(noteDataSnapshot.getKey());

                    relacount.setVisibility(View.VISIBLE);
                    daftarTugas.add(tugas);

                }

                adapter = new TaskAdapter(daftarTugas, ReadTaskActivity.this);
                rvView.setAdapter(adapter);

                if (adapter.getItemCount() < 1){
                    text.setVisibility(View.VISIBLE);
                    ivnull.setVisibility(View.VISIBLE);
                }else{
                    text.setVisibility(View.GONE);
                    ivnull.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ReadTaskActivity.super.dismissLoading();
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

        database.child("tugasDb")
                .orderByChild("status")
                .equalTo("N")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int size = (int) dataSnapshot.getChildrenCount();
                        count.setText(String.valueOf(size));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onDeleteData(TugasModel tugas, int position) {
        if (database != null){
            database.child("tugasDb").child(tugas.getId_tugas()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ReadTaskActivity.this, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
