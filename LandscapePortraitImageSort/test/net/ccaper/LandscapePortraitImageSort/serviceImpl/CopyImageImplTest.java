package net.ccaper.LandscapePortraitImageSort.serviceImpl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

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
      void copyFile(File sourceFile, File destinationFile) {
        return;
      }
    }

    File startDirectoryMock = createMock(File.class);
    String destinationDirectoryPath = "/some/destination";
    File destinationDirectoryMock = createMock(File.class);
    String fileDirectorySubPath = "/dir2/testImageFile.jpg";
    File imageFileMock = createMock(File.class);
    CopyImageImpl copyImageImplMock = new CopyImageImplMock(startDirectoryMock,
        destinationDirectoryMock);
    File copiedFile = copyImageImplMock.copyImageToOrientationDirectory(
        imageFileMock, ImageOrientation.LANDSCAPE);
    assertEquals(
        destinationDirectoryPath + "/"
            + ImageOrientation.LANDSCAPE.getDirectoryName()
            + fileDirectorySubPath, copiedFile.getAbsolutePath());
    assertEquals(1, copyImageImplMock.getNumberFileCopySuccess());
    assertEquals(0, copyImageImplMock.getNumberFileCopyFailures());
  }

  @Test
  public void testCopyImageToOrientationDirectorySadPath() throws Exception {
    class CopyImageImplMock extends CopyImageImpl {
      public CopyImageImplMock(File startDirectory, File destinationDirectory) {
        super(startDirectory, destinationDirectory);
      }

      @Override
      File createDestinationFile(File originalFile, ImageOrientation orientation) {
        return originalFile;
      }

      @Override
      void copyFile(File sourceFile, File destinationFile) throws IOException {
        throw new IOException("this is a test");
      }
    }

    File startDirectoryMock = createMock(File.class);
    File destinationDirectoryMock = createMock(File.class);
    File imageFileMock = createMock(File.class);
    expect(imageFileMock.getAbsolutePath()).andReturn(
        "/this/path/doesNotMatter").times(2);
    replay(imageFileMock);
    CopyImageImpl copyImageImplMock = new CopyImageImplMock(startDirectoryMock,
        destinationDirectoryMock);
    File copiedFile = copyImageImplMock.copyImageToOrientationDirectory(
        imageFileMock, ImageOrientation.LANDSCAPE);
    assertNull(copiedFile);
    assertEquals(0, copyImageImplMock.getNumberFileCopySuccess());
    assertEquals(1, copyImageImplMock.getNumberFileCopyFailures());
    verify(imageFileMock);
  }
}
