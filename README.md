[![Build Status](https://travis-ci.org/ScalaBot-Team/PalaBrot.svg?branch=master)](https://travis-ci.org/ScalaBot-Team/PalaBrot)
![Actions](https://github.com/ScalaBot-Team/PalaBrot/actions/workflows/scala.yml/badge.svg)
[![codecov](https://codecov.io/gh/ScalaBot-Team/PalaBrot/branch/master/graph/badge.svg?token=FATJCF9P7O)](https://codecov.io/gh/ScalaBot-Team/PalaBrot)
# PalaBrot: Un bot para hacer las conversaciones menos peñazo 

PalaBrot es un bot de Telegram que resume a través de distintas técnicas de procesamiento del lenguaje natural los
últimos n mensajes de un grupo, resolviendo así las necesidades de un usuario que ante la avalancha de
mensajes en un grupo quiere conocer qué es lo que se ha dicho pero sin tener que leer el resto de la conversación.

## Tipo de aplicación y módulos
Como solución a este problema emplearemos un cliente de API de Telegram (la telegram Bot API) que recibirá distintas
peticiones de los usuarios por un hook. Este cliente estará divido en los siguientes módulos:

- Interfaz: Define los comandos de interacción con el bot y maneja las peticiones a través del hook devolviendo la
  respuesta correcta.
- Módulo de persistencia: Guarda los mensajes y gestiona su almacenamiento en la base de datos. 

- Módulo de resumen: Construye el resumen del texto a partir del modelo de bolsa de palabras creado en el módulo de persistencia devolviendo los resultados correctos a la interfaz

##  Servicios externos y herramientas
Para la aplicación es necesario usar distintos servicios y herramientas. En primer lugar se necesita una base de datos para almacenar parte de los mensajes, la opción escogida ha sido [ElasticSearch](https://www.elastic.co/es/elasticsearch/) debido a principalmente sus capacidades de indexación de texto con analizadores propios y a su capacidad de la alta ingesta de mensajes que puede tener el bot, y que esta da garantías de escalabilidad.

Por otro lado para los logs emplearemos [scala-logging](https://github.com/lightbend/scala-logging) como librería de logs y [Logback](http://logback.qos.ch/) como backend del log.

Por último para desarrollar el bot se usa [Canoe](https://github.com/augustjune/canoe) como wrapper para la API de Telegram el cual a su vez está basado en la librería cats-effects que es una de las librerías básicas para orientar Scala a programación funcional pura.

Para los tests se utiliza el framework [MUnit](https://scalameta.org/munit/) con la integración [munit-cats-effect](https://github.com/typelevel/munit-cats-effect) 

## Instrucciones / Instructions

Como gestor de tareas, usamos `sbt` que usa `build.sbt` como archivo de configuración y declaración. Para ejecutar 
los tests **NO** se debe usar:

```
build.sbt test
build.sbt coverage
```

Sino esta secuencia de comandos, que incluyen los necesarios para habilitar la generación de informes de cobertura
realizar los tests y generar el informe de cobertura

```
sbt clean
sbt coverage
sbt test
sbt coverageReport
```

## Integrantes
- Juan Helios García Guzmán
- David Ventas Marín
- Sergio Flor García
- Blanca Cano Camarero   

Este repositorio ha sido creado gracias a la plantilla  https://jj.github.io/curso-tdd/temas/git


## Usuario dirigido   

### Paco Pragmático   

- Usuario medio de telegram.  
- Quiere consultar el resumen pero sin calentarse la cabeza.   
- Cómodo, rápido y fácil de usar. 

### Suspicious123  

- Usuario sin miedo a trastear la tecnología.  
- Receloso con su privacidad (probablemente haya bicheado el código del proyecto).  
- Quiere controlar el tiempo que los  mensajes permanecen en el servidor.   
