package net.ccaper.ImageOrientationSort.spring;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:net/ccaper/ImageOrientationSort/spring/spring-config.xml" })
public class AppConfigTest {
  private String startDirectory;
  private String destinationDirectory;
  private final List<File> ignoreDirectories = new ArrayList<File>();
  private final List<File> ignoreFiles = new ArrayList<File>();
  ApplicationContext context = new AnnotationConfigApplicationContext(
      AppConfig.class);

  @Before
  public void setUp() throws Exception {
    ResourceBundle labels = ResourceBundle
        .getBundle("ImageOrientationSort");
    startDirectory = labels.getString("start_directory");
    destinationDirectory = labels.getString("destination_directory");
    String ignoreDirectoriesString = labels.getString("ignore_directories");
    for (String directoryString : ignoreDirectoriesString
        .split(AppConfig.LIST_SEPARATOR)) {
      ignoreDirectories.add(new File(directoryString));
    }
    String ignoreFilesString = labels.getString("ignore_files");
    for (String fileString : ignoreFilesString.split(AppConfig.LIST_SEPARATOR)) {
      ignoreFiles.add(new File(fileString));
    }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testWiring() throws Exception {
    assertEquals(new File(startDirectory), context.getBean("startDirectory"));
    assertEquals(new File(destinationDirectory),
        context.getBean("destinationDirectory"));
    assertEquals(ignoreDirectories, context.getBean("ignoreDirectories"));
    assertEquals(ignoreFiles, context.getBean("ignoreFiles"));
  }

  @Test
  public void testConvertSlashToOsFileDelimiter_UnixSeparator()
      throws Exception {
    assertEquals("C:/dir1/dir2/file.txt",
        AppConfig.convertSlashToOsFileDelimiter("C:\\dir1\\dir2\\file.txt",
            AppConfig.UNIX_FILE_DELIMETER));
    assertEquals("blah", AppConfig.convertSlashToOsFileDelimiter("blah",
        AppConfig.UNIX_FILE_DELIMETER));
    assertEquals("", AppConfig.convertSlashToOsFileDelimiter("",
        AppConfig.UNIX_FILE_DELIMETER));
    assertEquals(null, AppConfig.convertSlashToOsFileDelimiter(null,
        AppConfig.UNIX_FILE_DELIMETER));
  }

  @Test
  public void testConvertSlashToOsFileDelimiter_MsSeparator() throws Exception {
    assertEquals("\\dir1\\dir2\\file.txt",
        AppConfig.convertSlashToOsFileDelimiter("/dir1/dir2/file.txt",
            AppConfig.MS_FILE_DELIMETER));
    assertEquals("blah", AppConfig.convertSlashToOsFileDelimiter("blah",
        AppConfig.MS_FILE_DELIMETER));
    assertEquals(null, AppConfig.convertSlashToOsFileDelimiter(null,
        AppConfig.MS_FILE_DELIMETER));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertSlashToOsFileDelimiter_BadDelimiters()
      throws Exception {
    AppConfig.convertSlashToOsFileDelimiter("someString", "-");
    AppConfig.convertSlashToOsFileDelimiter("someString", "");
    AppConfig.convertSlashToOsFileDelimiter("someString", null);
  }

  @Test
  public void testGenerateFileFromString() throws Exception {
    String string = " dir1/dir2/file1.txt ";
    assertEquals(new File(StringUtils.trimAllWhitespace(string)),
        AppConfig.generateFileFromString(string));
  }

  @Test
  public void testGenerateFilesFromString() throws Exception {
    assertEquals(null, AppConfig.generateFilesFromString(null));
    List<File> expected = new ArrayList<File>();
    expected.add(new File("/some/path/File1.txt"));
    expected.add(new File("/some/path/File 2.txt"));
    expected.add(new File("/some/path/File3.txt"));
    assertEquals(
        expected,
        AppConfig
        .generateFilesFromString(" /some/path/File1.txt ; /some/path/File 2.txt ; /some/path/File3.txt "));
  }
}
