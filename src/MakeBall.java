public class MakeBall extends Thread {
	@Override
	public void run() {
		while (Main.isRunning) {
			Main.giveBirth(1, 1, Math.random() * 1000.0, Math.random() * 1000.0);
			try {
				sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
}
