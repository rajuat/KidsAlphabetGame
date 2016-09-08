package com.itservz.android.mayekid.masing;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.utils.BitmapHelper;

import java.util.List;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MasingCardAdapter extends RecyclerView.Adapter<MasingCardAdapter.MasingCardViewHolder>{
    public MasingListener masingListener;
    List<MasingCard> masings;
    private Context context;

        MasingCardAdapter(Context context, List<MasingCard> masings, MasingListener masingListener){
        this.masings = masings;
        this.masingListener = masingListener;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return masings.size();
    }

    @Override
    public MasingCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.masing_layout, viewGroup, false);
        MasingCardViewHolder bvh = new MasingCardViewHolder(v, masingListener);
        return bvh;
    }

    @Override
    public void onBindViewHolder(MasingCardViewHolder masingCardViewHolder, int i) {
        masingCardViewHolder.imageId = masings.get(i).getPic();
        masingCardViewHolder.masingTitle.setText(masings.get(i).getTitle());
        Bitmap imageRes =  BitmapHelper.decodeSampledBitmapFromResource(context.getResources(), masingCardViewHolder.imageId, 100, 100);
        masingCardViewHolder.masingPic.setImageBitmap(imageRes);
        Bitmap countRes =  BitmapHelper.decodeSampledBitmapFromResource(context.getResources(), masings.get(i).getCount(), 100, 100);
        masingCardViewHolder.masingCount.setImageBitmap(countRes);
        masingCardViewHolder.masingCardView.setBackgroundResource(R.drawable.board);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MasingCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView masingCardView;
        TextView masingTitle;
        ImageView masingCount;
        ImageView masingPic;
        int imageId;
        public MasingListener mayekCardClickListener;

        MasingCardViewHolder(View itemView, MasingListener mayekCardClickListener) {
            super(itemView);
            masingCardView = (CardView)itemView.findViewById(R.id.masing_card_view);
            masingTitle = (TextView)itemView.findViewById(R.id.masing_title);
            masingCount = (ImageView)itemView.findViewById(R.id.masing_count);
            masingPic = (ImageView)itemView.findViewById(R.id.masing_pic);
            this.mayekCardClickListener = mayekCardClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mayekCardClickListener.recyclerViewClick(imageId);
        }
    }


}

