package net.ccaper.ImageOrientationSort.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import net.ccaper.ImageOrientationSort.enums.ImageOrientation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author ccaper (christian.caperton@gmail.com)
 * 
 *         Determines the image orientation for a file.
 * 
 */
@Component
public class ImageOrientationUtil {
  private static final Log LOG = LogFactory
      .getLog(ImageOrientationUtil.class);
  private int numberLandscapeOrientationImages = 0;
  private int numberPortraitOrientationImages = 0;
  private int numberEqualOrientationImages = 0;
  private int numberOrientationProblems = 0;

  /**
   * Gets the image orientation for a file
   * 
   * @param file
   *          The file to determine orientation
   * @return The image orientation
   */
  public ImageOrientation getImageOrientation(File file) {
    if (file == null) {
      ++numberOrientationProblems;
      return null;
    }
    String fileExtension = getFileExtension(file);
    ImageReader imageReader = getImageReaderForImageFile(fileExtension);
    if (imageReader == null) {
      LOG.error(String.format(
          "Could not find an image reader for image type '%s' for file '%s'.",
          fileExtension, file.getAbsolutePath()));
      ++numberOrientationProblems;
      return null;
    }
    ImageOrientation imageOrientation = getOrientationFromFile(file,
        imageReader);
    if (imageOrientation == null) {
      ++numberOrientationProblems;
    } else {
      switch (imageOrientation) {
      case LANDSCAPE:
        ++numberLandscapeOrientationImages;
        break;
      case PORTRAIT:
        ++numberPortraitOrientationImages;
        break;
      case EQUAL:
        ++numberEqualOrientationImages;
        break;
      default:
        break;
      }
    }
    return imageOrientation;
  }

  /**
   * Gets the file's image orientation
   * 
   * @param file
   *          The file to determine the image orientation
   * @param imageReader
   *          An image reader
   * @return The image's orientation
   */
  // visible for testing
  ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
    ImageInputStream imageInputStream = null;
    if (imageReader == null) {
      return null;
    }
    try {
      imageInputStream = getFileImageInputStream(file);
      return getOrientationFromImageInputStream(imageInputStream, imageReader);
    } catch (IOException e) {
      LOG.error(String.format("IOException while reading %s.",
          file.getAbsolutePath()), e);
      return null;
    } finally {
      imageReader.dispose();
      try {
        if (imageInputStream != null) {
          imageInputStream.close();
        }
      } catch (IOException e) {
        LOG.error(
            String.format("IOException while closing %s.",
                file.getAbsolutePath()), e);
      }
    }
  }

  /**
   * Gets a input stream for the image file. Isolated to method to control
   * testing.
   * 
   * @param file
   *          The file to get an input stream from
   * @return An input stream
   * @throws IOException
   *           Thrown when unable to get input stream
   */
  // visible for testing
  FileImageInputStream getFileImageInputStream(File file) throws IOException {
    return new FileImageInputStream(file);
  }

  /**
   * Gets an image orientation from an input stream
   * 
   * @param imageInputStream
   *          An input stream to the image file
   * @param imageReader
   *          An image reader
   * @return An image orientation
   * @throws IOException
   *           Thrown when unable to get the image orienation
   */
  // visible for testing
  ImageOrientation getOrientationFromImageInputStream(
      ImageInputStream imageInputStream, ImageReader imageReader)
          throws IOException {
    if (imageInputStream == null) {
      return null;
    }
    imageReader.setInput(imageInputStream);
    int minIndex = imageReader.getMinIndex();
    int width = imageReader.getWidth(minIndex);
    int height = imageReader.getHeight(minIndex);
    return getOrientationFromDimensions(width, height);
  }

  /**
   * Gets the appropriate image reader for the type of image file.
   * 
   * @param fileExtension
   *          The file's extension type, used to determine the type of image
   *          reader
   * @return An image reader
   */
  // visible for testing
  ImageReader getImageReaderForImageFile(String fileExtension) {
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

  /**
   * Gets the file extension from a file
   * 
   * @param file
   *          The file to get the file extension
   * @return The file extension
   */
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

  /**
   * Gets the image orientation from the image dimensions.
   * 
   * @param width
   *          The width of the image
   * @param height
   *          The height of the image
   * @return The image orientation
   */
  // visible for testing
  static ImageOrientation getOrientationFromDimensions(int width, int height) {
    if (height > width) {
      return ImageOrientation.PORTRAIT;
    } else if (height < width) {
      return ImageOrientation.LANDSCAPE;
    } else {
      return ImageOrientation.EQUAL;
    }
  }

  /**
   * Gets the number of landscape orientation images
   * 
   * @return the number of landscape orientation images
   */
  public int getNumberLandscapeOrientationImages() {
    return numberLandscapeOrientationImages;
  }

  /**
   * Gets the number of portrait orientation images
   * 
   * @return the number of portrait orientation images
   */
  public int getNumberPortraitOrientationImages() {
    return numberPortraitOrientationImages;
  }

  /**
   * Gets the number of equal orientation images
   * 
   * @return the number of equal orientation images
   */
  public int getNumberEqualOrientationImages() {
    return numberEqualOrientationImages;
  }

  /**
   * Gets the number of images where orientation could not be determined
   * 
   * @return the number of images where orientation could not be determined
   */
  public int getNumberOrientationProblems() {
    return numberOrientationProblems;
  }
}
