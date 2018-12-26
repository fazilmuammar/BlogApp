package app.blogapp.Adapter;

/**
 * Created by no name on 07/09/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import app.blogapp.config.server;
import app.blogapp.R;
import app.blogapp.config.server;


public class Search extends SimpleAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, String>> arrayList;

    public Search(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView image = (ImageView) view.findViewById(R.id.img_image);
        TextView content_id = (TextView)view.findViewById(R.id.content_id);
        TextView category_id = (TextView)view.findViewById(R.id.category_id);

        Log.d("SEARCH_IMG", server.category + arrayList.get(position).get("image"));

        Picasso.with(context).
                load(server.category + arrayList.get(position).get("image"))
                .placeholder(R.drawable.no_image)
                .into(image);

        content_id.setText(arrayList.get(position).get("content_id") + " ARTICLES");
        category_id.setText(arrayList.get(position).get("category_id"));

        return view;
    }
}