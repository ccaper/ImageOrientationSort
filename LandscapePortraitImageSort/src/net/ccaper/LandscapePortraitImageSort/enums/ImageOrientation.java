package net.ccaper.LandscapePortraitImageSort.enums;

public enum ImageOrientation {
  LANDSCAPE("landscape"),
  PORTRAIT("portrait"),
  EQUAL("portrait");

  private final String directoryName;

  private ImageOrientation(String directoryName) {
    this.directoryName = directoryName;
  }

  public String getDirectoryName() {
    return directoryName;
  }
}
