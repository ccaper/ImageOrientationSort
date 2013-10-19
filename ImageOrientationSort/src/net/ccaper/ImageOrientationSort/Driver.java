package net.ccaper.ImageOrientationSort;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.ccaper.ImageOrientationSort.enums.ImageOrientation;
import net.ccaper.ImageOrientationSort.service.CopyImage;
import net.ccaper.ImageOrientationSort.service.IterateDirectories;
import net.ccaper.ImageOrientationSort.spring.AppConfig;
import net.ccaper.ImageOrientationSort.utils.ImageOrientationUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Driver {
  public static final Log LOG = LogFactory.getLog(Driver.class);
  private final File startDirectory;
  private final File destinationDirectory;
  private final List<File> ignoreDirectories;
  private final List<File> ignoreFiles;
  private final IterateDirectories iterateDirectories;
  private final ImageOrientationUtil imageOrientationUtils;
  private final CopyImage copyImage;
  private final String[] imageTypesAllowed;

  @SuppressWarnings("unchecked")
  @Inject
  public Driver(@Named("startDirectory") File startDirectory,
      @Named("destinationDirectory") File destinationDirectory,
      @Named("ignoreFiles") Object ignoreFilesObject,
      @Named("ignoreDirectories") Object ignoreDirectoriesObject,
      @Named("imageTypesAllowed") Object imageTypesAllowed,
      IterateDirectories iterateDirectories,
      ImageOrientationUtil imageOrientationUtils, CopyImage copyImage)
          throws Exception {
    this.startDirectory = startDirectory;
    this.destinationDirectory = destinationDirectory;
    this.ignoreFiles = (List<File>) ignoreFilesObject;
    this.ignoreDirectories = (List<File>) ignoreDirectoriesObject;
    this.imageTypesAllowed = (String[]) imageTypesAllowed;
    this.iterateDirectories = iterateDirectories;
    this.imageOrientationUtils = imageOrientationUtils;
    this.copyImage = copyImage;
  }

  public void validateStartAndDestinationDirectories() throws Exception {
    if (!isExists(startDirectory) || !isReadable(startDirectory)) {
      throw new Exception(String.format("The start directory '%s' is invalid.",
          startDirectory.getAbsolutePath()));
    }
    if (!isExists(destinationDirectory) || !isWritable(destinationDirectory)) {
      throw new Exception(String.format(
          "The destination directory '%s' is invalid.",
          destinationDirectory.getAbsolutePath()));
    }
  }

  public void logStartMessages() {
    Driver.LOG.info("Starting Landscape Portrait Image Sort");
    Driver.LOG.info("Start directory: " + startDirectory);
    Driver.LOG.info("Destination directory: " + destinationDirectory);
    Driver.LOG.info("Ignore directories: "
        + StringUtils.join(ignoreDirectories, ", "));
    Driver.LOG.info("Ignore files: " + StringUtils.join(ignoreFiles, ", "));
    Driver.LOG.info("Filtering files for extensions: "
        + StringUtils.join(imageTypesAllowed, ", "));
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
        imageOrientationUtils.getNumberLandscapeOrientationImages()));
    Driver.LOG.info(String.format("Number of portrait orientation images: %d",
        imageOrientationUtils.getNumberPortraitOrientationImages()));
    Driver.LOG.info(String.format("Number of equal orientation images: %d",
        imageOrientationUtils.getNumberEqualOrientationImages()));
    Driver.LOG.info(String.format(
        "Number of images where orientation could not be determined: %d",
        imageOrientationUtils.getNumberOrientationProblems()));
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
      ImageOrientation orientation = imageOrientationUtils
          .getImageOrientation(file);
      if (orientation != null) {
        File copiedFile = copyImage.copyImageToOrientationDirectory(file,
            orientation);
        Driver.LOG.info(String.format(
            "File '%s' was found to have orientation %s, and was copied '%s'",
            file.getAbsolutePath(), orientation, copiedFile.getAbsolutePath()));
      }
      file = iterateDirectories.getFile();
    }
  }

  // visible for testing
  static boolean isExists(File file) {
    if (!file.exists()) {
      Driver.LOG.error(String.format("The directory '%s' does not exist.",
          file.getAbsolutePath()));
      return false;
    }
    return true;
  }

  // visible for testing
  static boolean isReadable(File file) {
    if (!file.canRead()) {
      Driver.LOG.error(String.format("The directory '%s' is not readable.",
          file.getAbsolutePath()));
      return false;
    }
    return true;
  }

  // visible for testing
  static boolean isWritable(File file) {
    if (!file.canWrite()) {
      Driver.LOG.error(String.format("The directory '%s' is not writable.",
          file.getAbsolutePath()));
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    ApplicationContext context = null;
    try {
      context = new AnnotationConfigApplicationContext(AppConfig.class);
      Driver driver = context.getBean(Driver.class);
      driver.validateStartAndDestinationDirectories();
      driver.logStartMessages();
      driver.copyImages();
      driver.logEndMessages();
      ((ConfigurableApplicationContext) context).close();
    } catch (Exception e) {
      Driver.LOG.fatal("A fatal error occurred, abnormal exit.", e);
      System.exit(1);
    } finally {
      ((ConfigurableApplicationContext) context).close();
    }
  }
}
