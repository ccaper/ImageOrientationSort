package net.ccaper.LandscapePortraitImageSort;

import java.io.File;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.enums.ImageOrientation;
import net.ccaper.LandscapePortraitImageSort.service.CopyImage;
import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;
import net.ccaper.LandscapePortraitImageSort.serviceImpl.CopyImageImpl;
import net.ccaper.LandscapePortraitImageSort.serviceImpl.IterateDirectoriesImpl;
import net.ccaper.LandscapePortraitImageSort.spring.AppConfig;
import net.ccaper.LandscapePortraitImageSort.util.LandscapePortraitOrientationUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Driver {
  public static final Log LOG = LogFactory.getLog(Driver.class);
  private ApplicationContext context = new AnnotationConfigApplicationContext(
      AppConfig.class);
  private File startDirectory;
  private File destinationDirectory;
  private List<File> ignoreDirectories;
  private List<File> ignoreFiles;
  private IterateDirectories iterateDirectories;
  private LandscapePortraitOrientationUtils landscapePortraitUtils;
  private CopyImage copyImage;
  
  @SuppressWarnings("unchecked")
  public Driver() {
    startDirectory = getAndVerifyStartDirectory(context);
    destinationDirectory = getAndVerifyDestinationDirectory(context);
    ignoreDirectories = (List<File>) context
        .getBean("ignoreDirectories");
    ignoreFiles = (List<File>) context.getBean("ignoreFiles");
    iterateDirectories = new IterateDirectoriesImpl(
        startDirectory, ignoreFiles, ignoreDirectories);
    landscapePortraitUtils = new LandscapePortraitOrientationUtils();
    copyImage = new CopyImageImpl(startDirectory,
        destinationDirectory);
  }

  public static File getAndVerifyStartDirectory(ApplicationContext context) {
    File startDirectory = (File) context.getBean("startDirectory");
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

  public static File getAndVerifyDestinationDirectory(ApplicationContext context) {
    File destinationDirectory = (File) context.getBean("destinationDirectory");
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

  public void logStartMessages() {
    Driver.LOG.info("Starting Landscape Portrait Image Sort");
    Driver.LOG.info("Start directory: " + startDirectory);
    Driver.LOG.info("Destination directory: " + destinationDirectory);
    Driver.LOG.info("Ignore directories: "
        + StringUtils.join(ignoreDirectories, ", "));
    Driver.LOG.info("Ignore files: " + StringUtils.join(ignoreFiles, ", "));
    Driver.LOG.info("Filtering files for extensions: "
        + StringUtils.join(AppConfig.IMAGE_TYPES, ", "));
  }

  public void logEndMessages() {
    Driver.LOG.info(String.format("Number files found: %d",
        iterateDirectories.getNumberFilesFound()));
    Driver.LOG.info(String.format("Number files skipped: %d",
        iterateDirectories.getNumberFilesSkipped()));
    Driver.LOG.info(String.format("Number files not skipped: %d",
        iterateDirectories.getNumberFilesNotSkipped()));
    Driver.LOG.info(String.format("Number directories found: %d",
        iterateDirectories.getNumberDirectoriesFound()));
    Driver.LOG.info(String.format("Number directories skipped: %d",
        iterateDirectories.getNumberDirectoriesSkipped()));
    Driver.LOG.info(String.format("Number directories not skipped: %d",
        iterateDirectories.getNumberDirectoriesNotSkipped()));
    Driver.LOG.info(String.format("Number non image files: %d",
        iterateDirectories.getNumberNonImageFiles()));
    Driver.LOG.info(String.format("Number of landscape orientation images: %d",
        landscapePortraitUtils.getNumberLandscapeOrientationImages()));
    Driver.LOG.info(String.format("Number of portrait orientation images: %d",
        landscapePortraitUtils.getNumberPortraitOrientationImages()));
    Driver.LOG.info(String.format("Number of equal orientation images: %d",
        landscapePortraitUtils.getNumberEqualOrientationImages()));
    Driver.LOG.info(String.format(
        "Number of images where orientation could not be determined: %d",
        landscapePortraitUtils.getNumberOrientationProblems()));
    Driver.LOG.info(String.format("Number of images successfully copied: %d",
        copyImage.getNumberFileCopySuccess()));
    Driver.LOG.info(String.format(
        "Number of images not successfully copied: %d",
        copyImage.getNumberFileCopyFailures()));
    Driver.LOG.info("Ending Landscape Portrait Image Sort");
  }

  public void copyImages() {
    File file = iterateDirectories.getFile();
    while (file != null) {
      ImageOrientation orientation = landscapePortraitUtils
          .getImageOrientation(file);
      if ((file != null) && orientation != null) {
        File copiedFile = copyImage.copyImageToOrientationDirectory(file,
            orientation);
        Driver.LOG.info(String.format(
            "File '%s' was found to have orientation %s, and was copied '%s'",
            file.getAbsolutePath(), orientation, copiedFile.getAbsolutePath()));
      }
      file = iterateDirectories.getFile();
    }
  }

  // TODO: unit test helper methods
  public static void main(String[] args) {
    try {
      Driver driver = new Driver();
      driver.logStartMessages();
      driver.copyImages();
      driver.logEndMessages();
    } catch (Exception e) {
      Driver.LOG.fatal("A fatal error occurred, abnormal exit.", e);
    }
  }
}
