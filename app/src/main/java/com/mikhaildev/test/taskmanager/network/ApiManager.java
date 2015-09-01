package com.mikhaildev.test.taskmanager.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.exception.ApiException;
import com.mikhaildev.test.taskmanager.exception.NetworkConnectionException;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.util.DateUtils;
import com.mikhaildev.test.taskmanager.util.Utils;

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
        if (!Utils.isThereInternetConnection(context))
            throw new NetworkConnectionException();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = openConnection(URL_TASKS);
            int responseCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                String taskData = Utils.convertStreamToString(urlConnection.getInputStream());
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
        urlConnection.setConnectTimeout(DateUtils.SECOND * 30);
        urlConnection.setReadTimeout(DateUtils.SECOND * 60);
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
}
