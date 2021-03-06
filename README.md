# Video Converter Application 
[Kevin](https://kevinroberts.us) needed to convert a bunch of videos and didn't want to create a bash script to do it so he built a GUI in Java Swing. 🤷‍
   
Prerequisites
===========
* [JDK 11](https://openjdk.java.net/). Java 11 or newer, Video Converter uses the Java language version 11, you will need a OpenJDK 11 installed on your machine.
* [FFMPEG](https://www.ffmpeg.org/) needs to be installed in order for this software to run. (FFMPEG being a complete, cross-platform solution to record, convert and stream audio and video.)

Build
===========
* [clone the Video Converter repository](https://help.github.com/articles/cloning-a-repository/)      
* run one of the following commands from the project root:  
**Build and install the jars in the local repository executing all the unit tests:**   
`./gradlew build`
or
`./gradlew run` to run / debug    

Run
===========
Once you built the artifacts you can find the zip bundle 
in the `distributions` directory of the `build` output folder, unzip it somewhere and run it using the provided script in the `bin` subdirectory.    

**When you run the software you will want to set the  directory to the FFMPEG executable and which directory you want encoded videos to be placed.**

