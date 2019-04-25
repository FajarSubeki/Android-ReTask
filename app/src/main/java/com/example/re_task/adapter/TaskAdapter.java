package com.example.re_task.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.re_task.AddTaskActivity;
import com.example.re_task.DetailTugasActivity;
import com.example.re_task.HistoryActivity;
import com.example.re_task.R;
import com.example.re_task.ReadTaskActivity;
import com.example.re_task.model.TugasModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TugasModel> tugasModels;
    private ArrayList<TugasModel> tugasModels2;
    Context context;
    FirebaseDataListener listener;
    DatabaseReference databaseReference;

    public TaskAdapter(ArrayList<TugasModel> tugasModels, Context context) {
        this.tugasModels = tugasModels;
        this.tugasModels2 = tugasModels;
        this.context = context;
        this.listener = (ReadTaskActivity)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nama, tanggal_target, tanggal;
        CardView read;
        ImageButton more;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.tvNamaTugasRead);
            tanggal_target = itemView.findViewById(R.id.tvTanggalTarget);
            tanggal = itemView.findViewById(R.id.tvTanggalRead);
            read = itemView.findViewById(R.id.card_view_read);
            more = itemView.findViewById(R.id.ibMore);
            relativeLayout = itemView.findViewById(R.id.relaread);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final String id = tugasModels.get(position).getId_tugas();
        String name = tugasModels.get(position).getNama_tugas();
        String tanggal_target = tugasModels.get(position).getTgl_target_tugas();
        String tanggal = tugasModels.get(position).getTgl_pengumpulan_tugas();

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = inFormat.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String tanggalTugas = outFormat.format(date);
        String subTanggalTugas = tanggalTugas.substring(0, 3);

        SimpleDateFormat inFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date tgl = null;
        try {
            //**
            tgl = inFormat2.parse(tanggal_target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat2 = new SimpleDateFormat("EEEE");
        String tanggalTarget = outFormat2.format(tgl);
        String subTanggalTarget = tanggalTarget.substring(0, 3);

        holder.nama.setText(name);
        holder.tanggal_target.setText(subTanggalTarget + ", " + tanggal_target);
        holder.tanggal.setText(subTanggalTugas + ", " + tanggal);

        Random rnd2 = new Random();
        int color = Color.argb(255, rnd2.nextInt(256), rnd2.nextInt(256), rnd2.nextInt(256));
        //holder.relativeLayout.setBackgroundColor(color);

        int[] colors = new int[2];
        colors[0] = getRandomColor();
        colors[1] = getRandomColor();


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gd.setGradientRadius(300f);
        gd.setCornerRadius(0f);
        holder.relativeLayout.setBackground(gd);
        holder.read.setCardBackgroundColor(color);

        Date d = new Date();
        final String dayOfTheWeek = inFormat.format(d);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if (tanggal.equals(dayOfTheWeek)) {
            databaseReference.child("tugasDb").child(id).child("status").setValue("F");
            databaseReference.child("tugasDb").child(id).child("tgl_selesai_tugas").setValue(dayOfTheWeek);


            //Alarm
            Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarmm);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Intent intent = new Intent(context, HistoryActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.drawable.ic_clipboarda);
            builder.setContentTitle(name);
            builder.setContentText(dayOfTheWeek);
            builder.setSound(alarmSound);
            builder.setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

            // Will display the notification in the notification bar
            notificationManager.notify(m, builder.build());

        }else{
            System.out.println(name + "BEDA");
        }

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_action);
                dialog.show();

                CardView update = dialog.findViewById(R.id.cvUpdate);
                CardView hapus = dialog.findViewById(R.id.cvHapus);
                CardView selesai = dialog.findViewById(R.id.cvSelesai);
                ImageView close = dialog.findViewById(R.id.ivClose);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        new iOSDialogBuilder(context)
                                .setTitle("Selesai")
                                .setSubtitle("Yakin tugas anda selesai sebelum tanggal pengumpulan ?")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Ya", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        databaseReference.child("tugasDb").child(id).child("status").setValue("F");
                                        databaseReference.child("tugasDb").child(id).child("tgl_selesai_tugas").setValue(dayOfTheWeek);
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

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        context.startActivity(AddTaskActivity.getActIntent((Activity) context).putExtra("data", tugasModels.get(position)));
                    }
                });

                hapus.setOnClickListener(new View.OnClickListener() {
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
                                        listener.onDeleteData(tugasModels.get(position), position);
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

        holder.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DetailTugasActivity.getActIntent((Activity) context).putExtra("data", tugasModels.get(position)));
            }
        });

    }


    @Override
    public int getItemCount() {
        return tugasModels.size();
    }

    public interface FirebaseDataListener{
        void onDeleteData(TugasModel tugas, int position);
    }


}
