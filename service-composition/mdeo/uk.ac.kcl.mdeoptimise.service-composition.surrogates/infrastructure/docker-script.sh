#!/usr/bin/env bash

docker build -f ./Dockerfile . -t mdeo:service-composition
docker run -ti --rm mdeo:service-composition /bin/bash

# Save the image to a file
docker save -o mdeo-composition.tar mdeo:service-composition