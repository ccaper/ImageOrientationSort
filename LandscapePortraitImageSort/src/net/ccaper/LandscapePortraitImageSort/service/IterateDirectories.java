package net.ccaper.LandscapePortraitImageSort.service;

import java.io.File;

public interface IterateDirectories {

  /**
   * Returns a file found in the directory, including any sub directories. If no
   * more files exist, returns null.
   * 
   * @return a file or null if no more files.
   */
  File getFile();
}
