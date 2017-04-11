package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import frames.LoginScreenGUI;
import utilities.MySQLDriver;
import utilities.MySQLSettings;

//@SuppressWarnings("unused")
public class GameClientListener extends Thread {

	private LoginScreenGUI mLoginScreenGUI;

	private MySQLDriver mySQLDriver;
	private Socket mClientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private PrintWriter pw;
	private BufferedReader br;

	public GameClientListener(LoginScreenGUI inLoginScreenGUI, Socket inClientSocket) {
		this.mLoginScreenGUI = inLoginScreenGUI;
		this.mClientSocket = inClientSocket;
		boolean socketReady = this.initializeVariables();
		if (socketReady) {
			this.start();
		}
	}

	private boolean initializeVariables() {
		try {
			this.mySQLDriver = null;
			this.oos = new ObjectOutputStream(this.mClientSocket.getOutputStream());
			this.ois = new ObjectInputStream(this.mClientSocket.getInputStream());
			this.pw = new PrintWriter(new OutputStreamWriter(this.mClientSocket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(this.mClientSocket.getInputStream()));
			System.out.println("Success: GameClientListener created for Client " + this.mClientSocket.getInetAddress()
					+ ":" + this.mClientSocket.getPort());
		} catch (IOException ioe) {
			System.out.println("Fail to create GameClientListener for Client " + this.mClientSocket.getInetAddress()
					+ ":" + this.mClientSocket.getPort());
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
			return false;
		}
		System.out.println("Success: Socket ready for Client " + this.mClientSocket.getInetAddress() + ":"
				+ this.mClientSocket.getPort());
		return true;
	}

	@Override
	public void run() {

		try {
			MySQLSettings mySQLSettings;
			while (true) {
				Object obj = this.ois.readObject();
				// in case the server sends another MySQLDriver to us
				if (obj instanceof MySQLSettings) {
					mySQLSettings = (MySQLSettings) obj;
					System.out.println("Success: MySQLSettings Recieved by Client "
							+ this.mClientSocket.getInetAddress() + ":" + this.mClientSocket.getPort());
					this.mySQLDriver = new MySQLDriver();
					this.mySQLDriver.connect(mySQLSettings);
					this.mLoginScreenGUI.setMySQLDriver(this.mySQLDriver);
				}

				// TODO add more (if instance of)
			}
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
			cnfe.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		this.pw.println(msg);
		this.pw.flush();
	}

	public void sendObejct(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

}

/*
 *
 * Listens to the GameServer that the client is connected to, waiting for
 * updates. GameClientListener : Thread - Socket : mSocket - ObjectInputStream :
 * ois - PrintWriter : pw - LoginMenuGUI : login (+) GameClientListener(Socket)
 * (+) sendMessage(String msg) : void (+) run() : void - initializeVariables() :
 * void
 *
 *
 */