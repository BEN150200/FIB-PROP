JAVAC = javac -d ../EXE/ --class-path ./lib/*:./src/*:$(CLASSPATH) 
sources = $(shell find ./src -type f -name '*.java')
javafile = $(sources:.java=.class)
classes = $(shell find .. -type f -name '*.class')

all : src.presentation.Driver.class

src.presentation.Driver.class : src.presentation.Driver.java
	$(JAVAC) src.presentation.Driver.java

clean :
	rm -f $(classes)