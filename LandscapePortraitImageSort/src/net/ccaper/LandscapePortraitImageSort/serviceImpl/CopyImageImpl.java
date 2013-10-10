package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.IOException;

import net.ccaper.LandscapePortraitImageSort.enums.ImageOrientation;
import net.ccaper.LandscapePortraitImageSort.service.CopyImage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CopyImageImpl implements CopyImage {
  private static final Log LOG = LogFactory.getLog(CopyImageImpl.class);
  private final File startDirectory;
  private final File destinationDirectory;

  // TODO: implement stats

  public CopyImageImpl(File startDirectory, File destinationDirectory) {
    this.startDirectory = startDirectory;
    this.destinationDirectory = destinationDirectory;
  }

  @Override
  public File copyImageToOrientationDirectory(File file,
      ImageOrientation orientation) {
    File destinationFile = createDestinationFile(file, orientation);
    boolean copySuccess = copyFile(file, destinationFile);
    if (copySuccess) {
      // TODO: increment positive counter
      return destinationFile;
    } else {
      // TODO: increment negative counter
      return null;
    }
  }

  //visible for testing
  // TODO: junit?
  File createDestinationFile(File originalFile, ImageOrientation orientation) {
    return new File(new File(destinationDirectory,
        orientation.getDirectoryName()), getFilePathAfterStartDirectory(originalFile));
  }

  // visible for testing
  boolean copyFile(File sourceFile, File destinationFile) {
    try {
      FileUtils.copyFile(sourceFile, destinationFile);
      return true;
    } catch (IOException e) {
      LOG.error(
          String.format("Could not copy '%s' to '%s'",
              sourceFile.getAbsolutePath(), destinationFile.getAbsolutePath()),
              e);
      return false;
    }
  }

  // visible for testing
  String getFilePathAfterStartDirectory(File file) {
    if (file == null) {
      return null;
    }
    return file.getAbsolutePath().replaceFirst(
        startDirectory.getAbsolutePath(), "");
  }
}
