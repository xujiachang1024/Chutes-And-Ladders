package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import utilities.MySQLDriver;

public class ServerListener extends Thread {

	private MySQLDriver mySQLDriver;
	private ServerSocket mServerSocket;
	private Vector<ServerClientCommunicator> sccVector;

	public ServerListener(MySQLDriver inMySQLDriver, ServerSocket inServerSocket) {
		this.mySQLDriver = inMySQLDriver;
		this.mServerSocket = inServerSocket;
		this.sccVector = new Vector<>();
	}

	public void removeSCC(ServerClientCommunicator scc) {
		this.sccVector.remove(scc);
	}

	@Override
	public void run() {

		try {
			while (true) {
				Socket clientSocket = this.mServerSocket.accept();
				System.out.println("Success: New Client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
						+ " Accepted");

				try {
					ServerClientCommunicator scc = new ServerClientCommunicator(clientSocket, this);
					scc.start();
					this.sccVector.add(scc);
					System.out.println("Success: ServerClientCommunicator  " + clientSocket.getInetAddress() + ":"
							+ clientSocket.getPort() + " Started");

					if (this.mySQLDriver.getMySQLSettings() != null) {
						scc.sendMySQLSettings(this.mySQLDriver.getMySQLSettings());
					}
				} catch (IOException ioe) {
					System.out.println("Fail to Establish ServerClientCommunicator " + clientSocket.getInetAddress()
							+ ":" + clientSocket.getPort());
					System.out.println("IOException: " + ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		} catch (BindException be) {
			System.out.println("Fail to accept a new Client");
			System.out.println("BindException: " + be.getMessage());
			be.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Fail to accept a new Client");
			System.out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			if (this.mServerSocket != null) {
				try {
					this.mServerSocket.close();
					System.out.println("Success: ServerSocket Closed");
				} catch (IOException ioe) {
					System.out.println("Fail to Close ServerSocket");
					System.out.println("IOException: " + ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		}
	}

	public void sendMySQLSettings() {
		if (this.mySQLDriver.getMySQLSettings() != null) {
			for (ServerClientCommunicator scc : this.sccVector) {
				scc.sendMySQLSettings(this.mySQLDriver.getMySQLSettings());
			}
		}
	}

}
