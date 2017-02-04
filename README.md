# eureka client in spring mvc project 

- First of all run Eureka Server - [http://localhost:8080/eureka/jsp/status.jsp](http://localhost:8080/eureka/jsp/status.jsp)

``
mvn clean jetty:run-war
``

- Then run Eureka Client application - [http://localhost:8081/eureka-client/rest/myservice/bhuwan](http://localhost:8081/eureka-client/rest/myservice/bhuwan)

``
mvn tomcat7:run
``