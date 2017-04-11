package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import utilities.MySQLSettings;

public class ServerClientCommunicator extends Thread {

	private Socket mClientSocket;
	private ServerListener mServerListener;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private PrintWriter pw;
	private BufferedReader br;

	public ServerClientCommunicator(Socket inClientSocket, ServerListener inServerListener) throws IOException {
		this.mClientSocket = inClientSocket;
		this.mServerListener = inServerListener;
		this.oos = new ObjectOutputStream(this.mClientSocket.getOutputStream());
		this.ois = new ObjectInputStream(this.mClientSocket.getInputStream());
		this.pw = new PrintWriter(new OutputStreamWriter(this.mClientSocket.getOutputStream()));
		this.br = new BufferedReader(new InputStreamReader(this.mClientSocket.getInputStream()));
	}

	@Override
	public void run() {

		try {
			String line = this.br.readLine();
			while (line != null) {
				// TODO
				line = this.br.readLine();
			}
		} catch (IOException ioe) {
			this.mServerListener.removeSCC(this);
			System.out.println(
					"Disconnect: Client " + this.mClientSocket.getInetAddress() + ":" + this.mClientSocket.getPort());
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
			try {
				this.mClientSocket.close();
			} catch (IOException ioe1) {
				System.out.println("IOException: " + ioe1.getMessage());
				ioe1.printStackTrace();
			}
		}

	}

	public void sendMySQLSettings(MySQLSettings inMySQLSettings) {
		try {
			this.oos.writeObject(inMySQLSettings);
			this.oos.flush();
			System.out.println("Success: MySQLSettings Sent to Client " + this.mClientSocket.getInetAddress() + ":"
					+ this.mClientSocket.getPort());
		} catch (IOException ioe) {
			System.out.println("Fail to send MySQLSettings to Client " + this.mClientSocket.getInetAddress() + ":"
					+ this.mClientSocket.getPort());
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

}
