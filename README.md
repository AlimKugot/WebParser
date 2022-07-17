# WebParser

Краткая информация о проекте. :octocat:

<br> 

***Цель*** 🎯 : Парсит цены с сайта Mvideo

***База*** 📚 : все алгоритмы основаны на XPath с использованием технологии Selenium.

***Язык и фреймы*** 🈯 : развёртывается на базе Spring Boot Framework на языке Java. (для браузера используется JS)

<br>

# Состояние микросервисов

|  Название   |                                                             Build                                                             | Code Coverage | SonarQube | 
|:-----------:|:-----------------------------------------------------------------------------------------------------------------------------:|:-------------:|:---------:|
|   client    | [![Build Status](http://51.250.69.170:8080/job/webparser-client/badge/icon)](http://51.250.69.170:8080/job/webparser-client/) |     empty     |   empty   |
|    admin    |  [![Build Status](http://51.250.69.170:8080/job/webparser-admin/badge/icon)](http://51.250.69.170:8080/job/webparser-admin/)  |     empty     |   empty   |
|   parser    |                                                             empty                                                             |     empty     |   empty   |
| mail-sender |  [![Build Status](http://51.250.69.170:8080/job/webparser-admin/badge/icon)](http://51.250.69.170:8080/job/webparser-admin/)  |     empty     |   empty   |

<br>
<br>

## CI/CD

Концептуальная модель того, как выглядит настройка ci/cd в моём приложении.

Разворачиваю в Jenkins (до этого использовал buddy)

<a href="https://ibb.co/bLznPqg"><img src="https://i.ibb.co/CvhFzN5/cicd-new.jpg" alt="cicd-new" border="0" /></a>