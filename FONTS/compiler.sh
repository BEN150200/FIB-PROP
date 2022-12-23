#!/bin/bash

find ./src/ -type f -name "*.java" > sources.txt

javac -cp lib/vavr-1.0.0-alpha-4.jar --module-path ./lib/JavaFx --add-modules javafx.controls,javafx.fxml -d ../EXE/ @sources.txt

cp -R ./src/presentation/img ./src/presentation/css ./src/presentation/fxml ../EXE/src/presentation/

cp -R ./src/domain/preprocessing/res ../EXE/src/domain/preprocessing/res

rm -f sources.txt