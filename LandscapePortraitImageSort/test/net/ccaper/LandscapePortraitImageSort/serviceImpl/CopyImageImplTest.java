package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import net.ccaper.LandscapePortraitImageSort.enums.ImageOrientation;

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
  public void testGetFilePathAfterStartDirectory() throws Exception {
    String startDirectoryPath = "/some/start/dir";
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.getAbsolutePath()).andReturn(startDirectoryPath);
    replay(startDirectoryMock);
    String fileDirectorySubPath = "/dir2/testImageFile.jpg";
    File imageFileMock = createMock(File.class);
    expect(imageFileMock.getAbsolutePath()).andReturn(
        startDirectoryPath + fileDirectorySubPath);
    replay(imageFileMock);
    CopyImageImpl copyImageImpl = new CopyImageImpl(startDirectoryMock, null);
    assertEquals(fileDirectorySubPath,
        copyImageImpl.getFilePathAfterStartDirectory(imageFileMock));
    verify(startDirectoryMock);
    verify(imageFileMock);
  }

  @Test
  public void testGetFilePathAfterStartDirectory_nullFile() throws Exception {
    CopyImageImpl copyImageImpl = new CopyImageImpl(null, null);
    assertNull(copyImageImpl.getFilePathAfterStartDirectory(null));
  }

  @Test
  public void testCopyImageToOrientationDirectoryHappyPath() throws Exception {
    class CopyImageImplMock extends CopyImageImpl {
      public CopyImageImplMock(File startDirectory, File destinationDirectory) {
        super(startDirectory, destinationDirectory);
      }

      @Override
      File createDestinationFile(File originalFile, ImageOrientation orientation) {
        File destinationFileMock = createMock(File.class);
        expect(destinationFileMock.getAbsolutePath()).andReturn(
            "/some/destination/landscape/dir2/testImageFile.jpg");
        replay(destinationFileMock);
        return destinationFileMock;
      }

      @Override
      boolean copyFile(File sourceFile, File destinationFile) {
        return true;
      }
    }

    File startDirectoryMock = createMock(File.class);
    String destinationDirectoryPath = "/some/destination";
    File destinationDirectory = createMock(File.class);
    String fileDirectorySubPath = "/dir2/testImageFile.jpg";
    File imageFileMock = createMock(File.class);
    CopyImageImpl copyImageImplMock = new CopyImageImplMock(startDirectoryMock,
        destinationDirectory);
    File copiedFile = copyImageImplMock.copyImageToOrientationDirectory(
        imageFileMock, ImageOrientation.LANDSCAPE);
    assertEquals(
        destinationDirectoryPath + "/"
            + ImageOrientation.LANDSCAPE.getDirectoryName()
            + fileDirectorySubPath, copiedFile.getAbsolutePath());
  }

  @Test
  public void testCopyImageToOrientationDirectorySadPath() throws Exception {
    class CopyImageImplMock extends CopyImageImpl {
      public CopyImageImplMock(File startDirectory, File destinationDirectory) {
        super(startDirectory, destinationDirectory);
      }

      @Override
      File createDestinationFile(File originalFile, ImageOrientation orientation) {
        return null;
      }

      @Override
      boolean copyFile(File sourceFile, File destinationFile) {
        return false;
      }
    }

    File startDirectoryMock = createMock(File.class);
    File destinationDirectory = createMock(File.class);
    File imageFileMock = createMock(File.class);
    CopyImageImpl copyImageImplMock = new CopyImageImplMock(startDirectoryMock,
        destinationDirectory);
    File copiedFile = copyImageImplMock.copyImageToOrientationDirectory(
        imageFileMock, ImageOrientation.LANDSCAPE);
    assertNull(copiedFile);
  }
}
