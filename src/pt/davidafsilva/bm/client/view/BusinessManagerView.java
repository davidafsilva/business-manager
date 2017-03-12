package pt.davidafsilva.bm.client.view;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.michaelbaranov.microba.calendar.CalendarPane;
import com.michaelbaranov.microba.calendar.DatePicker;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pt.davidafsilva.bm.client.enums.ImagesEnum;
import pt.davidafsilva.bm.client.presenter.BusinessManagerPresenter;
import pt.davidafsilva.bm.client.ui.DSCheckBox;
import pt.davidafsilva.bm.client.ui.DSDialog;
import pt.davidafsilva.bm.client.ui.DSItem;
import pt.davidafsilva.bm.client.ui.DSItemPanel;
import pt.davidafsilva.bm.client.ui.DSLabelButton;
import pt.davidafsilva.bm.client.ui.DSPanel;
import pt.davidafsilva.bm.client.ui.DSPasswordField;
import pt.davidafsilva.bm.client.ui.DSTextField;
import pt.davidafsilva.bm.client.ui.autocomplete.DSAutoComplete;
import pt.davidafsilva.bm.client.ui.event.ContentRequestEvent;
import pt.davidafsilva.bm.client.ui.event.ContentRequestEvent.ContentRequestEventListener;
import pt.davidafsilva.bm.client.ui.event.DataRequestEvent;
import pt.davidafsilva.bm.client.ui.event.DataRequestEvent.DataRequestEventListener;
import pt.davidafsilva.bm.client.ui.event.MouseClickEvent;
import pt.davidafsilva.bm.client.ui.event.MouseClickEvent.MouseClickEventListener;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent.SelectionChangeEventListener;
import pt.davidafsilva.bm.client.ui.grid.DSDataGrid;
import pt.davidafsilva.bm.client.ui.grid.DSDataGridColumn;
import pt.davidafsilva.bm.client.ui.grid.DSDataGridColumnType;
import pt.davidafsilva.bm.client.ui.grid.DSDataGridPane;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.SaleProduct;
import pt.davidafsilva.bm.shared.domain.UserPermission;
import pt.davidafsilva.bm.shared.enums.ProductUnit;


/**
 * BusinessManagerView.java
 * 
 * The business manager user interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:27:39 AM
 */
public class BusinessManagerView extends BaseView<BusinessManagerPresenter> {
	
	private static final long serialVersionUID = 5770295028959942360L;
	
	// -----------------
	// right top panel
	// -----------------
	private JPanel rigthTopPanel;
	private JLabel authUserLabel;
	private JButton logoutButton;
	
	// -----------------
	// top menu
	// -----------------
	private JPanel topMenuPanel;
	private DSPanel contentsPanel;
	private DSItemPanel menuPanel;
	private DSItem billingMenu;
	private DSItem historyMenu;
	private DSItem clientsMenu;
	private DSItem productsMenu;
	private DSItem preferencesMenu;
	
	// ------------------------
	// Sales menu components
	// ------------------------
	private JButton salesBtnSave, salesBtnCancel;
	private DSTextField salesTfPaymentConditions, salesTfObservations, salesTfDischargePlace;
	private DSAutoComplete<Client> salesAcClient;
	private DSAutoComplete<Product> salesAcProduct;
	private DatePicker salesDpDocDate;
	private DSDataGridPane salesProGrid;
	private SalesGridContextMenu saleContextMenu;
	private DSCheckBox salesCbCardex;
	
	// ------------------------
	// History menu components
	// ------------------------
	private JButton histBtnSearch;
	private DSAutoComplete<Client> histAcClient;
	private DatePicker histDpInitialDate, histDpFinalDate;
	private JLabel histUntilLabel;
	private DSDataGridPane histGrid;
	private JButton histBtnSaleDetail, histBtnSaleSend;
	
	// ------------------------
	// Clients menu components 
	// ------------------------
	private DSLabelButton cliBtnInsert, cliBtnEdit, cliBtnDelete;
	private DSTextField cliTfId, cliTfName, cliTfCode, cliTfAddr, cliTfPhone, cliTfFax, cliTfVat, cliTfDischargePlace;
	private DSDataGridPane cliGrid;
	private JButton cliBtnSave, cliBtnCancel;
	
	// ------------------------
	// Products menu components 
	// ------------------------
	private DSLabelButton prodBtnInsert, prodBtnEdit, prodBtnDelete;
	private DSTextField prodTfId, prodTfDescription, prodTfCode, prodTfPrice, prodTfDefaultAmount;
	private ButtonGroup prodBgUnits;
	private JRadioButton prodRbNone, prodRbGram, prodRbKiloGram;
	private JPanel prodUnitPanel;
	private DSDataGridPane prodGrid;
	private JButton prodBtnSave, prodBtnCancel;
	
	// ------------------------------
	// Configuration menu components 
	// ------------------------------
	private DSTextField cfgTfSourceEmail, cfgTfTargetEmail, cfgTfSubject, cfgTfBody, cfgTfAuthor;
	private JButton cfgEmailBtnSave, cfgEmailBtnCancel;
	private DSPasswordField cfgTfOldPwd, cfgTfEmailPwd, cfgTfNewPwd, cfgTfNewPwdConfirmation;
	private JButton cfgPwdBtnSave;
	private JCheckBox cfgCbProductCode, cfgCbProductDesc, cfgCbProductQty, cfgCbProductPrice,
			cfgCbDiscount, cfgCbVat, cfgCbTotals;
	private JButton cfgSaleResumeBtnSave, cfgSaleResumeBtnCancel;
	
	// -----------------
	// bottom panel
	// -----------------
	private DSPanel bottomPanel;
	private JLabel copyrightsLabel;
	
	/**
	 * Modes
	 */
	enum ScreenMode {
		DEFAULT, INSERT, EDITION;
	}
	
	private ScreenMode cMode = ScreenMode.DEFAULT;
	
	
	/**
	 * Create the dialog.
	 */
	public BusinessManagerView() {
		super("Business Manager");
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#initialize()
	 */
	@Override
	public void initialize() {
		setResizable(true);
		setPreferredSize(new Dimension(870, 700));
		setMinimumSize(getPreferredSize());
		getContentPane().setLayout(new BorderLayout());
		
		// -----------------
		// right top panel
		// -----------------
		rigthTopPanel = new JPanel();
		final FlowLayout rigthTopPanelLayout = (FlowLayout) rigthTopPanel.getLayout();
		rigthTopPanelLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(rigthTopPanel, BorderLayout.NORTH);
		
		authUserLabel = new JLabel("Autenticado como");
		authUserLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		authUserLabel.setForeground(Color.DARK_GRAY);
		rigthTopPanel.add(authUserLabel);
		
		logoutButton = new JButton("Logout");
		logoutButton.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		logoutButton.setPreferredSize(new Dimension(70, 20));
		logoutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BusinessManagerView.this.presenter.logout();
			}
		});
		rigthTopPanel.add(logoutButton);
		
		// -----------------
		// top menu
		// -----------------
		topMenuPanel = new DSPanel(new BorderLayout());
		getContentPane().add(topMenuPanel, BorderLayout.CENTER);
		
		menuPanel = new DSItemPanel();
		topMenuPanel.add(menuPanel, BorderLayout.NORTH);
		
		contentsPanel = new DSPanel(new BorderLayout());
		contentsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		topMenuPanel.add(contentsPanel, BorderLayout.CENTER);
		menuPanel.setItemsContainer(contentsPanel);
		menuPanel.addContentRequestListener(new ContentRequestEventListener() {
			
			/* (non-Javadoc)
			 * @see pt.davidafsilva.bm.client.ui.event.ContentRequestEvent.
			 * ContentRequestEventListener#onContentRequest
			 * (pt.davidafsilva.bm.client.ui.event.ContentRequestEvent)
			 */
			@Override
			public void onContentRequest(ContentRequestEvent evt) {
				if (evt.getSource() == billingMenu) {
					billingMenu.setContents(buildSalesContents(), BorderLayout.CENTER);
				} else if (evt.getSource() == historyMenu) {
					historyMenu.setContents(buildHistoryContents(), BorderLayout.CENTER);
				} else if (evt.getSource() == clientsMenu) {
					clientsMenu.setContents(buildClientsContents(), BorderLayout.CENTER);
				} else if (evt.getSource() == productsMenu) {
					productsMenu.setContents(buildProductsContents(), BorderLayout.CENTER);
				} else if (evt.getSource() == preferencesMenu) {
					preferencesMenu.setContents(buildConfigContents(), BorderLayout.CENTER);
				}
			}
		});
		
		// billing menu
		billingMenu = new DSItem("Vendas", new ImageIcon(ImagesEnum.MENU_BILLING.getPath()));
		menuPanel.add(billingMenu);
		
		// history menu;
		historyMenu = new DSItem("Hist\u00F3rico", new ImageIcon(ImagesEnum.MENU_HISTORIC.getPath()));
		menuPanel.add(historyMenu);
		
		// clients menu;
		clientsMenu = new DSItem("Clientes", new ImageIcon(ImagesEnum.MENU_CLIENTS.getPath()));
		menuPanel.add(clientsMenu);
		
		// products menu;
		productsMenu = new DSItem("Artigos", new ImageIcon(ImagesEnum.MENU_PRODUCTS.getPath()));
		menuPanel.add(productsMenu);
		
		// preferences  menu;
		preferencesMenu = new DSItem("Prefer\u00EAncias", new ImageIcon(ImagesEnum.MENU_PREFERENCES.getPath()));
		menuPanel.add(preferencesMenu);
		
		// -----------------
		// bottom panel
		// -----------------
		bottomPanel = new DSPanel();
		bottomPanel.setOpacity(0);
		final FlowLayout bottomPanelLayout = (FlowLayout) bottomPanel.getLayout();
		bottomPanelLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		copyrightsLabel = new JLabel("Business Manager \u00a9 David Silva");
		copyrightsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		copyrightsLabel.setForeground(Color.DARK_GRAY);
		bottomPanel.add(copyrightsLabel);
		bottomPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				bottomPanel.fadeOut();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				bottomPanel.fadeIn();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		pack();
	}
	
	// ##########
	// History
	// ##########
	
	/**
	 * Builds the history panel contents
	 * 
	 * @return The contents
	 */
	private JComponent buildHistoryContents() {
		histBtnSearch = new JButton("Pesquisar");
		histBtnSearch.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.searchSales(
							histAcClient.getSelectedItem(),
							histDpInitialDate.getDate(),
							histDpFinalDate.getDate()
							);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		// form e-mail configuration
		JPanel frmSearch = new JPanel(new FormLayout(
				// columns
				"pref, 10px, pref:grow",
				// rows
				"pref, pref"));
		frmSearch.setBorder(Borders.DIALOG_BORDER);
		CellConstraints frmConst = new CellConstraints();
		
		histAcClient = new DSAutoComplete<Client>() {
			
			private static final long serialVersionUID = 1982378921637812L;
			
			@Override
			public Object getId(Client object) {
				return object.getId();
			}
			
			@Override
			public String getDescription(Client object) {
				return object.getName();
			}
			
			@Override
			public String getCode(Client object) {
				return object.getCode();
			}
		};
		histAcClient.setDataRequestHandler(new DataRequestEventListener() {
			
			@Override
			public void onDataRequest(DataRequestEvent evt) {
				histAcClient.setDataSource(presenter.getClients());
			}
		});
		histAcClient.setPreferredSize(new Dimension(341, 23));
		buildFormItem(frmSearch, 1, frmConst, "Cliente", histAcClient);
		
		// inner form
		JPanel frmInner = new JPanel(new FormLayout(
				// columns
				"pref, 5px, pref, 5px, pref, 5px, pref",
				// rows
				"pref"));
		frmInner.setBorder(BorderFactory.createEmptyBorder());
		CellConstraints frmInnerConst = new CellConstraints();
		histDpInitialDate = new DatePicker(null);
		histDpInitialDate.setPreferredSize(new Dimension(155, 23));
		histDpInitialDate.addPropertyChangeListener(CalendarPane.PROPERTY_NAME_DATE, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (histDpFinalDate.getDate() != null && evt.getNewValue() != null) {
					if (histDpFinalDate.getDate().before((Date) evt.getNewValue())) {
						try {
							histDpFinalDate.setDate(null);
						} catch (PropertyVetoException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		histUntilLabel = new JLabel("at\u00E9");
		histDpFinalDate = new DatePicker(null);
		histDpFinalDate.setPreferredSize(new Dimension(155, 23));
		histDpFinalDate.addPropertyChangeListener(CalendarPane.PROPERTY_NAME_DATE, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (histDpInitialDate.getDate() != null && evt.getNewValue() != null) {
					if (histDpInitialDate.getDate().after((Date) evt.getNewValue())) {
						DSDialog.error(BusinessManagerView.this, "A data superior tem que ser necessariamente superior \u00E0 data inferior.", "Data inv\u00E1lida");
						try {
							histDpFinalDate.setDate((Date) evt.getOldValue());
						} catch (PropertyVetoException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		frmInner.add(histDpInitialDate, frmInnerConst.xy(1, 1, CellConstraints.LEFT, CellConstraints.CENTER));
		frmInner.add(histUntilLabel, frmInnerConst.xy(3, 1, CellConstraints.LEFT, CellConstraints.CENTER));
		frmInner.add(histDpFinalDate, frmInnerConst.xy(5, 1, CellConstraints.LEFT, CellConstraints.CENTER));
		frmInner.add(histBtnSearch, frmInnerConst.xy(7, 1, CellConstraints.LEFT, CellConstraints.CENTER));
		
		buildFormItem(frmSearch, 2, frmConst, "Intervalo", frmInner);
		
		// grid
		DSDataGridColumn<Sale, String> codeCol = new DSDataGridColumn<Sale, String>("C\u00F3digo cliente", 10) {
			
			private static final long serialVersionUID = -1892787979832200709L;
			
			@Override
			public String getValue(Sale object) {
				return object.getClient().getCode();
			}
			
			@Override
			public void setValue(Sale object, String value) {
			}
		};
		DSDataGridColumn<Sale, String> nameCol = new DSDataGridColumn<Sale, String>("Nome cliente", 15) {
			
			private static final long serialVersionUID = -2892787979832200709L;
			
			@Override
			public String getValue(Sale object) {
				return object.getClient().getName();
			}
			
			@Override
			public void setValue(Sale object, String value) {
			}
		};
		DSDataGridColumn<Sale, Date> dateCol = new DSDataGridColumn<Sale, Date>("Data", 15) {
			
			private static final long serialVersionUID = -3892787979832200709L;
			
			@Override
			public Date getValue(Sale object) {
				return object.getSaleDate();
			}
			
			@Override
			public void setValue(Sale object, Date value) {
			}
		};
		DSDataGridColumn<Sale, String> dischargeCol = new DSDataGridColumn<Sale, String>("Descarga", 20) {
			
			private static final long serialVersionUID = -4892787979832200709L;
			
			@Override
			public String getValue(Sale object) {
				return object.getDischargePlace();
			}
			
			@Override
			public void setValue(Sale object, String value) {
			}
		};
		DSDataGridColumn<Sale, String> obsCol = new DSDataGridColumn<Sale, String>("Obs.", 15) {
			
			private static final long serialVersionUID = -5892787979832200709L;
			
			@Override
			public String getValue(Sale object) {
				return object.getObservations();
			}
			
			@Override
			public void setValue(Sale object, String value) {
			}
		};
		DSDataGridColumn<Sale, Double> liqCol = new DSDataGridColumn<Sale, Double>("Total liquido (\u20ac)", 15) {
			
			private static final long serialVersionUID = -6892787979832200709L;
			
			@Override
			public Double getValue(Sale object) {
				return object.getTotalLiquid();
			}
			
			@Override
			public void setValue(Sale object, Double value) {
			}
		};
		DSDataGridColumn<Sale, String> sentCol = new DSDataGridColumn<Sale, String>("Enviado E-mail", 15) {
			
			private static final long serialVersionUID = -6892787979832200799L;
			
			@Override
			public String getValue(Sale object) {
				return object.wasSentByEmail() ? "Sim" : "N\u00E3o";
			}
			
			@Override
			public void setValue(Sale object, String value) {
			}
		};
		histGrid = new DSDataGridPane();
		ArrayList<DSDataGridColumn<Sale, ?>> cols = new ArrayList<DSDataGridColumn<Sale, ?>>();
		cols.add(codeCol);
		cols.add(nameCol);
		cols.add(dateCol);
		cols.add(dischargeCol);
		cols.add(obsCol);
		cols.add(liqCol);
		cols.add(sentCol);
		histGrid.getGrid().setColumns(cols);
		
		// detail button
		histBtnSaleDetail = new JButton("Visualizar Detalhe");
		histBtnSaleDetail.addMouseListener(new MouseAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (histGrid.getGrid().getSelectedItem() == null) {
						DSDialog.info(BusinessManagerView.this, "N\u00E3o possui nenhuma venda seleccionada.", "Detalhe de venda");
					} else {
						presenter.viewSaleDetail((Sale) histGrid.getGrid().getSelectedItem());
					}
				}
			}
		});
		// send button
		histBtnSaleSend = new JButton("Enviar por e-mail");
		histBtnSaleSend.addMouseListener(new MouseAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (histGrid.getGrid().getSelectedItem() == null) {
						DSDialog.info(BusinessManagerView.this, "N\u00E3o possui nenhuma venda seleccionada.", "Envio de venda");
					} else {
						presenter.sendSaleByEmail((Sale) histGrid.getGrid().getSelectedItem(), true);
					}
				}
			}
		});
		
		JPanel btnContainer = new JPanel();
		btnContainer.add(histBtnSaleDetail);
		btnContainer.add(histBtnSaleSend);
		
		// build the container
		JPanel container = new JPanel(new FormLayout(
				"fill:pref:grow",
				"pref, 5px, fill:0:grow, 5px, pref"));
		container.setBorder(Borders.createEmptyBorder("0,15,5,15"));
		CellConstraints cc = new CellConstraints();
		container.add(frmSearch, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.TOP));
		container.add(histGrid, cc.xy(1, 3));
		container.add(btnContainer, cc.xy(1, 5));
		
		return container;
	}
	
	/**
	 * Sets the search sales result
	 * 
	 * @param sales
	 *        The sales
	 */
	public void setSearchSalesResult(List<Sale> sales) {
		if (sales == null || sales.isEmpty()) {
			histGrid.getGrid().setDataSource(null);
			DSDialog.info(this, "N\u00E3o foram encontrados resultados para a sua pesquisa.", "Pesquisa de vendas");
		} else {
			histGrid.getGrid().setDataSource(sales);
		}
	}
	
	/**
	 * Updates the given sale in the history view
	 * 
	 * @param sale
	 *        The sale to be updated
	 */
	public void updateSaleHistory(Sale sale) {
		histGrid.getGrid().getModel().updateItem(sale);
	}
	
	
	// ##########
	// Sales
	// ##########
	
	/**
	 * Builds the sales panel contents
	 * 
	 * @return The contents
	 */
	private JComponent buildSalesContents() {
		// form
		JPanel frm = new JPanel(new FormLayout(
				// columns
				"pref, 10px, 0:grow",
				// rows
				"10px, pref, 2px, pref, pref, pref, pref, pref, 2px, pref, 20px, 0:grow, 5px, pref"));
		frm.setBorder(Borders.DIALOG_BORDER);
		
		// bottom buttons (save/cancel)
		JPanel bottomButtonContainer = new JPanel();
		salesBtnSave = new JButton("Gravar");
		salesBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.saveSale(
							salesAcClient.getSelectedItem(),
							salesTfPaymentConditions.getText(),
							salesTfObservations.getText(),
							salesTfDischargePlace.getText(),
							salesDpDocDate.getDate(),
							salesCbCardex.isSelected()
							);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		salesBtnCancel = new JButton("Cancelar");
		salesBtnCancel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.cancelSale();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		bottomButtonContainer.add(salesBtnSave);
		bottomButtonContainer.add(salesBtnCancel);
		
		CellConstraints frmConst = new CellConstraints();
		salesAcClient = new DSAutoComplete<Client>() {
			
			private static final long serialVersionUID = 1982378921637812L;
			
			@Override
			public Object getId(Client object) {
				return object.getId();
			}
			
			@Override
			public String getDescription(Client object) {
				return object.getName();
			}
			
			@Override
			public String getCode(Client object) {
				return object.getCode();
			}
		};
		salesAcClient.setDataRequestHandler(new DataRequestEventListener() {
			
			@Override
			public void onDataRequest(DataRequestEvent evt) {
				salesAcClient.setDataSource(presenter.getClients());
			}
		});
		salesAcClient.addSelectionChangeListener(new SelectionChangeEventListener() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent evt) {
				Client cli = salesAcClient.getSelectedItem();
				if (cli != null) {
					if (cli.getDefaultDischargePlace() != null && !cli.getDefaultDischargePlace().trim().isEmpty() &&
							salesTfDischargePlace.getText() != null && salesTfDischargePlace.getText().trim().isEmpty()) {
						salesTfDischargePlace.setText(cli.getDefaultDischargePlace().trim());
					}
				}
			}
		});
		salesAcClient.setPreferredSize(new Dimension(311, 23));
		buildFormItem(frm, 2, frmConst, "Cliente", salesAcClient);
		buildFormItem(frm, 4, frmConst, "Data doc.", (salesDpDocDate = new DatePicker()));
		salesDpDocDate.setPreferredSize(new Dimension(311, 23));
		salesAcProduct = new DSAutoComplete<Product>() {
			
			private static final long serialVersionUID = 7646044446544184104L;
			
			@Override
			public Object getId(Product object) {
				return object.getId();
			}
			
			@Override
			public String getDescription(Product object) {
				return object.getDescription();
			}
			
			@Override
			public String getCode(Product object) {
				return object.getCode();
			}
		};
		salesAcProduct.addSelectionChangeListener(new SelectionChangeEventListener() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent evt) {
				if (salesAcProduct.getSelectedItem() != null) {
					presenter.addSaleProduct(salesAcProduct.getSelectedItem());
					salesAcProduct.clearSelectedItems(false);
				}
			}
		});
		salesAcProduct.setDataRequestHandler(new DataRequestEventListener() {
			
			@Override
			public void onDataRequest(DataRequestEvent evt) {
				salesAcProduct.setDataSource(presenter.getProducts());
			}
		});
		salesAcProduct.setPreferredSize(new Dimension(311, 23));
		buildFormItem(frm, 5, frmConst, "Observa\u00E7\u00F5es", (salesTfObservations = createTextField(false, false, true)));
		buildFormItem(frm, 6, frmConst, "Condi\u00E7\u00F5es pagamento", (salesTfPaymentConditions = createTextField(false, false, true)));
		buildFormItem(frm, 7, frmConst, "Local descarga", (salesTfDischargePlace = createTextField(false, false, true)));
		buildFormItem(frm, 8, frmConst, "Cardex", (salesCbCardex = new DSCheckBox()));
		buildFormItem(frm, 10, frmConst, "Adiciona artigo", salesAcProduct);
		
		// products grid
		DSDataGridColumn<SaleProduct, String> codeCol = new DSDataGridColumn<SaleProduct, String>("C\u00F3digo", 10) {
			
			private static final long serialVersionUID = -7892787979832200709L;
			
			@Override
			public String getValue(SaleProduct object) {
				return object.getBaseProduct().getCode();
			}
			
			@Override
			public void setValue(SaleProduct object, String value) {
			}
		};
		DSDataGridColumn<SaleProduct, String> descriptionCol = new DSDataGridColumn<SaleProduct, String>("Descri\u00E7\u00E3o", 25) {
			
			private static final long serialVersionUID = 8176762744929998035L;
			
			@Override
			public String getValue(SaleProduct object) {
				return object.getBaseProduct().getDescription();
			}
			
			@Override
			public void setValue(SaleProduct object, String value) {
			}
		};
		DSDataGridColumn<SaleProduct, Double> qtyCol = new DSDataGridColumn<SaleProduct, Double>("Qnt.", DSDataGridColumnType.DECIMAL, 10) {
			
			private static final long serialVersionUID = 8176762744929998036L;
			
			@Override
			public Double getValue(SaleProduct object) {
				return object.getAmount();
			}
			
			@Override
			public void setValue(SaleProduct object, Double value) {
				presenter.updateSaleProductAmount(object, value);
			}
		};
		qtyCol.setEditable(true);
		
		DSDataGridColumn<SaleProduct, ProductUnit> unitCol = new DSDataGridColumn<SaleProduct, ProductUnit>("Unidade", DSDataGridColumnType.COMBOBOX, 10) {
			
			private static final long serialVersionUID = 8176762744929098036L;
			
			@Override
			public ProductUnit getValue(SaleProduct object) {
				return object.getUnit();
			}
			
			@Override
			public void setValue(SaleProduct object, ProductUnit value) {
				object.setUnit(value);
			}
		};
		unitCol.setModel(new DefaultComboBoxModel<ProductUnit>(ProductUnit.values()));
		unitCol.setEditable(true);
		
		DSDataGridColumn<SaleProduct, Double> priceCol = new DSDataGridColumn<SaleProduct, Double>("Pre\u00E7o Unit. (\u20ac)", DSDataGridColumnType.DECIMAL, 15) {
			
			private static final long serialVersionUID = 8176762744929998037L;
			
			@Override
			public Double getValue(SaleProduct object) {
				return object.getPrice();
			}
			
			@Override
			public void setValue(SaleProduct object, Double value) {
				presenter.updateSaleProductPrice(object, value);
			}
		};
		priceCol.setEditable(true);
		DSDataGridColumn<SaleProduct, String> discountCol = new DSDataGridColumn<SaleProduct, String>("% Desc.", 10) {
			
			private static final long serialVersionUID = 8176762744929998038L;
			
			@Override
			public String getValue(SaleProduct object) {
				return object.getDiscount();
			}
			
			@Override
			public void setValue(SaleProduct object, String value) {
				presenter.updateSaleProductDiscount(object, value);
			}
		};
		discountCol.setEditable(true);
		DSDataGridColumn<SaleProduct, Double> vatCol = new DSDataGridColumn<SaleProduct, Double>("% IVA", DSDataGridColumnType.DECIMAL, 10) {
			
			private static final long serialVersionUID = 8176762744929998038L;
			
			@Override
			public Double getValue(SaleProduct object) {
				return object.getVat();
			}
			
			@Override
			public void setValue(SaleProduct object, Double value) {
				presenter.updateSaleProductVat(object, value);
			}
		};
		vatCol.setEditable(true);
		DSDataGridColumn<SaleProduct, Double> totalCol = new DSDataGridColumn<SaleProduct, Double>("Total (\u20ac)", 10) {
			
			private static final long serialVersionUID = 8176762744929998038L;
			
			@Override
			public Double getValue(SaleProduct object) {
				return presenter.getSaleProductTotals(object);
			}
			
			@Override
			public void setValue(SaleProduct object, Double value) {
			}
		};
		
		salesProGrid = new DSDataGridPane();
		salesProGrid.getGrid().setEditable(true);
		ArrayList<DSDataGridColumn<SaleProduct, ?>> cols = new ArrayList<DSDataGridColumn<SaleProduct, ?>>();
		cols.add(codeCol);
		cols.add(descriptionCol);
		cols.add(qtyCol);
		cols.add(unitCol);
		cols.add(priceCol);
		cols.add(discountCol);
		cols.add(vatCol);
		cols.add(totalCol);
		salesProGrid.getGrid().setColumns(cols);
		salesProGrid.getGrid().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && e.getComponent() instanceof DSDataGrid) {
					final int r = salesProGrid.getGrid().rowAtPoint(e.getPoint());
					final int c = salesProGrid.getGrid().columnAtPoint(e.getPoint());
					
					// row
					if (r >= 0 && r < salesProGrid.getGrid().getRowCount()) {
						salesProGrid.getGrid().setRowSelectionInterval(r, r);
					} else {
						salesProGrid.getGrid().clearSelection();
					}
					
					if (salesProGrid.getGrid().getSelectedRow() < 0) {
						return;
					}
					
					// get the popup
					final SalesGridContextMenu popup = saleContextMenu == null ? (saleContextMenu = new SalesGridContextMenu()) : saleContextMenu;
					
					// column
					popup.handleColumnSelection(c);
					
					// show
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
		frm.add(salesProGrid, frmConst.xyw(1, 12, 3));
		frm.add(bottomButtonContainer, frmConst.xy(3, 14, CellConstraints.RIGHT, CellConstraints.CENTER));
		
		return frm;
	}
	
	/**
	 * Deletes the currently selected sale
	 */
	private void deleteSelectedSale() {
		presenter.deleteSaleProduct((SaleProduct) salesProGrid.getGrid().getSelectedItem());
	}
	
	/**
	 * Deletes the given sale product from the current sale
	 * 
	 * @param saleProduct
	 *        The sale product to delete
	 */
	public void deleteSaleProduct(SaleProduct saleProduct) {
		salesProGrid.getGrid().getModel().deleteItem(saleProduct);
	}
	
	/**
	 * The context menu for the sales grid
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 11:52:48 AM
	 */
	private class SalesGridContextMenu extends JPopupMenu {
		
		private static final long serialVersionUID = -5362393493590981257L;
		private final JMenuItem deleteItem;
		private final JMenuItem clipboardCopyItem, clipboardPasteItem;
		private int lastSelectedColumn = -1;
		
		public SalesGridContextMenu() {
			deleteItem = new JMenuItem("Apagar", KeyEvent.VK_DELETE);
			deleteItem.setIcon(new ImageIcon(ImagesEnum.BTN_DELETE_SMALL.getPath()));
			deleteItem.addMouseListener(new MouseAdapter() {
				
				/* (non-Javadoc)
				 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseReleased(MouseEvent e) {
					BusinessManagerView.this.deleteSelectedSale();
				}
			});
			clipboardCopyItem = new JMenuItem("Copiar", KeyEvent.VK_COPY);
			clipboardCopyItem.setIcon(new ImageIcon(ImagesEnum.BTN_COPY_SMALL.getPath()));
			clipboardCopyItem.addMouseListener(new MouseAdapter() {
				
				/* (non-Javadoc)
				 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseReleased(MouseEvent e) {
					if (lastSelectedColumn >= 0) {
						SaleProduct prod = (SaleProduct) BusinessManagerView.this.salesProGrid.getGrid().getSelectedItem();
						if (prod != null) {
							String cpValue = null;
							switch (lastSelectedColumn) {
								case 2:
								case 3:
									cpValue = prod.getAmount() + ":" + (prod.getUnit() == null ? 0 : prod.getUnit().getCode());
									break;
								case 4:
									cpValue = prod.getPrice() == null ? "" : String.valueOf(prod.getPrice());
									break;
								case 5:
									cpValue = prod.getDiscount();
									break;
								case 6:
									cpValue = prod.getVat() == null ? "" : String.valueOf(prod.getVat());
									break;
								default:
									break;
							}
							
							if (cpValue != null) {
								StringSelection selection = new StringSelection(cpValue);
								Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
								if (cb != null) {
									cb.setContents(selection, selection);
								}
							}
						}
					}
				}
			});
			clipboardPasteItem = new JMenuItem("Colar", KeyEvent.VK_PASTE);
			clipboardPasteItem.setIcon(new ImageIcon(ImagesEnum.BTN_PASTE_SMALL.getPath()));
			clipboardPasteItem.addMouseListener(new MouseAdapter() {
				
				/* (non-Javadoc)
				 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseReleased(MouseEvent e) {
					SaleProduct prod = (SaleProduct) BusinessManagerView.this.salesProGrid.getGrid().getSelectedItem();
					Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
					if (cb != null && prod != null) {
						Transferable t = cb.getContents(null);
						if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
							try {
								String data = (String) t.getTransferData(DataFlavor.stringFlavor);
								if (data != null) {
									boolean updateRow = false;
									switch (lastSelectedColumn) {
										case 2: // quantity
										case 3: // unit
											if (data.contains(":")) {
												String[] tmp = data.split(":", 2);
												if (tmp != null && tmp.length == 2) {
													presenter.updateSaleProductAmount(prod, getDouble(tmp[0]));
													try {
														ProductUnit unit = ProductUnit.fromCode(Integer.parseInt(tmp[1]));
														prod.setUnit(unit);
													} catch (Exception e2) {
													}
													updateRow = true;
												}
											} else {
												presenter.updateSaleProductAmount(prod, getDouble(data));
												updateRow = true;
											}
											break;
										case 4: // price
											presenter.updateSaleProductPrice(prod, getDouble(data));
											updateRow = true;
											break;
										case 5: // discount
											presenter.updateSaleProductDiscount(prod, data);
											updateRow = true;
											break;
										case 6: // vat
											presenter.updateSaleProductVat(prod, getDouble(data));
											updateRow = true;
											break;
										default:
											break;
									}
									
									if (updateRow) {
										BusinessManagerView.this.salesProGrid.getGrid().getModel().updateItem(prod);
									}
								}
							} catch (UnsupportedFlavorException | IOException e1) {
								// fail silently
							}
						}
					}
				}
			});
			
			add(deleteItem);
			add(clipboardCopyItem);
			add(clipboardPasteItem);
		}
		
		/**
		 * Handles the column selection
		 * 
		 * @param column
		 *        The column index
		 */
		public void handleColumnSelection(int column) {
			lastSelectedColumn = column;
			switch (lastSelectedColumn) {
				case 2:
				case 3:
					clipboardCopyItem.setText("Copiar quantidade");
					clipboardCopyItem.setVisible(true);
					clipboardPasteItem.setText("Colar quantidade");
					clipboardPasteItem.setVisible(true);
					break;
				case 4:
					clipboardCopyItem.setText("Copiar pre\u00E7o");
					clipboardCopyItem.setVisible(true);
					clipboardPasteItem.setText("Colar pre\u00E7o");
					clipboardPasteItem.setVisible(true);
					break;
				case 5:
					clipboardCopyItem.setText("Copiar desconto");
					clipboardCopyItem.setVisible(true);
					clipboardPasteItem.setText("Colar desconto");
					clipboardPasteItem.setVisible(true);
					break;
				case 6:
					clipboardCopyItem.setText("Copiar IVA");
					clipboardCopyItem.setVisible(true);
					clipboardPasteItem.setText("Colar IVA");
					clipboardPasteItem.setVisible(true);
					break;
				default:
					clipboardCopyItem.setVisible(false);
					clipboardPasteItem.setVisible(false);
					lastSelectedColumn = -1;
					break;
			}
		}
		
		private Double getDouble(String value) {
			try {
				return Double.parseDouble(value.replaceAll(",", "."));
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	// ##########
	//  Clients
	// ########## 
	
	
	/**
	 * Builds the clients panel contents
	 * 
	 * @return The contents
	 */
	private JComponent buildClientsContents() {
		// grid
		DSDataGridColumn<Client, String> codeCol = new DSDataGridColumn<Client, String>("C\u00F3digo", 30) {
			
			private static final long serialVersionUID = -7892787979832200708L;
			
			@Override
			public String getValue(Client object) {
				return object.getCode();
			}
			
			@Override
			public void setValue(Client object, String value) {
			}
		};
		DSDataGridColumn<Client, String> nameCol = new DSDataGridColumn<Client, String>("Nome", 70) {
			
			private static final long serialVersionUID = 8176762744929998034L;
			
			@Override
			public String getValue(Client object) {
				return object.getName();
			}
			
			@Override
			public void setValue(Client object, String value) {
			}
		};
		
		cliGrid = new DSDataGridPane();
		ArrayList<DSDataGridColumn<Client, ?>> cols = new ArrayList<DSDataGridColumn<Client, ?>>();
		cols.add(codeCol);
		cols.add(nameCol);
		cliGrid.getGrid().setColumns(cols);
		cliGrid.getGrid().setDataSource(presenter.getClients());
		cliGrid.getGrid().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Client client = (Client) cliGrid.getGrid().getSelectedItem();
				clean();
				if (client != null) {
					
					cliTfId.setText(String.valueOf(client.getId()));
					cliTfName.setText(client.getName());
					cliTfCode.setText(client.getCode());
					cliTfAddr.setText(client.getAddress());
					cliTfPhone.setText(client.getPhone());
					cliTfFax.setText(client.getFax());
					cliTfVat.setText(client.getVatNumber());
					cliTfDischargePlace.setText(client.getDefaultDischargePlace());
					
					setClientButtonsStatus(true, true, true);
				} else {
					setClientButtonsStatus(true, false, false);
				}
			}
		});
		
		// top buttons
		JPanel topButtonContainer = new JPanel();
		cliBtnInsert = createButton(ImagesEnum.BTN_INSERT, true);
		cliBtnInsert.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				clean();
				setClientButtonsStatus(false, false, false);
				enableClientFields(true);
				
				cliBtnSave.setVisible(true);
				cliBtnCancel.setVisible(true);
				
				// disable selection
				cliGrid.getGrid().setEnabled(false);
				
				cliTfName.requestFocus();
				
				cMode = ScreenMode.INSERT;
			}
		});
		cliBtnEdit = createButton(ImagesEnum.BTN_EDIT, false);
		cliBtnEdit.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				Client client = (Client) cliGrid.getGrid().getSelectedItem();
				
				if (client != null) {
					enableClientFields(true);
					setClientButtonsStatus(false, false, false);
					
					cliBtnSave.setVisible(true);
					cliBtnCancel.setVisible(true);
					
					// disable selection
					cliGrid.getGrid().setEnabled(false);
					
					cliTfName.requestFocus();
					
					cMode = ScreenMode.EDITION;
				}
			}
		});
		cliBtnDelete = createButton(ImagesEnum.BTN_DELETE, false);
		cliBtnDelete.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				Client client = (Client) cliGrid.getGrid().getSelectedItem();
				
				if (client != null) {
					if (DSDialog.question(BusinessManagerView.this, "Deseja realmente remover o cliente: "
							+ client.getCode(), "Confirma\u00E7\u00E3o")) {
						presenter.removeClient(client);
					}
				}
			}
		});
		topButtonContainer.add(cliBtnInsert);
		topButtonContainer.add(cliBtnEdit);
		topButtonContainer.add(cliBtnDelete);
		
		
		// bottom buttons (save/cancel)
		JPanel bottomButtonContainer = new JPanel();
		cliBtnSave = new JButton("Gravar");
		cliBtnSave.setVisible(false);
		cliBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (cMode == ScreenMode.INSERT) {
						presenter.saveCient(
								cliTfName.getText(),
								cliTfCode.getText(),
								cliTfAddr.getText(),
								cliTfPhone.getText(),
								cliTfFax.getText(),
								cliTfVat.getText(),
								cliTfDischargePlace.getText()
								);
					} else if (cMode == ScreenMode.EDITION) {
						presenter.updateClient(
								(Client) cliGrid.getGrid().getSelectedItem(),
								cliTfName.getText(),
								cliTfCode.getText(),
								cliTfAddr.getText(),
								cliTfPhone.getText(),
								cliTfFax.getText(),
								cliTfVat.getText(),
								cliTfDischargePlace.getText()
								);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		cliBtnCancel = new JButton("Cancelar");
		cliBtnCancel.setVisible(false);
		cliBtnCancel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					clean();
					enableClientFields(false);
					
					cliBtnSave.setVisible(false);
					cliBtnCancel.setVisible(false);
					// re-enable selection
					cliGrid.getGrid().setEnabled(true);
					
					// reset the values
					Client client = (Client) cliGrid.getGrid().getSelectedItem();
					if (client != null) {
						cliTfId.setText(String.valueOf(client.getId()));
						cliTfName.setText(client.getName());
						cliTfCode.setText(client.getCode());
						cliTfAddr.setText(client.getAddress());
						cliTfPhone.setText(client.getPhone());
						cliTfFax.setText(client.getFax());
						cliTfVat.setText(client.getVatNumber());
						cliTfDischargePlace.setText(client.getDefaultDischargePlace());
						setClientButtonsStatus(true, true, true);
					} else {
						setClientButtonsStatus(true, false, false);
					}
					
					cMode = ScreenMode.DEFAULT;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		bottomButtonContainer.add(cliBtnSave);
		bottomButtonContainer.add(cliBtnCancel);
		
		// form
		JPanel frm = new JPanel(new FormLayout(
				// columns
				"pref, 10px, pref:grow",
				// rows
				"50px, pref, pref, pref, pref, pref, pref, pref, pref, 5px, pref"));
		frm.setBorder(Borders.DIALOG_BORDER);
		CellConstraints frmConst = new CellConstraints();
		frm.add(topButtonContainer, frmConst.xywh(1, 1, 3, 1, CellConstraints.CENTER, CellConstraints.CENTER));
		buildFormItem(frm, 2, frmConst, "N\u00BA", (cliTfId = createTextField(true, false)));
		buildFormItem(frm, 3, frmConst, "Nome", (cliTfName = createTextField(false, false)));
		buildFormItem(frm, 4, frmConst, "C\u00F3digo", (cliTfCode = createTextField(false, false)));
		buildFormItem(frm, 5, frmConst, "NIF", (cliTfVat = createTextField(true, false)));
		buildFormItem(frm, 6, frmConst, "Endere\u00E7o", (cliTfAddr = createTextField(false, false)));
		buildFormItem(frm, 7, frmConst, "Telefone", (cliTfPhone = createTextField(true, false)));
		buildFormItem(frm, 8, frmConst, "Fax", (cliTfFax = createTextField(true, false)));
		buildFormItem(frm, 9, frmConst, "Local descarga", (cliTfDischargePlace = createTextField(false, false)));
		frm.add(bottomButtonContainer, frmConst.xy(3, 11, CellConstraints.RIGHT, CellConstraints.CENTER));
		
		// build the container
		JPanel container = new JPanel(new FormLayout(
				"0:grow(0.42), 20px, 0:grow(0.58)",
				"fill:0:grow"));
		container.setBorder(Borders.createEmptyBorder("5,15,5,15"));
		CellConstraints cc = new CellConstraints();
		container.add(cliGrid, cc.xy(1, 1));
		container.add(frm, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.TOP));
		return container;
	}
	
	/**
	 * Removes the given client from the listing
	 * 
	 * @param client
	 *        The client to be removed
	 */
	public void removeClient(Client client) {
		cliGrid.getGrid().getModel().deleteItem(client);
		
		// invalidate the AC for the sales
		if (salesAcClient != null) {
			salesAcClient.clearDataSource();
		}
	}
	
	
	/**
	 * Adds the given client to the listing
	 * 
	 * @param client
	 *        The client to be added
	 */
	public void addClient(Client client) {
		cliGrid.getGrid().getModel().addItem(client);
	}
	
	/**
	 * Updates the given client
	 * 
	 * @param client
	 *        The client to be updated
	 */
	public void updateClient(Client client) {
		cliGrid.getGrid().getModel().updateItem(client);
	}
	
	/**
	 * Marks the save/update client operation as finished
	 * 
	 * @param updated
	 *        The update flag (marks an update rather than a save)
	 */
	public void clientSaved(boolean updated) {
		if (!updated) {
			int idx = cliGrid.getGrid().getModel().getDataSource().size() - 1;
			cliGrid.getGrid().getSelectionModel().setSelectionInterval(idx, idx);
		}
		
		setClientButtonsStatus(true, true, true);
		enableClientFields(false);
		
		cliBtnSave.setVisible(false);
		cliBtnCancel.setVisible(false);
		cMode = ScreenMode.DEFAULT;
		
		// re-enable selection
		cliGrid.getGrid().setEnabled(true);
		
		// invalidate the AC for the sales
		if (salesAcClient != null) {
			salesAcClient.clearDataSource();
		}
	}
	
	private void enableClientFields(boolean status) {
		cliTfName.setEnabled(status);
		cliTfCode.setEnabled(status);
		cliTfAddr.setEnabled(status);
		cliTfPhone.setEnabled(status);
		cliTfFax.setEnabled(status);
		cliTfVat.setEnabled(status);
		cliTfDischargePlace.setEnabled(status);
	}
	
	private void setClientButtonsStatus(boolean insert, boolean edit, boolean delete) {
		cliBtnInsert.setEnabled(insert);
		cliBtnEdit.setEnabled(edit);
		cliBtnDelete.setEnabled(edit);
	}
	
	// ###########
	//  /Clients
	// ########### 
	
	// ##############
	// Configuration
	// ##############
	
	/**
	 * Builds the configuration panel contents
	 * 
	 * @return The contents
	 */
	private JComponent buildConfigContents() {
		// bottom buttons (save/cancel)
		JPanel emailButtonContainer = new JPanel();
		cfgEmailBtnSave = new JButton("Gravar");
		cfgEmailBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.saveConfiguration(
							cfgTfAuthor.getText(),
							cfgTfSourceEmail.getText(),
							String.valueOf(cfgTfEmailPwd.getPassword()),
							cfgTfTargetEmail.getText(),
							cfgTfSubject.getText(),
							cfgTfBody.getText()
							);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		cfgEmailBtnCancel = new JButton("Anular");
		cfgEmailBtnCancel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.resetEmailConfiguration();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		emailButtonContainer.add(cfgEmailBtnSave);
		emailButtonContainer.add(cfgEmailBtnCancel);
		
		// form e-mail configuration
		JPanel frmEmail = new JPanel(new FormLayout(
				// columns
				"pref, 10px, pref:grow",
				// rows
				"pref, pref, pref, pref, pref, pref, 5px, pref"));
		frmEmail.setBorder(BorderFactory.createTitledBorder("Configura\u00E7\u00F5es de e-mail"));
		CellConstraints frmConst = new CellConstraints();
		buildFormItem(frmEmail, 1, frmConst, "Nome", (cfgTfAuthor = createTextField(false, false, true)));
		cfgTfAuthor.setColumns(21);
		buildFormItem(frmEmail, 2, frmConst, "Endere\u00E7o Fonte", (cfgTfSourceEmail = createTextField(false, false, true)));
		cfgTfSourceEmail.setColumns(21);
		buildFormItem(frmEmail, 3, frmConst, "Password", (cfgTfEmailPwd = createPasswordField()));
		cfgTfEmailPwd.setColumns(21);
		buildFormItem(frmEmail, 4, frmConst, "Endere\u00E7o Destino", (cfgTfTargetEmail = createTextField(false, false, true)));
		cfgTfTargetEmail.setColumns(21);
		buildFormItem(frmEmail, 5, frmConst, "Assunto", (cfgTfSubject = createTextField(false, false, true)));
		cfgTfSubject.setColumns(21);
		buildFormItem(frmEmail, 6, frmConst, "Mensagem", (cfgTfBody = createTextField(false, false, true)));
		cfgTfBody.setColumns(21);
		frmEmail.add(emailButtonContainer, frmConst.xy(3, 8, CellConstraints.RIGHT, CellConstraints.BOTTOM));
		
		// form password changer
		cfgPwdBtnSave = new JButton("Alterar");
		cfgPwdBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.changePassword(
							cfgTfOldPwd.getPassword(),
							cfgTfNewPwd.getPassword(),
							cfgTfNewPwdConfirmation.getPassword()
							);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		JPanel pwdButtonContainer = new JPanel();
		pwdButtonContainer.add(cfgPwdBtnSave);
		
		JPanel frmPwd = new JPanel(new FormLayout(
				// columns
				"pref, 10px, pref:grow",
				// rows
				"pref, pref, pref, 5px, pref"));
		frmPwd.setBorder(BorderFactory.createTitledBorder("Altera\u00E7\u00E3o de password"));
		frmConst = new CellConstraints();
		buildFormItem(frmPwd, 1, frmConst, "Password actual", (cfgTfOldPwd = createPasswordField()));
		cfgTfOldPwd.setColumns(38);
		buildFormItem(frmPwd, 2, frmConst, "Nova password", (cfgTfNewPwd = createPasswordField()));
		cfgTfNewPwd.setColumns(38);
		buildFormItem(frmPwd, 3, frmConst, "Nova passsword (confirma\u00E7\u00E3o)", (cfgTfNewPwdConfirmation = createPasswordField()));
		cfgTfNewPwdConfirmation.setColumns(38);
		frmPwd.add(pwdButtonContainer, frmConst.xy(3, 5, CellConstraints.RIGHT, CellConstraints.BOTTOM));
		
		// bottom buttons (save/cancel)
		JPanel saleResumeButtonContainer = new JPanel();
		cfgSaleResumeBtnSave = new JButton("Gravar");
		cfgSaleResumeBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.saveSaleResumeConfiguration(
							cfgCbProductCode.isSelected(),
							cfgCbProductDesc.isSelected(),
							cfgCbProductQty.isSelected(),
							cfgCbProductPrice.isSelected(),
							cfgCbDiscount.isSelected(),
							cfgCbVat.isSelected(),
							cfgCbTotals.isSelected()
							);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		cfgSaleResumeBtnCancel = new JButton("Anular");
		cfgSaleResumeBtnCancel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.resetSaleResumeConfiguration();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		saleResumeButtonContainer.add(cfgSaleResumeBtnSave);
		saleResumeButtonContainer.add(cfgSaleResumeBtnCancel);
		
		// form e-mail configuration
		JPanel frmSaleResume = new JPanel(new FormLayout(
				// columns
				"150px, 150px",
				// rows
				"pref, pref, pref, pref, 5px, pref"));
		frmSaleResume.setBorder(BorderFactory.createTitledBorder("Campos do resumo de vendas"));
		frmConst = new CellConstraints();
		frmSaleResume.add((cfgCbProductCode = new JCheckBox("C\u00F3digo artigo")), frmConst.xy(1, 1));
		frmSaleResume.add((cfgCbProductDesc = new JCheckBox("Descri\u00E7\u00E3o artigo")), frmConst.xy(2, 1));
		frmSaleResume.add((cfgCbProductQty = new JCheckBox("Quant. artigo")), frmConst.xy(1, 2));
		frmSaleResume.add((cfgCbProductPrice = new JCheckBox("Pre\u00E7o artigo")), frmConst.xy(2, 2));
		frmSaleResume.add((cfgCbDiscount = new JCheckBox("Desconto")), frmConst.xy(1, 3));
		frmSaleResume.add((cfgCbVat = new JCheckBox("IVA")), frmConst.xy(2, 3));
		frmSaleResume.add((cfgCbTotals = new JCheckBox("Totais")), frmConst.xy(1, 4));
		frmSaleResume.add(saleResumeButtonContainer, frmConst.xyw(1, 6, 2, CellConstraints.RIGHT, CellConstraints.BOTTOM));
		
		
		// build the container
		JPanel container = new JPanel(new FormLayout(
				"0:grow(0.50), 0:grow(0.50)",
				"fill:pref, fill:pref"));
		container.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		CellConstraints cc = new CellConstraints();
		container.add(frmEmail, cc.xy(1, 1));
		container.add(frmSaleResume, cc.xy(2, 1));
		container.add(frmPwd, cc.xyw(1, 2, 2));
		
		// loads the configurations
		presenter.loadUserConfiguration();
		
		return container;
	}
	
	/**
	 * Confirms the password change
	 */
	public void passwordChanged() {
		cfgTfOldPwd.setText("");
		cfgTfNewPwd.setText("");
		cfgTfNewPwdConfirmation.setText("");
	}
	
	
	/**
	 * Sets the configuration values
	 * 
	 * @param author
	 *        The e-mail author
	 * @param subject
	 *        The subject value
	 * @param body
	 *        The body value
	 */
	public void setUserEmailConfigurationValues(Configuration author,
			Configuration sourceEmail, Configuration sourceEmailPassword,
			Configuration targetEmail, Configuration subject, Configuration body) {
		cfgTfAuthor.setText(author.getValue());
		cfgTfAuthor.setToolTipText(author.getDescription());
		cfgTfSourceEmail.setText(sourceEmail.getValue());
		cfgTfSourceEmail.setToolTipText(sourceEmail.getDescription());
		cfgTfEmailPwd.setText(sourceEmailPassword.getValue());
		cfgTfEmailPwd.setToolTipText(sourceEmailPassword.getDescription());
		cfgTfTargetEmail.setText(targetEmail.getValue());
		cfgTfTargetEmail.setToolTipText(targetEmail.getDescription());
		cfgTfSubject.setText(subject.getValue());
		cfgTfSubject.setToolTipText(subject.getDescription());
		cfgTfBody.setText(body.getValue());
		cfgTfBody.setToolTipText(body.getDescription());
		
	}
	
	/**
	 * Sets the configuration values
	 * 
	 * @param prodCodeIncluded
	 * @param prodDescIncluded
	 * @param prodQtyIncluded
	 * @param prodPriceIncluded
	 * @param discountIncluded
	 * @param vatIncluded
	 * @param totalsIncluded
	 */
	public void setUserSaleResumeConfigurationValues(boolean prodCodeIncluded, boolean prodDescIncluded,
			boolean prodQtyIncluded, boolean prodPriceIncluded, boolean discountIncluded,
			boolean vatIncluded, boolean totalsIncluded) {
		cfgCbProductCode.setSelected(prodCodeIncluded);
		cfgCbProductDesc.setSelected(prodDescIncluded);
		cfgCbProductQty.setSelected(prodQtyIncluded);
		cfgCbProductPrice.setSelected(prodPriceIncluded);
		cfgCbDiscount.setSelected(discountIncluded);
		cfgCbVat.setSelected(vatIncluded);
		cfgCbTotals.setSelected(totalsIncluded);
	}
	
	/**
	 * Sets the configuration values
	 * 
	 * @param author
	 *        The e-mail author
	 * @param subject
	 *        The subject value
	 * @param body
	 *        The body value
	 */
	public void setUserConfigurationValues(Configuration author,
			Configuration sourceEmail, Configuration sourceEmailPassword,
			Configuration targetEmail, Configuration subject, Configuration body,
			boolean prodCodeIncluded, boolean prodDescIncluded,
			boolean prodQtyIncluded, boolean prodPriceIncluded, boolean discountIncluded,
			boolean vatIncluded, boolean totalsIncluded) {
		setUserEmailConfigurationValues(author, sourceEmail, sourceEmailPassword, targetEmail,
				subject, body);
		setUserSaleResumeConfigurationValues(prodCodeIncluded, prodDescIncluded, prodQtyIncluded,
				prodPriceIncluded, discountIncluded, vatIncluded, totalsIncluded);
	}
	
	// ##########
	// Products 
	// ##########
	
	
	/**
	 * Builds the products panel contents
	 * 
	 * @return The contents
	 */
	private JComponent buildProductsContents() {
		// grid
		DSDataGridColumn<Product, String> codeCol = new DSDataGridColumn<Product, String>("C\u00F3digo", 30) {
			
			private static final long serialVersionUID = -7892787979832200709L;
			
			@Override
			public String getValue(Product object) {
				return object.getCode();
			}
			
			@Override
			public void setValue(Product object, String value) {
			}
		};
		DSDataGridColumn<Product, String> descriptionCol = new DSDataGridColumn<Product, String>("Descri\u00E7\u00E3o", 70) {
			
			private static final long serialVersionUID = 8176762744929998035L;
			
			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
			
			@Override
			public void setValue(Product object, String value) {
			}
		};
		
		prodGrid = new DSDataGridPane();
		ArrayList<DSDataGridColumn<Product, ?>> cols = new ArrayList<DSDataGridColumn<Product, ?>>();
		cols.add(codeCol);
		cols.add(descriptionCol);
		prodGrid.getGrid().setColumns(cols);
		prodGrid.getGrid().setDataSource(presenter.getProducts());
		prodGrid.getGrid().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Product product = (Product) prodGrid.getGrid().getSelectedItem();
				clean();
				if (product != null) {
					prodTfId.setText(String.valueOf(product.getId()));
					prodTfDescription.setText(product.getDescription());
					prodTfCode.setText(product.getCode());
					if (product.getPrice() != null) {
						prodTfPrice.setText(String.format("%.2f", product.getPrice()));
					}
					prodTfDefaultAmount.setText(product.getDefaultAmount() == null ? "" : String.valueOf(product.getDefaultAmount()));
					if (product.getDefaultUnit() != null) {
						switch (product.getDefaultUnit()) {
							case G:
								prodBgUnits.setSelected(prodRbGram.getModel(), true);
								break;
							case KG:
								prodBgUnits.setSelected(prodRbKiloGram.getModel(), true);
								break;
						}
					}
					
					setProductButtonsStatus(true, true, true);
				} else {
					setProductButtonsStatus(true, false, false);
				}
			}
		});
		
		// top buttons
		JPanel topButtonContainer = new JPanel();
		prodBtnInsert = createButton(ImagesEnum.BTN_INSERT, true);
		prodBtnInsert.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				clean();
				setProductButtonsStatus(false, false, false);
				enableProductFields(true);
				
				prodBtnSave.setVisible(true);
				prodBtnCancel.setVisible(true);
				
				// disable selection
				prodGrid.getGrid().setEnabled(false);
				
				prodTfDescription.requestFocus();
				
				cMode = ScreenMode.INSERT;
			}
		});
		prodBtnEdit = createButton(ImagesEnum.BTN_EDIT, false);
		prodBtnEdit.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				Product product = (Product) prodGrid.getGrid().getSelectedItem();
				
				if (product != null) {
					enableProductFields(true);
					setProductButtonsStatus(false, false, false);
					
					prodBtnSave.setVisible(true);
					prodBtnCancel.setVisible(true);
					
					// disable selection
					prodGrid.getGrid().setEnabled(false);
					
					prodTfDescription.requestFocus();
					
					cMode = ScreenMode.EDITION;
				}
			}
		});
		prodBtnDelete = createButton(ImagesEnum.BTN_DELETE, false);
		prodBtnDelete.addMouseClickListener(new MouseClickEventListener() {
			
			@Override
			public void onMouseClick(MouseClickEvent evt) {
				Product product = (Product) prodGrid.getGrid().getSelectedItem();
				
				if (product != null) {
					if (DSDialog.question(BusinessManagerView.this, "Deseja realmente remover o artigo: "
							+ product.getCode(), "Confirma\u00E7\u00E3o")) {
						presenter.removeProduct(product);
					}
				}
			}
		});
		topButtonContainer.add(prodBtnInsert);
		topButtonContainer.add(prodBtnEdit);
		topButtonContainer.add(prodBtnDelete);
		
		
		// bottom buttons (save/cancel)
		JPanel bottomButtonContainer = new JPanel();
		prodBtnSave = new JButton("Gravar");
		prodBtnSave.setVisible(false);
		prodBtnSave.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					ProductUnit pUnit = null;
					if (prodBgUnits.getSelection() != null) {
						if (prodBgUnits.getSelection() == prodRbGram.getModel()) {
							pUnit = ProductUnit.G;
						} else if (prodBgUnits.getSelection() == prodRbKiloGram.getModel()) {
							pUnit = ProductUnit.KG;
						}
					}
					if (cMode == ScreenMode.INSERT) {
						presenter.saveProduct(
								prodTfDescription.getText(),
								prodTfCode.getText(),
								prodTfPrice.getText(),
								prodTfDefaultAmount.getText(),
								pUnit
								);
					} else if (cMode == ScreenMode.EDITION) {
						presenter.updateProduct(
								(Product) prodGrid.getGrid().getSelectedItem(),
								prodTfDescription.getText(),
								prodTfCode.getText(),
								prodTfPrice.getText(),
								prodTfDefaultAmount.getText(),
								pUnit
								);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		prodBtnCancel = new JButton("Cancelar");
		prodBtnCancel.setVisible(false);
		prodBtnCancel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					clean();
					enableProductFields(false);
					
					prodBtnSave.setVisible(false);
					prodBtnCancel.setVisible(false);
					// re-enable selection
					prodGrid.getGrid().setEnabled(true);
					
					// reset the values
					Product product = (Product) prodGrid.getGrid().getSelectedItem();
					if (product != null) {
						prodTfId.setText(String.valueOf(product.getId()));
						prodTfDescription.setText(product.getDescription());
						prodTfCode.setText(product.getCode());
						if (product.getPrice() != null) {
							prodTfPrice.setText(String.format("%.2f", product.getPrice()));
						}
						prodTfDefaultAmount.setText(product.getDefaultAmount() == null ? "" : String.valueOf(product.getDefaultAmount()));
						if (product.getDefaultUnit() != null) {
							switch (product.getDefaultUnit()) {
								case G:
									prodBgUnits.setSelected(prodRbGram.getModel(), true);
									break;
								case KG:
									prodBgUnits.setSelected(prodRbKiloGram.getModel(), true);
									break;
							}
						}
						setProductButtonsStatus(true, true, true);
					} else {
						setProductButtonsStatus(true, false, false);
					}
					
					cMode = ScreenMode.DEFAULT;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		bottomButtonContainer.add(prodBtnSave);
		bottomButtonContainer.add(prodBtnCancel);
		
		// form
		JPanel frm = new JPanel(new FormLayout(
				// columns
				"pref, 10px, pref:grow",
				// rows
				"50px, pref, pref, pref, pref, pref, pref, 5px, pref"));
		frm.setBorder(Borders.DIALOG_BORDER);
		CellConstraints frmConst = new CellConstraints();
		frm.add(topButtonContainer, frmConst.xywh(1, 1, 3, 1, CellConstraints.CENTER, CellConstraints.CENTER));
		buildFormItem(frm, 2, frmConst, "N\u00BA", (prodTfId = createTextField(true, false)));
		buildFormItem(frm, 3, frmConst, "Descri\u00E7\u00E3o", (prodTfDescription = createTextField(false, false)));
		buildFormItem(frm, 4, frmConst, "C\u00F3digo", (prodTfCode = createTextField(false, false)));
		buildFormItem(frm, 5, frmConst, "Pre\u00E7o (\u20ac)", (prodTfPrice = createTextField(true, true)));
		buildFormItem(frm, 6, frmConst, "Qtd defeito", (prodTfDefaultAmount = createTextField(true, true)));
		prodBgUnits = new ButtonGroup();
		//Create the radio buttons.
		prodRbGram = new JRadioButton("Grama (g)");
		prodRbKiloGram = new JRadioButton("Kilograma (kg)");
		prodRbNone = new JRadioButton("Nenhum");
		prodBgUnits.add(prodRbNone);
		prodBgUnits.add(prodRbGram);
		prodBgUnits.add(prodRbKiloGram);
		prodUnitPanel = new JPanel(new GridLayout(0, 1));
		prodUnitPanel.add(prodRbNone);
		prodUnitPanel.add(prodRbGram);
		prodUnitPanel.add(prodRbKiloGram);
		prodRbNone.setEnabled(false);
		prodRbGram.setEnabled(false);
		prodRbKiloGram.setEnabled(false);
		buildFormItem(frm, 7, frmConst, "Unidade defeito", prodUnitPanel);
		frm.add(bottomButtonContainer, frmConst.xy(3, 9, CellConstraints.RIGHT, CellConstraints.CENTER));
		
		// build the container
		JPanel container = new JPanel(new FormLayout(
				"0:grow(0.42), 20px, 0:grow(0.58)",
				"fill:0:grow"));
		container.setBorder(Borders.createEmptyBorder("5,15,5,15"));
		CellConstraints cc = new CellConstraints();
		container.add(prodGrid, cc.xy(1, 1));
		container.add(frm, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.TOP));
		return container;
	}
	
	/**
	 * Removes the given product from the listing
	 * 
	 * @param product
	 *        The product to be removed
	 */
	public void removeProduct(Product product) {
		prodGrid.getGrid().getModel().deleteItem(product);
	}
	
	
	/**
	 * Adds the given product to the listing
	 * 
	 * @param product
	 *        The product to be added
	 */
	public void addProduct(Product product) {
		prodGrid.getGrid().getModel().addItem(product);
	}
	
	/**
	 * Updates the given product
	 * 
	 * @param product
	 *        The product to be updated
	 */
	public void updateProduct(Product product) {
		prodGrid.getGrid().getModel().updateItem(product);
	}
	
	/**
	 * Marks the save/update product operation as finished
	 * 
	 * @param updated
	 *        The update flag (marks an update rather than a save)
	 */
	public void productSaved(boolean updated) {
		if (!updated) {
			int idx = prodGrid.getGrid().getModel().getDataSource().size() - 1;
			prodGrid.getGrid().getSelectionModel().setSelectionInterval(idx, idx);
		}
		
		setProductButtonsStatus(true, true, true);
		enableProductFields(false);
		
		prodBtnSave.setVisible(false);
		prodBtnCancel.setVisible(false);
		cMode = ScreenMode.DEFAULT;
		
		// re-enable selection
		prodGrid.getGrid().setEnabled(true);
		
		// invalidate the AC for the sales
		if (salesAcProduct != null) {
			salesAcProduct.clearDataSource();
		}
	}
	
	private void enableProductFields(boolean status) {
		prodTfDescription.setEnabled(status);
		prodTfCode.setEnabled(status);
		prodTfPrice.setEnabled(status);
		prodTfDefaultAmount.setEnabled(status);
		prodRbNone.setEnabled(status);
		prodRbGram.setEnabled(status);
		prodRbKiloGram.setEnabled(status);
		
	}
	
	private void setProductButtonsStatus(boolean insert, boolean edit, boolean delete) {
		prodBtnInsert.setEnabled(insert);
		prodBtnEdit.setEnabled(edit);
		prodBtnDelete.setEnabled(edit);
	}
	
	// ###########
	//  /Products
	// ########### 
	
	// builds the form item
	private void buildFormItem(JPanel container, int idx, CellConstraints cc, String label, Component comp) {
		container.add(new JLabel(label), cc.xy(1, idx, CellConstraints.RIGHT, CellConstraints.CENTER));
		container.add(comp, cc.xy(3, idx, CellConstraints.LEFT, CellConstraints.TOP));
	}
	
	// creates a text field
	private DSTextField createTextField(boolean numeric, boolean decimal) {
		return createTextField(numeric, decimal, false);
	}
	
	private DSTextField createTextField(boolean numeric, boolean decimal, boolean status) {
		DSTextField tf = new DSTextField(25);
		tf.setEnabled(status);
		if (numeric) {
			if (decimal) {
				tf.setAllowedChars("[\\d\\.]");
			} else {
				tf.setAllowedChars("\\d");
			}
		}
		
		return tf;
	}
	
	private DSPasswordField createPasswordField() {
		DSPasswordField pf = new DSPasswordField();
		pf.setColumns(25);
		return pf;
	}
	
	// creates a button
	private DSLabelButton createButton(ImagesEnum img, boolean status) {
		DSLabelButton btn = new DSLabelButton(new ImageIcon(img.getPath())) {
			
			private static final long serialVersionUID = 2226771909756894981L;
			
			/* (non-Javadoc)
			 * @see javax.swing.JComponent#setEnabled(boolean)
			 */
			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (enabled) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		};
		btn.setEnabled(status);
		return btn;
	}
	
	/**
	 * Adds the given product to the current sale
	 * 
	 * @param product
	 *        The product to be added
	 */
	public void addSaleProduct(SaleProduct product) {
		salesProGrid.getGrid().getModel().addItem(product);
	}
	
	/**
	 * Refreshes the sales products
	 */
	public void refreshSaleProducts() {
		salesProGrid.getGrid().getModel().refreshListedItems();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#clean()
	 */
	@Override
	public void clean() {
		if (menuPanel.getSelectedItem() == billingMenu) {
			salesTfObservations.setText("");
			salesTfPaymentConditions.setText("");
			salesTfDischargePlace.setText("");
			salesCbCardex.setSelected(false);
			salesAcClient.clearSelectedItems();
			salesAcProduct.clearSelectedItems();
			try {
				salesDpDocDate.setDate(null);
			} catch (PropertyVetoException e) {
			}
			salesProGrid.getGrid().getModel().clear();
		} else if (menuPanel.getSelectedItem() == clientsMenu) {
			cliTfId.setText("");
			cliTfName.setText("");
			cliTfCode.setText("");
			cliTfAddr.setText("");
			cliTfPhone.setText("");
			cliTfFax.setText("");
			cliTfVat.setText("");
			cliTfDischargePlace.setText("");
		} else if (menuPanel.getSelectedItem() == productsMenu) {
			prodTfId.setText("");
			prodTfDescription.setText("");
			prodTfCode.setText("");
			prodTfPrice.setText("");
			prodTfDefaultAmount.setText("");
			prodBgUnits.setSelected(prodRbNone.getModel(), true);
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.view.BaseView#onShow()
	 */
	@Override
	public void onShow() {
		billingMenu.setSelected(true);
	}
	
	
	/**
	 * Sets the logged user information
	 * 
	 * @param name
	 *        The user name
	 * @param permission
	 *        The user permission
	 */
	public void setLoggedUser(String name, UserPermission permission) {
		setTitle(getTitle() + " - " + name);
		switch (permission) {
			case USER:
				break;
			case ADMIN:
				setTitle(getTitle() + " * ");
				break;
		}
		authUserLabel.setText(authUserLabel.getText() + " " + name);
	}
	
}
