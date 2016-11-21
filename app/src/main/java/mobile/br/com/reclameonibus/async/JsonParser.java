package mobile.br.com.reclameonibus.async;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Matheus on 14/09/2016.
 */
public class JsonParser {

    public static int POST = 1;
    public static int GET = 2;

    /**
     * Function to make request to server
     *
     * @param url    - the URL that will receive request
     * @param method - Supported methods: JsonParser.POST and JsonParser.GET
     * @param params - Array of params
     * @return JSONObject - the JsonObject
     */
    public static JSONObject postDataObject(String url, int method, ArrayList<NameValuePair> params) {

        JSONObject jObj = null;
        String json = getJsonString(url, method, params);

        //TODO: MELHORAR TRATAMENTO DE EXCEPTIONS
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", json);
            Log.e("JSON Parser - HTTP", url);
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            e.printStackTrace();
        }

        // return JSON String
        return jObj;
    }

    /**
     * Function to make request to server
     *
     * @param url    - the URL that will receive request
     * @param method - Supported methods: JsonParser.POST and JsonParser.GET
     * @param params - Array of params
     * @return JSONArray - the JsonArray
     */
    public static JSONArray postDataArray(String url, int method, ArrayList<NameValuePair> params) {

        JSONArray jObj = null;
        String json = getJsonString(url, method, params);

        //TODO: MELHORAR TRATAMENTO DE EXCEPTIONS
        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", json);
            Log.e("JSON Parser - HTTP", url);
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            e.printStackTrace();
        }

        // return JSON String
        return jObj;
    }

    /**
     * Establish connection and retrieve the json string
     *
     * @param url    - the URL that will receive request
     * @param method - Supported methods: JsonParser.POST and JsonParser.GET
     * @param params - Array of params
     * @return JSON string
     */
    private static String getJsonString(String url, int method, ArrayList<NameValuePair> params) {
        InputStream is = null;
        String json = "";

        // Making HTTP request
        try {
            // check for request method
            if (method == POST) {
                // request method is POST
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(params));

                // Execute HTTP Post Request
                HttpResponse httpResponse = httpclient.execute(httppost);
//                ...
                if (httpResponse == null) {
                    throw new Exception("Undefined error Http Response");
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                //Tentando pegar a excecao, mas nao fiz corretamente
                if (httpEntity == null) {
                    throw new ClientProtocolException("Undefined error Http Entity");
                }
                is = httpEntity.getContent();
            } else if (method == GET) {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;

                Log.i("JSON Parser", url);

                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

            //TODO: MELHORAR TRATAMENTO DE EXCEPTIONS
        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.i("Json String", json);
            return json;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return null;
        }
    }

    /**
     * Download some file using GET method;
     *
     * @param url - url where file is located
     * @return
     */
    public static InputStream downloadFile(String url) {
        try {
            URL urlClass = new URL(url);

            HttpURLConnection c;
            c = (HttpURLConnection) urlClass.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            return c.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to get a final url from redirected link
     *
     * @param url - the initial url
     * @return - the final url
     */
    public static String getRedirectedUrl(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");

            System.out.println("Request URL ... " + url);

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {
                return conn.getHeaderField("Location");
            } else {
                return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    public void connectForMultipart(String url, int method, ArrayList<NameValuePair> params) throws Exception {
//        HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
//        con.setRequestMethod("POST");
//        con.setDoInput(true);
//        con.setDoOutput(true);
//        con.setRequestProperty("Connection", "Keep-Alive");
//        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        con.connect();
//        os = con.getOutputStream();
//    }



}
