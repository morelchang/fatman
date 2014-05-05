package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

public interface Modifiable {

	public void setUpdateTime(Date time);

	public Date getUpdateTime();

	public void setCreateTime(Date time);

	public Date getCreateTime();

}
