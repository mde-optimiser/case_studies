FROM mjmgaro/centos-r-base

MAINTAINER Alex Burdusel alexandru.burdusel@kcl.ac.uk

# Expose the R server port
COPY ./scripts/ /var/app/current/scripts

EXPOSE 9090

#RUN Rscript /var/app/current/scripts/dependencies.R