package tw.com.mds.fet.femtocellportal.core.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LoggingService;
import tw.com.mds.fet.femtocellportal.core.LoginUser;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoProfileWrapper;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoUserWrapper;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLog;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.core.dao.UserLogDao;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
public class LoggingServiceImpl implements LoggingService {

	private static final Log logger = LogFactory.getLog(LoggingServiceImpl.class);
	
	private UserLogDao userLogDao;

	public UserLog logEvent(UserLogType type, String content) {
		return logEvent(type, null, content);
	}

	public UserLog logEvent(UserLogType type, String sourceIp, String content) {
		return logEvent(type, sourceIp, null, content);
	}

	public UserLog logEvent(UserLogType type, String sourceIp, LoginUser operator, String content) {
		return logEvent(type, sourceIp, operator, null, content);
	}

	public UserLog logEvent(UserLogType type, String sourceIp, LoginUser operator,
			FemtoProfile profile, String content) {
		return createUserLog(type, sourceIp, operator, profile, content);
	}

	private UserLog createUserLog(UserLogType type, String sourceIp, LoginUser operator,
			FemtoProfile target, String content) {
		UserLog log = new UserLog();
		// basic log properties
		log.setType(type);
		log.setCreateTime(new Date());
		log.setContent(content);
		log.setOperatorSourceIp(sourceIp);
		// set operator information
		if (operator != null) {
			String userId = operator.getUserId();
			String userName = operator.getUserName();
			if (operator instanceof FemtoUser) {
				userName = new MaskedFemtoUserWrapper((FemtoUser) operator).getUserName();
			}
			log.setOperatorId(userId);
			log.setOperatorName(userName);
		}
		// target profile information
		if (target != null) {
			MaskedFemtoProfileWrapper maskedTarget = new MaskedFemtoProfileWrapper(target);
			log.setProfileApei(maskedTarget.getApei());
			log.setProfileImsi(maskedTarget.getImsi());
			// target femto user information
			FemtoUser user = target.getUser();
			if (user != null) {
				MaskedFemtoUserWrapper maskedUser = new MaskedFemtoUserWrapper(user);
				log.setUserMobile(maskedUser.getMobile());
				log.setUserName(maskedUser.getUserName());
			}
		}
		Utils.debug(logger, "saving user log:{0}", log);
		return userLogDao.persist(log);
	}

	public UserLogDao getUserLogDao() {
		return userLogDao;
	}

	public void setUserLogDao(UserLogDao userLogDao) {
		this.userLogDao = userLogDao;
	}

}
