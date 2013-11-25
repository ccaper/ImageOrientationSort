package net.ccaper.ImageOrientationSort;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Driver {
  public static final Log LOG = LogFactory.getLog(Driver.class);
  @Inject
  @Named("startDirectory")
  private File startDirectory;
  @Inject
  @Named("destinationDirectory")
  private File destinationDirectory;
  @Resource(name="ignoreFiles")
  private List<File> ignoreDirectories;
  @Resource(name="ignoreDirectories")
  private List<File> ignoreFiles;
  @Inject
  private IterateDirectories iterateDirectories;
  @Inject
  private ImageOrientationUtil imageOrientationUtils;
  @Inject
  private CopyImage copyImage;
  @Resource(name="imageTypesAllowed")
  private String[] imageTypesAllowed;

  /**
   * Sets the start directory
   * @param startDirectory The start directory
   */
  public void setStartDirectory(File startDirectory) {
    this.startDirectory = startDirectory;
  }

  /**
   * Sets the destination directory
   * @param destinationDirectory The destination directory
   */
  public void setDestinationDirectory(File destinationDirectory) {
    this.destinationDirectory = destinationDirectory;
  }

  /**
   * Sets the ignore directories
   * @param ignoreDirectories The ignore directories
   */
  public void setIgnoreDirectories(List<File> ignoreDirectories) {
    this.ignoreDirectories = ignoreDirectories;
  }

  /**
   * Sets the ignore files
   * @param ignoreFiles The ignore files
   */
  public void setIgnoreFiles(List<File> ignoreFiles) {
    this.ignoreFiles = ignoreFiles;
  }

  /**
   * Sets the iterate directories service
   * @param iterateDirectories The iterate directories service
   */
  public void setIterateDirectories(IterateDirectories iterateDirectories) {
    this.iterateDirectories = iterateDirectories;
  }

  /**
   * Sets the image orientation utility
   * @param imageOrientationUtils The image orientation utility
   */
  public void setImageOrientationUtils(ImageOrientationUtil imageOrientationUtils) {
    this.imageOrientationUtils = imageOrientationUtils;
  }

  /**
   * Sets the copy image service
   * @param copyImage The copy image service
   */
  public void setCopyImage(CopyImage copyImage) {
    this.copyImage = copyImage;
  }

  /**
   * Sets the image types allowed
   * @param imageTypesAllowed The image types allowed
   */
  public void setImageTypesAllowed(String[] imageTypesAllowed) {
    this.imageTypesAllowed = imageTypesAllowed;
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
    AnnotationConfigApplicationContext context = null;
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
      context.close();
    }
  }
}
