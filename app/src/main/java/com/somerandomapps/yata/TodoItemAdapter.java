package com.somerandomapps.yata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.somerandomapps.yata.repository.TodoItem;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> implements View.OnClickListener {

    Context context;
    List<TodoItem> todoItems;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }


    public TodoItemAdapter(List<TodoItem> list, Context applicationContext) {
        super(applicationContext, R.layout.todo_row_item, list);
        context = applicationContext;
        todoItems = list;
    }

    @Override
    public void onClick(View v) {
        System.out.println("click");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TodoItem todoItem = todoItems.get(position);
//        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_row_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.tvName);

//            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(AnimationUtils.loadAnimation(context, ));
//        lastPosition = position;

        viewHolder.txtName.setText(todoItem.getName());
//        viewHolder.txtType.setText(dataModel.getType());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);

        return convertView;
    }
}
