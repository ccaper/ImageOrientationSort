package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IterateDirectoriesImpl implements IterateDirectories {
  private static final Log LOG = LogFactory
      .getLog(IterateDirectoriesImpl.class);
  // TODO: move to app config
  static final String[] IMAGE_TYPES = new String[] { "jpg", "jpeg" };
  // TODO: move to app config, pass in for testing
  private static final FilenameFilter IMAGE_FILE_FILTER = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      for (String extension : IMAGE_TYPES) {
        if (name.toLowerCase().endsWith("." + extension)) {
          return true;
        }
      }
      return false;
    }
  };
  private static final FilenameFilter DIRECTORY_FILTER = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      return new File(dir, name).isDirectory();
    }
  };
  private final Queue<File> files = new LinkedList<File>();
  private final Queue<File> dirs = new LinkedList<File>();

  public IterateDirectoriesImpl(File startDirectory) {
    files.addAll(Arrays.asList(startDirectory.listFiles(IMAGE_FILE_FILTER)));
    dirs.addAll(Arrays.asList(startDirectory.listFiles(DIRECTORY_FILTER)));
  }

  @Override
  public File getFile() {
    if (!files.isEmpty()) {
      return files.remove();
    } else if (!dirs.isEmpty()) {
      File dir = dirs.remove();
      files.addAll(Arrays.asList(dir.listFiles(IMAGE_FILE_FILTER)));
      dirs.addAll(Arrays.asList((dir.listFiles(DIRECTORY_FILTER))));
      return getFile();
    } else {
      return null;
    }
  }
}
