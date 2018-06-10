package com.ogbizi.android_library;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.ogbizi.android_editable_textview.AsyncEditTextBehavior;
import com.ogbizi.android_editable_textview.EditTextView;
import com.ogbizi.android_editable_textview.PatternEditTextBehavior;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditTextView textView1 = findViewById(R.id.editTextView1);
        textView1.setBehavior(new PatternEditTextBehavior("%s (format)"));

        EditTextView textView2 = findViewById(R.id.editTextView2);
        textView2.setBehavior(new AsyncEditTextBehavior());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void runTextColorAnimation(View v) {
        EditTextView textView = findViewById(R.id.editTextView1);
        Integer[] colors = new Integer[]{
                Color.rgb(255, 0, 0),
                Color.rgb(0, 255, 0),
                Color.rgb(0, 0, 255),
                Color.rgb(0, 0, 0)
        };
        textView.animateTextColor(1000, colors);
    }
}
