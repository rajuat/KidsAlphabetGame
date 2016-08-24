package com.itservz.android.mayekid.mayek;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.itservz.android.mayekid.R;

import java.util.List;

/**
 * Created by raju.athokpam on 24-08-2016.
 */
@Deprecated
public class MayekDrawAdapter extends ArrayAdapter<MayekDrawView> {
    int layoutResourceId;
    private final Context context;
    private List<MayekDrawView> views;
    private List<MayekDrawViewHolder> viewHolders;
    private ProgressDialog mProgressDialog;
    private ImageView downloadImageButton;

    public MayekDrawAdapter(Context context, int resource, List<MayekDrawView> views) {
        super(context, resource, views);
        this.context = context;
        this.views = views;

        /*this.layoutResourceId = resource;
        viewHolders = new ArrayList<MayekDrawView>();
        int size = this.views.size();
        for (int i = 0; i < size; i++) {
            viewHolders.add(null);
        }*/
    }

    static class MayekDrawViewHolder {
        MayekDrawView mayekDrawView;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        return views.get(position);
       /* MayekDrawViewHolder viewHolder  = new MayekDrawViewHolder();
        viewHolder.mayekDrawView = views.get(position);*/
        /*if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // convertView = inflater.inflate(R.layout.catalogslist_single_row,
            // parent, false);
            viewHolder = new MayekDrawViewHolder();
            viewHolder.position = position;
            viewHolder.downloadImageButton
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("DOWNLOAD PRESSED");
                            viewHolder.downloadImageButton.setTag("downloaded");
                            viewHolders.add(position, "downloaded");
                        }
                    });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MayekDrawViewHolder) convertView.getTag();
        }
         viewHolder.catlogTitle.setText(views.get(position));
        viewHolder.catlogTitle.setTypeface(regularDin);
        viewHolder.icon.setImageResource(R.drawable.cata);
        if (viewHolders.get(position) == "downloaded") {
            downloadImageButton.setImageResource(R.drawable.icon_ok);
        } else {
            downloadImageButton.setImageResource(R.drawable.icon_download);
        }

        viewHolder.position = position;
        */

       /* return convertView;*/
    }
}