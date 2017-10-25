FROM docker-registry.dawanda.in/qa-test-base:latest
MAINTAINER qa@dawandamail.com

COPY /desktop/ $ROOT/

# Define the ENV variable
ENV BROWSER="chrome" \
    PROFILE="desktopsmoketest" \
    HOST="dawanda.com" \
    SCHEMA="https" \
    PLATFORM="de" \
    DB_IP="10.10.47.4" \
    DB_PORT="3306" \
    DB_NAME="dawanda_production" \
    DB_USER="qauser" \
    DB_PASSWORD="EeBohr2AhKi7ui3aeyah" \
    TIMEOUT="10" \
    FROM_EMAIL_ADDRESS="dawanda@dawanda.com" \
    EMAIL_ADDRESS_HOST="dawanda.com"
#   JENKINS_BUILD_NUMBER --> Jenkins will pass the build number if possible
#   COOKIE_NAME --> It is possible to set an individual cookie with a name
#   COOKIE_VALUE --> It is possible to set an individual cookie with a value
#   HUB_HOST --> It is possible to set an individual HUB IP
#   PORT --> It is possible to set an individual HUB PORT

CMD ["./gradlew", "clean", "test"]
