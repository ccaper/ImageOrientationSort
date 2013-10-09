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

  public CopyImageImpl(File startDirectory, File destinationDirectory) {
    this.startDirectory = startDirectory;
    this.destinationDirectory = destinationDirectory;
  }

  @Override
  public void copyImageToOrientationDirectory(File file,
      ImageOrientation orientation) {
    String imageFilePathAfterStartDirectory = getFilePathAfterStartDirectory(file);
    File destinationFile = new File(destinationDirectory,
        imageFilePathAfterStartDirectory);
    try {
      FileUtils.copyFile(file, destinationFile);
    } catch (IOException e) {
      LOG.error(String.format("Could not copy '%s' to '%s'",
          file.getAbsolutePath(), destinationFile.getAbsolutePath()), e);
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
