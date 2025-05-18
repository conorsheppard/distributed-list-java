SHELL := /bin/bash

default: run

clean:
	mvn clean

compile: clean
	mvn compile

build: clean
	mvn package

deps:
	mvn dependency:copy-dependencies

test:
	mvn test

run:
	./shell/run

.SILENT:
.PHONY: default clean compile build deps test run
