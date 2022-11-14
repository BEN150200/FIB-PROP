dir /s /B .\*.java > sources.txt

javac -d ..\EXE\ --class-path lib\vavr-1.0.0-alpha-4.jar;lib\hamcrest-core-1.3.jar;lib\junit-4.13.2.jar @sources.txt

del /f "sources.txt"