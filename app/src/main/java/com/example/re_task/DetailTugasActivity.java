package com.example.re_task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.re_task.base.BaseActivity;
import com.example.re_task.model.TugasModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailTugasActivity extends BaseActivity {

    TextView namatugas, keterangantugas, tanggalpengumpulantugas, tanggaltargettugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        super.setUpActionBar("Detail Tugas");

        init();
    }

    private void init()
    {
        namatugas = findViewById(R.id.tvNamaTugasDetail);
        keterangantugas = findViewById(R.id.tvKet);
        tanggalpengumpulantugas = findViewById(R.id.tvPengumpulanTugas);
        tanggaltargettugas = findViewById(R.id.tvTargetDetail);

        TugasModel tugas = (TugasModel) getIntent().getSerializableExtra("data");
        if (tugas!=null)
        {

            if (keterangantugas.getText().toString().matches(""))
            {
                keterangantugas.setText("Tidak ada keterangan");
            }

            //Date Kumpul
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = inFormat.parse(tugas.getTgl_pengumpulan_tugas());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String tanggalTugas = outFormat.format(date);

            //Date 2
            Date tgl = null;
            try {
                tgl = inFormat.parse(tugas.getTgl_target_tugas());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat outFormat2 = new SimpleDateFormat("EEEE");
            String tanggalTugas2 = outFormat2.format(tgl);

            namatugas.setText(tugas.getNama_tugas());
            keterangantugas.setText(tugas.getKet_tugas());
            tanggalpengumpulantugas.setText(tanggalTugas + ", " + tugas.getTgl_pengumpulan_tugas());
            tanggaltargettugas.setText(tanggalTugas2 + ", " + tugas.getTgl_target_tugas());

        }
    }

    public static Intent getActIntent(Activity activity)
    {
        return new Intent(activity, DetailTugasActivity.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
