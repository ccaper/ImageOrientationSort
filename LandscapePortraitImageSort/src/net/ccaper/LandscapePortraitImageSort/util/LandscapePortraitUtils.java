package net.ccaper.LandscapePortraitImageSort.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LandscapePortraitUtils {
  private static final Log LOG = LogFactory
      .getLog(LandscapePortraitUtils.class);

  public enum Orientation {
    LANDSCAPE, PORTRAIT, EQUAL
  };

  // TODO: junit
  // TODO: refactor
  public static Orientation getImageOrientation(File file) {
    int height = 0;
    int width = 0;
    String fileExtension = getFileExtension(file);
    ImageReader imageReader = getImageReaderForImageFile(fileExtension);
    if (imageReader == null) {
      LOG.error(String.format(
          "Could not find an image reader for image type '%s' for file '%s'.",
          fileExtension, file.getAbsolutePath()));
    }
    ImageInputStream stream = null;
    try {
      stream = new FileImageInputStream(file);
      imageReader.setInput(stream);
      width = imageReader.getWidth(imageReader.getMinIndex());
      height = imageReader.getHeight(imageReader.getMinIndex());
    } catch (IOException e) {
      LOG.error(String.format("IOException while reading %s.",
          file.getAbsoluteFile()), e);
    } finally {
      imageReader.dispose();
      try {
        stream.close();
      } catch (IOException e) {
        LOG.error(
            String.format("IOException while closing %s.",
                file.getAbsoluteFile()), e);
      }
    }
    return getOrientationFromDimensions(width, height);
  }

  // visible for testing
  static ImageReader getImageReaderForImageFile(String fileExtension) {
    if (StringUtils.isEmpty(fileExtension)) {
      return null;
    }
    Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(fileExtension);
    if (iter.hasNext()) {
      return iter.next();
    } else {
      return null;
    }
  }

  // visible for testing
  static String getFileExtension(File file) {
    if (file == null) {
      return null;
    }
    if (!file.getName().contains(".")) {
      return null;
    }
    return file.getName().substring(file.getName().lastIndexOf('.') + 1);
  }

  // visible for testing
  static Orientation getOrientationFromDimensions(int width, int height) {
    if (height > width) {
      return Orientation.PORTRAIT;
    } else if (height < width) {
      return Orientation.LANDSCAPE;
    } else {
      return Orientation.EQUAL;
    }
  }
}
