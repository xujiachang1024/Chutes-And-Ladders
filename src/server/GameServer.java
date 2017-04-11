package server;

import java.net.ServerSocket;

import utilities.MySQLDriver;

public class GameServer {

	private static ServerListener mServerListener;

	public static void main(String[] args) {
		new GameServer();
	}

	public static void sendMySQLSettings() {
		if (mServerListener != null) {
			mServerListener.sendMySQLSettings();
		}
	}

	private MySQLDriver mySQLDriver;

	private ServerSocket mServerSocket;

	public GameServer() {
		GameServerGUI gameServerGUI = new GameServerGUI();

		this.mySQLDriver = gameServerGUI.getMySQLDriver();
		System.out.println("Success: MySQLDriver Connected in GameServer");

		this.mServerSocket = gameServerGUI.getServerSocket();
		System.out.println("Success: ServerSocket Created in GameServer");

		this.listenForConnections();
		System.out.println("Success: GameServer Started to Listen for Connection");
	}

	private void listenForConnections() {
		mServerListener = new ServerListener(this.mySQLDriver, this.mServerSocket);
		mServerListener.start();
	}

}
