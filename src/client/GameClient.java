package client;

import frames.LoginScreenGUI;

public class GameClient {

	public static void main(String[] args) {
		new GameClient();
	}

	private LoginScreenGUI mLoginScreenGUI;

	public GameClient() {
		this.mLoginScreenGUI = new LoginScreenGUI();
	}

	public LoginScreenGUI getLoginScreenGUI() {
		return this.mLoginScreenGUI;
	}
}