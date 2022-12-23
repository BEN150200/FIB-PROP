set PATH_TO_FX = "lib\javafx-sdk-19\lib"

dir /s /B .\*.java > sources.txt

mkdir ..\EXE

javac -d ..\EXE --module-path ".\lib\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml --class-path lib\vavr-1.0.0-alpha-4.jar @sources.txt

del /f "sources.txt"

cd ..\EXE\src\presentation

mkdir .\css
mkdir .\fxml
mkdir .\img

cd ..\..\..\FONTS\src\presentation

robocopy css ..\..\..\EXE\src\presentation\css
robocopy fxml ..\..\..\EXE\src\presentation\fxml
robocopy img ..\..\..\EXE\src\presentation\img

cd ..\..

cd ..\EXE\src

jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.base.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.controls.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.fxml.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.graphics.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.media.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.swing.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx.web.jar"
jar xf "..\..\FONTS\lib\javafx-sdk-19\lib\javafx-swt.jar"
jar xf "..\..\FONTS\lib\controlsfx-11.1.2.jar"
jar xf "..\..\FONTS\lib\vavr-1.0.0-alpha-4.jar"

cd ..\..\FONTS

copy .\lib\javafx-sdk-19\bin\prism*.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\javafx*.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\glass.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\decora_sse.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\api*.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\fxplugins.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\glib-lite.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\gstreamer-lite.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\msvcp*.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\ucrtbase.dll ..\EXE\src
copy .\lib\javafx-sdk-19\bin\vcruntime*.dll ..\EXE\src

cd ..\EXE

jar cfe subgrup-prop.33.2.jar src.Launcher src

cd ..\FONTS
