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
  private static final char MS_FILE_DELIMETER = '\\';
  private static final char UNIX_FILE_DELIMETER = '/';
  private @Value("${start_directory}")
  String startDirectory;

  @Bean(name = "startDirectory")
  public File getStartDirectory() {
    return generateFileFromString(startDirectory);
  }

  // TODO: test
  private File generateFileFromString(String string) {
    return new File(
        convertSlashToOsFileDelimiter(StringUtils
            .trimAllWhitespace(startDirectory)));
  }

  // TODO: test
  private String convertSlashToOsFileDelimiter(String string) {
    if (File.separator.equals(MS_FILE_DELIMETER)) {
      return string.replace(UNIX_FILE_DELIMETER, MS_FILE_DELIMETER);
    } else if (File.separator.equals(UNIX_FILE_DELIMETER)) {
      return string.replace(MS_FILE_DELIMETER, UNIX_FILE_DELIMETER);
    } else {
      throw new IllegalArgumentException(String.format(
          "The path %s contains illegal file delimiters.", string));
    }
  }
}
