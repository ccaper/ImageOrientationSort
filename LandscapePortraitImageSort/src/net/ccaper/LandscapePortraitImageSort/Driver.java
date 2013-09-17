package net.ccaper.LandscapePortraitImageSort;

import java.io.File;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.spring.AppConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Driver {
  public static final Log LOG = LogFactory.getLog(Driver.class);

  public static void main(String[] args) {
    @SuppressWarnings("resource")
    ApplicationContext context = new AnnotationConfigApplicationContext(
        AppConfig.class);
    File startDirectory = (File) context.getBean("startDirectory");
    File destinationDirectory = (File) context.getBean("destinationDirectory");
    @SuppressWarnings("unchecked")
    List<File> ignoreDirectories = (List<File>) context.getBean("ignoreDirectories");
    @SuppressWarnings("unchecked")
    List<File> ignoreFiles = (List<File>) context.getBean("ignoreFiles");

    Driver.LOG.info("Starting Landscape Portrait Image Sort");
    Driver.LOG.info("Start directory: " + startDirectory);
    Driver.LOG.info("Destination directory: " + destinationDirectory);
    Driver.LOG.info("Ignore directories: " + StringUtils.join(ignoreDirectories, ", "));
    Driver.LOG.info("Ignore files: " + StringUtils.join(ignoreFiles, ", "));
    Driver.LOG.info("Ending Landscape Portrait Image Sort");
  }

}
