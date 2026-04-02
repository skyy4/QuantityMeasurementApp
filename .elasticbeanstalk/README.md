This project can be deployed to AWS Elastic Beanstalk as a Java 21 Spring Boot application.

Recommended first deployment flow:

1. Build the application JAR locally with `mvn clean package`.
2. Create an Elastic Beanstalk environment using the Java SE Corretto 21 platform.
3. Upload the generated JAR from `target/`.
4. Set `SPRING_PROFILES_ACTIVE=prod` and the other environment variables listed in `DEPLOY_AWS_ELASTIC_BEANSTALK.md`.

You do not need Docker for the first deployment.
