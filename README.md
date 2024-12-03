# slack2pachka

## Описание
Проект предназначен для переноса данных из Слак-дампов (штатных и альтернативных) в мессенджер Пачка

## Инсталляция

- скачать исходники
- mvn clean install
- настроить application.properties в соответствии со своими нуждами и потребностями
- пользоваться!

## application.properties
Содержит комментарии, поясняющие зачем нужны те или иные параметры

## Использование
Программа представляет собой набор небольших утилит, основанных на [spring-shell](https://spring.io/projects/spring-shell)

AVAILABLE COMMANDS
- Built-In Commands
встроенные команды spring-shell, за подробными объяснениями обратитесь к документации
```
    help: Display help about available commands
    stacktrace: Display the full stacktrace of the last error.
    clear: Clear the shell screen.
    quit, exit: Exit the shell.
    history: Display or save the history of previously run commands
    version: Show version info
    script: Read and execute commands from a file.
```
- группа утилит для анализа информации дампа
```
analyze
    analyze alt: dump archived slack channels, with owners
    analyze owners: dump slack channel owners list
    analyze messages: aggregate slack messages to get compact field representation
    analyze archived: dump archived slack channels, with owners

```
- создание пользователя

```
create
    create user: create user
```
- группа утилит для удаления пользователей, участников бесед, каналов(бесед) 
беседа не удаляется, в API отсутствует возможность удалять каналы
костылим - делаем канал приватным, удаляем участников, называем канал "deleted"(например)
```
drop
    drop users: drop users
    drop members: drop channel members
    drop channels: drop channels

```
группа утилит для исправления уже созданных сущностей:
- информация о пользователе
- исправляем(дополняем) аттачменты сообщений. В процессе импорта несколько раз возникала необходимость дозаливки аттачментов
- исправляем комментарии (при работе с альтернативными дампами потеряли комментарии, пришлось исправлять)
- 
```
fix
    fix attachments: fix message attachments
    fix user: change user data
    fix threads: fix message threads
```
и, наконец, самая важная группа команд:
- импорт данных из штатного дампа
- импорт пользователей из штатного дампа
- импорт сообщений из альтернативного дампа
```
import
    import users: (stage 2) import users and update channel member lists
    import alt: import from alternate dump
    import messages: (stage 1) import messages
```
группа тестов для проверки основных настроек и работоспособности сервисов
```
test
    test message: message API tests
    test user: user API tests
    test all: all API complete tests
    test channel: channel API tests
```
## Поддержка
если вы столкнетесь с какими-либо проблемами и трудностями мы постараемся по возможности вам помочь
проконсультируем, подскажем и даже поправим код (в пределах разумного)

## Roadmap
программа одноразовая, мы не планируем ее дальнейшее развитие, наш переезд уже состоялся
все, для чего она предназначена, работает
возможно вызахотите реализовать что-то по-своему, по-другому
мы будем рады если то, что мы сделали, вам хотя бы немного упростит ваш переезд
удачных импортозамещений!

## Contributing

будем рады помочь в реализации Ваших идей и внести Ваши доработки

## Authors and acknowledgment
Большая и дружная команда Assist

## License
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

