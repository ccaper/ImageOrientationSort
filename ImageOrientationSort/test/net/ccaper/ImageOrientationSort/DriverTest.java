package net.ccaper.ImageOrientationSort;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;

import net.ccaper.ImageOrientationSort.Driver;

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
}
