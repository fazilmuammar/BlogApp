package app.blogapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import app.blogapp.R;
import app.blogapp.config.server;




public class Home extends SimpleAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, String>> arrayList;

    public Home(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView image = (ImageView) view.findViewById(R.id.img_image);
        TextView category = (TextView)view.findViewById(R.id.text_category);
        TextView title = (TextView)view.findViewById(R.id.text_title);
        TextView author = (TextView)view.findViewById(R.id.text_author);
        TextView created = (TextView)view.findViewById(R.id.text_created);
        TextView views = (TextView)view.findViewById(R.id.text_views);
        TextView love = (TextView)view.findViewById(R.id.text_loves);

        category.setText(arrayList.get(position).get("category"));
        title.setText(arrayList.get(position).get("title"));
        author.setText(arrayList.get(position).get("author"));
        created.setText(arrayList.get(position).get("created"));
        views.setText(arrayList.get(position).get("view"));
        love.setText(arrayList.get(position).get("love"));

        Picasso.with(context).
                load(server.img + arrayList.get(position).get("image"))
                .fit().centerCrop()
                .placeholder(R.drawable.no_image)
                .into(image);

        return view;
    }

}