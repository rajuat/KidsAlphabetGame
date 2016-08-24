package com.itservz.android.mayekid.mayek;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itservz.android.mayekid.R;

import java.util.List;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MayekCardAdapter extends RecyclerView.Adapter<MayekCardAdapter.MayekCardViewHolder>{
    public MayekCardAdapterClickListener mayekCardAdapterClickListener;
    List<MayekCard> mayeks;
    private Context context;

    public static class MayekCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView title;
        ImageView mayekIcon;
        ImageView pictureIcon;
        int imageId;
        public MayekCardClickListener mayekCardClickListener;

        MayekCardViewHolder(View itemView, MayekCardClickListener mayekCardClickListener) {
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
            System.out.println("MayekCardViewHolder constructor onclick" + imageId);
            mayekCardClickListener.mayekCardOnClick(imageId);
        }
    }

    MayekCardAdapter(Context context, List<MayekCard> books, MayekCardAdapterClickListener mayekCardAdapterClickListener){
        this.mayeks = books;
        this.mayekCardAdapterClickListener = mayekCardAdapterClickListener;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mayeks.size();
    }

    @Override
    public MayekCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
        MayekCardClickListener listener = new MayekCardClickListener() {
            @Override
            public void mayekCardOnClick(int imageId) {
                System.out.println("onCreateViewHolder onclick" + imageId);
                mayekCardAdapterClickListener.recyclerViewClick(imageId);
            }
        };
        MayekCardViewHolder bvh = new MayekCardViewHolder(v, listener);
        return bvh;
    }

    @Override
    public void onBindViewHolder(MayekCardViewHolder mayekCardViewHolder, int i) {
        mayekCardViewHolder.imageId = mayeks.get(i).getRes();
        mayekCardViewHolder.title.setText(mayeks.get(i).getTitle());
        System.out.println("image identifier " + mayekCardViewHolder.imageId);
        mayekCardViewHolder.mayekIcon.setImageResource(mayekCardViewHolder.imageId);
        mayekCardViewHolder.pictureIcon.setImageResource(mayeks.get(i).getPicture());
        mayekCardViewHolder.cv.setBackgroundResource(R.drawable.board);


        //perhaps to enable clink on card
        /*mayekCardViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked");
            }
        });*/

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

