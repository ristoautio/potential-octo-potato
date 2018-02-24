package com.somerandomapps.yata.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.somerandomapps.yata.R;
import com.somerandomapps.yata.repository.AppDatabase;
import com.somerandomapps.yata.repository.TodoItem;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.todo_create)
public class TodoCreateDialog extends DialogFragment {
    private View view;

    private static final String TAG = "TODO Fragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.todo_create, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage("FOOBAR")
                .setPositiveButton("POSSS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        System.out.println("nope");
                        EditText valueView = view.findViewById(R.id.etName); //here
                        Log.d(TAG, "onClick: testttt --> " + valueView.getText());
                        String name = valueView.getText().toString();
                        AppDatabase db = AppDatabase.getAppDatabase(null);
                        TodoItem item = new TodoItem();
                        item.setName(name);
                        db.todoItemDao().insertItemWithName(item);
                        return;
                    }
                })
                .setNegativeButton("CANC", null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}