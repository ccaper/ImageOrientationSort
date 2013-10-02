package net.ccaper.LandscapePortraitImageSort.util;

import static org.junit.Assert.assertEquals;

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

}
