package me.aquavit.liquidsense.utils.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.*;

public class HttpUtils {

    public HttpUtils(){
        HttpURLConnection.setFollowRedirects(true);
    }

    private static HttpURLConnection make(String url, String method, String agent) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url)).openConnection();
        httpConnection.setRequestMethod(method);
        httpConnection.setConnectTimeout(2000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        httpConnection.setInstanceFollowRedirects(true);
        httpConnection.setDoOutput(true);
        return httpConnection;
    }

    public static String request(String url, String method, String agent) throws IOException {
        HttpURLConnection connection = make(url, method, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static String get(String url) throws IOException {
        return request(url, "GET",null);
    }

    public static void download(String url, File file) throws IOException {
        FileUtils.copyInputStreamToFile(make(url, "GET", null).getInputStream(), file);
    }

    public static String getCurrentVersion() throws Exception {
        URL url = new URL("https://liquidsense.mingerxd.me/version.json");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        String result = getResultFormStream(urlConnection.getInputStream());
        JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        return json.getAsJsonObject(LiquidBounce.MINECRAFT_VERSION).get("version").getAsString();
    }

    public static String getResultFormStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }
        return builder.toString();
    }

}
