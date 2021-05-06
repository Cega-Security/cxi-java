package CxiJava;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import CryptoServerAPI.*;
import CryptoServerCXI.*;

public class EjemploFirma {

	public static final String HSM = "3001@127.0.0.1"; // puerto y direccion IP del HSM
	public static final String USUARIO = "user"; // usuario criptografico
	public static final String CONTRASENA = "pass"; // contraseña del usuario criptografico
	public static final String NOMBRE_LLAVE = "key_RSA"; // nombre de la llave a utilizar
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
			
			//Creacion del HASH para la firma con SHA 256
			String datos = "Cega Security";
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(datos.getBytes());
			byte [] hash = md.digest();
			
			//Generacion del mecanismo para firmar y la firma con llave RSA
			int mech = CryptoServerCXI.MECH_HASH_ALGO_SHA256 | CryptoServerCXI.MECH_PAD_PKCS1;
			byte [] sign = cxi.sign(llave, mech, hash);
			CryptoServerUtil.xtrace("Datos firmados ", sign);
			
			//Verificacion de la firma
			boolean verify = cxi.verify(llave, mech, hash, sign);
			System.out.println("La verificacion es " + verify);
			
			cxi.close();

		} catch (NumberFormatException | IOException | CryptoServerException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
