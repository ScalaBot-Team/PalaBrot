# PalaBrot: Un bot para hacer las conversaciones menos peñazo 

PalaBrot es un bot de Telegram que resume a través de distintas técnicas de procesamiento del lenguaje natural los
últimos n mensajes de un grupo, resolviendo así las necesidades de un usuario que ante la avalancha de
mensajes en un grupo quiere conocer qué es lo que se ha dicho pero sin tener que leer el resto de la conversación.

## Tipo de aplicación y módulos
Como solución a este problema emplearemos un cliente de API de Telegram (la telegram Bot API) que recibirá distintas
peticiones de los usuarios por un hook. Este cliente estará divido en los siguientes módulos:

- Interfaz: Define los comandos de interacción con el bot y maneja las peticiones a través del hook devolviéndoles la
  respuesta correcta.
- Módulo de preprocesamiento: Transforma el conjunto de mensajes dados en una representación normalizada en forma de
  bolsa de palabras.
- Módulo de resumen: Construye el resumen del texto a partir del modelo de bolsa de palabras creado en el módulo de
  preprocesamiento
  devolviéndo los resultados correctos a la interfaz

## Integrantes
- Juan Helios García Guzmán
- David Ventas Marín
- Sergio Flor García
- Blanca Cano Camarero   

Este repositorio ha sido creado gracias a la plantilla  https://jj.github.io/curso-tdd/temas/git
