JAVAC = javac --module-path ./lib/JavaFx --add-modules javafx.controls,javafx.fxml -d ../EXE/ --class-path ./lib/*:./src/*:$(CLASSPATH)
sources = $(shell find ./src -type f -name '*.java')
javafile = $(sources:.java=.class)
classes = $(shell find .. -type f -name '*.class')

all : Launcher.class

Launcher.class : Launcher.java
	$(JAVAC) Launcher.java

clean :
	rm -f $(classes)