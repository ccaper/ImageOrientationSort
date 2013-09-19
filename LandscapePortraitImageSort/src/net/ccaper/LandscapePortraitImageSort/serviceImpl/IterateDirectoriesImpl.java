package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.FilenameFilter;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IterateDirectoriesImpl implements IterateDirectories {
  private static final Log LOG = LogFactory.getLog(IterateDirectoriesImpl.class);
  static final String[] IMAGE_TYPES = new String[] { "jpg", "jpeg" };
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
  private final File startDirectory;

  public IterateDirectoriesImpl(File startDirectory) {
    this.startDirectory = startDirectory;
  }

  @Override
  public File getFile() {
    File[] imageFiles = startDirectory.listFiles(IMAGE_FILE_FILTER);

    // TODO Auto-generated method stub
    return null;
  }
}
