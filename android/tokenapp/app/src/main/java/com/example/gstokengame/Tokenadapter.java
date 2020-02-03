package com.example.gstokengame;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Tokenadapter extends ArrayAdapter<Token> {

    private Activity activity;

    public Tokenadapter(Activity activity, int resource, List<Token> li) {
        super(activity, resource, li);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.token_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Token t = getItem(position);

        holder.Image.setImageResource(R.mipmap.token_cat);
        holder.TokenId.setText(t.getId());
        holder.Conent.setText(t.getContent());
        holder.ConsumedAt.setText(" Consumed At :" + t.getConsumedA());
        holder.Consumed.setText(" Consumed :" + t.getConsumed());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView Image;
        private TextView TokenId;
        private TextView Conent;
        private TextView ConsumedAt;
        private TextView Consumed;

        public ViewHolder(View v) {
            Image = (ImageView)v.findViewById(R.id.item_image);
            TokenId = (TextView)v.findViewById(R.id.t_id);
            Conent = (TextView)v.findViewById(R.id.content);
            ConsumedAt = (TextView)v.findViewById(R.id.consumedAt);
            Consumed = (TextView)v.findViewById(R.id.consumed);
        }
    }
}
