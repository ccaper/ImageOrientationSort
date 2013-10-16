package net.ccaper.ImageOrientationSort.service;

import java.io.File;

import net.ccaper.ImageOrientationSort.enums.ImageOrientation;

/**
 * 
 * @author ccaper (christian.caperton@gmail.com)
 * 
 *         Copies an image file from original location to a destination location
 *         appending either landscape or portrait depending on the image
 *         orientation type, while maintaining the original files sub folder
 *         structure after the start directory.
 * 
 */
public interface CopyImage {

  /**
   * Copies an image file from original location to a destination location
   * appending either landscape or portrait depending on the image orientation
   * type, while maintaining the original files sub folder structure after the
   * start directory
   * 
   * @param file
   *          The file to copy
   * @param orientation
   *          The image orientation
   * @return The destination file
   */
  File copyImageToOrientationDirectory(File file, ImageOrientation orientation);

  /**
   * Gets the count of files successfully copied
   * 
   * @return The count of files successfully copied
   */
  int getNumberFileCopySuccess();

  /**
   * Gets the count of files not successfully copied
   * 
   * @return The count of files not successfully copied
   */
  int getNumberFileCopyFailures();
}
