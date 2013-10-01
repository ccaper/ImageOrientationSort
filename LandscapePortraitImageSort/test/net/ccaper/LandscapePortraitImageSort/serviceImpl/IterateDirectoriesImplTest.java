package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;
import net.ccaper.LandscapePortraitImageSort.spring.AppConfig;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IterateDirectoriesImplTest {
  AppConfig appConfig = new AppConfig();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetFile_NullStartDirectory() {
    IterateDirectories iterateDirs = new IterateDirectoriesImpl(null, null,
        null);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(0, files.size());
  }

  @Test
  public void testGetFile_NullFilesNullDirectories() {
    File startDirectoryMock = createMock(File.class);
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(null);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(null);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(0, files.size());
    assertTrue(files.containsAll(Arrays.asList(new File[] {})));
    assertEquals(0, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(0, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_EmptyFilesNullDirectories() {
    File[] expectedFiles = new File[] {};
    File startDirectoryMock = createMock(File.class);
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(null);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length, files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFiles)));
    assertEquals(0, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(0, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_EmptyFilesEmptyDirectories() {
    File[] expectedFiles = new File[] {};
    File[] expectedDirectories = new File[] {};
    File startDirectoryMock = createMock(File.class);
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectories);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length, files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFiles)));
    assertEquals(0, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(0, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_OnlyFilesNoDirectories() {
    File[] expectedFiles = new File[] { new File("file1.jpg"),
        new File("file2.jpg") };
    File startDirectoryMock = createMock(File.class);
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(null);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length, files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFiles)));
    assertEquals(2, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_OnlyFilesInSubDirectory() {
    File startDirectoryMock = createMock(File.class);
    File dir1Mock = createMock(File.class);
    File[] expectedFilesTopLevel = new File[] {};
    File[] expectedDirectoriesTopLevel = new File[] { dir1Mock };
    File[] expectedFilesDir1Level = new File[] { new File("file3.jpg"),
        new File("file4.jpg") };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFilesTopLevel);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectoriesTopLevel);
    expect(dir1Mock.listFiles(extensionFilter)).andReturn(
        expectedFilesDir1Level);
    expect(dir1Mock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
    .andReturn(null);
    replay(startDirectoryMock);
    replay(dir1Mock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    List<File> allExpectedFiles = new ArrayList<File>(
        Arrays.asList(expectedFilesTopLevel));
    allExpectedFiles.addAll(Arrays.asList(expectedFilesDir1Level));
    assertEquals(allExpectedFiles.size(), files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFilesTopLevel)));
    assertTrue(files.containsAll(allExpectedFiles));
    assertEquals(2, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
    verify(dir1Mock);
  }

  @Test
  public void testGetFile_FilesInStartDirAndFilesInSubDirectory() {
    File startDirectoryMock = createMock(File.class);
    File dir1Mock = createMock(File.class);
    File[] expectedFilesTopLevel = new File[] { new File("file1.jpg"),
        new File("file2.jpg") };
    File[] expectedDirectoriesTopLevel = new File[] { dir1Mock };
    File[] expectedFilesDir1Level = new File[] { new File("file3.jpg"),
        new File("file4.jpg") };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFilesTopLevel);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectoriesTopLevel);
    expect(dir1Mock.listFiles(extensionFilter)).andReturn(
        expectedFilesDir1Level);
    expect(dir1Mock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
    .andReturn(null);
    replay(startDirectoryMock);
    replay(dir1Mock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    List<File> allExpectedFiles = new ArrayList<File>(
        Arrays.asList(expectedFilesTopLevel));
    allExpectedFiles.addAll(Arrays.asList(expectedFilesDir1Level));
    assertEquals(allExpectedFiles.size(), files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFilesTopLevel)));
    assertTrue(files.containsAll(allExpectedFiles));
    assertEquals(4, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(4, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
    verify(dir1Mock);
  }

  @Test
  public void testGetFile_EmptySubDir() {
    File startDirectoryMock = createMock(File.class);
    File dir1Mock = createMock(File.class);
    File dir2Mock = createMock(File.class);
    File[] expectedFilesTopLevel = new File[] { new File("file1.jpg"),
        new File("file2.jpg") };
    File[] expectedDirectoriesTopLevel = new File[] { dir1Mock };
    File[] expectedDirectoriesSecondLevel = new File[] { dir2Mock };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, null, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFilesTopLevel);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectoriesTopLevel);
    expect(dir1Mock.listFiles(extensionFilter)).andReturn(null);
    expect(dir1Mock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
    .andReturn(expectedDirectoriesSecondLevel);
    expect(dir2Mock.listFiles(extensionFilter)).andReturn(null);
    expect(dir2Mock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
    .andReturn(null);
    replay(startDirectoryMock);
    replay(dir1Mock);
    replay(dir2Mock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    List<File> allExpectedFiles = new ArrayList<File>(
        Arrays.asList(expectedFilesTopLevel));
    assertEquals(allExpectedFiles.size(), files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFilesTopLevel)));
    assertTrue(files.containsAll(allExpectedFiles));
    assertEquals(2, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(2, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(2, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
    verify(dir1Mock);
    verify(dir2Mock);
  }

  @Test
  public void testGetFile_ignoreFiles() {
    List<File> ignoreFiles = new ArrayList<File>();
    File ignoreFile = new File("file3.jpg");
    ignoreFiles.add(ignoreFile);
    File startDirectoryMock = createMock(File.class);
    File[] expectedFiles = new File[] { new File("file1.jpg"),
        new File("file2.jpg"), ignoreFile };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, ignoreFiles, null);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(null);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length - 1, files.size());
    assertTrue(files.containsAll(Arrays.asList(ArrayUtils.subarray(
        expectedFiles, 0, 2))));
    assertEquals(3, iterateDirs.getNumberFilesFound());
    assertEquals(1, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesFound());
    assertEquals(0, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_ignoreDirs() {
    List<File> ignoreDirs = new ArrayList<File>();
    File ignoreDir = new File("dir2");
    ignoreDirs.add(ignoreDir);
    File startDirectoryMock = createMock(File.class);
    File[] expectedFiles = new File[] { new File("file1.jpg"),
        new File("file2.jpg") };
    File[] expectedDirectoriesTopLevel = new File[] { ignoreDir };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock,  null, ignoreDirs);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectoriesTopLevel);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length, files.size());
    assertTrue(files.containsAll(Arrays.asList(expectedFiles)));
    assertEquals(2, iterateDirs.getNumberFilesFound());
    assertEquals(0, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesFound());
    assertEquals(1, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testGetFile_ignoreFilesAndDirs() {
    List<File> ignoreFiles = new ArrayList<File>();
    File ignoreFile = new File("file3.jpg");
    ignoreFiles.add(ignoreFile);
    List<File> ignoreDirs = new ArrayList<File>();
    File ignoreDir = new File("dir2");
    ignoreDirs.add(ignoreDir);
    File startDirectoryMock = createMock(File.class);
    File[] expectedFiles = new File[] { new File("file1.jpg"),
        new File("file2.jpg"), ignoreFile };
    File[] expectedDirectoriesTopLevel = new File[] { ignoreDir };
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        startDirectoryMock, ignoreFiles, ignoreDirs);
    FilenameFilter extensionFilter = iterateDirs.extensionFilenameFilter;
    expect(startDirectoryMock.listFiles(extensionFilter)).andReturn(
        expectedFiles);
    expect(
        startDirectoryMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
        .andReturn(expectedDirectoriesTopLevel);
    replay(startDirectoryMock);
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(expectedFiles.length - 1, files.size());
    assertTrue(files.containsAll(Arrays.asList(ArrayUtils.subarray(
        expectedFiles, 0, 2))));
    assertEquals(3, iterateDirs.getNumberFilesFound());
    assertEquals(1, iterateDirs.getNumberFilesSkipped());
    assertEquals(2, iterateDirs.getNumberFilesNotSkipped());
    assertEquals(1, iterateDirs.getNumberDirectoriesFound());
    assertEquals(1, iterateDirs.getNumberDirectoriesSkipped());
    assertEquals(0, iterateDirs.getNumberDirectoriesNotSkipped());
    assertEquals(0, iterateDirs.getNumberNonImageFiles());
    verify(startDirectoryMock);
  }

  @Test
  public void testExtensionFilenameFilterAccept() {
    IterateDirectoriesImpl iterateDirs = new IterateDirectoriesImpl(
        null, null, null);
    assertTrue(iterateDirs.extensionFilenameFilter.accept(new File("blahDir"),
        "blahFile." + AppConfig.IMAGE_TYPES[0]));
    assertFalse(iterateDirs.extensionFilenameFilter.accept(new File("blahDir"),
        "blahFile.blah"));
  }
}
