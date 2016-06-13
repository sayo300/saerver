package zad1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ClientChat extends JFrame {
	private SocketChannel sc;
	private JTextArea okienko, rozmowa;
	private static Charset charset = Charset.forName("ISO-8859-2");
	private Thread thread;
	private boolean dzialaj = true;

	public ClientChat(String string) {
		// TODO Auto-generated constructor stub
		this.setTitle("Mateo1");

		this.setSize(400, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		// this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		rozmowa = new JTextArea(5, 5);
		panel.add(rozmowa, BorderLayout.CENTER);
		okienko = new JTextArea();
		okienko.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub

				if (!okienko.getText().equals("")) {
					wyslijWiadomosc(okienko.getText().toString());
				}
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				// TODO Auto-generated method stub

			}
		});
		panel.add(okienko, BorderLayout.SOUTH);
		JButton button = new JButton("Wyloguj: " + string);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				wyslijWiadomosc("bye");
				dzialaj = false;
				closeForm();

			}
		});
		panel.add(button, BorderLayout.NORTH);

		this.add(panel);
		// this.pack();
		this.setVisible(true);
		thread = new Thread() {
			public void run() {
				dzialaj(string);
			}
		};
		thread.start();
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				wyslijWiadomosc("bye");
				closeForm();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				wyslijWiadomosc("bye");
				closeForm();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void wyslijWiadomosc(String wiadomosc) {
		// TODO Auto-generated method stub
		String wyslij = wiadomosc + "\n";
		okienko.setText("");

		try {
			ByteBuffer byteBuffer = charset.encode(wyslij);
			while (byteBuffer.hasRemaining()) {
				sc.write(byteBuffer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void dzialaj(String nickname) {
		// TODO Auto-generated method stub

		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress("localhost", 1111));
			this.sc = socketChannel;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		while (dzialaj = true) {
			String result = readFromChannel();
			if (result.length() > 0) {

				rozmowa.append(result + "\n");
				rozmowa.setCaretPosition(rozmowa.getDocument().getLength());
				rozmowa.update(rozmowa.getGraphics());

			}
		}

	}

	protected void closeForm() {

		super.dispose();
		System.exit(0);
	}

	private String readFromChannel() {
		if (!sc.isOpen()) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		result.setLength(0);
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.clear();
		try {
			int licznik = 0;
			Loop: while (true) {
				int n = sc.read(byteBuffer);
				if (n > 0) {
					byteBuffer.flip();
					CharBuffer charBuffer = charset.decode(byteBuffer);
					while (charBuffer.hasRemaining()) {
						char c = charBuffer.get();
						if (c == '\r' || c == '\n')
							break Loop;
						result.append(c);
					}
				} else {
					licznik++;
					if (licznik > 1000) {
						break Loop;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result.toString();

	}
}
