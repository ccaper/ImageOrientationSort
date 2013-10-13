package net.ccaper.LandscapePortraitImageSort.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.Driver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.StringUtils;

@Configuration
@ImportResource("classpath:/net/ccaper/LandscapePortraitImageSort/spring/properties-config.xml")
public class AppConfig {
  public static final Log LOG = LogFactory.getLog(AppConfig.class);
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
  String ignoreDirectories;
  private @Value("${ignore_files}")
  String ignoreFiles;
  public static final String[] IMAGE_TYPES = new String[] { "jpg", "jpeg" };

  // TODO: check unit test coverage
  @Bean(name = "startDirectory")
  public File getStartDirectory() {
    File startDirectory = generateFileFromString(startDirectoryString);
    if (!startDirectory.exists()) {
      Driver.LOG.error(String.format(
          "The start directory '%s' does not exist.",
          startDirectory.getAbsolutePath()));
      System.exit(1);
    }
    if (!startDirectory.canRead()) {
      Driver.LOG.error(String.format(
          "The start directory '%s' is not readable.",
          startDirectory.getAbsolutePath()));
      System.exit(1);
    }
    return startDirectory;
  }

  // TODO: check unit test coverage
  @Bean(name = "destinationDirectory")
  public File getDestinationDirectory() {
    File destinationDirectory = generateFileFromString(destinationDirectoryString);
    if (!destinationDirectory.exists()) {
      Driver.LOG.error(String.format(
          "The destination directory '%s' does not exist.",
          destinationDirectory.getAbsolutePath()));
      System.exit(1);
    }
    if (!destinationDirectory.canWrite()) {
      Driver.LOG.error(String.format(
          "The destination directory '%s' is not writable.",
          destinationDirectory.getAbsolutePath()));
      System.exit(1);
    }
    return destinationDirectory;
  }

  @Bean(name = "ignoreDirectories")
  public List<File> getIgnoreDirectories() {
    return generateFilesFromString(ignoreDirectories);
  }

  @Bean(name = "ignoreFiles")
  public List<File> getIgnoreFiles() {
    return generateFilesFromString(ignoreFiles);
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