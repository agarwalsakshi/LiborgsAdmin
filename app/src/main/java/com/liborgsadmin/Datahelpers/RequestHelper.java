package com.liborgsadmin.Datahelpers;

import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.ApplicationConstants;
import com.loopj.android.http.RequestParams;

/**
 * Created by sakshiagarwal on 13/01/16.
 */
public class RequestHelper {

    public void loginUser(String userName, String password, ResponseInterface responseInterface) {
        RequestParams newparams;
        newparams = new RequestParams();
        newparams.add("email", userName);
        newparams.add("password", password);
        new NetworkHelper().postRequestForServer(ApplicationConstants.URL_LOGIN, newparams, "", responseInterface);
    }

    public void getAllBooksFromServer(String auth_token, final ResponseInterface responseInterface) {
        new NetworkHelper().getRequestForServer(ApplicationConstants.URL_ALL_BOOKS, auth_token, responseInterface);
    }


    public void getSearchedData(String url, String auth_token, final ResponseInterface responseInterface) {
        new NetworkHelper().getRequestForServer(url, auth_token, responseInterface);
    }

    public void shareRegIdWithAppServer(String regId, String auth_token, ResponseInterface responseInterface) {
        RequestParams newparams = new RequestParams();
        newparams.add("reg_id", regId);
        newparams.add("source", "android");
        new NetworkHelper().postRequestForServer(ApplicationConstants.URL_DEVICE_REGISTER, newparams, auth_token, responseInterface);
    }

    public void getAllUsersDetails(String auth_token, ResponseInterface responseInterface) {
        new NetworkHelper().getRequestForServer(ApplicationConstants.URL_ALL_USERS, auth_token, responseInterface);
    }

    public void getRequestedBooks(String auth_token, ResponseInterface responseInterface) {
        new NetworkHelper().getRequestForServer(ApplicationConstants.URL_REQUEST_BOOK, auth_token, responseInterface);
    }


    public void getReturnedBooks(String auth_token, ResponseInterface responseInterface) {
        new NetworkHelper().getRequestForServer(ApplicationConstants.URL_RETURNED_BOOKS, auth_token, responseInterface);
    }

    public void confirmReturnedBook(String auth_token, String email, String bookId, ResponseInterface responseInterface) {
        RequestParams newparams = new RequestParams();
        newparams.add("email", email);
        newparams.add("issue_id", bookId);
        new NetworkHelper().postRequestForServer(ApplicationConstants.URL_CONFIRM_RETURN, newparams, auth_token, responseInterface);
    }
}
