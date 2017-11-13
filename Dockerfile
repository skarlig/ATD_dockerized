FROM qa-test-base:latest
MAINTAINER chris.stefani@outlook.com

COPY /atd_example/ $ROOT/

# Define the ENV variable
ENV BROWSER="chrome" \
    HOST="dawanda.com" \
    PLATFORM="en" \
    TIMEOUT="5"

CMD ["./gradlew", "clean", "test"]
