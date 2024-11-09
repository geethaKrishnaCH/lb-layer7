package me.easylearnz.lb.health;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HeartBeat {
  public static boolean isServerHealthy(String server) {
    try {
      URL url = new URL(server);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(1000);
      connection.setReadTimeout(1000);
      connection.setRequestMethod("HEAD");
      int responseCode = connection.getResponseCode();
      if (!(responseCode >= 200 && responseCode < 400)) {
        return false;
      }
    } catch (IOException e) {
      return false;
    }
    return true;
  }
}
