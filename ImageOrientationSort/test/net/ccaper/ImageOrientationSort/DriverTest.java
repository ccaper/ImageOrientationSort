package net.ccaper.ImageOrientationSort;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ccaper.ImageOrientationSort.service.CopyImage;
import net.ccaper.ImageOrientationSort.service.IterateDirectories;
import net.ccaper.ImageOrientationSort.serviceImpl.CopyImageImpl;
import net.ccaper.ImageOrientationSort.serviceImpl.IterateDirectoriesImpl;
import net.ccaper.ImageOrientationSort.utils.ImageOrientationUtil;

import org.junit.Test;

public class DriverTest {

  @Test
  public void testIsExistsHappyPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.exists()).andReturn(true);
    replay(fileMock);
    Driver.isExists(fileMock);
    verify(fileMock);
  }

  @Test
  public void testIsExistsSadPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.exists()).andReturn(false);
    expect(fileMock.getAbsolutePath()).andReturn("/some/path");
    replay(fileMock);
    Driver.isExists(fileMock);
    verify(fileMock);
  }

  @Test
  public void testIsReadableHappyPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.canRead()).andReturn(true);
    replay(fileMock);
    Driver.isReadable(fileMock);
    verify(fileMock);
  }

  @Test
  public void testIsReadableSadPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.canRead()).andReturn(false);
    expect(fileMock.getAbsolutePath()).andReturn("/some/path");
    replay(fileMock);
    Driver.isReadable(fileMock);
    verify(fileMock);
  }

  @Test
  public void testIsWritableHappyPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.canWrite()).andReturn(true);
    replay(fileMock);
    Driver.isWritable(fileMock);
    verify(fileMock);
  }

  @Test
  public void testIsWritableSadPath() {
    File fileMock = createMock(File.class);
    expect(fileMock.canWrite()).andReturn(false);
    expect(fileMock.getAbsolutePath()).andReturn("/some/path");
    replay(fileMock);
    Driver.isWritable(fileMock);
    verify(fileMock);
  }

  @Test
  public void validateStartAndDestinationDirectories_HappyPath()
      throws Exception {
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.exists()).andReturn(true);
    expect(startDirectoryMock.canRead()).andReturn(true);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    expect(destinationDirectoryMock.exists()).andReturn(true);
    expect(destinationDirectoryMock.canWrite()).andReturn(true);
    replay(destinationDirectoryMock);
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        null, null, null, null, null, null);
    driver.validateStartAndDestinationDirectories();
    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
  }

  @Test(expected = Exception.class)
  public void validateStartAndDestinationDirectories_SadPath_StartDirNotExists()
      throws Exception {
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.exists()).andReturn(false);
    expect(startDirectoryMock.getAbsolutePath()).andReturn("/some/dir")
        .times(2);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    replay(destinationDirectoryMock);
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        null, null, null, null, null, null);
    driver.validateStartAndDestinationDirectories();
    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
  }

  @Test(expected = Exception.class)
  public void validateStartAndDestinationDirectories_SadPath_StartDirCannotRead()
      throws Exception {
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.exists()).andReturn(true);
    expect(startDirectoryMock.canRead()).andReturn(false);
    expect(startDirectoryMock.getAbsolutePath()).andReturn("/some/dir")
        .times(2);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    replay(destinationDirectoryMock);
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        null, null, null, null, null, null);
    driver.validateStartAndDestinationDirectories();
    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
  }

  @Test(expected = Exception.class)
  public void validateStartAndDestinationDirectories_SadPath_DestDirNotExists()
      throws Exception {
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.exists()).andReturn(true);
    expect(startDirectoryMock.canRead()).andReturn(true);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    expect(destinationDirectoryMock.exists()).andReturn(false);
    expect(destinationDirectoryMock.getAbsolutePath()).andReturn("/some/dir")
        .times(2);
    replay(destinationDirectoryMock);
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        null, null, null, null, null, null);
    driver.validateStartAndDestinationDirectories();
    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
  }

  @Test(expected = Exception.class)
  public void validateStartAndDestinationDirectories_SadPath_DestDirCannotWrite()
      throws Exception {
    File startDirectoryMock = createMock(File.class);
    expect(startDirectoryMock.exists()).andReturn(true);
    expect(startDirectoryMock.canRead()).andReturn(true);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    expect(destinationDirectoryMock.exists()).andReturn(true);
    expect(destinationDirectoryMock.canWrite()).andReturn(false);
    expect(destinationDirectoryMock.getAbsolutePath()).andReturn("/some/dir")
        .times(2);
    replay(destinationDirectoryMock);
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        null, null, null, null, null, null);
    driver.validateStartAndDestinationDirectories();
    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
  }

  @Test
  public void testCopyImages_NoFiles() throws Exception {
    File startDirectoryMock = createMock(File.class);
    replay(startDirectoryMock);
    File destinationDirectoryMock = createMock(File.class);
    replay(destinationDirectoryMock);
    List<File> ignoreFiles = new ArrayList<File>();
    List<File> ignoreDirectories = new ArrayList<File>();
    String[] imageTypesAllowed = { "jpg", "jpeg" };
    IterateDirectories iterateDirectoriesMock = createMock(IterateDirectoriesImpl.class);
    expect(iterateDirectoriesMock.getFile()).andReturn(null);
    replay(iterateDirectoriesMock);
    ImageOrientationUtil imageOrientationUtilsMock = createMock(ImageOrientationUtil.class);
    replay(imageOrientationUtilsMock);
    CopyImage copyImageMock = createMock(CopyImageImpl.class);
    replay(copyImageMock);
    
    Driver driver = new Driver(startDirectoryMock, destinationDirectoryMock,
        ignoreFiles, ignoreDirectories, imageTypesAllowed,
        iterateDirectoriesMock, imageOrientationUtilsMock, copyImageMock);
    driver.copyImages();

    verify(startDirectoryMock);
    verify(destinationDirectoryMock);
    verify(iterateDirectoriesMock);
    verify(imageOrientationUtilsMock);
    verify(copyImageMock);
  }
}
