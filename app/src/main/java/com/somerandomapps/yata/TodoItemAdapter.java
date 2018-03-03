package com.somerandomapps.yata;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.somerandomapps.yata.repository.AppDatabase;
import com.somerandomapps.yata.repository.TodoItem;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> implements View.OnClickListener {

    private static final String TAG = "Todoadapter";

    Context context;
    List<TodoItem> todoItems;

    private static class ViewHolder {
        TextView txtName;
        TextView tvDeadlinedate;
        CheckBox cbDone;
    }

    public TodoItemAdapter(List<TodoItem> list, Context applicationContext) {
        super(applicationContext, R.layout.todo_row_item, list);
        context = applicationContext;
        todoItems = list;
        needUpdate();
    }

    @Override
    public void onClick(View v) { }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final TodoItem todoItem = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_row_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.tvName);
            viewHolder.tvDeadlinedate = convertView.findViewById(R.id.tvDeadlinedate);
            viewHolder.cbDone = convertView.findViewById(R.id.cbDone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //FIXME dateformat from settings
        viewHolder.txtName.setText(todoItem.getName());
        viewHolder.tvDeadlinedate.setText(!todoItem.isHasDeadline() ? "" : DateFormat.getDateInstance().format(todoItem.getDeadline()));
        if (todoItem.isDone()) {
            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        viewHolder.cbDone.setOnCheckedChangeListener(null);
        viewHolder.cbDone.setChecked(todoItem.isDone());

        viewHolder.cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                AppDatabase database = AppDatabase.getAppDatabase(context);
                for (TodoItem item : todoItems) {
                    if (item.getId() == todoItem.getId()) {
                        if(isChecked) {
                            item.setDone(true);
                            item.setDoneAt(new Date());
                            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            item.setDone(false);
                            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }

                        database.todoItemDao().updateItem(item);
                    }
                }
                needUpdate();
            }
        });
        return convertView;
    }

    private void needUpdate() {
        Log.d(TAG, "needUpdate: do update");
        Collections.sort(todoItems, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() != o2.isDone()) {
                    return o1.isDone() ? 1 : -1;
                }

                if(o1.isHasDeadline() != o2.isHasDeadline()){
                    return o1.isHasDeadline() ? 1 : -1;
                } else if ( o1.isHasDeadline() ){
                    o1.getDeadline().compareTo(o2.getDeadline());
                } else {
                    o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }


                return o1.getName().compareTo(o2.getName());
            }
        });
        notifyDataSetChanged();
    }


}
