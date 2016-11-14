package com.liborgsadmin.Datahelpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.liborgsadmin.android.CompleteActivity;
import com.liborgsadmin.android.R;
import com.liborgsadmin.datamodel.AllBooks;

import java.util.List;

/**
 * Created by sakshiagarwal on 13/01/16.
 */
public class Tab2Adapter extends RecyclerView.Adapter<Tab2Adapter.ViewHolder> {

    private Context mContext;
    private List<AllBooks> mList;

    public Tab2Adapter(Context context) {
        mContext = context;
    }

    public void setDataList(List<AllBooks> allBooksList) {
        mList = allBooksList;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        AllBooks allBooks = mList.get(position);
        String bname = allBooks.getName();

        holder.mTextView.setText(bname);
        holder.position = position;
        holder.theBitmap = Glide.
                with(mContext).
                load(allBooks.getUrl2()).
                asBitmap().
                into(holder.imageview);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView imageview;
        public int position;
        private Target<Bitmap> theBitmap = null;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.bookname);
            imageview = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (position < mList.size()) {
                        AllBooks allBooks = mList.get(position);
                        Intent i = new Intent(mContext, CompleteActivity.class);
                        i.putExtra("bookName", allBooks.getName());
                        i.putStringArrayListExtra("authorName", allBooks.getAuthor());
                        i.putExtra("publisherName", allBooks.getPublisher());
                        i.putExtra("pageCount", allBooks.getPageCount());
                        i.putExtra("subtitle", allBooks.getSubtitle());
                        i.putStringArrayListExtra("categories", allBooks.getCategories());
                        i.putExtra("imageUrl", allBooks.getUrl2());
                        i.putExtra("description", allBooks.getDesc());
                        i.putExtra("available", allBooks.getAvailable());
                        mContext.startActivity(i);
                    }
                }
            });
        }
    }
}