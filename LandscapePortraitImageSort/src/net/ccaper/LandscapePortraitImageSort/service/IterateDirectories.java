package net.ccaper.LandscapePortraitImageSort.service;

import java.io.File;

/**
 * @author ccaper (christian.caperton@gmail.com)
 * 
 *         Returns a file in a directory, traversing sub directories, until
 *         there are no more files. Only objects kept in memory are contents of
 *         current directory in an attempt to keep memory foot print small.
 *         Allows list of ignore files and ignore directories to be passed in.
 *         If a directory is in the ignore directory list, all sub directories
 *         of that directory are ignored.
 * 
 */
public interface IterateDirectories {

  /**
   * Returns a file found in the directory, including any sub directories. If no
   * more files exist, returns null.
   * 
   * @return a file or null if no more files exist.
   */
  File getFile();
}
