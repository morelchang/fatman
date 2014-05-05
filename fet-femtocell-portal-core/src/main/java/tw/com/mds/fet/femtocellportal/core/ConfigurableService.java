package tw.com.mds.fet.femtocellportal.core;


public interface ConfigurableService {

	/**
	 * reload and apply configuration immediately from storage
	 * <p>
	 * if no configuration found, nothing will be changed
	 * </p>
	 * 
	 * @throws ConfigurationException
	 */
	public void reloadConfig() throws ConfigurationException;

	/**
	 * apply and save specified configuration immediately
	 * 
	 * @param config
	 * @throws ConfigurationException
	 */
	public void applyConfig(Config config) throws ConfigurationException;

	/**
	 * get current configuration
	 * 
	 * @return
	 */
	public Config getConfig();

}