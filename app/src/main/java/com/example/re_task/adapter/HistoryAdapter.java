package com.example.re_task.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.re_task.HistoryActivity;
import com.example.re_task.R;
import com.example.re_task.ReadTaskActivity;
import com.example.re_task.model.TugasModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<TugasModel> tugasModels;
    private ArrayList<TugasModel> tugasModelsFiltered;
    public Context context;
    FirebaseDataListener listener;
    DatabaseReference databaseReference;

    public HistoryAdapter(ArrayList<TugasModel> tugasModels, Context context) {
        this.tugasModels = tugasModels;
        this.context = context;
        this.tugasModelsFiltered = tugasModels;
        this.listener = (HistoryActivity)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        String name = tugasModelsFiltered.get(i).getNama_tugas();
        String tgl = tugasModelsFiltered.get(i).getTgl_selesai_tugas();

        viewHolder.nama.setText(name);
        viewHolder.tglselesai.setText(tgl);

        viewHolder.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_history);
                dialog.show();

                CardView delete = dialog.findViewById(R.id.cvHapusHistory);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        new iOSDialogBuilder(context)
                                .setTitle("Hapus")
                                .setSubtitle("Yakin ingin menghapus ?")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Ya", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        listener.onDeleteData(tugasModels.get(i), i);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeListener("Tidak", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).build().show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return tugasModelsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    tugasModelsFiltered = tugasModels;
                } else {
                    ArrayList<TugasModel> filteredList = new ArrayList<>();
                    for (TugasModel row : tugasModels) {

                        if (row.getNama_tugas().toLowerCase().contains(charString.toLowerCase()) || row.getTgl_selesai_tugas().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    tugasModelsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tugasModelsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tugasModelsFiltered = (ArrayList<TugasModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView history;
        View mView;
        TextView nama, tglselesai;

        public ViewHolder(@NonNull View v) {
            super(v);

            mView = v;
            nama = mView.findViewById(R.id.tvNamaTugasHistory);
            history = mView.findViewById(R.id.cvHistory);
            tglselesai = mView.findViewById(R.id.tvTanggalSelesai);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setDetails(Context ctx, String name, String tgl)
        {
            nama = mView.findViewById(R.id.tvNamaTugasHistory);
            tglselesai = mView.findViewById(R.id.tvTanggalSelesai);

            nama.setText(name);
            tglselesai.setText(tgl);
        }

    }

    public interface FirebaseDataListener{
        void onDeleteData(TugasModel tugas, int position);
    }

}
