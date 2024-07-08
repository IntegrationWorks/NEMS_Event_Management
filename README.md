cd into NEMS_Test_Harness
docker-compose -f docker-compose_solace.yml up --build -d

# Build and run the publisher
cd NEMS_Test_Publisher
./mvnw clean package -DskipTests
cd ..
docker-compose -f docker-compose_publisher.yml up --build -d

# Build and run the subscribers
cd NEMS_Test_Subscriber
./mvnw clean package -DskipTests
cd ..
docker-compose -f docker-compose_subscribers.yml up --build -d


# Run the react app

cd event-app
docker-compose --build -d

