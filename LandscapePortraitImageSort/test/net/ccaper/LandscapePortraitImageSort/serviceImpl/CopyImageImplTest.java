package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CopyImageImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetFilePathAfterStartDirectory() {
    String startDirectoryPath = "/some/star/dir";
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.getAbsolutePath()).andReturn(startDirectoryPath);
    replay(startDirectoryMock);
    String fileDirectorySubPath = "/dir2/testImageFile.jpg";
    File imageFileMock = createMock(File.class);
    expect(imageFileMock.getAbsolutePath()).andReturn(startDirectoryPath + fileDirectorySubPath);
    replay(imageFileMock);
    CopyImageImpl copyImageImpl = new CopyImageImpl(startDirectoryMock, null);
    assertEquals(fileDirectorySubPath, copyImageImpl.getFilePathAfterStartDirectory(imageFileMock));
    verify(startDirectoryMock);
    verify(imageFileMock);
  }

  @Test
  public void testGetFilePathAfterStartDirectory_nullFile() {
    CopyImageImpl copyImageImpl = new CopyImageImpl(null, null);
    assertNull(copyImageImpl.getFilePathAfterStartDirectory(null));
  }
}
