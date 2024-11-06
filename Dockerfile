# Start from an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set environment variables for Nexus credentials and artifact details
ARG NEXUS_USER
ARG NEXUS_PASSWORD
ARG NEXUS_URL="http://192.168.1.15:8081/repository/maven-releases/"
ARG GROUP_ID="tn/esprit"
ARG ARTIFACT_ID="tpFoyer-17"

# Install necessary tools (curl and jq for parsing JSON)
RUN apt-get update && apt-get install -y curl jq

# Download the latest version of the JAR file from Nexus
RUN mkdir /app \
    && cd /app \
    && LATEST_VERSION=$(curl -u $NEXUS_USER:$NEXUS_PASSWORD -s "$NEXUS_URL$GROUP_ID/$ARTIFACT_ID/maven-metadata.xml" | \
        xmllint --xpath "string(//metadata/versioning/latest)" -) \
    && echo "Latest version is $LATEST_VERSION" \
    && curl -u $NEXUS_USER:$NEXUS_PASSWORD -O "$NEXUS_URL/$GROUP_ID/$ARTIFACT_ID/$LATEST_VERSION/$ARTIFACT_ID-$LATEST_VERSION.jar"

# Rename the JAR file for convenience
RUN mv "$ARTIFACT_ID-$LATEST_VERSION.jar" app.jar

# Expose the application port
EXPOSE 8082

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
