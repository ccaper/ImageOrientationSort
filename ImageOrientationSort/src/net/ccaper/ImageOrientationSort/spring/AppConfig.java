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
  @Value("${start_directory}")
  private String startDirectoryString;
  @Value("${destination_directory}")
  private String destinationDirectoryString;
  @Value("${ignore_directories}")
  private String ignoreDirectoriesString;
  @Value("${ignore_files}")
  private String ignoreFilesString;
  @Value("${image_types_allowed}")
  private String imageTypeAllowed;

  @Bean(name = "imageTypesAllowed")
  String[] imageTypesAllowed() {
    return StringUtils.split(imageTypeAllowed, FileSystemUtil.LIST_SEPARATOR);
  }

  @Bean(name = "startDirectory")
  File startDirectory() {
    return FileSystemUtil.generateFileFromString(startDirectoryString);
  }

  @Bean(name = "destinationDirectory")
  File destinationDirectory() {
    return FileSystemUtil.generateFileFromString(destinationDirectoryString);
  }

  @Bean(name = "ignoreDirectories")
  List<File> ignoreDirectories() {
    return FileSystemUtil.generateFilesFromString(ignoreDirectoriesString);
  }

  @Bean(name = "ignoreFiles")
  List<File> ignoreFiles() {
    return FileSystemUtil.generateFilesFromString(ignoreFilesString);
  }
}