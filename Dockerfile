FROM docker-registry.dawanda.in/qa-test-base:latest
MAINTAINER chris.stefani@outlook.com

COPY /atd_example/ $ROOT/

# Define the ENV variable
ENV BROWSER="chrome" \
    HOST="dawanda.com" \
    SCHEMA="https" \
    PLATFORM="en" \
    TIMEOUT="5"

CMD ["./gradlew", "clean", "test"]
