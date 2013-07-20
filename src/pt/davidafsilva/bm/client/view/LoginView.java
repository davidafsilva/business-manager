package pt.davidafsilva.bm.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import pt.davidafsilva.bm.client.enums.ImagesEnum;
import pt.davidafsilva.bm.client.presenter.LoginPresenter;
import pt.davidafsilva.bm.client.ui.AnimationType;
import pt.davidafsilva.bm.client.ui.DSDialog;
import pt.davidafsilva.bm.client.ui.DSPasswordField;
import pt.davidafsilva.bm.client.ui.DSTextField;


/**
 * LoginView.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:20:47 PM
 */
public class LoginView extends BaseView<LoginPresenter> {
	
	private static final long serialVersionUID = -6978786222068260456L;
	
	/** UI related properties **/
	private JPanel contentPanel;
	private JPanel bottomPane;
	private JPanel errorPane;
	private DSTextField userField;
	private DSPasswordField passwordField;
	private JButton okButton, exitButton;
	private JLabel logoLabel;
	private final Timer timer;
	
	/**
	 * Create the dialog.
	 */
	public LoginView() {
		super("Business Manager - Autentica\u00E7\u00E3o");
		timer = new Timer(8000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPane.remove(errorPane);
				animate(AnimationType.RESIZE, new Dimension(getBounds().width, 140));
				
				timer.stop();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#initialize()
	 */
	@Override
	public void initialize() {
		setPreferredSize(new Dimension(350, 140));
		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			final JLabel userLabel = new JLabel("Utilizador:");
			userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			userLabel.setBounds(78, 31, 92, 28);
			contentPanel.add(userLabel);
		}
		{
			userField = new DSTextField();
			userField.setBounds(171, 31, 166, 28);
			userField.setForeground(Color.LIGHT_GRAY);
			userField.setToolTipText("Insira o nome do seu utilizador");
			userField.setErrorToolTipText("Nome do utilizador inv\u00E1lido");
			userField.setText("<nome utilizador>");
			userField.setColumns(10);
			userField.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == 10) {
						passwordField.grabFocus();
						e.consume();
					}
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
				}
				
			});
			userField.addFocusListener(new FocusListener() {
				
				@Override
				public void focusGained(FocusEvent e) {
					if (userField.getText().equals("<nome utilizador>")) {
						userField.setText("");
						userField.setForeground(Color.DARK_GRAY);
					}
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (userField.getText().equals("")) {
						userField.setText("<nome utilizador>");
						userField.setForeground(Color.LIGHT_GRAY);
					}
				}
			});
			contentPanel.add(userField);
		}
		{
			final JLabel passwordLabel = new JLabel("Password:");
			passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			passwordLabel.setBounds(78, 58, 92, 28);
			contentPanel.add(passwordLabel);
		}
		
		passwordField = new DSPasswordField();
		passwordField.setBounds(171, 58, 166, 28);
		passwordField.setForeground(Color.LIGHT_GRAY);
		passwordField.setToolTipText("Digite a sua password");
		passwordField.setErrorToolTipText("Password inv\u00E1lida.");
		passwordField.setText("$password$");
		passwordField.setColumns(32);
		passwordField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				if (Arrays.equals(passwordField.getPassword(), "$password$".toCharArray())) {
					passwordField.setText("");
					passwordField.setForeground(Color.DARK_GRAY);
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getPassword().length == 0) {
					passwordField.setText("$password$");
					passwordField.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		contentPanel.add(passwordField);
		
		logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setBounds(6, 22, 84, 69);
		ImageIcon icon = new ImageIcon(ImagesEnum.VAULT.getPath());
		logoLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(logoLabel.getWidth(), logoLabel.getHeight(), Image.SCALE_SMOOTH)));
		contentPanel.add(logoLabel);
		{
			bottomPane = new JPanel();
			bottomPane.setLayout(new BorderLayout());
			bottomPane.setBorder(new EmptyBorder(0, 0, 10, 0));
			getContentPane().add(bottomPane, BorderLayout.SOUTH);
			
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			buttonPane.setLayout(fl_buttonPane);
			bottomPane.add(buttonPane, BorderLayout.NORTH);
			{
				okButton = new JButton("Login");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						final String username = userField.getText().trim();
						final String passwd = new String(passwordField.getPassword()).trim();
						
						if (username.length() == 0 || username.equals("<nome utilizador>")) {
							new Thread() {
								
								@Override
								public void run() {
									userField.animate(AnimationType.ERROR);
								}
							}.start();
						} else if (passwd.length() == 0 || passwd.equals("$password$")) {
							new Thread() {
								
								@Override
								public void run() {
									passwordField.animate(AnimationType.ERROR);
								}
							}.start();
						} else {
							new Thread() {
								
								@Override
								public void run() {
									if (timer.isRunning()) {
										timer.stop();
										timer.getActionListeners()[0].actionPerformed(null);
									}
									
									okButton.setEnabled(false);
									getPresenter().authenticate(username, passwd);
								}
							}.start();
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				exitButton = new JButton("Sair");
				exitButton.setActionCommand("Exit");
				exitButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						getPresenter().close();
					}
				});
				buttonPane.add(exitButton);
			}
			{
				errorPane = new JPanel();
				errorPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				JLabel errLabel = new JLabel();
				errLabel.setForeground(Color.red);
				errLabel.setText("Utilizador e/ou password inv\u00E1lidas.");
				errorPane.add(errLabel);
			}
		}
		
		setUndecorated(true);
		pack();
	}
	
	/**
	 * Notifies the user that the provided
	 * login credentials are invalid.
	 */
	public void invalidLoginCredentials() {
		if (timer.isRunning()) {
			timer.stop();
		}
		
		animate(AnimationType.RESIZE, new Dimension(getBounds().width, 140 + 26));
		bottomPane.add(errorPane, BorderLayout.SOUTH);
		passwordField.requestFocus();
		okButton.setEnabled(true);
		
		timer.start();
	}
	
	/**
	 * Notifies the user that there was an error
	 * while trying to authenticate the to the application
	 */
	public void loginError() {
		DSDialog.error(this, "Ocorreu um erro na aplica\u00E7\u00E3o ao invocar o processo de autentica\u00E7\u00E3o.\nQueira tentar novamente por favor.", "Autentica\u00E7\u00E3o - erro");
		okButton.setEnabled(true);
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#clean()
	 */
	@Override
	public void clean() {
		userField.setText(null);
		passwordField.setText(null);
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#onShow()
	 */
	@Override
	public void onShow() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				userField.setText("<nome utilizador>");
				userField.setForeground(Color.DARK_GRAY);
				passwordField.setText("$password$");
				passwordField.setForeground(Color.LIGHT_GRAY);
				okButton.setEnabled(true);
				
				okButton.requestFocus();
				userField.requestFocus();
			}
		});
	}
}
