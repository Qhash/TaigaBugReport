package de.techfunder.taigabugreport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.techfunder.taigabugreport.R;
import de.techfunder.taigabugreport.fragment.FeedbackFragment;
import de.techfunder.taigabugreport.pojo.BugReportParams;

/**
 * Created by ik on 02.04.2018.
 */

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taigabugreport_activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.taigabugreport_menu_botreport_bug);
        getSupportActionBar().setElevation(0);

        FeedbackFragment fragment = FeedbackFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getName()).commit();
    }

    public static Intent newInstance(Context context, BugReportParams params) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra("bug_report_params", params);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

