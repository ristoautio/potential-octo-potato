package com.somerandomapps.yata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private SharedPreferences sharedPref;

    @ViewById
    protected Toolbar toolbar;

    @ViewById
    protected TextView tvCurrentRetentionDuration;

    private Resources resources;

    public enum Duration {
        HOUR(1),
        DAY(24),
        WEEK(24 * 7),
        MONTH(24 * 7 * 4);

        private int hours;
        private static int defaultValue;

        Duration(int hours) {
            this.hours = hours;
        }

        public static Duration findBy(int i) {
            for (Duration duration : Duration.values()) {
                if (duration.hours == i) {
                    return duration;
                }
            }
            throw new Resources.NotFoundException();
        }

        public static int getDefaultValue() {
            return Duration.DAY.hours;
        }
    };

    @AfterViews
    protected void afterViews() {
        Context context = getApplicationContext();
        resources = context.getResources();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeButtonEnabled(true);
        }

        int retentionSpanInHours = sharedPref.getInt(getString(R.string.saved_retention_span_key), Duration.getDefaultValue());
        updateSelectedValue(retentionSpanInHours);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: click " + item.getItemId());
        if( item.getItemId() == android.R.id.home){
            Log.d(TAG, "onOptionsItemSelected: go home");
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSelectedValue(int hours) {
        String name = "dialog_duration_select_value_" + Duration.findBy(hours).toString();
        int resId = resources.getIdentifier(name, "string", getPackageName());
        tvCurrentRetentionDuration.setText(getString(resId));
    }

    @Click(R.id.rlOption)
    protected void clickedRetentionSpanOption() {
        final Duration[] durationValues = Duration.values();
        CharSequence[] res = new CharSequence[durationValues.length];
        for (int i = 0; i < durationValues.length; i++) {
            int resId = resources.getIdentifier("dialog_duration_select_value_" + durationValues[i].toString(), "string", getPackageName());
            res[i] = getString(resId);
        }


        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_duration_select_title))
                .setItems(res, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.saved_retention_span_key), durationValues[which].hours);
                        editor.apply();
                        updateSelectedValue(durationValues[which].hours);
                    }
                }).show();

    }
}
