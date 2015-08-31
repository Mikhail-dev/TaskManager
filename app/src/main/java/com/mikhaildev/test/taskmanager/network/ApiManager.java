package com.mikhaildev.test.taskmanager.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.exception.ApiException;
import com.mikhaildev.test.taskmanager.exception.NetworkConnectionException;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.util.DateHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class ApiManager {

    private static final String APP_URL    = "http://test.boloid.com:9000";
    private static final String URL_TASKS  = APP_URL + "/tasks";

    /**
     * Returns massive of {@link Task}
     * @param context
     * @return Task[]
     * @throws Exception
     */
    public static Task[] getTasks(Context context) throws Exception {
        if (!isThereInternetConnection(context))
            throw new NetworkConnectionException();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = openConnection(URL_TASKS);
            int responseCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                String taskData = convertStreamToString(urlConnection.getInputStream());
                Type type = new TypeToken<Task[]>() { }.getType();
                Task[] tasks = new Gson().fromJson(new JSONObject(taskData).getString("tasks"), type);
                return tasks;
            } else {
                throw getException(responseCode);
            }
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }


    private static HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.addRequestProperty("Accept-Language", Locale.getDefault().getLanguage());
        urlConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
        urlConnection.setConnectTimeout(DateHelper.SECOND * 30);
        urlConnection.setReadTimeout(DateHelper.SECOND * 60);
        return urlConnection;
    }

    private static Exception getException(int responseCode) {
        return getException(null, responseCode);
    }

    private static Exception getException(Exception e, int responseCode) {
        if (e!=null
                && (e.getClass().equals(java.net.UnknownHostException.class) || e.getClass().equals(java.net.SocketTimeoutException.class))) {
            return new NetworkConnectionException();
        } else if (HttpURLConnection.HTTP_INTERNAL_ERROR == responseCode) {
            return new ApiException(new Exception("Response code " + responseCode), R.string.server_error, responseCode);
        } else {
            return new ApiException(new Exception("Response code " + responseCode), R.string.error_code_frmt, responseCode);
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private static boolean isThereInternetConnection(Context context) {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
