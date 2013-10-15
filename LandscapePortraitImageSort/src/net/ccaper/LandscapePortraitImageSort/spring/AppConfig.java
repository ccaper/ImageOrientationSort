package net.ccaper.LandscapePortraitImageSort.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.service.CopyImage;
import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;
import net.ccaper.LandscapePortraitImageSort.serviceImpl.CopyImageImpl;
import net.ccaper.LandscapePortraitImageSort.serviceImpl.IterateDirectoriesImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.StringUtils;

// TODO: switch to auto-wiring
@Configuration
@ImportResource("classpath:/net/ccaper/LandscapePortraitImageSort/spring/spring-config.xml")
public class AppConfig {
  // visible for testing
  static final String MS_FILE_DELIMETER = "\\";
  // visible for testing
  static final String UNIX_FILE_DELIMETER = "/";
  // visible for testing
  static final String LIST_SEPARATOR = ";";
  private @Value("${start_directory}")
  String startDirectoryString;
  private @Value("${destination_directory}")
  String destinationDirectoryString;
  private @Value("${ignore_directories}")
  String ignoreDirectoriesString;
  private @Value("${ignore_files}")
  String ignoreFilesString;
  public static final String[] IMAGE_TYPES = new String[] { "jpg", "jpeg" };

  @Bean(name = "startDirectory")
  public File startDirectory() {
    return generateFileFromString(startDirectoryString);
  }

  @Bean(name = "destinationDirectory")
  public File destinationDirectory() {
    return generateFileFromString(destinationDirectoryString);
  }

  @Bean(name = "ignoreDirectories")
  public List<File> ignoreDirectories() {
    return generateFilesFromString(ignoreDirectoriesString);
  }

  @Bean(name = "ignoreFiles")
  public List<File> ignoreFiles() {
    return generateFilesFromString(ignoreFilesString);
  }

  @Bean
  public CopyImage copyImage() {
    return new CopyImageImpl(startDirectory(), destinationDirectory());
  }

  // visible for testing
  static File generateFileFromString(String string) {
    return new File(convertSlashToOsFileDelimiter(
        StringUtils.trimWhitespace(string), File.separator));
  }

  // visible for testing
  static List<File> generateFilesFromString(String string) {
    if (string == null) {
      return null;
    }
    List<File> files = new ArrayList<File>();
    for (String trimmedString : StringUtils.trimWhitespace(string).split(
        LIST_SEPARATOR)) {
      files.add(generateFileFromString(trimmedString));
    }
    return files;
  }

  // visible for testing
  static String convertSlashToOsFileDelimiter(String string,
      String fileDelimiter) {
    if (MS_FILE_DELIMETER.equals(fileDelimiter)) {
      if (string == null) {
        return null;
      } else {
        // TODO: add not in readme that ms must be \\ in prop file
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