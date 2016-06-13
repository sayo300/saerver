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
import javax.swing.JTextField;

public class Zaloguj extends JFrame {

	public Zaloguj() {
		// TODO Auto-generated constructor stub
		this.setTitle("Mateo1");
		this.setVisible(true);
		this.setSize(400, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		JPanel panel = new JPanel(new GridLayout(3, 1));
		this.add(panel);
		JLabel label = new JLabel("Podaj imie");
		panel.add(label);

		JTextField field = new JTextField();
		panel.add(field);
		JButton button = new JButton("Zatwierdz");
		panel.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if (field.getText().toString().equals("")) {

					new Message("Nie poda³eœ Imienia");

				} else {

					closeForm();
					new ClientChat(field.getText().toString());
				}
			}
		});
		this.pack();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	protected void closeForm() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}
