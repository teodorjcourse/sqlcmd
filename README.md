#SQLCmd - это консольный клиент для работы с базами данных PostgreSQL

***
##Перед запуском
Настройте конфигурационный файл **database.properties**

        jdbc.driver=org.postgresql.Driver
        jdbc.connection_string=jdbc:postgresql://
        jdbc.host=localhost
        jdbc.port=5432
        jdbc.username=postgres
        jdbc.password=qwerty
***
Воспользуется командой **help** и ознакомьтесь с доступными командами:
1. **help** - выведет полный список команд с подробным описанием.
2. **help | list** - выведет досутпный список команд.
3. **help | <command>** - выведет подробное описание для указанной команды.
***
Интерфейс приложения доступен на двух языках: русском и английском.<br/>
Воспользуется командой **language | lang** для смены языка:<br/>
**lang** может принимать два значения: 
 - **ru** - русский язык
 - **en** - английский язык
 
 ##Запуск тестов
 Во время тестов, приложение будет пытаться подсоединиться к серверу базы данных по адресу:
 
          jdbc:postgresql://localhost:5432
 В классе **DataBaseManagerTestBase.java** задайте необходимые свойства:
 
```java
testDBProps.setProperty("jdbc.driver", "org.postgresql.Driver");
testDBProps.setProperty("jdbc.connection_string", "jdbc:postgresql://");
testDBProps.setProperty("jdbc.host", "localhost");
testDBProps.setProperty("jdbc.port", "5432");
testDBProps.setProperty("jdbc.username", "postgres");
testDBProps.setProperty("jdbc.password", "qwerty");
```

Также обратите внимание, что во время теста приложение будет
многократно создавать и удалять тестовые базы данных 
 ***test_junit_auto*** и ***test_junit_auto_2***. Если у вас есть базы 
 данных с идентичными именами - не запускайте тест или задайте собственные
 значения.

 
 
