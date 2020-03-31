For Windows:
Instructions for setting up this project:
1. Install jdk-8u231-windows-x64: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Install SceneBuilder 8.5: https://gluonhq.com/products/scene-builder/
3. Download JFoenix 8
4. Install NetBeans and specify the the paths of jdk-8u231-windows-x62 and Scenebuilder to NetBeans:
    a. for SceneBuilder: Tools -> Options -> Java -> JavaFx
    b. for jdk-8u231-windows-x62: go to the folder where NetBeans is installed -> open folder 'etc' -> open netbeans.conf -> modify netbeans_jdkhome
5. In NetBeans libraries, add derby.jar, derbyclient.jar, and jfoenix.jar
For macOS: IDK

Build exe:
Instal Launch4J
Open Launch4j.jar
Open config in /LibraryHelper/launcher configuration
Select output file (exe file) /Library Helper Software/Library Helper.exe
Select jar /LibraryHelper/dist/LibraryHelper.jar
Select icon /LibraryHelper/utility/books_zuX_icon.ico
To bring the program to somewhere else
Put exe file and lib (/LibraryHelper/dist/lib) folder together to run