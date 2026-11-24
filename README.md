# Principio de Inversión de Dependencias (DIP) — La "D" de SOLID

**Curso:** Programación II — Grupo 412 — IU Antonio José Camacho
**Tema (Grupo 5):** D — *Dependency Inversion* / Inversión de Dependencias
**Integrantes:** Jefferson Paredes · Deibit Urbano

---

## 1. ¿Qué dice el principio?

Lo formuló **Robert C. Martin ("Uncle Bob")** y tiene dos reglas:

> **A.** Los módulos de **alto nivel** no deben depender de los módulos de **bajo nivel**. Ambos deben depender de **abstracciones**.
>
> **B.** Las **abstracciones** no deben depender de los **detalles**. Los **detalles** deben depender de las **abstracciones**.

| Concepto | Qué es | En nuestro ejemplo |
|---|---|---|
| **Alto nivel** | La lógica de negocio, lo importante del sistema | `ServicioPedidos` (procesar un pedido) |
| **Bajo nivel** | Detalles técnicos: BD, correo, archivos, APIs | `RepositorioMySQL`, `NotificadorCorreo`, `NotificadorWhatsApp` |
| **Abstracción** | Una interfaz que dice *qué* se hace, no *cómo* | `RepositorioPedidos`, `Notificador` |

### ¿Por qué se llama "inversión"?

Normalmente la dependencia va **de arriba hacia abajo**: la lógica de negocio usa directamente la base de datos.

```
SIN DIP:   ServicioPedidos ───────► BaseDatosMySQL
           (alto nivel)              (bajo nivel)
```

Con DIP, la clase de bajo nivel pasa a depender de una interfaz que pertenece al alto nivel. La flecha que llegaba a MySQL ahora **sale** de MySQL: la dependencia se **invierte**.

```
CON DIP:   ServicioPedidos ───────► «interface» RepositorioPedidos
                                              ▲
                                              │ implements
                                     RepositorioMySQL / RepositorioEnMemoria
```

---

## 2. Estructura del repositorio

```
src/
├── sindip/                     ❌ Ejemplo que VIOLA el principio
│   ├── BaseDatosMySQL.java
│   ├── CorreoGmail.java
│   ├── ServicioPedidos.java    ← crea sus dependencias con "new"
│   └── Main.java
│
└── condip/                     ✅ Ejemplo que CUMPLE el principio
    ├── modelo/
    │   └── Pedido.java
    ├── abstracciones/          ← interfaces (el "contrato")
    │   ├── RepositorioPedidos.java
    │   └── Notificador.java
    ├── servicio/
    │   └── ServicioPedidos.java   ← alto nivel: solo conoce interfaces
    ├── infraestructura/        ← detalles de bajo nivel
    │   ├── RepositorioMySQL.java
    │   ├── RepositorioEnMemoria.java
    │   ├── NotificadorCorreo.java
    │   └── NotificadorWhatsApp.java
    ├── pruebas/
    │   └── PruebaServicioPedidos.java  ← prueba sin BD real
    └── Main.java               ← aquí se "arman" las piezas
```

El contexto es una **ferretería** (igual que nuestro Proyecto Integrador): cuando llega un pedido, hay que **guardarlo** y **avisarle al cliente**.

---

## 3. El problema: código SIN inversión de dependencias

```java
public class ServicioPedidos {

    private final BaseDatosMySQL baseDatos = new BaseDatosMySQL();
    private final CorreoGmail correo = new CorreoGmail();

    public void procesarPedido(String cliente, String producto, int cantidad) {
        baseDatos.insertarPedido(cliente, producto, cantidad);
        correo.enviarCorreo(cliente, "Tu pedido fue registrado");
    }
}
```

**¿Qué tiene de malo si funciona?**

1. **Acoplamiento fuerte:** `ServicioPedidos` conoce y crea (`new`) las clases concretas `BaseDatosMySQL` y `CorreoGmail`.
2. **Difícil de cambiar:** si la ferretería quiere avisar por **WhatsApp** o pasar a **PostgreSQL**, hay que abrir y modificar la clase de negocio (también rompe el principio Abierto/Cerrado).
3. **Difícil de probar:** para probar la regla "no se aceptan pedidos con cantidad 0" necesitamos una base de datos y un correo reales.
4. **Efecto dominó:** un cambio en el detalle técnico (por ejemplo, el nombre de un método de `BaseDatosMySQL`) obliga a cambiar la lógica de negocio.

---

## 4. La solución: aplicar DIP en 3 pasos

### Paso 1 — Crear abstracciones (interfaces)

```java
public interface RepositorioPedidos {
    void guardar(Pedido pedido);
}

public interface Notificador {
    void notificar(String destinatario, String mensaje);
}
```

Las interfaces dicen **qué** necesita el negocio, sin decir **cómo** se hace.

### Paso 2 — El alto nivel depende de las interfaces y las recibe por constructor

```java
public class ServicioPedidos {

    private final RepositorioPedidos repositorio;
    private final Notificador notificador;

    public ServicioPedidos(RepositorioPedidos repositorio, Notificador notificador) {
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public void procesarPedido(Pedido pedido) {
        if (pedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        repositorio.guardar(pedido);
        notificador.notificar(pedido.getCliente(), "Tu pedido fue registrado");
    }
}
```

Ya no hay ningún `new MySQL...` ni `import ...MySQL` dentro de la clase de negocio.

### Paso 3 — Los detalles implementan las interfaces

```java
public class RepositorioMySQL implements RepositorioPedidos { ... }
public class RepositorioEnMemoria implements RepositorioPedidos { ... }
public class NotificadorCorreo implements Notificador { ... }
public class NotificadorWhatsApp implements Notificador { ... }
```

Y en `Main` decidimos qué piezas usar:

```java
ServicioPedidos s1 = new ServicioPedidos(new RepositorioMySQL(), new NotificadorCorreo());
ServicioPedidos s2 = new ServicioPedidos(new RepositorioEnMemoria(), new NotificadorWhatsApp());
```

**Mismo `ServicioPedidos`, distinto comportamiento, sin tocar su código.**

---

## 5. DIP vs. Inyección de Dependencias (no son lo mismo)

| | Inversión de Dependencias (DIP) | Inyección de Dependencias (DI) |
|---|---|---|
| ¿Qué es? | Un **principio** de diseño | Una **técnica** para aplicarlo |
| Dice | "Depende de abstracciones, no de clases concretas" | "Pásale las dependencias desde afuera" |
| Ejemplo | `ServicioPedidos` usa `RepositorioPedidos` (interfaz) | Recibirlo por el **constructor** |

Formas de inyectar: por **constructor** (la recomendada, la usamos aquí), por **setter** o por **método**. Frameworks como **Spring** automatizan esta inyección.

---

## 6. Ventajas y cuándo NO exagerar

**Ventajas**
- ✅ Bajo acoplamiento: el negocio no sabe qué BD o canal se usa.
- ✅ Fácil de extender: agregar `NotificadorSMS` no modifica nada existente.
- ✅ Fácil de probar: usamos implementaciones falsas (ver `PruebaServicioPedidos`).
- ✅ Trabajo en equipo: una persona hace el servicio y otra la BD, acordando solo la interfaz.

**Cuidado**
- ⚠️ No hace falta una interfaz para *todo*: clases estables como `String`, `ArrayList` o un `Pedido` simple se pueden usar directamente.
- ⚠️ Demasiadas interfaces con una sola implementación que nunca cambiará solo agregan complejidad.

---

## 7. Cómo ejecutar el proyecto

Requisito: **JDK 8 o superior**. Desde la carpeta raíz del repositorio (Git Bash, CMD o terminal):

```bash
# Compilar todo
javac -d out $(find src -name "*.java")

# 1) Ejemplo SIN DIP
java -cp out sindip.Main

# 2) Ejemplo CON DIP (tres configuraciones distintas)
java -cp out condip.Main

# 3) Prueba con implementaciones falsas (sin base de datos)
java -cp out condip.pruebas.PruebaServicioPedidos
```

> En **CMD de Windows** (sin `find` de Linux), compile con:
> `dir /s /b src\*.java > fuentes.txt` y luego `javac -d out @fuentes.txt`

**En NetBeans:** *File → New Project → Java with Ant → Java Project with Existing Sources*, seleccione la carpeta `src` y ejecute la clase `Main` que quiera (clic derecho → *Run File*).

### Salida esperada (`condip.Main`)

```
=== EJEMPLO CON INVERSION DE DEPENDENCIAS ===

-- Configuracion 1: MySQL + Correo --
[MySQL] INSERT INTO pedidos VALUES ('cliente@correo.com', 'Martillo', 2)
[Correo] Para: cliente@correo.com -> Tu pedido de 2 x Martillo fue registrado

-- Configuracion 2: Memoria + WhatsApp --
[Memoria] Pedido guardado en lista: 1 x Taladro (+57 300 000 0000)
[WhatsApp] Para: +57 300 000 0000 -> Tu pedido de 1 x Taladro fue registrado

-- Configuracion 3: MySQL + aviso por consola (lambda) --
[MySQL] INSERT INTO pedidos VALUES ('Bodega', 'Tornillos', 100)
[Consola] Bodega: Tu pedido de 100 x Tornillos fue registrado
```

### Salida esperada (pruebas)

```
[Memoria] Pedido guardado en lista: 3 x Pala (ana@correo.com)
[OK]    Se guardo 1 pedido
[OK]    Se envio 1 aviso
[OK]    Cantidad 0 lanza error
[OK]    El pedido invalido no se guardo

TODAS LAS PRUEBAS PASARON
```

---

## 8. Ejercicio propuesto

Agregue un `NotificadorSMS` que implemente `Notificador` y úselo en `Main`.
**Pregunta:** ¿cuántas líneas tuvo que cambiar en `ServicioPedidos`? (Respuesta: **cero**. Eso es DIP.)

---

## 9. Referencias

- Martin, R. C. (2003). *Agile Software Development: Principles, Patterns, and Practices*. Prentice Hall.
- Martin, R. C. (2017). *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Prentice Hall.
- Fowler, M. (2004). *Inversion of Control Containers and the Dependency Injection pattern*. https://martinfowler.com/articles/injection.html
