# Дипломная работа «Облачное хранилище»

## Описание проекта

Задача — разработать REST-сервис. Сервис должен предоставить REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.

Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок,
а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Требования к приложению

- Сервис должен предоставлять REST-интерфейс для интеграции с FRONT.
- Сервис должен реализовывать все методы, описанные в [yaml-файле](./CloudServiceSpecification.yaml):
    1. Вывод списка файлов.
    2. Добавление файла.
    3. Удаление файла.
    4. Авторизация.
- Все настройки должны вычитываться из файла настроек (yml).
- Информация о пользователях сервиса (логины для авторизации) и данные должны храниться в базе данных (на выбор студента).

## Описание и запуск FRONT

1. Установите nodejs (версия не ниже 19.7.0) на компьютер, следуя [инструкции](https://nodejs.org/ru/download/current/).
2. Скачайте [FRONT](./netology-diplom-frontend) (JavaScript).
3. Перейдите в папку FRONT приложения и все команды для запуска выполняйте из неё.
4. Следуя описанию README.md FRONT проекта, запустите nodejs-приложение (`npm install`, `npm run serve`).
5. Далее нужно задать url для вызова своего backend-сервиса.
    1. В файле `.env` FRONT (находится в корне проекта) приложения нужно изменить url до backend, например: `VUE_APP_BASE_URL=http://localhost:8080`.
        1. Нужно указать корневой url вашего backend, к нему frontend будет добавлять все пути согласно спецификации
        2. Для `VUE_APP_BASE_URL=http://localhost:8080` при выполнении логина frontend вызовет `http://localhost:8080/login`
    2. Запустите FRONT снова: `npm run serve`.
    3. Изменённый `url` сохранится для следующих запусков.
6. По умолчанию FRONT запускается на порту 8080 и доступен по url в браузере `http://localhost:8080`.
    1. Если порт 8080 занят, FRONT займёт следующий доступный (`8081`). После выполнения `npm run serve` в терминале вы увидите, на каком порту он запустился.

## Запуск REST приложения
1) Скачать репозиторий приложения с помощью git clone или архивом с git.
2) Собрать проект при помощи maven.

### Для запуска приложения используются команды Docker
В командной строке выполнить:
1) Создать образ при помощи команды "docker build -t diploma:latest -t diploma:1.0 ." (Точка в конце обязательна).
2) Запустить контейнер командой "docker-compose up".

После выполнения команд будут запущены два контейнера. Один с базой данных, другой с самим приложением.

## Авторизация
Для авторизации используются login и password пользователя, которые хранятся в таблице users поднимаемой базы данных.

## Работа приложения
FRONT приложение отправляет данные пользователя на endpoint /login rest-сервиса. ъ
Запрос проходит через CORS filter, CSRF filter, далее проверяется, что пользователь имеется в базе данных и является активным.
Если пользователь успешно найден, то возвращается auth-token для дальнейшей работы. 
Последующие запросы FRONT отправляет auth-token в headers. 
Методы /list, /file (post, put, delete, get) имеют проверку авторизованного пользователя по token, также дополнительно проверяются роли пользователя,
которые хранятся в таблице roles соответствующей базы данных.
Для того, чтобы разлогинитсья в приложении FRONT отправляет /logout. После чего токен пользователя удаляется и дальнейшие запросы 
становятся недоступны.

