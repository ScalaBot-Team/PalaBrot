# PalaBrot: Un bot para hacer las conversaciones menos peñazo 

PalaBrot es un bot de Telegram que resume a través de distintas técnicas de procesamiento del lenguaje natural los
últimos n mensajes de un grupo, resolviendo así las necesidades de un usuario que ante la avalancha de
mensajes en un grupo quiere conocer qué es lo que se ha dicho pero sin tener que leer el resto de la conversación.

## Tipo de aplicación y módulos
Como solución a este problema emplearemos un cliente de API de Telegram (la telegram Bot API) que recibirá distintas
peticiones de los usuarios por un hook. Este cliente estará divido en los siguientes módulos:

- Interfaz: Define los comandos de interacción con el bot y maneja las peticiones a través del hook devolviendo la
  respuesta correcta.
- Módulo de preprocesamiento: Transforma el conjunto de mensajes dados en una representación normalizada en forma de
  bolsa de palabras.
- Módulo de resumen: Construye el resumen del texto a partir del modelo de bolsa de palabras creado en el módulo de
  preprocesamiento devolviendo los resultados correctos a la interfaz.

##  Servicios externos y herramientas
Para la aplicación es necesario usar distintos servicios y herramientas. En primer lugar se necesita una base de datos 
para almacenar parte de los mensajes, la opción escogida ha sido [MongoDB](https://www.mongodb.com/es) 
debido a la alta ingesta de mensajes que puede tener el bot, ya que esta da garantías de escalabilidad.

Por otro lado para los logs emplearemos [scala-logging](https://github.com/lightbend/scala-logging) como librería de 
logs y [Logback](http://logback.qos.ch/) como backend del log.

Por último para desarrollar el bot se usa [Canoe](https://github.com/augustjune/canoe) como wrapper para la API de 
Telegram el cual a su vez está basado en la librería cats-effects que es una de las librerías básicas para orientar 
Scala a programación funcional pura. 

## Integrantes
- Juan Helios García Guzmán
- David Ventas Marín
- Sergio Flor García
- Blanca Cano Camarero   

Este repositorio ha sido creado gracias a la plantilla  https://jj.github.io/curso-tdd/temas/git
