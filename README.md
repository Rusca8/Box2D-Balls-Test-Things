# Box2D-Balls-Test-Things

##Ball Physics with LibGDX, powered by Box2D.
*Right click to create balls, left click to select them for destroyal, and space to actually destroy the selected ones.*

Written in spanish, actually.


Prueba de todas las cosas que voy a necesitar si hago algo con el motor de física **Box2D** sobre **LibGDX** en **java**.

La aplicación empieza en *Primes.java*, y aquí se abre una pantalla del juego (*Pantalla.java*, `Screen`) que funciona a través de una cámara (`OrthographicCamera`) y un encuadre (`Viewport`), que he elegido de tipo `fitViewport` para que amplie la imagen al máximo sin recortarla ni distorsionarla. 

Elijo una proporción **3:2** porque está entre el mínimo (**3:4**) y el máximo (**16:9**) de las proporciones típicas en dispositivos actuales, de modo que minimizo las barras negras que saldrán a los lados. La aplicación corre tipo `portrait` (lado largo en vertical), así que hay que ir al `AndroidManifest` en la versión para Android del proyecto *Gradle* que ha montado *LibGDX* y fijar la orientación de este modo.

La imagen del fondo de pantalla la guardo en *Recursos.java*, que es donde guardaré todas las imágenes y todo lo que vaya a necesitar cargar al principio. En `Recursos` monto un método `cargar` que se llama al empezar el juego y que carga todo lo que necesitaremos.

`Boludo` es como he llamado al objeto que asociaré a los cuerpos de *Box2D* (`bolas`) para asignarles información y sprites. De momento sólo tiene un boleano para marcar si hay que destruirlo. Está aparte en `Boludo.java`.

A los cachos de cuerpo (`Fixture`) les he llamado `galletas`, porque es como coger una masa y un molde y recortar jejej
