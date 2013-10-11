package net.ccaper.LandscapePortraitImageSort.enums;

/**
 * 
 * @author ccaper (christian.caperton@gmail.com)
 * 
 *         Enum for possible image orientations, and the filesystem subfolder it
 *         should map to. Equal orientation is when height and width are the
 *         same.
 * 
 */
public enum ImageOrientation {
  LANDSCAPE("landscape"), PORTRAIT("portrait"), EQUAL("portrait");

  private final String directoryName;

  private ImageOrientation(String directoryName) {
    this.directoryName = directoryName;
  }

  /**
   * Gets the filesystem subfolder the image orientation maps to.
   * 
   * @return The filesystem subfolder the image orientation maps to
   */
  public String getDirectoryName() {
    return directoryName;
  }
}
