package com.example.re_task.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePicker extends AppCompatEditText implements DatePickerDialog.OnDateSetListener{

    private Date date;

    public DatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes();
    }

    public DatePicker(Context context) {
        super(context);
        setAttributes();
    }

    private void setAttributes() {

        setHint("Pilih Tanggal");
        setGravity(Gravity.LEFT | Gravity.CENTER);
        setFocusable(false);
        // setTextSize(18);
        //  setPadding(10, 10, 10, 10);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if (date != null) {
                    calendar.setTime(date);
                }
                DatePickerDialog datePicker = new DatePickerDialog(
                        DatePicker.this.getContext(), DatePicker.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);

                // datePicker.setCanceledOnTouchOutside(true);
                datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();

                        }
                    }
                });
                datePicker.show();
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();

        setDate(date);
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = date;
            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = newformat.format(date);
            setText(formattedDate);
        } else {

            setText("");
        }
    }

    public Date getDate() {
        return date;
    }

}
