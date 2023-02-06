sources := $(shell find -name '*.java')
main := com.gigamonkeys.chess.Chess
resources :=

all: build

build: compile resources

compile: $(sources)
	javac -Xdiags:verbose -Xlint:deprecation -Xlint:unchecked --enable-preview --source 17 -cp src:classes -d classes/ $(sources)

pretty:
	prettier --plugin ~/node_modules/prettier-plugin-java/ --write **/*.java

resources: $(resources)

run:
	java --enable-preview -cp classes $(main)

test:
	java -cp classes com.gigamonkeys.chess.Test

clean:
	rm -rf classes

tidy:
	find . -name '*~' -delete

chess.jar:
	jar --create --file $@ --main-class $(main) -C classes .
