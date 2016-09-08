package com.itservz.android.mayekid.mayek;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itservz.android.mayekid.utils.BitmapHelper;
import com.itservz.android.mayekid.utils.MayekCard;
import com.itservz.android.mayekid.R;

import java.util.List;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MayekCardAdapter extends RecyclerView.Adapter<MayekCardAdapter.MayekCardViewHolder>{
    public MayekListener mayekListener;
    List<MayekCard> mayeks;
    private Context context;

        MayekCardAdapter(Context context, List<MayekCard> books, MayekListener mayekListener){
        this.mayeks = books;
        this.mayekListener = mayekListener;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mayeks.size();
    }

    @Override
    public MayekCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
        MayekCardViewHolder bvh = new MayekCardViewHolder(v, mayekListener);
        return bvh;
    }

    @Override
    public void onBindViewHolder(MayekCardViewHolder mayekCardViewHolder, int i) {
        mayekCardViewHolder.imageId = mayeks.get(i).getRes();
        mayekCardViewHolder.title.setText(mayeks.get(i).getTitle());
        Bitmap imageRes =  BitmapHelper.decodeSampledBitmapFromResource(context.getResources(), mayekCardViewHolder.imageId, 100, 100);
        mayekCardViewHolder.mayekIcon.setImageBitmap(imageRes);
        Bitmap pictureRes =  BitmapHelper.decodeSampledBitmapFromResource(context.getResources(), mayeks.get(i).getPicture(), 100, 100);
        mayekCardViewHolder.pictureIcon.setImageBitmap(pictureRes);
        mayekCardViewHolder.cv.setBackgroundResource(R.drawable.board);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MayekCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView title;
        ImageView mayekIcon;
        ImageView pictureIcon;
        int imageId;
        public MayekListener mayekCardClickListener;

        MayekCardViewHolder(View itemView, MayekListener mayekCardClickListener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardview);
            title = (TextView)itemView.findViewById(R.id.title);
            mayekIcon = (ImageView)itemView.findViewById(R.id.mayekicon);
            pictureIcon = (ImageView)itemView.findViewById(R.id.mayekpic);
            this.mayekCardClickListener = mayekCardClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mayekCardClickListener.recyclerViewClick(imageId);
        }
    }


}

