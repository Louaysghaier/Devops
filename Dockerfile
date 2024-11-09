# Start from an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set environment variables for Nexus credentials and artifact details
ARG NEXUS_USER
ARG NEXUS_PASSWORD
ARG NEXUS_URL="http://192.168.1.16:8081/repository/maven-releases/"
ARG GROUP_ID="tn/esprit"
ARG ARTIFACT_ID="tpFoyer-17"

# Install required dependencies
RUN apt-get update && apt-get install -y curl libxml2-utils && apt-get clean

# Fetch the latest version from Nexus
RUN mkdir /app \
    && cd /app \
    && echo "Fetching latest version from Nexus..." \
    && LATEST_VERSION=$(curl -u $NEXUS_USER:$NEXUS_PASSWORD -s "${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/maven-metadata.xml" | xmllint --xpath "string(//metadata/versioning/latest)" -) \
    && echo "Latest version retrieved: $LATEST_VERSION" \
    && JAR_FILE="${ARTIFACT_ID}-${LATEST_VERSION}.jar" \
    && curl -u $NEXUS_USER:$NEXUS_PASSWORD -O "${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/${LATEST_VERSION}/${JAR_FILE}" \
    && mv "$JAR_FILE" app.jar

# Expose the application port
EXPOSE 8082

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]