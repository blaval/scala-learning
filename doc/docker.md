# Learn how to use docker

## Table of content

1. [How to create a docker file](#how-to-create-a-docker-file)
2. [Useful commands](#useful-commands)

## How to create a docker file

usual content

```dockerfile
FROM alpine:3.13.10
RUN mkdir -p /build
COPY . /
RUN echo "foo" > /build/result

CMD ["cat", "/build/result"]
```

## Useful commands

```bash
docker build .
docker build . -t blaval02/demo:0.0.1

docker run --rm blaval02/demo:0.0.1
```