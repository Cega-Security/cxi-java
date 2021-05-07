# Cxi Java
Este proyecto es un ejemplo para implementar la librería de Cxi proporcionada por Utimaco, para ser utilizada dentro de un proyecto Java para conexión a HSMs Utimaco

## Requisitos
- Java versión 1.8 en adelante 
- Reemplazar las políticas de seguridad criptográfica por su versión sin límites en la instalación de Java
- Utilizar la librería adecuada para 32 o 64 bits

En caso de alguna duda con los requisitos previos contactar a personal de Cega Security

## Librerías
1. **CryptoServerCXI.jar** - Librería en Java, que contiene las funciones y las referencias para la conexión al HSM

## Uso básico
1. Hacer referencia a la librería **CryptoServerCXI.jar** en la configuración del proyecto
2. Importar las clases **CryptoServerCXI** y **CryptoServerAPI** 

### Conexión
Se crea una instancia del objeto `CryptoServerCXI` con la dirección IP del HSM y se inicia sesión con un usuario criptográfico

``` java
CryptoServerCXI cxi = new CryptoServerCXI("192.168.1.100");
cxi.logonPassword("USUARIO", "CONTRASENA");
```

### Buscar Llave
Se crea un objeto `KeyAttributes` con el nombre y el grupo de la llave a utilizar

``` java
CryptoServerCXI.KeyAttributes attr = new CryptoServerCXI.KeyAttributes();
attr.setGroup("NOMBRE_GRUPO");
attr.setName("NOMBRE_LLAVE");
CryptoServerCXI.Key llave = cxi.findKey(0, attr);
```

### Encriptación y Desencriptación
Para encriptar se crea el mecanismo con los parámetros de modo encrypt y el padding deseado. Se obtienen los bytes a encriptar y se ejecuta la función `cxi.crypt` con la llave correspondiente

``` java
int mech = CryptoServerCXI.MECH_MODE_ENCRYPT | CryptoServerCXI.MECH_CHAIN_CBC | CryptoServerCXI.MECH_PAD_PKCS5;
String texto = "Cega Security";
byte[] datos = texto.getBytes();
byte[] crypt = cxi.crypt(0, llave, mech, null, datos, null, null, null);
```

Para desencriptar se crea el mecanismo con los parámetros de modo decrypt y el padding deseado. Se ejecuta la función `cxi.crypt` con la llave correspondiente

``` java
int mechDecrypt = CryptoServerCXI.MECH_MODE_DECRYPT | CryptoServerCXI.MECH_CHAIN_CBC | CryptoServerCXI.MECH_PAD_PKCS5;
byte[] decrypt = cxi.crypt(0, llave, mechDecrypt, null, crypt, null, null, null);
```

### Firma y verificación
Se crea el hash de los datos a firmar con la clase `MessageDigest` y el tipo de hash a utilizar

``` java
String datos = "Cega Security";
MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(datos.getBytes());
byte [] hash = md.digest();
```

Se crea el mecanismo con el hash y el padding correspondiente y se ejecuta la firma con la función `cxi.sign` con la llave correspondiente

``` java
int mech = CryptoServerCXI.MECH_HASH_ALGO_SHA256 | CryptoServerCXI.MECH_PAD_PKCS1;
byte [] sign = cxi.sign(llave, mech, hash);
```

Posteriormente se puede hacer la verificación de la firma con la función `cxi.verify`

``` java
boolean verify = cxi.verify(llave, mech, hash, sign);
```

### Cierre
Es necesario cerrar la conexión al HSM para evitar problemas con el número de conexiones

``` java
cxi.close();
```

## Documentación 
La documentación detallada se encuentra en la ruta `Documentation\Crypto_APIs\CXI_Java` de la instalación de las herramientas proporcionadas por Utimaco para conexión al HSM

## Errores comunes
Si se presenta algún problema se puede poner en contacto con personal de Cega Security. Por lo pronto se enlistan las soluciones a problemas comunes:
1. Validar que la versión de la instalación de Java es la misma que el JAR (32 o 64 bits)
2. Validar que se ha reemplazado las políticas de seguridad en la instalación de Java, por las que no tienen límites
3. Verificar la referencia a la librería CryptoServerCXI.jar dentro del proyecto