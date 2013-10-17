package net.ccaper.ImageOrientationSort.spring;

import java.io.File;
import java.util.List;

import net.ccaper.ImageOrientationSort.utils.FileSystemUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/net/ccaper/ImageOrientationSort/spring/spring-config.xml")
public class AppConfig {
  private @Value("${start_directory}")
  String startDirectoryString;
  private @Value("${destination_directory}")
  String destinationDirectoryString;
  private @Value("${ignore_directories}")
  String ignoreDirectoriesString;
  private @Value("${ignore_files}")
  String ignoreFilesString;
  private @Value("${image_types_allowed}")
  String imageTypeAllowed;

  @Bean(name = "imageTypesAllowed")
  public String[] imageTypesAllowed() {
    return StringUtils.split(imageTypeAllowed, FileSystemUtil.LIST_SEPARATOR);
  }

  @Bean(name = "startDirectory")
  public File startDirectory() {
    return FileSystemUtil.generateFileFromString(startDirectoryString);
  }

  @Bean(name = "destinationDirectory")
  public File destinationDirectory() {
    return FileSystemUtil.generateFileFromString(destinationDirectoryString);
  }

  @Bean(name = "ignoreDirectories")
  public List<File> ignoreDirectories() {
    return FileSystemUtil.generateFilesFromString(ignoreDirectoriesString);
  }

  @Bean(name = "ignoreFiles")
  public List<File> ignoreFiles() {
    return FileSystemUtil.generateFilesFromString(ignoreFilesString);
  }
}