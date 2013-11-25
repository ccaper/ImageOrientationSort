package net.ccaper.ImageOrientationSort.serviceImpl;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import net.ccaper.ImageOrientationSort.enums.ImageOrientation;
import net.ccaper.ImageOrientationSort.service.CopyImage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("copyImageService")
public class CopyImageImpl implements CopyImage {
  private static final Log LOG = LogFactory.getLog(CopyImageImpl.class);
  private final File startDirectory;
  private final File destinationDirectory;
  private int numberFileCopySuccesses = 0;
  private int numberFileCopyFailures = 0;

  /**
   * 
   * @param startDirectory
   *          The start directory
   * @param destinationDirectory
   *          The destination directory
   */
  @Inject
  public CopyImageImpl(@Named("startDirectory") File startDirectory,
      @Named("destinationDirectory") File destinationDirectory) {
    this.startDirectory = startDirectory;
    this.destinationDirectory = destinationDirectory;
  }

  @Override
  public File copyImageToOrientationDirectory(File file,
      ImageOrientation orientation) {
    File destinationFile = createDestinationFile(file, orientation);
    try {
      copyFile(file, destinationFile);
      ++numberFileCopySuccesses;
      return destinationFile;
    } catch (IOException e) {
      LOG.error(String.format("Could not copy '%s' to '%s'",
          file.getAbsolutePath(), destinationFile.getAbsolutePath()), e);
      ++numberFileCopyFailures;
      return null;
    }
  }

  /**
   * Creates the destination file, without touching file system, including the
   * image orientation into the path, and maintaining the original file's
   * subfolder structure after the start directory. For example, with a start
   * directory of "/startDir" and a destination of "/destinationDir" and an
   * image file with path "/startDir/dir2/imageFile.jpg" and an orientation of
   * landscape, the destination file will be
   * "/destinationDir/dir2/imageFile.jpg". In isolated method to control
   * testing.
   * 
   * @param originalFile
   *          The original file
   * @param orientation
   *          The image orientation
   * @return
   */
  // visible for testing
  File createDestinationFile(File originalFile, ImageOrientation orientation) {
    return new File(new File(destinationDirectory,
        orientation.getDirectoryName()),
        getFilePathAfterStartDirectory(originalFile));
  }

  /**
   * Copies the file. In isolated method to control testing.
   * 
   * @param sourceFile
   *          The source file
   * @param destinationFile
   *          The destination file
   * @throws IOException
   *           thrown upon copy failure
   */
  // visible for testing
  void copyFile(File sourceFile, File destinationFile) throws IOException {
    FileUtils.copyFile(sourceFile, destinationFile);
  }

  /**
   * Gets the file's path after the start directory.
   * 
   * @param file
   *          The file
   * @return The file's path after the start directory
   */
  // visible for testing
  String getFilePathAfterStartDirectory(File file) {
    if (file == null) {
      return null;
    }
    return file.getAbsolutePath().substring(
        startDirectory.getAbsolutePath().length());
  }

  @Override
  public int getNumberFileCopySuccess() {
    return numberFileCopySuccesses;
  }

  @Override
  public int getNumberFileCopyFailures() {
    return numberFileCopyFailures;
  }
}
