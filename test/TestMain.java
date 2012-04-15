package test;

import game.Main;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.Server;

public class TestMain {
	public static void main(String[] args) throws IOException {
		Main.registerHandlers();
		ExecutorService exs = Executors.newCachedThreadPool();
		exs.execute(new Server(1337));
	}
}