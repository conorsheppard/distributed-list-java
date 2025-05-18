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

test-coverage:
	mvn clean org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent verify org.jacoco:jacoco-maven-plugin:0.8.13:report

check-coverage:
	open -a Google\ Chrome target/jacoco-report/index.html

coverage-badge-gen:
	python3 -m jacoco_badge_generator -j target/jacoco-report/jacoco.csv

test-suite: test-coverage check-coverage coverage-badge-gen

.SILENT:
.PHONY: default clean compile build deps test run test-coverage check-coverage coverage-badge-gen test-suite
