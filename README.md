LandscapePortraitImageSort
==========================

Traverses a directory, for known image types decides if landscape or portrait, copies image to appropriate directory.


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
-Apache Commons Logging 1.1.1 (commons-logging-1.1.1.jar)
-log4j 1.2.17 (log4j/1.2.17/log4j-1.2.17.jar)
-Apache Commons Lang 3.3.1 (commons-lang3-3.1.jar)

The 3rd party libs for unit tests are:
-CGLIB 3.0 (cglib-nodep-3.0.jar)
-JUnit 4.11 (junit-4.11.jar)
-Spring 3.2.2 (spring-test-3.2.2.RELEASE.jar)
````

For easiest use of my ant script, I suggest creating a libs directtory with a structure like the following:
````
|-libs
  |
  |-cglib
  | |
  | |-cglib-3.0
  |   |
  |   |-cglib-nodep-3.0.jar
  |
  |-commons-lang
  | |
  | |-commons-lang-3.3.1
  |   |
  |   |-commons-lang-3.3.1.jar
  |
  |-commons-logging
  | |
  | |-commons-logging-1.1.1
  |   |
  |   |-commons-logging-1.1.1.jar
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

Description of values:
-start_directory: the top level directory you want to scan for images
-destination_directory: the directory you want the images copied to when sorted
-ignore_directories: comma separated list of directories you want to ignore, within the start directory.  Any contents, including subfolders, will not be copied to destination directory
-ignore_files: comma separated list of files you want to ignore.  Any file in this list will not be copied to the destination directory.

Note, you can write the file paths as either Unix or DOS style separators (Unix uses '/', DOS uses '\').  The application will detect the operating system you are using, and convert them as needed for the environement you are running.

Running The Application
-----------------------

Execute the command:
java -jar LandscapePortraitImageSort.jar
