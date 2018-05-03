FROM debian

COPY sleepforever /sleepforever
RUN \
	chmod 755 /sleepforever && \
	apt-get update -y && \
	apt-get install -y vim nano git && \
	rm -rf /var/lib/apt/lists/* && \
	mkdir /git && \
	cd /git && \
	git config --global user.email "demo@dynatrace.com" && \
	git config --global user.name "Demo" && \
	git clone "https://github.com/grabnerandi/SpringBootBookstoreWithProblems"

WORKDIR /git
CMD /sleepforever
