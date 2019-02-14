.PHONY: test docker

VERSION?=$(shell git describe --always | sed 's/^v//')
DOCKERNAME?=interview
DOCKERTAG?=${DOCKERNAME}:${VERSION}

NON_PRODUCTION_AWS_ACCOUNT?="..."


# Apache Maven related side notes:
# --batch-mode : recommended in CI to inform maven to not run in interactive mode (less logs)
# --show-version  : strongly recommended in CI, will display the JDK and Maven versions in use.
#      Very useful to be quickly sure the selected versions were the ones you think.
# --update-snapshots : force maven to update snapshots each time (default : once an hour, makes no sense in CI).
# -Dsurefire.useFile=false : useful in CI. Displays test errors in the logs directly (instead of
#                            having to crawl the workspace files to see the cause).
# sh "mvn --batch-mode -V -U -e clean deploy -Dsurefire.useFile=false"

test:
	./kickstart.sh
	mvn --batch-mode --show-version --update-snapshots --errors clean install -Dmaven.test.skip=true -Dsurefire.useFile=false -f ./pom.xml
	mvn package -Dmaven.test.skip=true -Dsurefire.useFile=false -f ./application/pom.xml
	mvn clover:setup test clover:aggregate clover:clover

docker:
	# mvn clean deploy -DskipTests=true
	@cp application/target/interview-application-0.0.1.jar docker
	@cp -R ./application/environment docker
	docker build -t ${DOCKERTAG} docker