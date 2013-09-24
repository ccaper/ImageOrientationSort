package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;

public class IterateDirectoriesImpl implements IterateDirectories {
  // visible for testing
  static final FilenameFilter DIRECTORY_FILTER = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      return new File(dir, name).isDirectory();
    }
  };
  private final Queue<File> files = new LinkedList<File>();
  private final Queue<File> dirs = new LinkedList<File>();
  private final FilenameFilter extensionFilter;
  private final File startDirectory;
  private boolean filesAndDirsSeeded = false;
  private final List<File> ignoreFiles;
  private final List<File> ignoreDirectories;

  public IterateDirectoriesImpl(File startDirectory,
      FilenameFilter extensionFilter, List<File> ignoreFiles,
      List<File> ignoreDirectories) {
    this.extensionFilter = extensionFilter;
    this.startDirectory = startDirectory;
    if (ignoreFiles == null) {
      this.ignoreFiles = new ArrayList<File>();
    } else {
      this.ignoreFiles = ignoreFiles;
    }
    if (ignoreDirectories == null) {
      this.ignoreDirectories = new ArrayList<File>();
    } else {
      this.ignoreDirectories = ignoreDirectories;
    }
  }

  @Override
  // TODO: test ignore files and dirs
  public File getFile() {
    if (filesAndDirsSeeded == false) {
      seedFilesAndDirs();
    }
    if (!files.isEmpty()) { // exhaust files before stepping into sub dirs
      File file = files.remove();
      if (ignoreFiles.contains(file)) { // skip if file should be ignored
        return getFile();
      } else {
        return file;
      }
    } else if (!dirs.isEmpty()) { // files exhausted, step into sub dir
      File dir = dirs.remove();
      if (ignoreDirectories.contains(dir)) { // skip if dir should be ignored
        return getFile();
      } else {
        File[] filesInDir = dir.listFiles(extensionFilter);
        if (filesInDir != null) {
          files.addAll(Arrays.asList(filesInDir));
        }
        File[] dirsInDir = dir.listFiles(DIRECTORY_FILTER);
        if (dirsInDir != null) {
          dirs.addAll(Arrays.asList((dirsInDir)));
        }
        return getFile();
      }
    } else { // files and dirs exhausted, return
      return null;
    }
  }

  // TODO: test
  private void seedFilesAndDirs() {
    if (startDirectory != null) {
      File[] filesArray = startDirectory.listFiles(extensionFilter);
      if (filesArray != null) {
        files.addAll(Arrays.asList(filesArray));
      }
      File[] dirsArray = startDirectory.listFiles(DIRECTORY_FILTER);
      if (dirsArray != null) {
        dirs.addAll(Arrays.asList(dirsArray));
      }
    }
    filesAndDirsSeeded = true;
  }
}
