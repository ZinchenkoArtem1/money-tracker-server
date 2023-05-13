FROM gradle:jdk17-alpine
RUN mkdir /home/gradle/buildWorkspace
COPY . /home/gradle/buildWorkspace

WORKDIR /home/gradle/buildWorkspace
RUN gradle build
EXPOSE 8080

CMD gradle startSpringBoot