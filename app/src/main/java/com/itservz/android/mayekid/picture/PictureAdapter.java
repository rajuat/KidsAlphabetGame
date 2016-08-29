package com.itservz.android.mayekid.picture;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.MayekCard;

import java.util.List;

/**
 * Created by Raju on 8/25/2016.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder>{
    private List<MayekCard> pictureIds = null;
    private PictureAdapterListener listener;
    Animation animation = null;
    public PictureAdapter(Context context, List<MayekCard> pictureIds, PictureAdapterListener listener, Animation animation){
        this.pictureIds = pictureIds;
        this.listener = listener;
        this.animation = animation;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_layout, parent, false);
        PictureViewHolder pictureViewHolder = new PictureViewHolder(v, listener);
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        holder.drawView.setBackgroundResource(pictureIds.get(position).getPicture());
        holder.pictureId = pictureIds.get(position).getPicture();
        holder.cardView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return pictureIds.size();
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView drawView;
        CardView cardView;
        PictureAdapterListener listener;
        int pictureId;
        public PictureViewHolder(View itemView, PictureAdapterListener listener) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.pictureCardView);
            this.drawView = (ImageView) itemView.findViewById(R.id.pictureDrawing);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("View holder on click pictureid" + pictureId);
            listener.recyclerViewClick(pictureId);
        }
    }
}
