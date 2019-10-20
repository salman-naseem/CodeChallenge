package com.example.codechallenge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.codechallenge.R;
import com.example.codechallenge.callbacks.ItemClickListener;
import com.example.codechallenge.utils.Validations;
import com.example.trimulabstask.GetArtworksQuery;

import java.util.List;
import java.util.Objects;

/*
    RecyclerAdapter is used to show data in a Recycler View.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<GetArtworksQuery.Artwork> listArticles;
    private ItemClickListener itemClickListener;
    public RecyclerAdapter(Context context, List<GetArtworksQuery.Artwork> listArticles, ItemClickListener itemClickListener) {
        this.mContext = context;//Activity context
        this.listArticles = listArticles;//Data source
        this.itemClickListener = itemClickListener;//Callback Listener
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);//Setting View
        return new ViewHolder(view);//returning view holder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetArtworksQuery.Artwork dataItem = listArticles.get(position);//Get DataItem by position
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_image); //On Glide call failed/timeout, this image will be used as default
        if(dataItem.images().size() != 0) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(defaultOptions)
                    .load(Objects.requireNonNull(dataItem.images().get(0).cropped().url()))//providing url
                    .into(holder.itemPic);//imageView reference where image will be loaded
        }
        holder.tvTitle.setText(dataItem.title());//Set title in TextView
        String artistName = mContext.getString(R.string.artist) + " " + dataItem.artist_names();
        holder.tvSubTitle.setText(artistName);//Set artistName in TextView
        String category = mContext.getString(R.string.category) + "\"" + dataItem.category() +"\"";
        holder.tvDescription.setText(category);//Set category in TextView
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(position, dataItem));//onItemClick trigger method with item position in list and object
    }

    @Override
    public int getItemCount() {
        return listArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemPic;
        TextView tvTitle, tvSubTitle, tvDescription;

         ViewHolder(@NonNull View itemView) {//binding resources
            super(itemView);
            itemPic = itemView.findViewById(R.id.iv_item_image);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvSubTitle = itemView.findViewById(R.id.tv_item_sub_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
        }
    }
}