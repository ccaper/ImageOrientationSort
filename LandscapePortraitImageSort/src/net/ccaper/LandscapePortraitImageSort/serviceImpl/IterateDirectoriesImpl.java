package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IterateDirectoriesImpl implements IterateDirectories {
  private static final Log LOG = LogFactory
      .getLog(IterateDirectoriesImpl.class);
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
  private int filesFound = 0;
  private int directoriesFound = 0;
  private int filesSkipped = 0;
  private int directoriesSkipped = 0;
  private int filesNotSkipped = 0;
  private int directoriesNotSkipped = 0;


  /**
   * 
   * @param startDirectory
   *          The top level directory to traverse
   * @param extensionFilter
   *          A filename filter for the type of file extensions to allow in
   * @param ignoreFiles
   *          A list of files to ignore
   * @param ignoreDirectories
   *          A list of directories to ignore, which also ignores all sub
   *          directories of an ignore directory
   */
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
  public File getFile() {
    if (filesAndDirsSeeded == false) {
      seedFilesAndDirs();
    }
    if (!files.isEmpty()) { // exhaust files before stepping into sub dirs
      return getFileFromFiles();
    } else if (!dirs.isEmpty()) { // files exhausted, step into sub dir
      return getFileFromSubDirectory();
    } else { // files and dirs exhausted, return
      return null;
    }
  }

  /**
   * Handles files in directories
   * 
   * @return a file, or null if no more exist
   */
  private File getFileFromFiles() {
    File file = files.remove();
    ++filesFound;
    // fix ignore bug
    if (ignoreFiles.contains(file)) { // skip if file should be ignored
      ++filesSkipped;
      LOG.info(String.format(
          "The file '%s' was found in the ignore files list, skipped.",
          file.getAbsolutePath()));
      return getFile();
    } else {
      ++filesNotSkipped;
      return file;
    }
  }

  /**
   * Handles directories in directories
   * 
   * @return A file, or null if no more files exist
   */
  private File getFileFromSubDirectory() {
    File dir = dirs.remove();
    ++directoriesFound;
    // TODO: fix ignore bug
    if (ignoreDirectories.contains(dir)) { // skip if dir should be ignored
      ++directoriesSkipped;
      LOG.info(String.format(
          "The directory '%s' was found in the ignore files list, skipped.",
          dir.getAbsolutePath()));
    } else {
      ++directoriesNotSkipped;
      File[] filesInDir = dir.listFiles(extensionFilter);
      if (filesInDir != null) {
        files.addAll(Arrays.asList(filesInDir));
      }
      File[] dirsInDir = dir.listFiles(DIRECTORY_FILTER);
      if (dirsInDir != null) {
        dirs.addAll(Arrays.asList((dirsInDir)));
      }
    }
    return getFile();
  }

  /**
   * Seeds the list of files and list of directories that exist in the start
   * directory
   */
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

  @Override
  public int getFilesFound() {
    return filesFound;
  }

  @Override
  public int getFilesSkipped() {
    return filesSkipped;
  }

  @Override
  public int getFilesNotSkipped() {
    return filesNotSkipped;
  }

  @Override
  public int getDirectoriesFound() {
    return directoriesFound;
  }

  @Override
  public int getDirectoriesSkipped() {
    return directoriesSkipped;
  }

  @Override
  public int getDirectoriesNotSkipped() {
    return directoriesNotSkipped;
  }
}
