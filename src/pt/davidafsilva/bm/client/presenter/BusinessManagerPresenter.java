package pt.davidafsilva.bm.client.presenter;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import pt.davidafsilva.bm.client.Application;
import pt.davidafsilva.bm.client.ui.DSDialog;
import pt.davidafsilva.bm.client.view.BusinessManagerView;
import pt.davidafsilva.bm.server.service.interfaces.IClientService;
import pt.davidafsilva.bm.server.service.interfaces.IConfigurationService;
import pt.davidafsilva.bm.server.service.interfaces.IProductService;
import pt.davidafsilva.bm.server.service.interfaces.ISaleService;
import pt.davidafsilva.bm.server.service.interfaces.IUserService;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.SaleProduct;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.domain.UserPermission;
import pt.davidafsilva.bm.shared.enums.ConfigurationKey;
import pt.davidafsilva.bm.shared.enums.ProductUnit;
import pt.davidafsilva.bm.shared.exception.ApplicationException;
import pt.davidafsilva.bm.shared.utils.FileUtil;


/**
 * BusinessManagerPresenter.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:27:59 AM
 */
public class BusinessManagerPresenter extends BasePresenter<BusinessManagerView> {
	
	/**
	 * The client service
	 */
	private IClientService clientService = IClientService.Util.getInstance();
	
	/**
	 * The product service
	 */
	private IProductService productService = IProductService.Util.getInstance();
	
	/**
	 * The sale service
	 */
	private final ISaleService saleService = ISaleService.Util.getInstance();
	
	/**
	 * The user service
	 */
	private final IUserService userService = IUserService.Util.getInstance();
	
	/**
	 * The configuration service
	 */
	private final IConfigurationService configService = IConfigurationService.Util.getInstance();
	
	/**
	 * The coming from logout flag
	 */
	private boolean comingFromLogout;
	
	/**
	 * The coming from shutdown flag
	 */
	private boolean comingFromShutdown;
	
	
	/**
	 * The list of clients
	 */
	private List<Client> clients = null;
	
	/**
	 * The list of products
	 */
	private List<Product> products = null;
	
	/**
	 * The map of the configuration
	 */
	private Map<String, Configuration> configurations;
	
	//---------
	// Sale
	//---------
	
	/**
	 * The current sale
	 */
	private Sale sale;
	
	
	/**
	 * Instantiates the BM presenter
	 * 
	 * @param view
	 *        The BM view
	 */
	public BusinessManagerPresenter(BusinessManagerView view) {
		super(view);
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#show()
	 */
	@Override
	public void show() {
		User authUser = Application.get().getAuthenticatedUser();
		getView().setLoggedUser(authUser.getName(), UserPermission.fromCode(authUser.getPermission()));
		super.show();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#destroy()
	 */
	@Override
	public void destroy() {
		if (clients != null) {
			clients.clear();
		}
		clients = null;
		if (products != null) {
			products.clear();
		}
		products = null;
		clientService = null;
		productService = null;
		super.destroy();
	}
	
	/**
	 * Logs out from the application
	 */
	public synchronized void logout() {
		comingFromLogout = true;
		Application.get().logout();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#close()
	 */
	@Override
	public synchronized void close() {
		if (comingFromLogout) {
			comingFromLogout = false;
			super.close();
		} else {
			if (comingFromShutdown) {
				super.close();
			} else {
				comingFromShutdown = true;
				Application.get().shutdown();
			}
		}
	}
	
	// #####################
	// Clients
	// #####################
	
	/**
	 * Gets the list of clients
	 * 
	 * @return The clients
	 */
	public List<Client> getClients() {
		if (clients == null) {
			clients = clientService.getAll();
		}
		return clients;
	}
	
	/**
	 * Removes the given client from the database.
	 * 
	 * @param client
	 *        The client to remove
	 */
	public void removeClient(Client client) {
		try {
			clientService.delete(client);
			view.removeClient(client);
		} catch (ApplicationException e) {
			DSDialog.error(view, "N\u00E3o foi poss\u00EDvel remover o cliente por este " +
					"j\u00E1 se encontrar associado a pelo menos uma venda.", "Remo\u00E7\u00E3o de cliente");
		}
	}
	
	/**
	 * Saves a new client
	 * 
	 * @param name
	 *        The name
	 * @param code
	 *        The code
	 * @param address
	 *        The address
	 * @param phone
	 *        The phone
	 * @param fax
	 *        The fax number
	 * @param vatNumber
	 *        The vat number
	 * @param defaultDischargePlace
	 *        The default discharge place
	 */
	public void saveCient(String name, String code, String address, String phone, String fax,
			String vatNumber, String defaultDischargePlace) {
		if (!validateClient(name, code, address, phone, fax, vatNumber, defaultDischargePlace)) {
			return;
		}
		
		// create the client
		Client client = new Client();
		client.setName(name.trim());
		client.setCode(code.trim());
		client.setVatNumber(vatNumber.trim());
		client.setAddress(address == null || address.trim().length() == 0 ? null : address.trim());
		client.setPhone(phone == null || phone.trim().length() == 0 ? null : phone.trim());
		client.setFax(fax == null || fax.trim().length() == 0 ? null : fax.trim());
		client.setDefaultDischargePlace(defaultDischargePlace == null || defaultDischargePlace.trim().length() == 0 ? null : defaultDischargePlace.trim());
		
		// disable the view
		view.setEnabled(false);
		
		try {
			clientService.save(client);
			
			view.addClient(client);
			view.clientSaved(false);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Erro ao gravar cliente");
		}
		view.setEnabled(true);
	}
	
	/**
	 * Updates the given client
	 * 
	 * @param client
	 *        The client to be updated
	 * @param name
	 *        The name
	 * @param code
	 *        The code
	 * @param address
	 *        The address
	 * @param phone
	 *        The phone
	 * @param fax
	 *        The fax
	 * @param vatNumber
	 *        The vat number
	 * @param defaultDischargePlace
	 *        The default discharge place
	 */
	public void updateClient(Client client, String name, String code, String address,
			String phone, String fax, String vatNumber, String defaultDischargePlace) {
		if (!validateClient(name, code, address, phone, fax, vatNumber, defaultDischargePlace)) {
			return;
		}
		
		// create the client
		Client newClient = (Client) client.clone();
		newClient.setName(name.trim());
		newClient.setCode(code.trim());
		newClient.setVatNumber(vatNumber.trim());
		newClient.setAddress(address == null || address.trim().length() == 0 ? null : address.trim());
		newClient.setPhone(phone == null || phone.trim().length() == 0 ? null : phone.trim());
		newClient.setFax(fax == null || fax.trim().length() == 0 ? null : fax.trim());
		newClient.setDefaultDischargePlace(defaultDischargePlace == null || defaultDischargePlace.trim().length() == 0 ? null : defaultDischargePlace.trim());
		
		// disable the view
		view.setEnabled(false);
		try {
			clientService.update(newClient);
			
			view.updateClient(newClient);
			view.clientSaved(true);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Erro ao actualizar cliente");
		}
		view.setEnabled(true);
	}
	
	/**
	 * Validates the client
	 * 
	 * @param name
	 *        The name
	 * @param code
	 *        The code
	 * @param address
	 *        The address
	 * @param phone
	 *        The phone
	 * @param fax
	 *        The fax
	 * @param vatNumber
	 *        The vat number
	 * @param defaultDischargePlace
	 *        The default discharge place
	 */
	private boolean validateClient(String name, String code, String address,
			String phone, String fax, String vatNumber, String defaultDischargePlace) {
		// name validation
		if (name == null || name.trim().length() == 0 || name.trim().length() > 100) {
			DSDialog.error(view, "Tem de especificar um nome v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// code validation
		if (code == null || code.trim().length() == 0 || code.trim().length() > 10) {
			DSDialog.error(view, "Tem de especificar um c\u00F3digo v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// vat number validation
		if (vatNumber == null || !isInteger(vatNumber.trim()) || vatNumber.trim().length() > 10) {
			DSDialog.error(view, "Tem de especificar um NIF v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// address validation
		if (address != null && address.trim().length() > 100) {
			DSDialog.error(view, "Tem de especificar um endere\u00E7o v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// phone validation
		if (phone != null && phone.trim().length() > 20) {
			DSDialog.error(view, "Tem de especificar um n\u00FAmero telef\u00F3nico v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// fax validation
		if (fax != null && fax.trim().length() > 20) {
			DSDialog.error(view, "Tem de especificar um n\u00FAmero de fax v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		// default discharge place
		if (defaultDischargePlace != null && defaultDischargePlace.trim().length() > 255) {
			DSDialog.error(view, "Tem de especificar um local de descarga v\u00E1lido.", "Novo cliente");
			return false;
		}
		
		return true;
	}
	
	// #####################
	// Products
	// #####################
	
	/**
	 * Gets the list of products
	 * 
	 * @return The products
	 */
	public List<Product> getProducts() {
		if (products == null) {
			products = productService.getAll();
		}
		return products;
	}
	
	/**
	 * Removes the given product from the database.
	 * 
	 * @param product
	 *        The product to remove
	 */
	public void removeProduct(Product product) {
		try {
			productService.delete(product);
			view.removeProduct(product);
		} catch (ApplicationException e) {
			DSDialog.error(view, "N\u00E3o foi poss\u00EDvel remover o artigo por este " +
					"j\u00E1 se encontrar associado a pelo menos uma venda.", "Remo\u00E7\u00E3o de artigo");
		}
	}
	
	/**
	 * Saves a new product
	 * 
	 * @param description
	 *        The description
	 * @param code
	 *        The code
	 * @param price
	 *        The price
	 * @param defaultAmount
	 *        The default amount
	 * @param defaultUnit
	 *        The default unit
	 */
	public void saveProduct(String description, String code, String price, String defaultAmount,
			ProductUnit defaultUnit) {
		if (!validateProduct(description, code, price, defaultAmount, defaultUnit)) {
			return;
		}
		
		// create the product
		Product product = new Product();
		product.setDescription(description.trim());
		product.setCode(code.trim());
		if (price != null && !price.trim().isEmpty()) {
			product.setPrice(Double.parseDouble(price.trim()));
		} else {
			product.setPrice(null);
		}
		product.setDefaultAmount(defaultAmount == null || defaultAmount.trim().length() == 0 ? null : Double.valueOf(new DecimalFormat("#.##").format(Double.valueOf(defaultAmount.trim())).replaceAll(",", ".")));
		product.setDefaultUnit(defaultUnit);
		
		// disable the view
		view.setEnabled(false);
		
		try {
			productService.save(product);
			view.addProduct(product);
			view.productSaved(false);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Erro ao gravar artigo");
		}
		view.setEnabled(true);
	}
	
	/**
	 * Updates the given product
	 * 
	 * @param product
	 *        The product to be updated
	 * @param description
	 *        The description
	 * @param code
	 *        The code
	 * @param price
	 *        The price
	 * @param defaultAmount
	 *        The default amount
	 * @param defaultUnit
	 *        The default unit
	 */
	public void updateProduct(Product product, String description, String code, String price,
			String defaultAmount, ProductUnit defaultUnit) {
		if (!validateProduct(description, code, price, defaultAmount, defaultUnit)) {
			return;
		}
		
		// create the product
		Product newProduct = (Product) product.clone();
		newProduct.setDescription(description.trim());
		newProduct.setCode(code.trim());
		if (price != null && !price.trim().isEmpty()) {
			newProduct.setPrice(Double.parseDouble(price.trim()));
		} else {
			newProduct.setPrice(null);
		}
		newProduct.setDefaultAmount(defaultAmount == null || defaultAmount.trim().length() == 0 ? null : Double.valueOf(new DecimalFormat("#.##").format(Double.valueOf(defaultAmount.trim())).replaceAll(",", ".")));
		newProduct.setDefaultUnit(defaultUnit);
		
		// disable the view
		view.setEnabled(false);
		try {
			productService.update(newProduct);
			view.updateProduct(newProduct);
			view.productSaved(true);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Erro ao actualizar artigo");
		}
		view.setEnabled(true);
	}
	
	/**
	 * Validates the product
	 * 
	 * @param code
	 *        The code
	 */
	private boolean validateProduct(String description, String code, String price, String defaultAmount, ProductUnit defaultUnit) {
		// name validation
		if (description == null || description.trim().length() == 0 || description.trim().length() > 150) {
			DSDialog.error(view, "Tem de especificar uma descri\u00E7\u00E3o v\u00E1lida.", "Novo artigo");
			return false;
		}
		
		// code validation
		if (code == null || code.trim().length() == 0 || code.trim().length() > 10) {
			DSDialog.error(view, "Tem de especificar um c\u00F3digo v\u00E1lido.", "Novo artigo");
			return false;
		}
		
		// price validation
		if (price != null && !price.trim().isEmpty() && (!isDouble(price.trim()) || Double.valueOf(price.trim()) < 0)) {
			DSDialog.error(view, "Tem de especificar um pre\u00E7o v\u00E1lido.", "Novo artigo");
			return false;
		}
		
		// default amount validation
		if (defaultAmount != null && defaultAmount.trim().length() > 0 && !isDouble(defaultAmount.trim())) {
			DSDialog.error(view, "Tem de especificar uma quantidade por defeito v\u00E1lida.", "Novo artigo");
			return false;
		}
		
		return true;
	}
	
	private boolean isDouble(String txt) {
		try {
			Double.parseDouble(txt);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private boolean isInteger(String txt) {
		try {
			Integer.parseInt(txt);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	// -------------
	// Sale search
	// -------------
	
	
	/**
	 * Search for sales with the given filters
	 * 
	 * @param client
	 *        The client
	 * @param initialDate
	 *        The initial date (interval)
	 * @param finalDate
	 *        The final date (interval)
	 */
	public void searchSales(Client client, Date initialDate, Date finalDate) {
		view.setSearchSalesResult(saleService.searchSales(Application.get().getAuthenticatedUser(),
				client, initialDate, finalDate));
	}
	
	// -------------
	// Sale 
	// -------------
	
	/**
	 * Sets the given sale by e-mail
	 * 
	 * @param _sale
	 *        The sale to be sent
	 */
	public void sendSaleByEmail(final Sale _sale, final boolean updateHistory) {
		final File report = getSaleReportFile(_sale, false);
		if (!report.exists()) {
			// needs to be generated
			try {
				saleService.generateReport(Application.get().getAuthenticatedUser(), report, _sale);
			} catch (ApplicationException e) {
				DSDialog.error(view, "Ocorreu um erro ao gerar o resumo da venda.", "Detalhe da venda");
				if (report.exists()) {
					report.delete();
				}
				return;
			}
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					boolean resend = _sale.wasSentByEmail();
					boolean proceed = true;
					if (resend) {
						proceed = DSDialog.question(view, "A venda que seleccionou j\u00E1 foi enviada anteriormente.\nPretende reenviar?", "Reenvio de venda");
					}
					
					if (proceed) {
						saleService.sendReport(Application.get().getAuthenticatedUser(), _sale, report);
						if (updateHistory) {
							view.updateSaleHistory(_sale);
						}
						
						if (resend) {
							DSDialog.info(view, "A venda foi reenviada com sucesso.", "Reenvio de venda");
						}
					}
				} catch (ApplicationException e) {
					DSDialog.error(view, e.getMessage(), "Erro ao enviar resumo da venda.");
				}
			}
		}).start();
	}
	
	/**
	 * Presents the detail of the given sale
	 * 
	 * @param _sale
	 *        The sale
	 */
	public void viewSaleDetail(Sale _sale) {
		final File report = getSaleReportFile(_sale, false);
		if (!report.exists()) {
			// needs to be generated
			try {
				saleService.generateReport(Application.get().getAuthenticatedUser(), report, _sale);
			} catch (ApplicationException e) {
				DSDialog.error(view, "Ocorreu um erro ao gerar o resumo da venda.", "Detalhe da venda");
				if (report.exists()) {
					report.delete();
				}
				return;
			}
		}
		
		boolean opened = false;
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(report);
				opened = true;
			}
		} catch (IOException e) {
			//DSDialog.error(view, "Ocorreu um erro ao tentar abrir o resumo da venda.\nVerifique se possui algum leitor de ficheiros pdf definido por defeito no sistema.", "Detalhe da venda");
		}
		
		if (!opened) {
			// open the pdf using icedpf
			
			// build a component controller
			SwingController controller = new SwingController();
			SwingViewBuilder factory = new SwingViewBuilder(controller);
			JPanel viewerComponentPanel = factory.buildViewerPanel();
			
			// add interactive mouse link annotation support via callback
			controller.getDocumentViewController().setAnnotationCallback(
					new org.icepdf.ri.common.MyAnnotationCallback(
							controller.getDocumentViewController()));
			
			JFrame applicationFrame = new JFrame();
			applicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			applicationFrame.getContentPane().add(viewerComponentPanel);
			
			// Now that the GUI is all in place, we can try openning a PDF
			try {
				controller.openDocument(report.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// show the component
			applicationFrame.pack();
			applicationFrame.setVisible(true);
		}
	}
	
	/**
	 * Adds the given product to the current sale
	 * 
	 * @param product
	 *        The product to be added
	 */
	public void addSaleProduct(Product product) {
		if (sale == null) {
			sale = new Sale();
		}
		
		for (SaleProduct sp : sale.getProducts()) {
			if (sp.getBaseProduct().equals(product)) {
				sp.setAmount(sp.getAmount() + (product.getDefaultAmount() == null ? 1 : product.getDefaultAmount()));
				view.refreshSaleProducts();
				return;
			}
		}
		
		SaleProduct sp = new SaleProduct();
		sp.setBaseProduct(product);
		sp.setAmount(product.getDefaultAmount() == null ? 1 : product.getDefaultAmount());
		sp.setUnit(product.getDefaultUnit());
		sp.setDiscount(null);
		sp.setVat(null);
		sp.setPrice(product.getPrice());
		sale.getProducts().add(sp);
		
		view.addSaleProduct(sp);
	}
	
	/**
	 * Deletes the given sale product from the current sale
	 * 
	 * @param saleProduct
	 *        The sale product to delete
	 */
	public void deleteSaleProduct(SaleProduct saleProduct) {
		if (sale != null && sale.getProducts() != null && !sale.getProducts().isEmpty()) {
			if (sale.getProducts().remove(saleProduct)) {
				view.deleteSaleProduct(saleProduct);
			}
		}
	}
	
	/**
	 * Gets the sale product totals
	 * 
	 * @param saleProduct
	 *        product The sale product
	 * @return
	 *         The totals
	 */
	public Double getSaleProductTotals(SaleProduct saleProduct) {
		double basePrice = (saleProduct.getPrice() == null ? 0 : saleProduct.getPrice()) * saleProduct.getAmount();
		double totals = basePrice;
		
		// VAT percentage
		if (saleProduct.getVat() != null && saleProduct.getVat() > 0) {
			totals += (totals * (saleProduct.getVat() / 100));
		}
		
		// discount percentage
		if (saleProduct.getDiscount() != null && !saleProduct.getDiscount().trim().isEmpty()) {
			try {
				Calculable calc = new ExpressionBuilder(saleProduct.getDiscount().trim()).build();
				totals -= (totals * (calc.calculate() / 100));
			} catch (Exception e) {
				// ignore
			}
		}
		
		return totals;
	}
	
	/**
	 * Checks if the current sale is empty (no products)
	 * 
	 * @return <code>true</code> if the sale is empty, <code>false</code> otherwise
	 */
	public boolean isCurrentSaleEmpty() {
		return sale == null || sale.getProducts().isEmpty();
	}
	
	/**
	 * Cancels the current sale
	 */
	public void cancelSale() {
		if (!isCurrentSaleEmpty()) {
			if (DSDialog.question(view, "Deseja realmente cancelar a venda actual?", "Confirma\u00E7\u00E3o")) {
				sale = null;
				view.clean();
			}
		}
	}
	
	/**
	 * Saves the current sale
	 * 
	 * @param client
	 *        The seller client
	 * @param paymentConditions
	 *        The payment conditions
	 * @param observations
	 *        The sale observations
	 * @param dischargePlace
	 *        The discharge place
	 * @param date
	 *        The sale date
	 * @param hasCardex
	 *        The has cardex flag value
	 */
	public void saveSale(Client client, String paymentConditions, String observations,
			String dischargePlace, Date date, boolean hasCardex) {
		List<SaleProduct> saleProducts = sale == null ? null : sale.getProducts();
		
		// pre-validate discharge place
		if (dischargePlace == null || dischargePlace.trim().isEmpty()) {
			if (DSDialog.question(view, "N\u00E3o especificou um local de descarga, pretende assumir o valor \"Morada do cliente\" ?", "Confirma\u00E7\u00E3o")) {
				dischargePlace = "Morada do cliente";
			} else {
				dischargePlace = ""; // eclipse NPE validation fails ;_;
			}
		}
		
		if (!validateSale(client, paymentConditions, observations, dischargePlace, date, saleProducts)) {
			return;
		}
		
		sale.setClient(client);
		sale.setObservations(observations.trim());
		sale.setDischargePlace(dischargePlace.trim());
		sale.setPaymentConditions(paymentConditions.trim());
		sale.setSaleDate(date);
		sale.setSeller(Application.get().getAuthenticatedUser());
		sale.setHasCardex(hasCardex);
		
		// save the sale
		view.setEnabled(false);
		boolean saved = false;
		try {
			saleService.save(sale);
			saved = true;
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Erro ao gravar venda.");
		}
		
		// print the saved sale
		if (saved) {
			try {
				final File report = getSaleReportFile(sale, true);
				saleService.generateReport(Application.get().getAuthenticatedUser(), report, sale);
				
				if (DSDialog.question(view, "Deseja enviar o resumo por e-mail?", "Resumo")) {
					sendSaleByEmail(sale, false);
				}
			} catch (ApplicationException e) {
				DSDialog.error(view, e.getMessage(), "Erro ao criar resumo da venda.");
			}
			
			view.clean();
			sale = null;
		}
		
		view.setEnabled(true);
	}
	
	/**
	 * Gets the sale report file pointer
	 * 
	 * @param _sale
	 *        The sale
	 * @param create
	 *        To create the directories if they don't exist
	 * @return The file pointer for the current sale report file
	 */
	private File getSaleReportFile(Sale _sale, boolean create) {
		Date now = _sale.getSaleDate();
		
		// base path/directory
		String path = FileUtil.getApplicationDirectory() + "/Vendas";
		File fp = new File(path);
		if (create && !fp.exists()) {
			fp.mkdir();
		}
		
		// year
		path += "/" + new SimpleDateFormat("yyyy").format(now);
		fp = new File(path);
		if (create && !fp.exists()) {
			fp.mkdir();
		}
		
		// month
		path += "/" + new SimpleDateFormat("MMMM").format(now);
		fp = new File(path);
		if (create && !fp.exists()) {
			fp.mkdir();
		}
		
		// day
		path += "/" + new SimpleDateFormat("dd").format(now);
		fp = new File(path);
		if (create && !fp.exists()) {
			fp.mkdir();
		}
		
		// client
		path += "/Cliente " + _sale.getClient().getCode();
		fp = new File(path);
		if (create && !fp.exists()) {
			fp.mkdir();
		}
		
		// PDF file name
		path += "/bm-venda-" + _sale.getId() + ".pdf";
		fp = new File(path);
		
		return fp;
	}
	
	/**
	 * Validates the current sale
	 * 
	 * @param client
	 *        The seller client
	 * @param paymentConditions
	 *        The payment conditions
	 * @param observations
	 *        The sale observations
	 * @param dischargePlace
	 *        The discharge place
	 * @param date
	 *        The sale date
	 * @param saleProducts
	 *        The sale products
	 * @return <code>true</code> if everything went smoothly, <code>false</code> otherwise.
	 */
	private boolean validateSale(Client client, String paymentConditions, String observations,
			String dischargePlace, Date date, List<SaleProduct> saleProducts) {
		
		// validate client
		if (client == null) {
			DSDialog.error(view, "Tem de especificar um cliente v\u00E1lido.", "Nova venda");
			return false;
		}
		
		// validate sale date
		if (date == null) {
			DSDialog.error(view, "Tem de especificar uma data de venda v\u00E1lida.", "Nova venda");
			return false;
		}
		
		// validate observations
		if (observations != null && observations.trim().isEmpty() && observations.trim().length() > 255) {
			DSDialog.error(view, "As observa\u00E7\u00F5es excedem o limite m\u00E1ximo de caracteres (255).", "Nova venda");
			return false;
		}
		
		// validate payment conditions
		if (paymentConditions != null && paymentConditions.trim().isEmpty() && paymentConditions.trim().length() > 255) {
			DSDialog.error(view, "As condi\u00E7\u00F5es de pagamento excedem o limite m\u00E1ximo de caracteres (255).", "Nova venda");
			return false;
		}
		
		// validate discharge place		
		if (dischargePlace == null || dischargePlace.trim().isEmpty() || dischargePlace.trim().length() > 255) {
			DSDialog.error(view, "Tem de especificar um local de descarga v\u00E1lido.", "Nova venda");
			return false;
		}
		
		// validate sale products
		if (saleProducts == null || saleProducts.isEmpty()) {
			DSDialog.error(view, "Tem de adicionar pelo menos um artigo \u00E0 venda.", "Nova venda");
			return false;
		}
		
		
		return true;
	}
	
	/**
	 * Updates the sale product amount
	 * 
	 * @param product
	 *        The sale product
	 * @param value
	 *        The amount
	 */
	public void updateSaleProductAmount(SaleProduct product, Double value) {
		if (value != null && value <= 99999.99 && value >= 0) {
			product.setAmount(Double.valueOf(new DecimalFormat("#.##").format(value).replaceAll(",", ".")));
		} else {
			product.setAmount(1);
		}
	}
	
	/**
	 * Updates the sale product price
	 * 
	 * @param product
	 *        The sale product
	 * @param value
	 *        The price
	 */
	public void updateSaleProductPrice(SaleProduct product, Double value) {
		if (value != null && value <= 9999999.99 && value >= 0) {
			product.setPrice(Double.valueOf(new DecimalFormat("#.##").format(value).replaceAll(",", ".")));
		} else {
			product.setPrice(null);
		}
	}
	
	/**
	 * Updates the sale product discount
	 * 
	 * @param product
	 *        The sale product
	 * @param value
	 *        The discount (percentage)
	 */
	public void updateSaleProductDiscount(SaleProduct product, String value) {
		if (value != null && !value.trim().isEmpty()) {
			try {
				value = value.trim().replaceFirst(",", ".");
				Calculable calc = new ExpressionBuilder(value.trim()).build();
				double discount = calc.calculate();
				if (isDouble(value) || isInteger(value)) {
					if (discount >= 0 && discount <= 100) {
						product.setDiscount(new DecimalFormat("#.##").format(discount).replaceAll(",", "."));
					} else {
						// do nothing
					}
				} else {
					product.setDiscount(value.trim());
				}
			} catch (Exception e) {
				product.setDiscount(null);
			}
		} else {
			product.setDiscount(null);
		}
	}
	
	/**
	 * Updates the sale product VAT
	 * 
	 * @param product
	 *        The sale product
	 * @param value
	 *        The VAT (percentage)
	 */
	public void updateSaleProductVat(SaleProduct product, Double value) {
		if (value != null && value <= 100 && value >= 0) {
			product.setVat(Double.valueOf(new DecimalFormat("#.##").format(value).replaceAll(",", ".")));
		} else {
			product.setVat(null);
		}
	}
	
	
	// #####################
	// Configuration
	// #####################
	
	/**
	 * Loads the current user configuration
	 */
	public void loadUserConfiguration() {
		try {
			configurations = configService.getUserConfiguration(Application.get().getAuthenticatedUser().getId());
			view.setUserConfigurationValues(
					configurations.get(ConfigurationKey.EMAIL_AUTHOR.getCode()),
					configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode()),
					configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD.getCode()),
					configurations.get(ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode()),
					configurations.get(ConfigurationKey.EMAIL_SUBJECT.getCode()),
					configurations.get(ConfigurationKey.EMAIL_BODY.getCode()),
					configurations.get(ConfigurationKey.SALE_INFO_PROD_CODE.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_PROD_QTY.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_PROD_PRICE.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_DISCOUNT.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_VAT.getCode()).getValue().equals("1"),
					configurations.get(ConfigurationKey.SALE_INFO_TOTALS.getCode()).getValue().equals("1")
					);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Obter configura\u00E7\u00F5es");
		}
	}
	
	/**
	 * Resets the modifications done by the user at his currently unsaved e-mail configurations
	 */
	public void resetEmailConfiguration() {
		view.setUserEmailConfigurationValues(
				configurations.get(ConfigurationKey.EMAIL_AUTHOR.getCode()),
				configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode()),
				configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD.getCode()),
				configurations.get(ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode()),
				configurations.get(ConfigurationKey.EMAIL_SUBJECT.getCode()),
				configurations.get(ConfigurationKey.EMAIL_BODY.getCode())
				);
	}
	
	/**
	 * Saves the given configuration
	 * 
	 * @param author
	 *        The e-mail author
	 * @param sourceEmail
	 *        The source e-mail address
	 * @param sourcePassword
	 *        The source e-mail password
	 * @param targetEmail
	 *        The target e-mail address
	 * @param subject
	 *        The e-mail subject
	 * @param body
	 *        The e-mail body
	 */
	public void saveConfiguration(String author, String sourceEmail, String sourcePassword,
			String targetEmail,  String subject, String body) {
		if (!validateConfigurationValue(author, "o nome")
				|| !validateConfigurationValue(sourceEmail, "o e-mail fonte")
				|| !validateConfigurationValue(sourcePassword, "a password do e-mail")
				|| !validateConfigurationValue(targetEmail, "o e-mail destino")
				|| !validateConfigurationValue(subject, "o assunto")
				|| !validateConfigurationValue(body, "a mensagem")) {
			return;
		}
		
		view.setEnabled(false);
		
		Configuration cfgAuthor = (Configuration) configurations.get(ConfigurationKey.EMAIL_AUTHOR.getCode()).clone();
		Configuration cfgEmailTarget = (Configuration) configurations.get(ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode()).clone();
		Configuration cfgEmailSource = (Configuration) configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode()).clone();
		Configuration cfgEmailSourcePwd = (Configuration) configurations.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD
				.getCode()).clone();
		Configuration cfgSubject = (Configuration) configurations.get(ConfigurationKey.EMAIL_SUBJECT.getCode()).clone();
		Configuration cfgBody = (Configuration) configurations.get(ConfigurationKey.EMAIL_BODY.getCode()).clone();
		
		cfgAuthor.setValue(author);
		cfgEmailSource.setValue(sourceEmail);
		cfgEmailSourcePwd.setValue(sourcePassword);
		cfgEmailTarget.setValue(targetEmail);
		cfgSubject.setValue(subject);
		cfgBody.setValue(body);
		
		// save the values
		try {
			List<Configuration> configs = new ArrayList<Configuration>();
			configs.add(cfgAuthor);
			configs.add(cfgEmailSource);
			configs.add(cfgEmailSourcePwd);
			configs.add(cfgEmailTarget);
			configs.add(cfgSubject);
			configs.add(cfgBody);
			configService.update(configs);
			
			// update local configuration
			configurations.put(ConfigurationKey.EMAIL_AUTHOR.getCode(), cfgAuthor);
			configurations.put(ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode(), cfgEmailSource);
			configurations.put(ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD.getCode(), cfgEmailSourcePwd);
			configurations.put(ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode(), cfgEmailTarget);
			configurations.put(ConfigurationKey.EMAIL_SUBJECT.getCode(), cfgSubject);
			configurations.put(ConfigurationKey.EMAIL_BODY.getCode(), cfgBody);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Gravar configura\u00E7\u00F5es");
		}
		
		view.setEnabled(true);
	}
	
	/**
	 * Resets the sale resume configuration
	 */
	public void resetSaleResumeConfiguration() {
		view.setUserSaleResumeConfigurationValues(
				configurations.get(ConfigurationKey.SALE_INFO_PROD_CODE.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_PROD_QTY.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_PROD_PRICE.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_DISCOUNT.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_VAT.getCode()).getValue().equals("1"),
				configurations.get(ConfigurationKey.SALE_INFO_TOTALS.getCode()).getValue().equals("1")
				);
	}
	
	/**
	 * Saves the given sale resume configurations
	 * 
	 * @param prodCodeIncluded
	 * @param prodDescIncluded
	 * @param prodQtyIncluded
	 * @param prodPriceIncluded
	 * @param discountIncluded
	 * @param vatIncluded
	 * @param totalsIncluded
	 */
	public void saveSaleResumeConfiguration(boolean prodCodeIncluded, boolean prodDescIncluded,
			boolean prodQtyIncluded, boolean prodPriceIncluded, boolean discountIncluded,
			boolean vatIncluded, boolean totalsIncluded) {
		view.setEnabled(false);
		
		Configuration cfgProdCode = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_PROD_CODE.getCode()).clone();
		Configuration cfgProdDesc = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode()).clone();
		Configuration cfgProdQty = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_PROD_QTY.getCode()).clone();
		Configuration cfgProdPrice = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_PROD_PRICE.getCode()).clone();
		Configuration cfgDiscount = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_DISCOUNT.getCode()).clone();
		Configuration cfgVat = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_VAT.getCode()).clone();
		Configuration cfgTotals = (Configuration) configurations.get(ConfigurationKey.SALE_INFO_TOTALS.getCode()).clone();
		
		cfgProdCode.setValue(prodCodeIncluded ? "1" : "0");
		cfgProdDesc.setValue(prodDescIncluded ? "1" : "0");
		cfgProdQty.setValue(prodQtyIncluded ? "1" : "0");
		cfgProdPrice.setValue(prodPriceIncluded ? "1" : "0");
		cfgDiscount.setValue(discountIncluded ? "1" : "0");
		cfgVat.setValue(vatIncluded ? "1" : "0");
		cfgVat.setValue(totalsIncluded ? "1" : "0");
		
		// save the values
		try {
			List<Configuration> configs = new ArrayList<Configuration>();
			configs.add(cfgProdCode);
			configs.add(cfgProdDesc);
			configs.add(cfgProdQty);
			configs.add(cfgProdPrice);
			configs.add(cfgDiscount);
			configs.add(cfgVat);
			configs.add(cfgTotals);
			configService.update(configs);
			
			// update local configuration
			configurations.put(ConfigurationKey.SALE_INFO_PROD_CODE.getCode(), cfgProdCode);
			configurations.put(ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode(), cfgProdDesc);
			configurations.put(ConfigurationKey.SALE_INFO_PROD_QTY.getCode(), cfgProdQty);
			configurations.put(ConfigurationKey.SALE_INFO_PROD_PRICE.getCode(), cfgProdPrice);
			configurations.put(ConfigurationKey.SALE_INFO_DISCOUNT.getCode(), cfgDiscount);
			configurations.put(ConfigurationKey.SALE_INFO_VAT.getCode(), cfgVat);
			configurations.put(ConfigurationKey.SALE_INFO_TOTALS.getCode(), cfgTotals);
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Gravar configura\u00E7\u00F5es");
		}
		
		view.setEnabled(true);
	}
	
	/**
	 * Validates a configuration value
	 * 
	 * @return <code>true</code> if everything went smoothly, <code>false</code> otherwise.
	 */
	private boolean validateConfigurationValue(String value, String field) {
		// validate value
		if (value == null || value.isEmpty() || value.length() > 255) {
			DSDialog.error(view, "Tem de especificar um valor v\u00E1lido para " + field + ".", "Valida configura\u00E7\u00F5es");
			return false;
		}
		
		return true;
	}
	
	
	// ------------------
	// change password
	// ------------------
	
	/**
	 * Changes the current user password
	 * 
	 * @param oldPassword
	 *        The old (current) password
	 * @param newPassword
	 *        The new password
	 * @param newPasswordConfirmation
	 *        The new password confirmation
	 */
	public void changePassword(char[] oldPassword, char[] newPassword, char[] newPasswordConfirmation) {
		if (oldPassword == null || oldPassword.length == 0) {
			DSDialog.error(view, "A password actual que introduziu \u00E9 inv\u00E1lida.", "Altera\u00E7\u00E3o de password");
			return;
		}
		
		if (newPassword == null || newPassword.length == 0) {
			DSDialog.error(view, "A nova password que introduziu \u00E9 inv\u00E1lida.", "Altera\u00E7\u00E3o de password");
			return;
		}
		
		if (newPasswordConfirmation == null || newPasswordConfirmation.length == 0) {
			DSDialog.error(view, "A password de confirma\u00E7\u00E3o que introduziu \u00E9 inv\u00E1lida.", "Altera\u00E7\u00E3o de password");
			return;
		}
		
		if (!Arrays.equals(newPassword, newPasswordConfirmation)) {
			DSDialog.error(view, "A nova password e a sua confirma\u00E7\u00E3o n\u00E3o coincidem.", "Altera\u00E7\u00E3o de password");
			return;
		}
		
		view.setEnabled(false);
		
		// change the password
		try {
			userService.changePassword(Application.get().getAuthenticatedUser(),
					new String(oldPassword), new String(newPassword));
			DSDialog.info(view, "A sua password foi alterada com sucesso.", "Altera\u00E7\u00E3o de password");
			view.passwordChanged();
		} catch (ApplicationException e) {
			DSDialog.error(view, e.getMessage(), "Altera\u00E7\u00E3o de password");
		}
		
		view.setEnabled(true);
	}
	
}
