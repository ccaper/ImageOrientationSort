package net.ccaper.LandscapePortraitImageSort.util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LandscapePortriatUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetOrientationFromDimensions() {
    assertEquals(LandscapePortraitUtils.Orientation.LANDSCAPE,
        LandscapePortraitUtils.getOrientationFromDimensions(10, 5));
    assertEquals(LandscapePortraitUtils.Orientation.PORTRAIT,
        LandscapePortraitUtils.getOrientationFromDimensions(5, 10));
    assertEquals(LandscapePortraitUtils.Orientation.EQUAL,
        LandscapePortraitUtils.getOrientationFromDimensions(5, 5));
  }

  @Test
  public void testGetFileExtention() {
    assertEquals("jpg",
        LandscapePortraitUtils.getFileExtension(new File("blah.jpg")));
    assertEquals(null, LandscapePortraitUtils.getFileExtension(null));
    assertEquals(null,
        LandscapePortraitUtils.getFileExtension(new File("blah")));
    assertEquals(null,
        LandscapePortraitUtils.getFileExtension(new File(StringUtils.EMPTY)));
  }
}
