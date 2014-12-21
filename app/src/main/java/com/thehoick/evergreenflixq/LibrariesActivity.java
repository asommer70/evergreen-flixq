package com.thehoick.evergreenflixq;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class LibrariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraries);

        int position = getIntent().getIntExtra("position", 0);

        final Dvd dvd = MainActivity.mDvdList.get(position);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.libraries);

        final TextView libraryDvdTitle = (TextView)findViewById(R.id.libraryDvdTitle);
        libraryDvdTitle.setText(dvd.getTitle());

        libraryDvdTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Flash a transparency over the View.
                AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
                alpha.setDuration(500);
                libraryDvdTitle.startAnimation(alpha);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getEvergreenLink()));
                startActivity(intent);
            }
        });

        for (Library library : dvd.getLibaries()) {
            TableRow row = new TableRow(this);
            row.setPadding(4, 4, 4, 4);

            TextView name = new TextView(this);
            TextView status = new TextView(this);
            name.setText("\t" + library.getName());
            status.setText("\t\t\t\t\t" + library.getStatus());

            row.addView(name);
            row.addView(status);
            tableLayout.addView(row);
        }
    }
}
