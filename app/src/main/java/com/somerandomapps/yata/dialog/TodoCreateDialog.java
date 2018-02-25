package com.somerandomapps.yata.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.somerandomapps.yata.R;
import com.somerandomapps.yata.repository.AppDatabase;
import com.somerandomapps.yata.repository.TodoItem;
import org.androidannotations.annotations.EFragment;

import java.text.DateFormat;
import java.util.Calendar;

@EFragment(R.layout.todo_create)
public class TodoCreateDialog extends DialogFragment {
    private View view;

    private static final String TAG = "TODO Fragment";
    private Calendar myCalendar = Calendar.getInstance();
    private Boolean useDeadline = false;
    private TextView tvDeadline;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.todo_create, null);
        tvDeadline = view.findViewById(R.id.tvDeadline);
        tvDeadline.setVisibility(View.INVISIBLE);

        final Switch deadlineSwitch= view.findViewById(R.id.swHasDeadline);
        deadlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    Log.d(TAG, "onDateSet: Date set " + dayOfMonth);
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    int date = myCalendar.get(Calendar.DAY_OF_MONTH);
                    Log.d(TAG, "onClick: date .---->> " + date);
                    String currentDateTimeString = DateFormat.getDateInstance().format(myCalendar.getTime());
                    tvDeadline.setText(currentDateTimeString);
                    useDeadline = true;
                    tvDeadline.setVisibility(View.VISIBLE);
                }

            };

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showDatePicker(buttonView);
                } else {
                    useDeadline = false;
                    tvDeadline.setVisibility(View.INVISIBLE);
                }
            }

            private void showDatePicker(CompoundButton buttonView) {
                DatePickerDialog datePicker = new DatePickerDialog(buttonView.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePicker.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        deadlineSwitch.setChecked(false);
                        useDeadline = false;
                        tvDeadline.setVisibility(View.INVISIBLE);
                    }
                });

                datePicker.show();
            }
        });

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.item_create_title)
                .setPositiveButton(R.string.item_create_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText valueView = view.findViewById(R.id.etName); //here
                        String name = valueView.getText().toString();
                        AppDatabase db = AppDatabase.getAppDatabase(null);
                        TodoItem item = new TodoItem();
                        item.setName(name);
                        item.setHasDeadline(useDeadline);
                        if (useDeadline) {
                            item.setDeadline(myCalendar.getTime());
                        }
                        db.todoItemDao().insertItemWithName(item);
                    }
                })
                .setNegativeButton(R.string.item_create_cancel, null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}