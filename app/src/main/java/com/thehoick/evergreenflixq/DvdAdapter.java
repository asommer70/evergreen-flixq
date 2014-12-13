package com.thehoick.evergreenflixq;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by adam on 12/8/14.
 * Hope I'm doing this right.
 */
public class DvdAdapter extends ArrayAdapter<Dvd> {
    private String TAG = DvdAdapter.class.getSimpleName();

    protected Context mContext;
    protected List<Dvd> mDvds;

    public DvdAdapter(Context context, List<Dvd> dvds) {
        super(context, R.layout.dvd_item, dvds);
        mContext = context;
        mDvds = dvds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dvd_item, null);
            holder = new ViewHolder();
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.nameLabel = (TextView)convertView.findViewById(R.id.dvdTitle);
            holder.dvdImage = (ImageView)convertView.findViewById(R.id.dvdImage);
            holder.status = (TextView)convertView.findViewById(R.id.status);
            //holder.libraries = (TableLayout)convertView.findViewById(R.id.libraries);
            holder.libraryIcon = (ImageView)convertView.findViewById(R.id.libraryIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Dvd dvd = MainActivity.mDvdList.get(position);

        holder.nameLabel.setText(dvd.getTitle());
        //holder.description.loadData(dvd.getDescription(), "text/html", null);
        //holder.description.setText(Html.fromHtml(dvd.getDescription()));
        holder.description.setText(dvd.getDescription());

        Picasso.with(MainActivity.mContext).load(dvd.getImgUrl()).into(holder.dvdImage);

        holder.dvdImage.setOnClickListener(new DvdOnClickListener(dvd, position) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getLink()));
                mContext.startActivity(intent);
            }
        });

        holder.status.setText(dvd.getStatus());

        holder.status.setOnClickListener(new DvdOnClickListener(dvd, position) {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getEvergreenLink()));
                mContext.startActivity(intent);*/

                if (dvd.getStatus() != "Not Available") {

                    Intent intent = new Intent(mContext, LibrariesActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Sorry, " + dvd.getTitle() +
                            " isn't available in your configured library.", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*try {
            //Log.i(TAG, "libraries size: " + dvd.getLibaries().size());
            // Add the whole Libraries TableLayout dynamically.
            TableLayout tableLayout = new TableLayout(mContext);
            TableRow rowLabels = new TableRow(mContext);
            TextView statusLabel = new TextView(mContext);
            TextView nameLabel = new TextView(mContext);
            statusLabel.setText(R.string.library_status);
            nameLabel.setText(R.string.name_label);
            tableLayout.addView(rowLabels);

            for (Library library : dvd.getLibaries()) {

                TableRow row = new TableRow(mContext);
                //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                //row.setLayoutParams(lp);

                TextView name = new TextView(mContext);
                TextView status = new TextView(mContext);
                name.setText(library.getName());
                status.setText(library.getStatus());

                row.addView(name);
                row.addView(status);
                tableLayout.addView(row);
            }
            holder.libraries.addView(tableLayout);*/
        //} catch (Exception e) {
          //Log.i(TAG, e.getMessage());
        //}

        return convertView;
    }

    private static class ViewHolder {
        TextView description;
        ImageView dvdImage;
        TextView nameLabel;
        TextView status;
        //TableLayout libraries;
        ImageView libraryIcon;
    }

    public void refill(List<Dvd> dvds) {
        mDvds.clear();
        mDvds.addAll(dvds);
        notifyDataSetChanged();
    }

    public class DvdOnClickListener implements View.OnClickListener {

        Dvd dvd;
        int position;
        public DvdOnClickListener(Dvd dvd, int postition) {
            this.position = postition;
            this.dvd = dvd;
        }

        @Override
        public void onClick(View v) {
            //read your lovely variable
        }

    }
}
