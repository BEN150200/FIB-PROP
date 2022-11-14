JAVAC = javac -d ../EXE/
sources = $(shell find . -type f -name '*.java')
javafile = $(sources:.java=.class)
classes = $(shell find .. -type f -name '*.class')

all : $(javafile)

%.class : %.java
	$(JAVAC) $<

clean :
	rm -f $(classes)