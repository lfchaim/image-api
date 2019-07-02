# image-api
Image API  

## Baixando o projeto
$ git clone https://github.com/lfchaim/image-api  

## Implantando o projeto
### Maven
$ mvn clean install  
$ cd target  
$ java -jar image-api-0.0.1-SNAPSHOT.jar  

### Eclipse
Abra a classe ImageApiApplication e clique no menu "Run" / "Run As" / "Java Application"  

## Testando o projeto
### Spring Actuator
Abra um navegador e informe a seguinte URL.  
[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)  

Deverá retornar um JSON parecido com:  
{"status":"UP","details":{"diskSpace":{"status":"UP","details":{"total":306253819904,"free":187956105216,"threshold":10485760}}}} 
 
