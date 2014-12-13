package com.thehoick.evergreenflixq;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class LibrariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraries);

        Intent intent = getIntent();
        int position = getIntent().getIntExtra("position", 0);

        final Dvd dvd = MainActivity.mDvdList.get(position);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.libraries);

        TextView libraryDvdTitle = (TextView)findViewById(R.id.libraryDvdTitle);
        libraryDvdTitle.setText(dvd.getTitle());

        libraryDvdTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getEvergreenLink()));
                startActivity(intent);
            }
        });

        for (Library library : dvd.getLibaries()) {
            TableRow row = new TableRow(this);
            //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            //row.setLayoutParams(lp);

            TextView name = new TextView(this);
            TextView status = new TextView(this);
            name.setText(library.getName());
            status.setText(library.getStatus());

            row.addView(name);
            row.addView(status);
            tableLayout.addView(row);
        }
    }
}
