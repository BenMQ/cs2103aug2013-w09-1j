package sg.edu.nus.cs2103.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.UIConstants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Liu Dake
 */

import javax.swing.text.DefaultCaret;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.(NetBeans)
 */

/**
 * This is the GUI class.
 * 
 * @author Liu Dake
 */

public class NewGUI extends javax.swing.JFrame implements NativeKeyListener {

	/**
	 * Creates new form GUI
	 */
	public NewGUI() {
		manager = TaskManager.getTaskManager();
		logicHandler = LogicHandler.getLogicHandler(manager, null);
		initComponents();
		this.setVisible(true);
	}

	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// .TODO Auto-generated method stub
		// To implement unimplemented method
	}

	/**
	 * . Implemented methods for NativeHook. nativeKeyPressed: detect which key
	 * is pressed. Here when Space and Tab are detected the visibility of the
	 * window will be changed. nativeKeyReleased: detect which key is released,
	 * then set the boolean values for the key to false.
	 * 
	 */
	public void nativeKeyPressed(NativeKeyEvent e) {
		currentKey = e.getKeyCode();
		if (currentKey == UIConstants.KEYBOARD_TAB) {
			keyOne = true;
		}
		if (currentKey == UIConstants.KEYBOARD_SPACE) {
			keyTwo = true;
		}
		if (keyOne && keyTwo) {
			if (isShown) {
				isShown = false;
				toInvisible();
			} else {
				isShown = true;
				toVisible();
			}
		}
		if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
			GlobalScreen.unregisterNativeHook();
			System.exit(0);
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		currentKey = e.getKeyCode();
		if (currentKey == UIConstants.KEYBOARD_TAB) {
			keyOne = false;
		}
		if (currentKey == UIConstants.KEYBOARD_SPACE) {
			keyTwo = false;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		// Capture all system output
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Initialization of all the components for NewGUI
		foundationPanel = new javax.swing.JPanel();
		mainTextFrameScrollPane = new javax.swing.JScrollPane();
		mainTextFrame = new javax.swing.JTextArea();
		inputText = new javax.swing.JTextField();
		inputText.setForeground(new java.awt.Color(40, 40, 205));
		sudooleButton = new javax.swing.JButton();
		floatingTaskScrollPane = new javax.swing.JScrollPane();
		floatingTextFrame = new javax.swing.JTextArea();
		floatingTextFrame.setFont(new java.awt.Font("Monaco", 0, 12));
		DefaultCaret caret = (DefaultCaret) floatingTextFrame.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// floatingList = new javax.swing.JList();
		// floatingList.setForeground(new java.awt.Color(0, 100, 245));
		// floatingList.setBackground(new java.awt.Color(150, 245, 100));
		labelFloatingTasks = new javax.swing.JLabel();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Initialization of properties
		mainTextFrame.setEditable(false);
		mainTextFrame.setFont(new java.awt.Font("Monaco", 0, 17));
		mainTextFrame.setForeground(new java.awt.Color(56, 220, 40));
		mainTextFrame.setBackground(new java.awt.Color(0, 0, 0));
		mainTextFrame.setColumns(20);
		mainTextFrame.setRows(5);
		mainTextFrameScrollPane.setViewportView(mainTextFrame);
		if (manager.isReloaded()) {
			mainTextFrame.setText(UIConstants.MESSAGE_WELCOME_TO_SUDO_RELOAD);
		} else {
			mainTextFrame.setText(UIConstants.MESSAGE_WELCOME_TO_SUDO_FIRST);
		}
		try {
			floatingTextFrame.setText(manager.allFloatingTasks());
		} catch (IllegalStateException e) {
			floatingTextFrame.setText(UIConstants.MESSAGE_EMPTY_LIST);
		}
		inputText.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				inputTextActionPerformed(evt);
			}
		});
		inputText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (inputText.getText().equals("demo")) {
						if (isDemo) {
							mainTextFrame.setFont(new java.awt.Font("Monaco",
									0, 17));
							floatingTextFrame.setFont(new java.awt.Font(
									"Monaco", 0, 12));
							inputText.setText("");
							isDemo = false;
						} else {
							mainTextFrame.setFont(new java.awt.Font("Monaco",
									0, 25));
							floatingTextFrame.setFont(new java.awt.Font(
									"Monaco", 0, 16));
							// mainTextFrame.setText("Font:Demo mode on");
							inputText.setText("");
							isDemo = true;
						}
					} else {
						String userInput = inputText.getText();
						logicHandler.executeCommand(userInput);
						inputText.setText("");
						mainTextFrame.setText(outContent.toString());
						outContent.reset();
						// String[] floatings =
						// updateFloating(manager.getFloatingTask());
						try {
							floatingTextFrame.setText(manager
									.allFloatingTasks());
						} catch (IllegalStateException w) {
							floatingTextFrame
									.setText(UIConstants.MESSAGE_EMPTY_LIST);
						}
					}
				}
			}
		});

		sudooleButton.setText("Sudo it");
		sudooleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sudooleButtonActionPerformed(evt);
			}
		});

		labelFloatingTasks.setText("Floating Tasks");

		floatingTextFrame.setEditable(false);
		floatingTextFrame.setColumns(10);
		floatingTextFrame.setRows(40);
		floatingTaskScrollPane.setViewportView(floatingTextFrame);

		// Initialization of the window frame. DO NOT MODIFY ANYTHING HERE.
		org.jdesktop.layout.GroupLayout foundationPanelLayout = new org.jdesktop.layout.GroupLayout(
				foundationPanel);
		foundationPanel.setLayout(foundationPanelLayout);
		foundationPanelLayout
				.setHorizontalGroup(foundationPanelLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(foundationPanelLayout
								.createSequentialGroup()
								.add(foundationPanelLayout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING)
										.add(foundationPanelLayout
												.createSequentialGroup()
												.add(inputText,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														610,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(
														org.jdesktop.layout.LayoutStyle.RELATED)
												.add(sudooleButton,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										.add(foundationPanelLayout
												.createSequentialGroup()
												.addContainerGap()
												.add(foundationPanelLayout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(foundationPanelLayout
																.createSequentialGroup()
																.add(6, 6, 6)
					
																.addPreferredGap(
																		org.jdesktop.layout.LayoutStyle.RELATED,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))
														.add(foundationPanelLayout
																.createSequentialGroup()
																.add(mainTextFrameScrollPane)
																.addPreferredGap(
																		org.jdesktop.layout.LayoutStyle.RELATED)
																.add(foundationPanelLayout
																		.createParallelGroup(
																				org.jdesktop.layout.GroupLayout.LEADING)
																		.add(floatingTaskScrollPane,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				173,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.add(org.jdesktop.layout.GroupLayout.TRAILING,
																				labelFloatingTasks))))))
								.addContainerGap()));
		foundationPanelLayout
				.setVerticalGroup(foundationPanelLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(org.jdesktop.layout.GroupLayout.TRAILING,
								foundationPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.add(foundationPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(foundationPanelLayout
														.createSequentialGroup()
														.add(labelFloatingTasks)
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED)
														.add(floatingTaskScrollPane))
												.add(mainTextFrameScrollPane,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														793,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(foundationPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(foundationPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.BASELINE)
												.add(inputText,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
												.add(sudooleButton))));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup()
						.add(foundationPanel,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(0, 0, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(foundationPanel,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));

		pack();
	}// </editor-fold>

	private void inputTextActionPerformed(java.awt.event.ActionEvent evt) {
		// Not used now
	}

	private void sudooleButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String userInput = inputText.getText();
		logicHandler.executeCommand(userInput);
		inputText.setText("");
		mainTextFrame.setText(outContent.toString());
		
		try {
			floatingTextFrame.setText(manager.allFloatingTasks());
		} catch (IllegalStateException e) {
			floatingTextFrame.setText(UIConstants.MESSAGE_EMPTY_LIST);
		}
	}

	// Set visibility
	public void toInvisible() {
		this.setVisible(false);
	}

	public void toVisible() {
		this.setVisible(true);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] argv) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			GlobalScreen.registerNativeHook();
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(NewGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(NewGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(NewGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(NewGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		/* Create and display the form, set up the key listener */

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				GlobalScreen.getInstance().addNativeKeyListener(new NewGUI());
			}
		});
	}

	// Variables declaration
	private int currentKey;
	private Boolean keyOne = false;
	private Boolean keyTwo = false;
	private Boolean isShown = true;
	private Boolean isDemo = false;
	private ByteArrayOutputStream outContent;
	private TaskManager manager;
	private LogicHandler logicHandler;
	private javax.swing.JButton sudooleButton;
	private javax.swing.JLabel labelFloatingTasks;
	private javax.swing.JPanel foundationPanel;
	private javax.swing.JScrollPane floatingTaskScrollPane;
	private javax.swing.JScrollPane mainTextFrameScrollPane;
	private javax.swing.JTextArea mainTextFrame;
	private javax.swing.JTextArea floatingTextFrame;
	private javax.swing.JTextField inputText;
	// End of variables declaration
}
