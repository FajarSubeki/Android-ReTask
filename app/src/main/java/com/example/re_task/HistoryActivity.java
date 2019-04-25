package com.example.re_task;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.re_task.adapter.HistoryAdapter;
import com.example.re_task.adapter.TaskAdapter;
import com.example.re_task.base.BaseActivity;
import com.example.re_task.model.TugasModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity implements HistoryAdapter.FirebaseDataListener {

    private DatabaseReference database;
    private RecyclerView rvView;
    HistoryAdapter historyAdapter;
    private ArrayList<TugasModel> historyTugas;
    private RecyclerView.LayoutManager mManager;
    SearchView searchView;
    ImageView history;
    TextView nullText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        super.showLoading();
        super.setUpActionBar("Riwayat Tugas");
        init();
    }

    private void init()
    {
        rvView = findViewById(R.id.recyclerHistory);
        history = findViewById(R.id.ivNullHistory);
        nullText = findViewById(R.id.idTexHistory);

        rvView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvView.setLayoutManager(mManager);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("tugasDb")
                .orderByChild("status")
                .equalTo("F")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HistoryActivity.super.dismissLoading();
                        historyTugas = new ArrayList<>();
                        for (DataSnapshot dataSnapshol : dataSnapshot.getChildren()){

                            TugasModel tugasModel = dataSnapshol.getValue(TugasModel.class);
                            tugasModel.setId_tugas(dataSnapshol.getKey());

                            historyTugas.add(tugasModel);

                        }

                        historyAdapter = new HistoryAdapter(historyTugas, HistoryActivity.this);
                        rvView.setAdapter(historyAdapter);

                        if (historyAdapter.getItemCount() < 1){
                            history.setVisibility(View.VISIBLE);
                            nullText.setVisibility(View.VISIBLE);
                        }else{
                            history.setVisibility(View.GONE);
                            nullText.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        HistoryActivity.super.dismissLoading();
                        System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                historyAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                historyAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDeleteData(TugasModel tugas, int position) {
        if (database != null){
            database.child("tugasDb").child(tugas.getId_tugas()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(HistoryActivity.this, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
