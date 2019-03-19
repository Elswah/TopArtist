package com.example.ahmed.topartist.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ahmed.topartist.AppController;
import com.example.ahmed.topartist.R;
import com.example.ahmed.topartist.model.Artist;

import java.util.ArrayList;

/**
 * Created by ahmed on 9/10/2016.
 */
public class CustomListviewAdapter extends ArrayAdapter<Artist> {
    private LayoutInflater inflater;
    private ArrayList<Artist> data;
    private Activity mContext;
    private int layoutResourceId;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListviewAdapter(Activity context, int resource, ArrayList<Artist> objs) {
        super(context, resource, objs);
        data = objs;
        mContext = context;
        layoutResourceId = resource;
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(Artist item) {
        return super.getPosition(item);
    }

    @Override
    public Artist getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        ViewHolder viewHolder = null;

        if ( row == null) {

            inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();


            //Get references to our views
            viewHolder.bandImage = (NetworkImageView)row.findViewById(R.id.bandImage);
            viewHolder.artistName = (TextView) row.findViewById(R.id.artistName);
            viewHolder.playCount = (TextView) row.findViewById(R.id.playCount);
            viewHolder.listernters = (TextView) row.findViewById(R.id.listeners);


            row.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) row.getTag();
        }
try {
    viewHolder.artist = data.get(position);


        //We can now display the data

        viewHolder.artistName.setText("artistName: " + viewHolder.artist.getArtistName());
        viewHolder.playCount.setText("playCount: " + viewHolder.artist.getPlayCount());
        viewHolder.listernters.setText("listernters: " + viewHolder.artist.getListeners());

        viewHolder.bandImage.setImageUrl(viewHolder.artist.getArtistIamge(), imageLoader);
    }catch (IndexOutOfBoundsException e){

   }

      //  Log.v("VENUE", viewHolder.event.getVenueName());

/*
        final ViewHolder finalViewHolder = viewHolder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ActivityEventDetails.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("eventObj", finalViewHolder.event);
                i.putExtras(mBundle);
                mContext.startActivity(i);



            }
        });
        */



        return row;
    }





    public class ViewHolder {
        Artist artist;
        TextView artistName;
        TextView playCount;
        TextView listernters;
        NetworkImageView bandImage;


    }

}
