package com.somerandomapps.yata;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.somerandomapps.yata.dialog.OnCloseListener;
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

    private static final String TAG = "MainActivity";

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
        TodoCreateDialog dialog = new TodoCreateDialog();
        dialog.setDismissListener(new OnCloseListener() {
            @Override
            public void onClose(DialogInterface dialogInterface) {
                Log.d(TAG, "onClose: closed dialog");
                updateList();
            }
        });
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
