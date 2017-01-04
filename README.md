# play-anorm-example
A small study project on [play](https://www.playframework.com/) with [Anorm](https://www.playframework.com/documentation/2.5.x/ScalaAnorm)

## Launching
Just clone this repository and launch the project using sbt run

## Persistence
This project uses [Anorm](https://www.playframework.com/documentation/2.5.x/ScalaAnorm) to access the persistence store
and by default starts a [H2 Database Engine](http://www.h2database.com/html/main.html) that stores everything in-memory.

## Available REST resources
Using [httpie](https://httpie.org/), (brew install httpie), a command-line HTTP client you can easily access
REST resources you can test the REST service. You can easily see which resources are available by
reading the file `/conf/routes`:

### Swagger
[Swagger](http://swagger.io/) A REST API framework is available:

```
GET           /api-docs              controllers.ApiHelpController.getResources
```

```bash
$ http -v :9000/api-docs

GET /api-docs HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8

HTTP/1.1 200 OK
Access-Control-Allow-Origin: *
Connection: close
Content-Length: 4666
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:28:15 GMT

{
    "basePath": "http://localhost:9000",
    ...
}
```

### Buildinfo
A handy component to get information about the build of the service:

```
GET           /api/info              com.github.dnvriend.component.buildinfo.controller.BuildInfoController.info
```

```bash
$ http -v :9000/api/info

GET /api/info HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8

HTTP/1.1 200 OK
Content-Length: 173
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:29:30 GMT

{
    "builtAtMillis": "1483514862905",
    "builtAtString": "2017-01-04 07:27:42.905",
    "name": "play-anorm-example",
    "sbtVersion": "0.13.13",
    "scalaVersion": "2.11.8",
    "version": "1.0.0"
}
```

### Ping controller
To find out whether or not a service is available:

```
GET           /api/ping              com.github.dnvriend.component.ping.controller.PingController.ping
```

```bash
$ http -v :9000/api/ping
GET /api/ping HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8



HTTP/1.1 200 OK
Content-Length: 4
Content-Type: text/plain; charset=utf-8
Date: Wed, 04 Jan 2017 07:30:39 GMT

pong
```

### Health check
To find out what the health is of our component; this means whether or not it can service by checking if the
dependent components like databases, kafka and third party services are accessible:

```
GET           /api/health            com.github.dnvriend.component.health.controller.HealthController.check
```

```bash
$ http -v :9000/api/health
GET /api/health HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8

HTTP/1.1 200 OK
Content-Length: 1
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:32:04 GMT

1
```

### A simple CRUD service using the Anorm database access library
CRUD stands for Create, Read, Update and Delete and are the four basic operations you wish to do on entities. Most
registration services do this out of the box and are very easy to create.

```
GET           /api/person            com.github.dnvriend.component.personregister.controller.PersonController.getAll(limit: Int ?= 10, offset: Int ?= 0)
GET           /api/person/:id        com.github.dnvriend.component.personregister.controller.PersonController.getById(id: Long)
POST          /api/person            com.github.dnvriend.component.personregister.controller.PersonController.save()
PUT           /api/person/:id        com.github.dnvriend.component.personregister.controller.PersonController.updateById(id: Long)
DELETE        /api/person/:id        com.github.dnvriend.component.personregister.controller.PersonController.deleteById(id: Long)
```

### Creating a person

```bash
$ http -v post :9000/api/person name=Dennis age:=42

POST /api/person HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 29
Content-Type: application/json
Host: localhost:9000
User-Agent: HTTPie/0.9.8

{
    "age": 42,
    "name": "Dennis"
}

HTTP/1.1 200 OK
Content-Length: 33
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:33:44 GMT

{
    "age": 42,
    "id": 1,
    "name": "Dennis"
}
```

Lets add another person

```bash
$ http -v post :9000/api/person name=foo age:=22

POST /api/person HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 26
Content-Type: application/json
Host: localhost:9000
User-Agent: HTTPie/0.9.8

{
    "age": 22,
    "name": "foo"
}

HTTP/1.1 200 OK
Content-Length: 30
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:36:13 GMT

{
    "age": 22,
    "id": 2,
    "name": "foo"
}
```

### Updating a person by id

```bash
$ http -v put :9000/api/person/1 name=dennis age:=40

PUT /api/person/1 HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 29
Content-Type: application/json
Host: localhost:9000
User-Agent: HTTPie/0.9.8

{
    "age": 40,
    "name": "dennis"
}

HTTP/1.1 200 OK
Content-Length: 26
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:35:10 GMT

{
    "age": 40,
    "name": "dennis"
}
```

### Getting a list of person

```bash
$ http -v :9000/api/person

GET /api/person HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8


HTTP/1.1 200 OK
Content-Length: 66
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:36:53 GMT

[
    {
        "age": 40,
        "id": 1,
        "name": "dennis"
    },
    {
        "age": 22,
        "id": 2,
        "name": "foo"
    }
]
```

### Getting a list of person with an explicit max number of records

```bash
$ http -v :9000/api/person limit==1

GET /api/person?limit=1 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8



HTTP/1.1 200 OK
Content-Length: 35
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:37:39 GMT

[
    {
        "age": 40,
        "id": 1,
        "name": "dennis"
    }
]
```

### Getting a list of person with an explicit offset and limit

```bash
$ http -v :9000/api/person offset==1 limit==1

GET /api/person?offset=1&limit=1 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:9000
User-Agent: HTTPie/0.9.8



HTTP/1.1 200 OK
Content-Length: 32
Content-Type: application/json
Date: Wed, 04 Jan 2017 07:38:09 GMT

[
    {
        "age": 22,
        "id": 2,
        "name": "foo"
    }
]
```

### Deleting a person

```bash
$ http -v delete :9000/api/person/1

DELETE /api/person/1 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 0
Host: localhost:9000
User-Agent: HTTPie/0.9.8


HTTP/1.1 204 No Content
Date: Wed, 04 Jan 2017 07:39:03 GMT

$ http -v delete :9000/api/person/2
DELETE /api/person/2 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 0
Host: localhost:9000
User-Agent: HTTPie/0.9.8

HTTP/1.1 204 No Content
Date: Wed, 04 Jan 2017 07:39:03 GMT
```