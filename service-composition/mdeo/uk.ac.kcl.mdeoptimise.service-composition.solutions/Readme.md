# Service Composition

In order to run, build the surrogates project using `mvn clean compile package` and add the compiled
jar with dependencies to the path of this project.

Then run the docker container that will install all the required R scripts and rJava depencencies, then
it will compile the surrogates project again and run the RPC server listening on port 9090.

Chekc java library path:

java -XshowSettings:properties method

add JRI to library path on linux:

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib64/R/library/rJava/jri

Remember that the R model script saves some files to /tmp without cleanup.