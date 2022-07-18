# WebParser

Краткая информация о проекте. :octocat:

<br> 

***Основная цель*** 🎯 : Парсит цены с сайта Mvideo

***Технологии в проекте*** 🈯 :

- API - Spring boot (Data, JPA, Security)
- парсинг - Selenium
- базы данных - Postgres, Mongodb
- CI/CD - Jenkins

> Примечание*: проект ещё в разработке. Однако парсинг работает корректно:
> [ссылка на xml документ](https://drive.google.com/file/d/1jSm5ieMOyr9Y5xlYUTcLxAyu0O97I5B7/view?usp=sharing)

<br>

# Состояние микросервисов

|  Название   |                                                                  Build                                                                  | Code Coverage | SonarQube | 
|:-----------:|:---------------------------------------------------------------------------------------------------------------------------------------:|:-------------:|:---------:|
|   client    |      [![Build Status](http://51.250.69.170:8080/job/webparser-client/badge/icon)](http://51.250.69.170:8080/job/webparser-client/)      |     empty     |   empty   |
|    admin    |       [![Build Status](http://51.250.69.170:8080/job/webparser-admin/badge/icon)](http://51.250.69.170:8080/job/webparser-admin/)       |     empty     |   empty   |
|   parser    |                                                                  empty                                                                  |     empty     |   empty   |
| mail-sender | [![Build Status](http://51.250.69.170:8080/job/webparser-mail-sender/badge/icon)](http://51.250.69.170:8080/job/webparser-mail-sender/) |     empty     |   empty   |

<br>
<br>

## Архитектура проекта

Архитектура состоит из микросервисов, разворачивающихся в pod-ах в **Kubernetes**-а.

Внешней точкой соприкосновения является **Nginx Ingress Controller**.

Далее весь трафик переходит во
внутренний [Ingress](https://github.com/AlimKugot/WebParser/blob/DevOps/DevOps/k8s/ingress.yaml), который служит нам в
качестве
Service Discovery и LoadBalancer-а.

**Микросервисы** (client, parser...) же состоят из Spring Boot приложений в Docker-контейнерах.

> Примечание*: Kubernetes должен разворачиваться хотя бы на трёх серверах (master и slave ноды), однако
> в целях экономии я использовал костыли, чтоб развернуть его на одном VPS сервере.

<br>

<a href="https://i.ibb.co/BCCsrST/image.jpg">
    <img src="https://i.ibb.co/qmFs8d7/Smaller.jpg" alt="Архитектура" border="0">
</a>

### CI/CD

Концептуальная модель того, как выглядит настройка ci/cd в моём приложении.

Более точно описано в Groovy
скрипте [Jenkins](https://github.com/AlimKugot/WebParser/blob/DevOps/DevOps/jenkins/Jenkins)

<a href="https://ibb.co/2kkKsfc">
    <img src="https://i.ibb.co/Snhf62c/Spring-boot-Kubernetes.jpg" alt="Spring-boot-Kubernetes" border="0">
</a>