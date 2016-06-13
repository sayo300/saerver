package zad1;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Message extends JFrame {

	public Message(String string) {
		// TODO Auto-generated constructor stub
		this.setTitle("Mateo1");
		this.setVisible(true);
		this.setSize(400, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		JPanel panel = new JPanel(new GridLayout(2, 1));
		this.add(panel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel label = new JLabel(string);
		panel.add(label);
		JButton button = new JButton("ok");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				closeForm();
			}
		});
		panel.add(button);
		this.pack();
	}

	protected void closeForm() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}
