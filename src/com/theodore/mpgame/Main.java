package com.theodore.mpgame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/*
 * Open-source java 3D network FPS game
 */

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel jp = new JPanel();

	private JButton host;
	private JButton client;
	private JTextField serverIp;
	private JComboBox jcb;

	private String[] res = { "800 x 600", "1280 x 720", "1280 x 1024",
			"1980 x 1080" };

	// Initializes Main class
	public static void main(String[] args) {
		new Main();
	}

	// Setting up JFrame & JPanel for a simple launcher
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setTitle("Open-source java 3D network FPS game");
		setResizable(false);
		setLocationRelativeTo(null);
		jp.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));

		add(jp);

		host = new JButton("Host");
		host.setPreferredSize(new Dimension(100, 50));
		client = new JButton("Connect");
		client.setPreferredSize(new Dimension(100, 50));
		serverIp = new JTextField();
		serverIp.setText("localhost");
		serverIp.setPreferredSize(new Dimension(125, 25));
		jcb = new JComboBox(res);
		jcb.setPreferredSize(new Dimension(300, 20));
		jcb.setSelectedIndex(1);

		jp.add(jcb);
		jp.add(host);
		jp.add(serverIp);
		jp.add(client);
		setVisible(true);

		host.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				// Send with username, chosen resolution & if you're hosting.
				int width = 800;
				int height = 600;
				if (jcb.getSelectedIndex() == 0) {
					width = 800;
					height = 600;
				} else if (jcb.getSelectedIndex() == 1) {
					width = 1280;
					height = 720;
				} else if (jcb.getSelectedIndex() == 2) {
					width = 1280;
					height = 1024;
				} else if (jcb.getSelectedIndex() == 3) {
					width = 1920;
					height = 1080;
				}

				setVisible(false);

				new GameWindow(width, height, true, JOptionPane
						.showInputDialog(this, "Please enter a username"),
						"localhost");
			}

		});

		client.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				// Same here but with hosting = false
				int width = 800;
				int height = 600;
				if (jcb.getSelectedIndex() == 0) {
					width = 800;
					height = 600;
				} else if (jcb.getSelectedIndex() == 1) {
					width = 1280;
					height = 720;
				} else if (jcb.getSelectedIndex() == 2) {
					width = 1280;
					height = 1024;
				} else if (jcb.getSelectedIndex() == 3) {
					width = 1920;
					height = 1080;
				}

				setVisible(false);

				new GameWindow(width, height, false, JOptionPane
						.showInputDialog("", "Please enter a username"),
						serverIp.getText().trim());
			}

		});
	}
}
