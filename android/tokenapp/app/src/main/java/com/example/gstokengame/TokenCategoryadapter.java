package com.example.gstokengame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TokenCategoryadapter extends ArrayAdapter<TokenCategory> {

    private Activity activity;

    public TokenCategoryadapter(Activity activity, int resource, List<TokenCategory> li) {
        super(activity, resource, li);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TokenCategory tc = getItem(position);

        holder.Image.setImageResource(R.mipmap.token_cat);
        holder.C_Id.setText(tc.getId());
        holder.Name.setText(tc.getName());
        holder.GreetingCount.setText(" Greeting Count :" + tc.getGreetingCount());
        holder.Modified.setText(" Modified :" + tc.getModified());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView Image;
        private TextView C_Id;
        private TextView Name;
        private TextView GreetingCount;
        private TextView Modified;

        public ViewHolder(View v) {
            Image = (ImageView)v.findViewById(R.id.item_image);
            C_Id = (TextView)v.findViewById(R.id.c_id);
            Name = (TextView)v.findViewById(R.id.name);
            GreetingCount = (TextView)v.findViewById(R.id.greeting_count);
            Modified = (TextView)v.findViewById(R.id.modified);
        }
    }
}
