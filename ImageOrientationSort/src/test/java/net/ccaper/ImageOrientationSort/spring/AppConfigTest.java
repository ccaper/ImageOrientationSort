package net.ccaper.ImageOrientationSort.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.ccaper.ImageOrientationSort.Driver;
import net.ccaper.ImageOrientationSort.serviceImpl.CopyImageImpl;
import net.ccaper.ImageOrientationSort.serviceImpl.IterateDirectoriesImpl;
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
  private ApplicationContext context;

  @Before
  public void setUp() throws Exception {
    context = new AnnotationConfigApplicationContext(AppConfig.class);
    ResourceBundle resourceBundle = ResourceBundle
        .getBundle("ImageOrientationSort");
    startDirectory = resourceBundle.getString("start_directory");
    destinationDirectory = resourceBundle.getString("destination_directory");
    String ignoreDirectoriesString = resourceBundle
        .getString("ignore_directories");
    for (String directoryString : ignoreDirectoriesString
        .split(FileSystemUtil.LIST_SEPARATOR)) {
      ignoreDirectories.add(new File(directoryString));
    }
    String ignoreFilesString = resourceBundle.getString("ignore_files");
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
    assertNotNull(context.getBean(Driver.class));
    assertNotNull(context.getBean(CopyImageImpl.class));
    assertNotNull(context.getBean(IterateDirectoriesImpl.class));
  }
}
