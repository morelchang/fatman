package tw.com.mds.fet.femtocellportal.nedb.impl;


public interface NedbConfigDao {

	public NedbConfig load();
	
	public NedbConfig save(NedbConfig config);

}
