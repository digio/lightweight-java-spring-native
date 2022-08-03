# Spring Native

This application is part of the Lightweight Java Tech Assessment. It's mainly focused on Spring Native with Graalvm.



---
## Regular Spring Boot
### Build
To build the regular Spring Boot application, run the following command:
```bash
cd app
./gradlew clean build
```

### Run
To run the regular Spring Boot application, run the following command:
```bash
./gradlew bootRun
```

Once it's running in local, to test things out, try
```bash
localhost:8080/airports
```


---

## Spring Native
### Build
To build the Spring Native application, run the following command(_build time for native takes way longer than regular spring boot, grab a coffee_):
```bash
cd app
./gradlew clean nativeCompile
```
This will create an executable `app/build/native/nativeCompile/app`

### Run
Once the build is done, run the following command:
```bash
.app/build/native/nativeCompile/app
```

Once it's running, to test things out, try
```bash
localhost:8080/airports
```


---
## Deployment
The application currently only build into native image and deploy to AWS lambda via CDK.

To bootstrap the AWS environment:
```bash
cd infra
cdk bootstrap --toolkit-stack-name=GSNCDKToolkit --qualifier=graalvm-spring-native -v
```
To build and deploy the application, make sure docker is running then:
```bash
cdk deploy
```

This will pull the docker image, build the native image and deploy to AWS lambda.
The reason this will build the project again is that the previous `nativeCompile` will only build the executable based on
you local machine OS. However, to customize runtime on AWS lambda is based on Linux, which requires the executable build on 
Linux image. So, we need to build the native image again. Time to grab your second coffee,this process will take a while.
