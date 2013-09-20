package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;

public class IterateDirectoriesImpl implements IterateDirectories {
  private static final FilenameFilter DIRECTORY_FILTER = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      return new File(dir, name).isDirectory();
    }
  };
  private final Queue<File> files = new LinkedList<File>();
  private final Queue<File> dirs = new LinkedList<File>();
  private final FilenameFilter extensionFilter;

  public IterateDirectoriesImpl(File startDirectory,
      FilenameFilter extensionFilter) {
    this.extensionFilter = extensionFilter;
    files.addAll(Arrays.asList(startDirectory.listFiles(this.extensionFilter)));
    dirs.addAll(Arrays.asList(startDirectory.listFiles(DIRECTORY_FILTER)));
  }

  @Override
  public File getFile() {
    if (!files.isEmpty()) {
      return files.remove();
    } else if (!dirs.isEmpty()) {
      File dir = dirs.remove();
      files.addAll(Arrays.asList(dir.listFiles(extensionFilter)));
      dirs.addAll(Arrays.asList((dir.listFiles(DIRECTORY_FILTER))));
      return getFile();
    } else {
      return null;
    }
  }
}
