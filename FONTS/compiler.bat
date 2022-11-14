dir /s /B .\src\*.java > sources.txt

javac -d ..\EXE\ --class-path ..\lib\vavr-1.0.0-alpha-4.jar @sources.txt

del/f "sources.txt"
