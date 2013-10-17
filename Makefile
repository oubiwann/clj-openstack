VERSION=0.1.0
PROJECT=clj-rackspace
STANDALONE=target/$(PROJECT)-$(VERSION)-SNAPSHOT-standalone.jar

clean:
	rm -rf target

script-setup:
	wget https://raw.github.com/kumarshantanu/lein-exec/master/lein-exec
	wget https://raw.github.com/kumarshantanu/lein-exec/master/lein-exec-p
	chmod a+x lein-exec lein-exec-p
	clear
	@echo "Preparing to move lein-exec and lein-exec-p into /usr/local/bin ..."
	@read
	sudo mv lein-exec lein-exec-p  /usr/local/bin

build: clean
	@lein compile
	@lein uberjar

shell:
	@lein repl

dev:
	@lein run --development

run:
	@lein run

standalone: build
	java -jar $(STANDALONE)

kibit-only:
	@lein with-profile testing kibit

test-only:
	@lein with-profile testing test

coverage-only:
	@lein with-profile testing cloverage --text --html
	@cat target/coverage/coverage.txt
	@echo "body {background-color: #000; color: #fff;} \
	a {color: #A5C0F0;}" >> target/coverage/coverage.css

check: kibit-only test-only coverage-only

upload:
	@lein deploy clojars