package net.ccaper.LandscapePortraitImageSort.spring;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.StringUtils;

@Configuration
@ImportResource("classpath:/net/ccaper/LandscapePortraitImageSort.spring.properties-config.xml")
public class AppConfig {
  // visible for testing
  static final String MS_FILE_DELIMETER = "\\";
  // visible for testing
  static final String UNIX_FILE_DELIMETER = "/";
  private @Value("${start_directory}")
  String startDirectory;

  @Bean(name = "startDirectory")
  public File getStartDirectory() {
    return generateFileFromString(startDirectory);
  }

  // visible for testing
  static File generateFileFromString(String string) {
    return new File(
        convertSlashToOsFileDelimiter(StringUtils
            .trimAllWhitespace(string), File.separator));
  }

  // visible for testing
  static String convertSlashToOsFileDelimiter(String string, String fileDelimiter) {
    if (MS_FILE_DELIMETER.equals(fileDelimiter)) {
      if (string == null) {
        return null;
      } else {
        return string.replace(UNIX_FILE_DELIMETER, MS_FILE_DELIMETER);
      }
    } else if (UNIX_FILE_DELIMETER.equals(fileDelimiter)) {
      if (string == null) {
        return null;
      } else {
        return string.replace(MS_FILE_DELIMETER, UNIX_FILE_DELIMETER);
      }
    } else {
      throw new IllegalArgumentException(String.format(
          "The path %s contains illegal file delimiters.", string));
    }
  }
}
