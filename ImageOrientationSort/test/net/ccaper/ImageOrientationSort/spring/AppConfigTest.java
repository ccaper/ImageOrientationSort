package net.ccaper.ImageOrientationSort.spring;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.ccaper.ImageOrientationSort.utils.FileSystemUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    ResourceBundle labels = ResourceBundle.getBundle("ImageOrientationSort");
    startDirectory = labels.getString("start_directory");
    destinationDirectory = labels.getString("destination_directory");
    String ignoreDirectoriesString = labels.getString("ignore_directories");
    for (String directoryString : ignoreDirectoriesString
        .split(FileSystemUtil.LIST_SEPARATOR)) {
      ignoreDirectories.add(new File(directoryString));
    }
    String ignoreFilesString = labels.getString("ignore_files");
    for (String fileString : ignoreFilesString
        .split(FileSystemUtil.LIST_SEPARATOR)) {
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
}
