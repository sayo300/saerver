/**
 *
 *  @author Bień Mateusz S12504
 *
 */

package zad1;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		new Thread() {
			public void run() {
				try {
					new Server();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ClientChat("Janek");
		new ClientChat("Krzysiek");

	}
}
