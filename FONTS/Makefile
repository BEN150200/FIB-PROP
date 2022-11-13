JAVAC = javac -Xlint:unchecked -d ../EXE/ 
sources = $(shell find . -type f -name '*.java')
javafile = $(sources:.java=.class)
classes = $(shell find .. -type f -name '*.class')

all : $(javafile)

clean :
	rm -f $(classes)

%.class : %.java
	$(JAVAC) $<