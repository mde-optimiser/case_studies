FROM mjmgaro/centos-r-base

MAINTAINER Alex Burdusel alexandru.burdusel@kcl.ac.uk

COPY ./scripts/ /var/app/current/scripts

# Install maven
RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
RUN yum install -y apache-maven

# Install the R dependencies
RUN Rscript /var/app/current/scripts/dependencies.R

# Networking
EXPOSE 9090
