dir /s /B *.java > sources.txt
javac @sources.txt
jar -cf myJar.jar MainClass.class