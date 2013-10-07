package net.ccaper.LandscapePortraitImageSort.enums;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageOrientationTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    assertEquals("landscape", ImageOrientation.LANDSCAPE.getDirectoryName());
    assertEquals("portrait", ImageOrientation.PORTRAIT.getDirectoryName());
    assertEquals("portrait", ImageOrientation.EQUAL.getDirectoryName());
  }

}
