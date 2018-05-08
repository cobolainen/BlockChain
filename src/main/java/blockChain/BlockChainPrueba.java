package blockChain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.security.Security;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.List;
//import com.google.gson.GsonBuilder;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BlockChainPrueba {

	public static ArrayList<Bloque> blockchain = new ArrayList<Bloque>();
	public static HashMap<String, OutputTransaccion> UTXOs = new HashMap<String, OutputTransaccion>();

	public static int dificultad = 3;
	public static float transaccionMinima = 0.1f;
	public static Monedero monederoA;
	public static Monedero monederoB;
	public static Transaccion primeraTransaccion;

	public static void main(String[] args) throws ClassNotFoundException {
		// se añaden los bloques al arraylist blockchain:
		Security.addProvider(new BouncyCastleProvider()); // Proveedor de seguridad

		// Create wallets:
		monederoA = new Monedero();
		monederoB = new Monedero();
		Monedero coinbase = new Monedero();

		// crea una primera transaccion,que envia 100 monedas al monederoA :
		primeraTransaccion = new Transaccion(coinbase.clavePublica, monederoA.clavePublica, 100f, null);
		primeraTransaccion.generarFirma(coinbase.clavePrivada); // se firma la transaccion
		primeraTransaccion.id = "0"; // de le da el id 0
		primeraTransaccion.outputs.add(
				new OutputTransaccion(primeraTransaccion.receptor, primeraTransaccion.valor, primeraTransaccion.id)); // manually
																														// add
																														// the
																														// Transactions
																														// Output
		UTXOs.put(primeraTransaccion.outputs.get(0).id, primeraTransaccion.outputs.get(0));

		System.out.println("Creando y minando el primer bloque... ");
		Bloque genesis = new Bloque("0");
		genesis.anadirTransaccion(primeraTransaccion);
		anadirBloque(genesis);

		Bloque bloque1 = new Bloque(genesis.hash);
		System.out.println("\nEl balance del monedero A es: " + monederoA.getBalance());
		System.out.println("\nEl monedero A esta intentando enviar fondos (40) al monedero B...");
		bloque1.anadirTransaccion(monederoA.enviarFondos(monederoB.clavePublica, 40f));
		anadirBloque(bloque1);
		System.out.println("\nEl balance del monedero A es: " + monederoA.getBalance());
		System.out.println("El balance del monedero B es: " + monederoB.getBalance());

		Bloque bloque2 = new Bloque(bloque1.hash);
		System.out.println("\nMonedero A intentando enviar fondos al monedero B (1000)...");
		bloque2.anadirTransaccion(monederoA.enviarFondos(monederoB.clavePublica, 1000f));
		anadirBloque(bloque2);
		System.out.println("\nEl balance del monedero A es: " + monederoA.getBalance());
		System.out.println("El balance del monedero B es: " + monederoB.getBalance());

		Bloque bloque3 = new Bloque(bloque2.hash);
		System.out.println("\nEl monedero B esta intentando enviar fondos al monedero A (20)...");
		bloque3.anadirTransaccion(monederoB.enviarFondos(monederoA.clavePublica, 20));
		System.out.println("\nEl balance del monedero A es: " + monederoA.getBalance());
		System.out.println("El balance del monedero B es: " + monederoB.getBalance());
		anadirBloque(bloque3);
		cadenaEsValida();
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setFileFilter(new FileNameExtensionFilter(".wallet", "wallet"));
		int returnValue = jfc.showSaveDialog(null);

		// int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {

			File selectedFile = jfc.getSelectedFile();
			try {
				ObjectInputStream oos = new ObjectInputStream(new FileInputStream(selectedFile));
				blockchain = (ArrayList<Bloque>) oos.readObject();
				System.out.println(StringUtil.getJson(blockchain));

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static Boolean cadenaEsValida() {
		Bloque bloqueActual;
		Bloque bloqueAnterior;
		String hashObjetivo = new String(new char[dificultad]).replace('\0', '0');
		HashMap<String, OutputTransaccion> tempUTXOs = new HashMap<String, OutputTransaccion>(); // una lista temporal
																									// de transacciones
																									// sin gastar en un
																									// bloque
		tempUTXOs.put(primeraTransaccion.outputs.get(0).id, primeraTransaccion.outputs.get(0));

		// recorre el blockchain para comprobar los hashes:
		for (int i = 1; i < blockchain.size(); i++) {

			bloqueActual = (Bloque) blockchain.get(i);
			bloqueAnterior = blockchain.get(i - 1);
			// compara el hash registrado y el calculado:
			if (!bloqueActual.hash.equals(bloqueActual.calcularHash())) {
				System.out.println("Los hashes actuales no coinciden");
				return false;
			}
			// compara el hash previo y el hash previo registrado
			if (!bloqueAnterior.hash.equals(bloqueActual.hashPrevio)) {
				System.out.println("Los hash previos no coinciden");
				return false;
			}
			// comprueba si se ha solucionado el hash
			if (!bloqueActual.hash.substring(0, dificultad).equals(hashObjetivo)) {
				System.out.println("El bloque no ha sido minado");
				return false;
			}

			// se recorren las transacciones del blockchain:
			OutputTransaccion tempOutput;
			for (int t = 0; t < bloqueActual.transacciones.size(); t++) {
				Transaccion transaccionActual = bloqueActual.transacciones.get(t);

				if (!transaccionActual.comprobarFirma()) {
					System.out.println("La firma de la transaccion(" + t + ") es valida");
					return false;
				}
				if (transaccionActual.getValorInput() != transaccionActual.getValorOutput()) {
					System.out.println("Los inputs no son iguales a los outputs de la transaccion(" + t + ")");
					return false;
				}

				for (InputTransaccion input : transaccionActual.inputs) {
					tempOutput = tempUTXOs.get(input.idOutput);

					if (tempOutput == null) {
						System.out.println("Este input no esta en la transaccion(" + t + ")");
						return false;
					}

					if (input.UTXO.valor != tempOutput.valor) {
						System.out.println("El valor del input(" + t + ") no es valido");
						return false;
					}

					tempUTXOs.remove(input.idOutput);
				}

				for (OutputTransaccion output : transaccionActual.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (transaccionActual.outputs.get(0).receptor != transaccionActual.receptor) {
					System.out.println("El receptor de la transaccion(" + t + ") no es el que deberia ser");
					return false;
				}
				if (transaccionActual.outputs.get(1).receptor != transaccionActual.emisor) {
					System.out.println(
							"El receptor no ha sido quien ha cambiado el output de la transaccion (" + t + ").");
					return false;
				}

			}

		}
		System.out.println("Blockchain valida");
		return true;
	}

	public static void anadirBloque(Bloque nuevoBloque) {
		nuevoBloque.minarBloque(dificultad);
		blockchain.add(nuevoBloque);
	}
}
