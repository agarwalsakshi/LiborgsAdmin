package com.liborgsadmin.utils;

/**
 * Created by sakshiagarwal on 11/01/16.
 */
public class ApplicationConstants {

    static final String GOOGLE_PROJECT_ID = "926498080108";
    static final String APP_SHARED_PREFS = "app_details";

    private static final String URL_BASE = "http://liborgs.cybrilla.io/";
    public static final String URL_RETURNED_BOOKS = URL_BASE + "admin/returned_books";
    public static final String URL_UPLOAD_BOOKS = URL_BASE + "admin/upload_books";
    public static final String URL_LOGIN = URL_BASE + "admin/login";
    public static final String URL_DEVICE_REGISTER = URL_BASE + "device/register";
    public static final String URL_CONFIRM_RETURN = URL_BASE + "admin/confirm_return";
    public static final String URL_ALL_BOOKS = URL_BASE + "books/get_all_books";
    public static final String URL_SEARCH_QUERY = URL_BASE + "books/search";
    public static final String URL_REQUEST_BOOK = URL_BASE + "admin/requested_books";
    public static final String URL_CYBRILLA_HOME = "http://www.cybrilla.com";
    public static final String URL_ALL_USERS = URL_BASE + "admin/cyborgs";

    public static final String GCM_REQUEST_BOOK_TYPE = "request_book";
    public static final String GCM_RETURN_BOOK_TYPE = "book_return";
}
