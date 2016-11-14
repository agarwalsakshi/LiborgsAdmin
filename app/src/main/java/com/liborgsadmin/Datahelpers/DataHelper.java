package com.liborgsadmin.Datahelpers;

import android.app.Activity;
import android.content.Context;

import com.liborgsadmin.android.LoginActivity;
import com.liborgsadmin.android.R;
import com.liborgsadmin.datamodel.AllBooks;
import com.liborgsadmin.datamodel.AllUsers;
import com.liborgsadmin.datamodel.ReturnBook;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakshiagarwal on 10/11/16.
 */

public class DataHelper {

    public List<ReturnBook> getRequestData(Context context, JSONObject object) {
        List<ReturnBook> returnBooksList = new ArrayList<ReturnBook>();
        try {
            AppUtils.getInstance().dismissProgressDialog();
            if (object.getBoolean("status")) {

                if (!object.has("data")) {
                    if (object.has("token_expired")) {
                        AppUtils.getInstance().showAlertDialogBox(context, context.getResources().getString(R.string.token_expire_text));
                        new SharedPreferences().deleteAuthKey(context);
                        AppUtils.getInstance().pageTransition((Activity) context, LoginActivity.class);
                    } else {
                        AppUtils.getInstance().showAlertDialogBox(context, context.getResources().getString(R.string.no_book_request_text));
                    }
                } else {
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ReturnBook returnBooks = new ReturnBook();
                        JSONObject newObject = jsonArray.getJSONObject(i);
                        String bookName = newObject.getString("title");
                        returnBooks.setName(bookName);

                        JSONArray authorArray = newObject.getJSONArray("author");
                        ArrayList<String> authorList = new ArrayList<String>();
                        for (int s = 0; s < authorArray.length(); s++) {
                            String authorName = authorArray.getString(s);
                            authorList.add(authorName);
                        }

                        String publisher = newObject.getString("publisher");

                        String imageurl = newObject.getString("thumbnail");
                        returnBooks.setUrl(imageurl);

                        String userName = newObject.getString("username");
                        returnBooks.setUserName(userName);

                        String email = newObject.getString("email");
                        returnBooks.setEmail(email);

                        returnBooks.setAuthor(authorList);
                        returnBooksList.add(returnBooks);
                    }
                }
            } else {
                AppUtils.getInstance().dismissProgressDialog();
                AppUtils.getInstance().showAlertDialogBox(context, object.getString("message"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnBooksList;
    }

    public List<AllBooks> getAllBooks(JSONObject object) {
        List<AllBooks> allBooksList = new ArrayList<AllBooks>();
        try {
            if (object.has("data")) {

                JSONArray jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    AllBooks allBooks = new AllBooks();
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String bookName = newObject.getString("title");
                    allBooks.setName(bookName);

                    JSONArray authorArray = newObject.getJSONArray("author");
                    ArrayList<String> authorList = new ArrayList<String>();
                    for (int s = 0; s < authorArray.length(); s++) {
                        String authorName = authorArray.getString(s);
                        authorList.add(authorName);

                    }
                    if (newObject.has("categories")) {
                        JSONArray categoriesArray = newObject.getJSONArray("categories");
                        ArrayList<String> categories = new ArrayList<String>();
                        for (int s = 0; s < categoriesArray.length(); s++) {
                            String category = categoriesArray.getString(s);
                            categories.add(category);
                            allBooks.setCategories(categories);
                        }
                    }

                    int avail = newObject.getInt("available");
                    allBooks.setAvailable(avail);


                    if (newObject.has("publisher")) {
                        String publisher = newObject.getString("publisher");
                        allBooks.setPublisher(publisher);
                    } else {
                        allBooks.setPublisher("N/A");
                    }
                    if (newObject.has("pagecount")) {
                        String pagecount = newObject.getString("pagecount");
                        allBooks.setPageCount(pagecount);
                    } else {
                        allBooks.setPageCount("N/A");
                    }

                    if (newObject.has("subtitle")) {
                        String subtitles = newObject.getString("subtitle");
                        allBooks.setSubtitle(subtitles);
                    } else allBooks.setSubtitle("N/A");

                    String desc = newObject.getString("description");
                    allBooks.setDesc(desc);

                    String imageurl = newObject.getString("thumbnail");
                    allBooks.setUrl2(imageurl);

                    allBooks.setAuthor(authorList);
                    allBooksList.add(allBooks);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allBooksList;
    }

    public List<ReturnBook> getReturnBookData(Context context, JSONObject object) {
        List<ReturnBook> returnBooksList = new ArrayList<ReturnBook>();
        try {
            AppUtils.getInstance().dismissProgressDialog();
            if (object.has("token_expired")) {
                new SharedPreferences().deleteAuthKey(context);
                AppUtils.getInstance().showAlertDialogBox(context, context.getResources().getString(R.string.token_expire_text));
                AppUtils.getInstance().pageTransition((Activity) context, LoginActivity.class);
            }
            if (object.getBoolean("status")) {
                if (!object.has("data")) {
                    AppUtils.getInstance().showAlertDialogBox(context, context.getResources().getString(R.string.no_books_return_text));
                }

                JSONArray jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ReturnBook returnBooks = new ReturnBook();
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String bookName = newObject.getString("title");
                    returnBooks.setName(bookName);

                    JSONArray authorArray = newObject.getJSONArray("authors");
                    ArrayList<String> authorList = new ArrayList<String>();
                    for (int s = 0; s < authorArray.length(); s++) {
                        String authorName = authorArray.getString(s);
                        authorList.add(authorName);
                    }

                    String imageurl = newObject.getString("pic");
                    returnBooks.setUrl(imageurl);

                    String userName = newObject.getString("user_name");
                    returnBooks.setUserName(userName);

                    String email = newObject.getString("email");
                    returnBooks.setEmail(email);

                    String issueId = newObject.getString("issue_id");
                    returnBooks.setIssueId(issueId);

                    long returnDate = newObject.getInt("return_date");
                    returnBooks.setReturnDate(returnDate);

                    returnBooks.setAuthor(authorList);
                    returnBooksList.add(returnBooks);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnBooksList;
    }

    public List<AllUsers> getAllUsers(Context context, JSONObject object) {
        List<AllUsers> allUsersList = new ArrayList<AllUsers>();
        try {
            AppUtils.getInstance().dismissProgressDialog();
            if (object.has("token_expired")) {
                new SharedPreferences().deleteAuthKey(context);
                AppUtils.getInstance().showAlertDialogBox(context, context.getResources().getString(R.string.token_expire_text));
                AppUtils.getInstance().pageTransition((Activity) context, LoginActivity.class);
            }
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                AllUsers allUsers = new AllUsers();
                JSONObject newObject = jsonArray.getJSONObject(i);
                String firstName = newObject.getString("firstname");
                String lastName = newObject.getString("lastname");
                allUsers.setUserName(firstName + " " + lastName);

                String userEmail = newObject.getString("email");
                allUsers.setUserEmail(userEmail);

                allUsersList.add(allUsers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allUsersList;
    }

}
