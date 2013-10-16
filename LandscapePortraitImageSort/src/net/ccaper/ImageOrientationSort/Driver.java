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

// TODO: refactor all refs to LandscapePortrait to ImageOrientation
@Component
public class Driver {
  public static final Log LOG = LogFactory.getLog(Driver.class);
  @Inject
  @Named("startDirectory")
  private File startDirectory;
  @Inject
  @Named("destinationDirectory")
  private File destinationDirectory;
  @Inject
  @Named("ignoreDirectories")
  private Object ignoreDirectoriesObject;
  private final List<File> ignoreDirectories;
  @Inject
  @Named("ignoreFiles")
  private Object ignoreFilesObject;
  private final List<File> ignoreFiles;
  @Inject
  private IterateDirectories iterateDirectories;
  @Inject
  private ImageOrientationUtil landscapePortraitUtils;
  @Inject
  private CopyImage copyImage;

  @SuppressWarnings("unchecked")
  public Driver() throws Exception {
    ignoreDirectories = (List<File>) ignoreDirectoriesObject;
    ignoreFiles = (List<File>) ignoreFilesObject;
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
