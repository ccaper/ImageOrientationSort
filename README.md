LandscapePortraitImageSort
==========================

Summary: Traverses a directory, for known image types decides if landscape or portrait, copies image to appropriate directory.

Details: Have you ever want to separate all your digital photos into two categories, Landscape (longer on the horizontal) and Portrait (longer on the vertical), but the amount of your digital photo stock makes this too daunting to ever consider?  Great usage scenario is you have two digital photo frames, and you want to load one with only landscape images, and the other with portrait images, so you can turn the fram the optimal direction to minimize those black bars and make the image as large as possible.  Well, my LandscapePortraitImageSort can help you.  It will traverse your image directories non desctructively, and copy your images to a new directory, mimicking the original directory structure, with landscape images going to one root folder, and portrait images going to another.  Have a few embarassing pictures of you wearing a lampshade?  No fret, you can set a property file to ignore files and complete directories.


To Build With Ant
-----------------

This project has some third part library dependencies that must be downloaded for ant to build.

````
The 3rd party libs for application are:
-Spring 3.2, specifically
  -spring-beans-3.2.2.RELEASE.jar
  -spring-context-3.2.2.RELEASE.jar
  -spring-context-support-3.2.2.RELEASE.jar
  -spring-core-3.2.2.RELEASE.jar
  -spring-expression-3.2.2.RELEASE.jar
-Apache Commons IO 2.2.4 (commons-io-2.4.jar)
-Apache Commons Logging 1.1.1 (commons-logging-1.1.1.jar)
-log4j 1.2.17 (log4j/1.2.17/log4j-1.2.17.jar)
-Apache Commons Lang 3.3.1 (commons-lang3-3.1.jar)

The 3rd party libs for unit tests are:
-CGLIB 2.2.3 (cglib-nodep-2.2.3.jar)
-JUnit 4.11 (junit-4.11.jar)
-Spring 3.2.2 (spring-test-3.2.2.RELEASE.jar)
-EasyMock 3.1 (easymock-3.1.jar & easymockclassextension-3.1.jar)
-Objenesis 1.1 (objenesis-1.1.jar)
````

For easiest use of my ant script, I suggest creating a libs directtory with a structure like the following:
````
|-libs
  |
  |-cglib
  | |
  | |-cglib-2.2.3
  |   |
  |   |-cglib-nodep-2.2.3.jar
  |
  |-commons-lang
  | |
  | |-commons-lang-3.3.1
  |   |
  |   |-commons-lang-3.3.1.jar
  |
  |-commons-io
  | |
  | |-commons-io-2.4
  |   |
  |   |-commons-io-2.4.jar
  |
  |-commons-logging
  | |
  | |-commons-logging-1.1.1
  |   |
  |   |-commons-logging-1.1.1.jar
  |
  |-easymock
  | |
  | |-easymock-3.1
  | | |
  | | |-easymock-3.1.jar
  | |
  | |-easymockckassextension-3.1
  |   |
  |   |-easymockclassextension-3.1.jar
  |
  |-log4j
  | |
  | |-log4j-1.2.17
  |   |
  |   |-log4j-1.2.17.jar
  |
  |-junit
  | |
  | |-junit-4.11
  |   |
  |   |-junit-4.11.jar
  |
  |-objenesis
  | |
  | |-objenesis-1.1
  |   |
  |   |-objenesis-1.1.jar
  |
  |-spring
    |
    |-spring-3.2.1
      |
      |-spring-beans-3.2.2.RELEASE.jar
      |
      |-spring-context-3.2.2.RELEASE.jar
      |
      |-spring-context-support-3.2.2.RELEASE.jar
      |
      |-spring-core-3.2.2.RELEASE.jar
      |
      |-spring-expression-3.2.2.RELEASE.jar
      |
      |-spring-test-3.2.2.RELEASE.jar
````

If you follow this model for your libs, all you will need to for the ant script to compile is to locate in the ant file (build.xml) the line <property name="third_party_libs" value="../../ccaper-local/libs/libs" /> near the beginning of the file, and change the value to either be the full path to your libs directory, or the relative path from the location of the ant script to your directory.


The Properties File
-------------------

The properties file, after building, will be located outside of the executable jar at
"conf/LandscapePortraitImageSort.properties".

Please edit this file to your needs.

````
Description of values:
-start_directory: the top level directory you want to scan for images
-destination_directory: the directory you want the images copied to when sorted
-ignore_directories: semicolon (';') separated list of directories you want to ignore,
within the start directory.  Any contents, including subfolders, will not be
copied to destination directory
-ignore_files: semicolon (';') separated list of files you want to ignore.  Any file in
this list will not be copied to the destination directory.
````

Note, you can write the file paths as either Unix or DOS style separators (Unix uses '/', DOS uses '\').  The application will detect the operating system you are using, and convert them as needed for the environement you are running.

Note, all paths must be full absolute paths.

Note, if a directory is added to ignore_directories, all subdirectories in that directory will also be ignored.

Running The Application
-----------------------

Execute the command:
java -jar LandscapePortraitImageSort.jar
