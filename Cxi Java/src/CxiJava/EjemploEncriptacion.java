package CxiJava;

import java.io.IOException;

import CryptoServerAPI.CryptoServerException;
import CryptoServerAPI.CryptoServerUtil;
import CryptoServerCXI.*;

public class EjemploEncriptacion {

	public static final String HSM = "3001@127.0.0.1"; // puerto y direccion IP del HSM
	public static final String USUARIO = "user"; // usuario criptografico
	public static final String CONTRASENA = "pass"; // contraseña del usuario criptografico
	public static final String NOMBRE_LLAVE = "key_AES"; // nombre de la llave a utilizar
	public static final String NOMBRE_GRUPO = "group"; // grupo de la llave
	
	public static void main(String[] args) {
		
		try {
			// Conectarse al HSM
			CryptoServerCXI cxi = new CryptoServerCXI(HSM);
			System.out.println("Conectado a HSM " + HSM);
			
			// Iniciar sesion
			cxi.logonPassword(USUARIO, CONTRASENA);
			System.out.println("Usuario inicio sesion " + USUARIO);
			
			// Crear objeto Key Attributes para buscar la llave, si no tiene specifier por defecto es -1
			CryptoServerCXI.KeyAttributes attr = new CryptoServerCXI.KeyAttributes();
			attr.setGroup(NOMBRE_GRUPO);
			attr.setName(NOMBRE_LLAVE);
			CryptoServerCXI.Key llave = cxi.findKey(0, attr);
			System.out.println("Llave encontrada " + NOMBRE_LLAVE);
			
			//Generacion del mecanismo para encriptar y la encriptacion con llave AES
			int mech = CryptoServerCXI.MECH_MODE_ENCRYPT | CryptoServerCXI.MECH_CHAIN_CBC | CryptoServerCXI.MECH_PAD_PKCS5;
			String texto = "Cega Security";
			byte[] datos = texto.getBytes();
			byte[] crypt = cxi.crypt(0, llave, mech, null, datos, null, null, null);
			CryptoServerUtil.xtrace("Datos cifrados ", crypt);
			
			//Generacion del mecanismo para desencriptar y la desencriptacion con llave AES
			int mechDecrypt = CryptoServerCXI.MECH_MODE_DECRYPT | CryptoServerCXI.MECH_CHAIN_CBC | CryptoServerCXI.MECH_PAD_PKCS5;
			byte[] decrypt = cxi.crypt(0, llave, mechDecrypt, null, crypt, null, null, null);
			System.out.println("Datos descifrados: " + new String(decrypt));
			
			cxi.close();
			
		} catch (NumberFormatException | IOException | CryptoServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
