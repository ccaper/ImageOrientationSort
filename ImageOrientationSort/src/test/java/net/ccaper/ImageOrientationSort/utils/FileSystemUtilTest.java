package net.ccaper.ImageOrientationSort.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class FileSystemUtilTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConvertSlashToOsFileDelimiter_UnixSeparator()
      throws Exception {
    assertEquals("C:/dir1/dir2/file.txt",
        FileSystemUtil.convertSlashToOsFileDelimiter(
            "C:\\dir1\\dir2\\file.txt", FileSystemUtil.UNIX_FILE_DELIMETER));
    assertEquals("blah", FileSystemUtil.convertSlashToOsFileDelimiter("blah",
        FileSystemUtil.UNIX_FILE_DELIMETER));
    assertEquals("", FileSystemUtil.convertSlashToOsFileDelimiter("",
        FileSystemUtil.UNIX_FILE_DELIMETER));
    assertEquals(null, FileSystemUtil.convertSlashToOsFileDelimiter(null,
        FileSystemUtil.UNIX_FILE_DELIMETER));
  }

  @Test
  public void testConvertSlashToOsFileDelimiter_MsSeparator() throws Exception {
    assertEquals("\\dir1\\dir2\\file.txt",
        FileSystemUtil.convertSlashToOsFileDelimiter("/dir1/dir2/file.txt",
            FileSystemUtil.MS_FILE_DELIMETER));
    assertEquals("blah", FileSystemUtil.convertSlashToOsFileDelimiter("blah",
        FileSystemUtil.MS_FILE_DELIMETER));
    assertEquals(null, FileSystemUtil.convertSlashToOsFileDelimiter(null,
        FileSystemUtil.MS_FILE_DELIMETER));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertSlashToOsFileDelimiter_BadDelimiters()
      throws Exception {
    FileSystemUtil.convertSlashToOsFileDelimiter("someString", "-");
    FileSystemUtil.convertSlashToOsFileDelimiter("someString", "");
    FileSystemUtil.convertSlashToOsFileDelimiter("someString", null);
  }

  @Test
  public void testGenerateFileFromString() throws Exception {
    String string = " dir1/dir2/file1.txt ";
    assertEquals(new File(StringUtils.trimAllWhitespace(string)),
        FileSystemUtil.generateFileFromString(string));
  }

  @Test
  public void testGenerateFilesFromString() throws Exception {
    assertEquals(null, FileSystemUtil.generateFilesFromString(null));
    List<File> expected = new ArrayList<File>();
    expected.add(new File("/some/path/File1.txt"));
    expected.add(new File("/some/path/File 2.txt"));
    expected.add(new File("/some/path/File3.txt"));
    assertEquals(
        expected,
        FileSystemUtil
        .generateFilesFromString(" /some/path/File1.txt ; /some/path/File 2.txt ; /some/path/File3.txt "));
  }

}
