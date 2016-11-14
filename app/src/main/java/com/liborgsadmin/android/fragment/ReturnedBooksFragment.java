package com.liborgsadmin.android.fragment;

/**
 * Created by sakshiagarwal on 16/11/15.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liborgsadmin.Datahelpers.DataHelper;
import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.android.R;
import com.liborgsadmin.datamodel.ReturnBook;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReturnedBooksFragment extends Fragment{

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.tab_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getReturndBookData();
    }


    private void getReturndBookData()
    {
        AppUtils.getInstance().showProgressDialog(getActivity(), "", getString(R.string.loading));
        new RequestHelper().getReturnedBooks(new SharedPreferences().getAuthKey(getActivity()), new ResponseInterface() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                AppUtils.getInstance().dismissProgressDialog();
                List<ReturnBook> returnBooks = new DataHelper().getReturnBookData(getActivity(), jsonObject);
                ReturnedBooksAdapter adapter = new ReturnedBooksAdapter(returnBooks);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                AppUtils.getInstance().dismissProgressDialog();
                AppUtils.getInstance().showAlertDialogBox(getContext(), message);
            }
        });
    }
    class ReturnedBooksAdapter extends RecyclerView.Adapter<ReturnedBooksAdapter.ViewHolder> {
        private List<ReturnBook> mList;

        ReturnedBooksAdapter(List<ReturnBook> returnBookList) {
            mList = returnBookList;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ReturnBook returnBook = mList.get(position);
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(returnBook.getReturnDate() * 1000L));
            holder.mTextView.setText(returnBook.getName());
            holder.eTextView.setText(returnBook.getEmail());
            holder.uTextview.setText(returnBook.getUserName());
            holder.rTextView.setText(date);

            ArrayList<String> authors = returnBook.getAuthor();
            String authorName = authors.get(0);
            for (int z = 1; z < authors.size(); z++) {
                authorName += ", " + authors.get(z);
            }
            holder.aTextView.setText(authorName);
            Glide.
                    with(getActivity()).
                    load(returnBook.getUrl()).
                    asBitmap().
                    into(holder.imageview);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView, aTextView, rTextView, sendButton, uTextview, eTextView;
            ImageView imageview;
            int position;

            ViewHolder(View itemView) {
                super(itemView);

                mTextView = (TextView) itemView.findViewById(R.id.mybook);
                aTextView = (TextView) itemView.findViewById(R.id.authorname);
                imageview = (ImageView) itemView.findViewById(R.id.imageView);
                rTextView = (TextView) itemView.findViewById(R.id.returnDate);
                uTextview = (TextView) itemView.findViewById(R.id.name);
                eTextView = (TextView) itemView.findViewById(R.id.email);
                sendButton = (TextView) itemView.findViewById(R.id.returnButton);
                sendButton.setEnabled(true);

                sendButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (position < mList.size()) {
                            sendButton.setEnabled(false);
                            ReturnBook books = mList.get(position);
                            confirmReturnedBook(books.getEmail(), books.getIssueId());
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

            private void confirmReturnedBook(String email, String issueId)
            {
                AppUtils.getInstance().showProgressDialog(getActivity(), "", getString(R.string.loading));
                new RequestHelper().confirmReturnedBook(new SharedPreferences().getAuthKey(getActivity()), email, issueId, new ResponseInterface() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        AppUtils.getInstance().dismissProgressDialog();
                        sendButton.setEnabled(true);
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        AppUtils.getInstance().dismissProgressDialog();
                        sendButton.setEnabled(true);
                        AppUtils.getInstance().showAlertDialogBox(getActivity(), message);
                    }
                });
            }

        }
    }

}