# Diseño del bot por grafos   

Para cierto conjunto de mensajes ordenados este modelo de comportamiento prentende:   
- Ser capaz de determinar las temáticas sobre las que se ha tratado.   
- Almacenar de manera ordenada los mensaje a teniendo en cuenta las temáticas anteriores.   
- Mostrar a petición los mensajes de la temática demandada.  

## Diseño  
A grandes rasgos el diseño se compone de los siguientes pasos:  

### 1. Preprocesado del mensaje  
#### 1.1 Filtrado por carga semática: Dado un mensaje eliminar las palabras que no aporten significado.   
Por ejemplo en la oración:

```
Hoy hace un bonito día de verano, espero poder salir a dar un agradable paseo antes de que mis queridos
quehaceres asomen por la puerta. 
```

El contenido relevante sería eliminando adjetivos, determinantes, preposiciones, verbos tipo ser, estar o parecer (no recuerdo cómo se les decía a estos verbso), desiderativos... 

Resultando: 
```
Hoy día verano poder salir dar paseo quehaceres asomen puerta
```

#### 1.2 Determinación de raiz de las palabras filtradas. (podemos hacerlo quitando una serie de prefijos y subfijos para no calentarnos mucho la cabeza)

Si por ejemplo elimináramos las terminaciones : ` -a, -o, -e, -as, -os, -es, -ar, -er, -ir, conjugaciones...`
Y los comienzos `que- des- in im-...`  
```
hoy dí veran pod sal d pase hac asom puert
```
(Puede parecer este sistema ineficaz e impreciso pero más adelante veremos que no tiene demasiada importancia.   

### 2 Clasificación de las temáticas   

Para este proceso se cuenta con una base de datos de palabras (ya procesadas con el método 1.2) que estén relacionadas entre sí. 

Y se irán asociando menajes a otros mensajes según su coincidencia con el siguiente algoritmo: 
Normas previas: 

- Varios menajes consecutivos o en un periodo de tiempo "cercano" de un mismo usuario cuentan como menaje único, es decir, da lugar a la misma categoría. A partir de ahora nos definiremos a ella como unidad. 
- Existe $f (C: Categoría , m: unidad) \longrightarrow \mathbb R$ capaz de discernir en una probabilidad o cualquier otro sistema, si $m$ pertenece por relación de significado a $C$. $f$ induce una relación de equivalencia. Y diremos que $m$ pertenece a $C$.

1. La primera unidad $m_0$ será procesada y formará una categoría $C_0$.
2. Sea $\mathcal C$ una familia de categoría existentes y disjuntas, dado una nueva unidad $m_n$ con $n>1$ natural, 
se comprobará  gracias a  $f$  si $m_n$ está relacionado con alguna categoría $C$ de la familia.  
Si es así se añadade a la lista de unidades que pertenecen a esta, si no crea su nueva categría $C_m$ que se añade a la familia $C_m$.   



Llegado este punto te preguntarás cómo construir $f$.   

## Determinación de las distintas categoría   

Podemos sacarla analizando libros ya que son fáciles de obtener en `.txt`, cada párrafo recibirá un preprocesado de tipo (1) obteniendo así un conjunto de palabras prepocesadas $P$.

Asignaremos ahora una relación al par no ordenado $a,b \in P$ con $a \neq b$ $(a,b)++$ o $(a,b) = 1$ si era su primera aparición. Además incrementaremos en una unidad la incidencia (número de relaciones que tienen) de $I(a) ++$ y $I(b)++$.  

Una base de datos relacional sería muy útil en este caso ( pobre mongo). 

A partir de aquí podremos determinar una eurística $a ~ b$ si por ejemplo la frecuencia relativa de la relación entre el número de incidencia es mayor que una cierta constante $k$, esto es $\frac{ f(a,b)} {min( I(a), I(b) )} > k.$  


Una mejora de este algoritmo es que vaya mejorando las ponderaciones a partir de los chat.  
(Hay que tener en cuenta un posible desbordamiento, bastará con un mapeado de a para arregarlo) 





