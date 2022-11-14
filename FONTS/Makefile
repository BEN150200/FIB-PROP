JAVAC = javac -d ../EXE/ --class-path ../lib/:./src/*:$(CLASSPATH) 
sources = $(shell find ./src -type f -name '*.java')
javafile = $(sources:.java=.class)
classes = $(shell find .. -type f -name '*.class')

all : Driver.class

Driver.class : Driver.java
	$(JAVAC) Driver.java

clean :
	rm -f $(classes)