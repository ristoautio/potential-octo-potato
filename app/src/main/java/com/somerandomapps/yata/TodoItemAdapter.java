package com.somerandomapps.yata;

import android.content.Context;
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
import java.util.Date;
import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> implements View.OnClickListener {

    private static final String TAG = "Todoadapter";

    Context context;
    List<TodoItem> todoItems;

    private static class ViewHolder {
        TextView txtName;
        TextView tvCompeletDate;
        CheckBox cbDone;
    }

    public TodoItemAdapter(List<TodoItem> list, Context applicationContext) {
        super(applicationContext, R.layout.todo_row_item, list);
        context = applicationContext;
        todoItems = list;
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
            viewHolder.tvCompeletDate = convertView.findViewById(R.id.tvCompeletDate);
            viewHolder.cbDone = convertView.findViewById(R.id.cbDone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //FIXME dateformat from settings
        viewHolder.txtName.setText(todoItem.getName());
//        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
//        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        viewHolder.tvCompeletDate.setText(!todoItem.isDone() ? "" : DateFormat.getDateInstance().format(todoItem.getDoneAt()));
//        viewHolder.tvCompeletDate.setText(!todoItem.isDone() ? "" : dateFormat.format(todoItem.getDoneAt()));


        //        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(AnimationUtils.loadAnimation(context, ));
//        lastPosition = position;


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
                            viewHolder.tvCompeletDate.setText(DateFormat.getDateInstance().format(todoItem.getDoneAt()));
                        } else {
                            item.setDone(false);
                            viewHolder.tvCompeletDate.setText("");
                        }

                        database.todoItemDao().updateItem(item);
                    }
                }
            }
        });
        return convertView;
    }
}
