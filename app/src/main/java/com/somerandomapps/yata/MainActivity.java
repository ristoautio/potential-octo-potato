package com.somerandomapps.yata;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.somerandomapps.yata.dialog.TodoCreateDialog;
import com.somerandomapps.yata.repository.AppDatabase;
import com.somerandomapps.yata.repository.TodoItem;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView tvNoItems;

    @ViewById
    ListView lvItems;

    List<TodoItem> list;

    @AfterViews
    protected void afterViews() {
        setSupportActionBar(toolbar);
        updateList();
    }

    private void updateList() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        list = db.todoItemDao().getAll();
        if (list.isEmpty()) {
            tvNoItems.setVisibility(View.VISIBLE);
        }

        TodoItemAdapter adapter = new TodoItemAdapter(list, getApplicationContext());
        lvItems.setAdapter(adapter);
    }

    @Click(R.id.fab)
    protected void fabClicked(View view) {
        DialogFragment dialog = new TodoCreateDialog();
        dialog.show(getFragmentManager(), "foobar2");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
