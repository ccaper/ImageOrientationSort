package net.ccaper.LandscapePortraitImageSort.service;

import java.io.File;

import net.ccaper.LandscapePortraitImageSort.enums.ImageOrientation;

public interface CopyImage {
  public File copyImageToOrientationDirectory(File file,
      ImageOrientation orientation);
}
