# Use the official Kafka image
FROM apache/kafka:3.7.0

# Expose the default Kafka port
EXPOSE 9092

# Command to run Kafka (usually handled by the image entrypoint, but good practice)
# CMD ["kafka-server-start", "/opt/kafka/config/server.properties"]
