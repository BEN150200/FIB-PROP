cd ../FONTS

dir /s /B .\*.java > sources.txt

javac -d ..\EXE\ --module-path "lib\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml --class-path lib\vavr-1.0.0-alpha-4.jar @sources.txt

del /f "sources.txt"