package net.ccaper.ImageOrientationSort.utils;

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

import net.ccaper.ImageOrientationSort.enums.ImageOrientation;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageOrientationUtilTest {
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
        ImageOrientationUtil.getOrientationFromDimensions(10, 5));
    assertEquals(ImageOrientation.PORTRAIT,
        ImageOrientationUtil.getOrientationFromDimensions(5, 10));
    assertEquals(ImageOrientation.EQUAL,
        ImageOrientationUtil.getOrientationFromDimensions(5, 5));
  }

  @Test
  public void testGetFileExtention() throws Exception {
    assertEquals("jpg",
        ImageOrientationUtil.getFileExtension(new File("blah.jpg")));
    assertEquals(null, ImageOrientationUtil.getFileExtension(null));
    assertEquals(null, ImageOrientationUtil.getFileExtension(new File("blah")));
    assertEquals(null,
        ImageOrientationUtil.getFileExtension(new File(StringUtils.EMPTY)));
  }

  @Test
  public void testGetImageReaderForImageFile() throws Exception {
    ImageOrientationUtil imageOrientationUtil = new ImageOrientationUtil();
    assertNotNull(imageOrientationUtil.getImageReaderForImageFile("jpg"));
    assertNull(imageOrientationUtil.getImageReaderForImageFile(null));
    assertNull(imageOrientationUtil
        .getImageReaderForImageFile(StringUtils.EMPTY));
    assertNull(imageOrientationUtil.getImageReaderForImageFile("txt"));
  }

  @Test
  public void testGetOrientationFromImageInputStreamNull() throws Exception {
    ImageOrientationUtil imageOrientationUtil = new ImageOrientationUtil();
    assertNull(imageOrientationUtil.getOrientationFromImageInputStream(null,
        createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromImageInputStream() throws Exception {
    ImageOrientationUtil imageOrientationUtil = new ImageOrientationUtil();
    ImageInputStream imageInputStreamMock = createMock(ImageInputStream.class);
    ImageReader imageReaderMock = createMock(ImageReader.class);
    expect(imageReaderMock.getMinIndex()).andReturn(0);
    expect(imageReaderMock.getWidth(0)).andReturn(10);
    expect(imageReaderMock.getHeight(0)).andReturn(5);
    imageReaderMock.setInput(imageInputStreamMock);
    replay(imageReaderMock);
    replay(imageInputStreamMock);
    assertEquals(LANDSCAPE,
        imageOrientationUtil.getOrientationFromImageInputStream(
            imageInputStreamMock, imageReaderMock));
    verify(imageReaderMock);
    verify(imageInputStreamMock);
  }

  @Test
  public void testGetOrientationFromFile_NullImageReader() throws Exception {
    ImageOrientationUtil imageOrientationUtil = new ImageOrientationUtil();
    assertNull(imageOrientationUtil.getOrientationFromFile(
        createMock(File.class), null));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamThrowsException()
      throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      FileImageInputStream getFileImageInputStream(File file)
          throws IOException {
        throw new IOException("this is a test");
      }
    }

    ImageReader imageReaderMock = createMock(ImageReader.class);
    imageReaderMock.dispose();
    replay(imageReaderMock);
    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertNull(imageOrientationUtilMock.getOrientationFromFile(
        createMock(File.class), imageReaderMock));
    verify(imageReaderMock);
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStream_OrientationFromImageInputStreamThrowsException()
      throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
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
    ImageOrientationUtil imageOrienationUtilMock = new ImageOrientationUtilMock();
    assertNull(imageOrienationUtilMock.getOrientationFromFile(
        createMock(File.class), imageReaderMock));
    verify(imageReaderMock);
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamNull()
      throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
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

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertEquals(LANDSCAPE, imageOrientationUtilMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamMock()
      throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
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

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertEquals(LANDSCAPE, imageOrientationUtilMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetOrientationFromFile_FileImageInputStreamMockThrowsExceptionWhenClosed()
      throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
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

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertEquals(LANDSCAPE, imageOrientationUtilMock.getOrientationFromFile(
        createMock(File.class), createMock(ImageReader.class)));
  }

  @Test
  public void testGetImageOrientation_NullFile() throws Exception {
    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtil();
    assertNull(imageOrientationUtilMock.getImageOrientation(null));
  }

  @Test
  public void testGetImageOrientation_ImageReaderNull() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return null;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertNull(imageOrientationUtilMock
        .getImageOrientation(new File("blah.jpg")));
  }

  @Test
  public void testGetImageOrientation() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return ImageOrientation.LANDSCAPE;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    assertEquals(LANDSCAPE,
        imageOrientationUtilMock.getImageOrientation(new File("blah.jpg")));
  }

  @Test
  public void testLandscapeCount() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return LANDSCAPE;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    imageOrientationUtilMock.getImageOrientation(new File("blah.jpg"));
    assertEquals(1,
        imageOrientationUtilMock.getNumberLandscapeOrientationImages());
    assertEquals(0,
        imageOrientationUtilMock.getNumberPortraitOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberEqualOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberOrientationProblems());
  }

  @Test
  public void testPortraitCount() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return ImageOrientation.PORTRAIT;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    imageOrientationUtilMock.getImageOrientation(new File("blah.jpg"));
    assertEquals(0,
        imageOrientationUtilMock.getNumberLandscapeOrientationImages());
    assertEquals(1,
        imageOrientationUtilMock.getNumberPortraitOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberEqualOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberOrientationProblems());
  }

  @Test
  public void testEqualsCount() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return ImageOrientation.EQUAL;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    imageOrientationUtilMock.getImageOrientation(new File("blah.jpg"));
    assertEquals(0,
        imageOrientationUtilMock.getNumberLandscapeOrientationImages());
    assertEquals(0,
        imageOrientationUtilMock.getNumberPortraitOrientationImages());
    assertEquals(1, imageOrientationUtilMock.getNumberEqualOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberOrientationProblems());
  }

  @Test
  public void testProblemCount() throws Exception {
    class ImageOrientationUtilMock extends ImageOrientationUtil {
      @Override
      ImageReader getImageReaderForImageFile(String fileExtension) {
        return createMock(ImageReader.class);
      }

      @Override
      ImageOrientation getOrientationFromFile(File file, ImageReader imageReader) {
        return null;
      }
    }

    ImageOrientationUtil imageOrientationUtilMock = new ImageOrientationUtilMock();
    imageOrientationUtilMock.getImageOrientation(new File("blah.jpg"));
    assertEquals(0,
        imageOrientationUtilMock.getNumberLandscapeOrientationImages());
    assertEquals(0,
        imageOrientationUtilMock.getNumberPortraitOrientationImages());
    assertEquals(0, imageOrientationUtilMock.getNumberEqualOrientationImages());
    assertEquals(1, imageOrientationUtilMock.getNumberOrientationProblems());
  }
}
