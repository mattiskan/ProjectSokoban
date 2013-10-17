all:
	mkdir -p bin
	javac -d bin src/*.java

.PHONY: check-syntax

check-syntax:
	javac -Xlint -d bin -cp .:bin $(CHK_SOURCES)

clean:
	touch bin/dummy.class
	rm -r bin/*.class
