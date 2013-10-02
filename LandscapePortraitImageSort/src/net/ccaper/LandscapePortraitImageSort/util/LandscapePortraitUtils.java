package net.ccaper.LandscapePortraitImageSort.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

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
  public static Orientation getOrientation(File file) {
    int height = 0;
    int width = 0;
    String suffix = file.getName().substring(
        file.getName().lastIndexOf('.') + 1);
    Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
    if (iter.hasNext()) {
      ImageReader reader = iter.next();
      ImageInputStream stream = null;
      try {
        stream = new FileImageInputStream(file);
        reader.setInput(stream);
        width = reader.getWidth(reader.getMinIndex());
        height = reader.getHeight(reader.getMinIndex());
      } catch (IOException e) {
        LOG.error(
            String.format("IOException while reading %s.",
                file.getAbsoluteFile()), e);
      } finally {
        reader.dispose();
        try {
          stream.close();
        } catch (IOException e) {
          LOG.error(
              String.format("IOException while closing %s.",
                  file.getAbsoluteFile()), e);
        }
      }
    }
    return getOrientationFromDimensions(width, height);
  }

  // TODO: junit
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
