package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ccaper.LandscapePortraitImageSort.service.IterateDirectories;
import net.ccaper.LandscapePortraitImageSort.spring.AppConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IterateDirectoriesImplTest {
  AppConfig appConfig = new AppConfig();
  File[] expectedFiles = new File[] { new File("file1.jpg"),
      new File("file2.jpg") };

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetFile() {
    File fileMock = createMock(File.class);
    expect(fileMock.listFiles(appConfig.getFilenameFilter())).andReturn(
        expectedFiles);
    expect(fileMock.listFiles(IterateDirectoriesImpl.DIRECTORY_FILTER))
    .andReturn(null);
    replay(fileMock);
    IterateDirectories iterateDirs = new IterateDirectoriesImpl(fileMock,
        appConfig.getFilenameFilter());
    File file = iterateDirs.getFile();
    List<File> files = new ArrayList<File>();
    while (file != null) {
      files.add(file);
      file = iterateDirs.getFile();
    }
    assertEquals(2, files.size());
    verify(fileMock);
  }
}
