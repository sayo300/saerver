/**
 *
 *  @author Bień Mateusz S12504
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

public class Server {
	private static Charset charset = Charset.forName("ISO-8859-2");
	private Selector selector;
 private ServerSocketChannel socketChannel;
 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
// Calendar cal = Calendar.getInstance();
	public Server() throws IOException {
		try{
		
		socketChannel = ServerSocketChannel.open();
		socketChannel.configureBlocking(false);
		InetSocketAddress Address = new InetSocketAddress("localhost", 1111);
		
		socketChannel.bind(Address);
		
		selector = Selector.open();

		SelectionKey key = socketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}catch (Exception e){}
		while (true) {
			try{
			selector.select();

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey myKey = (SelectionKey) iterator.next();

				if (myKey.isAcceptable()) {
					SocketChannel Client = socketChannel.accept();
				if(Client != null){
					Client.configureBlocking(false);
					Client.register(selector, SelectionKey.OP_READ
							| SelectionKey.OP_WRITE);
				}
				continue;
				} else if (myKey.isReadable()) {
					SocketChannel client = (SocketChannel) myKey.channel();
					
					String result = readFromChannel(client);
					if(result.length() > 0){
					wyslijdowszystkich(dateFormat.format(Calendar.getInstance().getTime())+" : " + result);
					}
					if (result.equals("bye")) {
						client.close();
					}
					continue;
				}
				iterator.remove();
			}
			}catch (Exception exc) {
				exc.printStackTrace();
				continue;
			}
		}

	}

	private void wyslijdowszystkich(String msg) {
		// System.out.println("1");
		try {
			selector.select();
			Set myKey = selector.selectedKeys();
			Iterator iterator = myKey.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				if (key.isWritable()) {
					SocketChannel cc = (SocketChannel) key.channel();
					writeToChannel(msg, cc);
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void writeToChannel(String msg, SocketChannel sc) {
		// TODO Auto-generated method stub
		// System.out.println("2");
		msg += "\n";
		try {
			ByteBuffer byteBuffer = charset.encode(msg);
			while (byteBuffer.hasRemaining()) {
				sc.write(byteBuffer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public  String readFromChannel(SocketChannel sc) {
		if (!sc.isOpen()){
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
	}
}
