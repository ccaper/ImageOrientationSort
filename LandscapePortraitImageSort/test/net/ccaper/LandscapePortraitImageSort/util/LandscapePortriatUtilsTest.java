package net.ccaper.LandscapePortraitImageSort.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import net.ccaper.LandscapePortraitImageSort.enums.ImageOrientation;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LandscapePortriatUtilsTest {
  private static final ImageOrientation LANDSCAPE = ImageOrientation.LANDSCAPE;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetOrientationFromDimensions() throws Exception {
    assertEquals(LANDSCAPE,
        LandscapePortraitUtils.getOrientationFromDimensions(10, 5));
    assertEquals(ImageOrientation.PORTRAIT,
        LandscapePortraitUtils.getOrientationFromDimensions(5, 10));
    assertEquals(ImageOrientation.EQUAL,
        LandscapePortraitUtils.getOrientationFromDimensions(5, 5));
  }

  @Test
  public void testGetFileExtention() throws Exception {
    assertEquals("jpg",
        LandscapePortraitUtils.getFileExtension(new File("blah.jpg")));
    assertEquals(null, LandscapePortraitUtils.getFileExtension(null));
    assertEquals(null,
        LandscapePortraitUtils.getFileExtension(new File("blah")));
    assertEquals(null,
        LandscapePortraitUtils.getFileExtension(new File(StringUtils.EMPTY)));
  }

  @Test
  public void testGetImageReaderForImageFile() throws Exception {
    LandscapePortraitUtils landscapePortraitUtils = new LandscapePortraitUtils();
    assertNotNull(landscapePortraitUtils.getImageReaderForImageFile("jpg"));
    assertNull(landscapePortraitUtils.getImageReaderForImageFile(null));
    assertNull(landscapePortraitUtils
        .getImageReaderForImageFile(StringUtils.EMPTY));
    assertNull(landscapePortraitUtils.getImageReaderForImageFile("txt"));
  }

  @Test
  public void testGetOrientationFromImageInputStreamNull() throws Exception {
    LandscapePortraitUtils landscapePortraitUtils = new LandscapePortraitUtils();
    assertNull(landscapePortraitUtils.getOrientationFromImageInputStream(null,
        createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromImageInputStream() throws Exception {
    LandscapePortraitUtils landscapePortraitUtils = new LandscapePortraitUtils();
    ImageInputStream imageInputStreamMock = createMock(ImageInputStream.class);
    ImageReader imageReaderMock = createMock(ImageReader.class);
    expect(imageReaderMock.getMinIndex()).andReturn(0);
    expect(imageReaderMock.getWidth(0)).andReturn(10);
    expect(imageReaderMock.getHeight(0)).andReturn(5);
    imageReaderMock.setInput(imageInputStreamMock);
    replay(imageReaderMock);
    replay(imageInputStreamMock);
    assertEquals(LANDSCAPE,
        landscapePortraitUtils.getOrientationFromImageInputStream(
            imageInputStreamMock, imageReaderMock));
    verify(imageReaderMock);
    verify(imageInputStreamMock);
  }

  @Test
  public void testGetOrientationFromFile_NullImageReader() throws Exception {
    LandscapePortraitUtils landscapePortraitUtils = new LandscapePortraitUtils();
    assertNull(landscapePortraitUtils.getOrientationFromFile(
        createMock(File.class), null));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamThrowsException()
      throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        throw new IOException("this is a test");
      }
    }

    ImageReader imageReaderMock = createMock(ImageReader.class);
    imageReaderMock.dispose();
    replay(imageReaderMock);
    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertNull(landscapePortraitUtilsMock.getOrientationFromFile(
        createMock(File.class), imageReaderMock));
    verify(imageReaderMock);
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStream_OrientationFromImageInputStreamThrowsException()
      throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        return createMock(FileImageInputStream.class);
      }

      @Override
      ImageOrientation getOrientationFromImageInputStream(
          ImageInputStream imageInputStream, ImageReader imageReader)
              throws IOException {
        throw new IOException("this is a test");
      }
    }

    ImageReader imageReaderMock = createMock(ImageReader.class);
    imageReaderMock.dispose();
    replay(imageReaderMock);
    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertNull(landscapePortraitUtilsMock.getOrientationFromFile(
        createMock(File.class), imageReaderMock));
    verify(imageReaderMock);
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamNull()
      throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        return null;
      }

      @Override
      ImageOrientation getOrientationFromImageInputStream(
          ImageInputStream imageInputStream, ImageReader imageReader)
              throws IOException {
        return LANDSCAPE;
      }
    }

    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertEquals(LANDSCAPE, landscapePortraitUtilsMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamMock()
      throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        return createMock(FileImageInputStream.class);
      }

      @Override
      ImageOrientation getOrientationFromImageInputStream(
          ImageInputStream imageInputStream, ImageReader imageReader)
              throws IOException {
        return LANDSCAPE;
      }
    }

    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertEquals(LANDSCAPE, landscapePortraitUtilsMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamMockThrowsExceptionWhenClosed()
      throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        FileImageInputStream fileImageInputStremMock = createMock(FileImageInputStream.class);
        fileImageInputStremMock.close();
        expectLastCall().andThrow(new IOException("this is a test"));
        replay(fileImageInputStremMock);
        return fileImageInputStremMock;
      }

      @Override
      ImageOrientation getOrientationFromImageInputStream(
          ImageInputStream imageInputStream, ImageReader imageReader)
              throws IOException {
        return LANDSCAPE;
      }
    }

    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertEquals(LANDSCAPE, landscapePortraitUtilsMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetImageOrientation_NullFile() throws Exception {
    LandscapePortraitUtils landscapePortraitUtils = new LandscapePortraitUtils();
    assertNull(landscapePortraitUtils.getImageOrientation(null));
  }

  @Test
  public void testGetImageOrientation_ImageReaderNull() throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return null;
      }
    }

    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertNull(landscapePortraitUtilsMock.getImageOrientation(new File(
        "blah.jpg")));
  }

  @Test
  public void testGetImageOrientation() throws Exception {
    class LandscapePortraitUtilsMock extends LandscapePortraitUtils {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return ImageOrientation.LANDSCAPE;
      }
    }

    LandscapePortraitUtils landscapePortraitUtilsMock = new LandscapePortraitUtilsMock();
    assertEquals(LANDSCAPE,
        landscapePortraitUtilsMock.getImageOrientation(new File("blah.jpg")));
  }
}
