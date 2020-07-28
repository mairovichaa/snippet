build_:
	rm -r -f build
	mkdir build
	javac -d build com/amairovi/Test.java

run:
	java -cp "build:resources" com.amairovi.Test ${FILE}

run2:
	java -cp "resources:build" com.amairovi.Test ${FILE}