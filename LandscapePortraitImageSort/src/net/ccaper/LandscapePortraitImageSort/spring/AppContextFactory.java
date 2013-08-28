package net.ccaper.LandscapePortraitImageSort.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This class provides singleton access to the Spring Application Context.
 */
public class AppContextFactory {
  private static ApplicationContext appContext = null;

  public static synchronized ApplicationContext getContext() {
    if (appContext == null) {
      appContext = new AnnotationConfigApplicationContext(AppConfig.class);
    }
    return appContext;
  }
}
