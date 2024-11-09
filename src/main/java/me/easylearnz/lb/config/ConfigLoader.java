package me.easylearnz.lb.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigLoader {
  private String configFile;

  public ConfigLoader(String configFilePath) {
    this.configFile = configFilePath;
  }

  public Configuration loadConfig() {

    try (InputStream ioStream = ConfigLoader.class.getClassLoader().getResourceAsStream(configFile);
        InputStreamReader isr = new InputStreamReader(ioStream);
        BufferedReader bfr = new BufferedReader(isr)) {
      StringBuilder contentBuilder = new StringBuilder();
      String line;
      while ((line = bfr.readLine()) != null) {
        contentBuilder.append(line);
      }
      System.out.println(contentBuilder.toString());
      String content = contentBuilder.toString();
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(content, Configuration.class);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
