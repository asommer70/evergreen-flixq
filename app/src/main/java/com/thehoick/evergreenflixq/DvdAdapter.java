package com.thehoick.evergreenflixq;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adam on 12/8/14.
 * Hope I'm doing this right.
 */
public class DvdAdapter extends ArrayAdapter<Dvd> {
    protected Context mContext;
    protected List<Dvd> mDvds;

    protected Dvd mDvd;

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Dvd dvd = MainActivity.mDvdList.get(position);

        holder.nameLabel.setText(dvd.getTitle());
        //holder.description.loadData(dvd.getDescription(), "text/html", null);
        //holder.description.setText(Html.fromHtml(dvd.getDescription()));
        holder.description.setText(dvd.getDescription());

        Picasso.with(MainActivity.mContext).load(dvd.getImgUrl()).into(holder.dvdImage);

        holder.dvdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getLink()));
                mContext.startActivity(intent);
            }
        });

        holder.status.setText(dvd.getStatus());

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dvd.getEvergreenLink()));
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView description;
        ImageView dvdImage;
        TextView nameLabel;
        TextView status;
    }

    public void refill(List<Dvd> dvds) {
        mDvds.clear();
        mDvds.addAll(dvds);
        notifyDataSetChanged();
    }
}
