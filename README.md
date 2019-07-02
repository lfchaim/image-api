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
 
## Postman
### POST /image/info
Response  
```
{
    "fileName": "25321427.jpg",
    "imageInfo": {
        "formatDetails": "Jpeg/DCM",
        "bitsPerPixel": 24,
        "comments": [],
        "format": "JPEG",
        "formatName": "JPEG (Joint Photographic Experts Group) Format",
        "height": 460,
        "mimeType": "image/jpeg",
        "numberOfImages": 1,
        "physicalHeightDpi": -1,
        "physicalHeightInch": -1,
        "physicalWidthDpi": -1,
        "physicalWidthInch": -1,
        "width": 460,
        "progressive": false,
        "transparent": false,
        "colorType": "YCbCr",
        "compressionAlgorithm": "JPEG"
    }
}
```

