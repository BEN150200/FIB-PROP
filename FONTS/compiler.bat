dir /s /B .\*.java > sources.txt

javac -d ..\EXE\ --module-path "lib\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml --class-path lib\vavr-1.0.0-alpha-4.jar @sources.txt

xcopy /E /I /y ".\src\presentation\img" "..\EXE\src\presentation\img"
xcopy /E /I /y ".\src\presentation\css" "..\EXE\src\presentation\css"
xcopy /E /I /y ".\src\presentation\fxml" "..\EXE\src\presentation\fxml"
xcopy /E /I /y ".\src\domain\preprocessing\res" "..\EXE\src\domain\preprocessing\res"

del /f "sources.txt"