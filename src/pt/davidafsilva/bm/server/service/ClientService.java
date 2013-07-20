package pt.davidafsilva.bm.server.service;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IClientDAO;
import pt.davidafsilva.bm.server.service.interfaces.IClientService;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * ClientService.java
 * 
 * The client service (business code) implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:55:41 AM
 */
public class ClientService implements IClientService {
	
	// logger
	private final Logger log = Logger.getLogger(ClientService.class);
	
	private final IClientDAO clientDAO = IClientDAO.Util.getInstance();
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IClientService#save(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void save(Client client) throws ApplicationException {
		log.debug("save(): saving the client.. ");
		try {
			// checks if there's already a client with the given code
			if (clientDAO.get(0, client.getCode()) != null) {
				throw new ApplicationException("J\u00E1 existe um cliente com o c\u00F3digo '" + client.getCode() + "'.");
			}
			clientDAO.save(client);
			
			log.debug("save(): client saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("save(): error saving client", e);
			throw new ApplicationException("Ocorreu um erro a gravar o cliente.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IClientService#update(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void update(Client client) throws ApplicationException {
		log.debug("update(): updating the client.. ");
		try {
			// checks if there's already a client with the given code
			if (clientDAO.get(client.getId(), client.getCode()) != null) {
				throw new ApplicationException("J\u00E1 existe um cliente com o c\u00F3digo '" + client.getCode() + "'.");
			}
			clientDAO.update(client);
			
			log.debug("update(): client updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating client", e);
			throw new ApplicationException("Ocorreu um erro a actualizar o cliente.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IClientService#delete(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void delete(Client client) throws ApplicationException {
		log.debug("delete(): deleting the client.. ");
		try {
			clientDAO.delete(client);
			
			log.debug("delete(): client deleted sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("delete(): error deleting client", e);
			e.printStackTrace();
			throw new ApplicationException("Ocorreu um erro a remover o cliente.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IClientService#getAll()
	 */
	@Override
	public List<Client> getAll() {
		List<Client> clients = null;
		log.debug("getAll(): getting all the clients.. ");
		try {
			clients = clientDAO.getAll();
			
			log.debug("getAll(): clients obtained sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("getAll(): error getting all clients", e);
		}
		
		return clients;
	}
	
}
