package com.liborgsadmin.Datahelpers.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.liborgsadmin.android.R;
import com.liborgsadmin.datamodel.ReturnBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakshiagarwal on 13/01/16.
 */
public class BookRequestAdapter extends RecyclerView.Adapter<BookRequestAdapter.ViewHolder> {
    private Context mContext;
    private List<ReturnBook> mList;

    public BookRequestAdapter(Context context) {
        mContext = context;
    }

    public void setDataList(List<ReturnBook> returnBookList) {
        mList = returnBookList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReturnBook returnBook = (ReturnBook) mList.get(position);

        ArrayList<String> authorNameList = returnBook.getAuthor();
        holder.mTextView.setText(returnBook.getName().toUpperCase());
        holder.aTextView.setText(authorNameList.get(0));
        holder.eTextView.setText(returnBook.getEmail());
        holder.uTextview.setText(returnBook.getUserName());

        for (int z = 1; z < authorNameList.size(); z++) {
            holder.aTextView.append(", " + authorNameList.get(z));
        }

        holder.position = position;
        holder.theBitmap = Glide.
                with(mContext).
                load(returnBook.getUrl()).
                asBitmap().
                into(holder.imageview);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_request_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView, aTextView, deleteButton, acceptButton, uTextview, eTextView;
        public ImageView imageview;
        int position;
        private Target<Bitmap> theBitmap = null;

        ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.mybook);
            aTextView = (TextView) itemView.findViewById(R.id.authorname);
            imageview = (ImageView) itemView.findViewById(R.id.imageView);
            uTextview = (TextView) itemView.findViewById(R.id.name);
            eTextView = (TextView) itemView.findViewById(R.id.email);
            deleteButton = (TextView) itemView.findViewById(R.id.deleteButton);
            acceptButton = (TextView) itemView.findViewById(R.id.acceptButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    deleteButton.setTextColor(mContext.getResources().getColor(R.color.white));
                    if (position < mList.size()) {
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });
        }
    }


}
