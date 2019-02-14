FROM java:8
MAINTAINER Francisco Roa Prieto <franciscoroaprieto@gmail.com>

EXPOSE 8080
EXPOSE 8081

#RUN useradd -m franroa -d /opt/interview && \
#    yum makecache fast

RUN useradd -m franroa -d /opt/interview

RUN curl -O https://bootstrap.pypa.io/get-pip.py
RUN python get-pip.py
RUN pip install awscli --upgrade

USER franroa

WORKDIR /opt/interview

COPY run_script.sh \
     /opt/interview/run_script.sh

COPY cli \
     /opt/interview/cli

COPY ./environment/ \
     /opt/interview/

COPY interview-application-0.0.1.jar \
     /opt/interview/interview.jar

CMD /opt/interview/run_script.sh