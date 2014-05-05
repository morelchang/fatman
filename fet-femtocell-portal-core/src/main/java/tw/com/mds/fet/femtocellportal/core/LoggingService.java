package tw.com.mds.fet.femtocellportal.core;

public interface LoggingService {

	public UserLog logEvent(UserLogType type, String content);
	
	public UserLog logEvent(UserLogType type, String sourceIp, String content);

	public UserLog logEvent(UserLogType type, String sourceIp, LoginUser operator, String content);

	public UserLog logEvent(UserLogType type, String sourceIp, LoginUser operator,
			FemtoProfile profile, String content);

}
