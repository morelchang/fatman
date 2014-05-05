package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.mds.fet.femtocellportal.ahr.AhrException;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.CellType;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.util.Utils;

/**
 * factory method to create AHR message instances
 * <p>
 * using {@link #newMessage(String, int)} first to create a AHR message.
 * </p>
 * <p>
 * usage:<br/>
 * <code>
 * FemtoUser femtoUser = getUser();<br/>
 * RegisterAp registerMessage = new MessageBuilder().newMessage().registerAp(femtoUser)
 * </code>
 * </p>
 * 
 * @author morel
 *
 */
public class MessageBuilder {

	private static final Log logger = LogFactory.getLog(MessageBuilder.class);
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String interfaceVersion;
	private int serialNo;
	private SoapEnvelope message;

	public MessageBuilder newMessage(String interfaceVersion, int serialNo) {
		this.interfaceVersion = interfaceVersion;
		this.serialNo = serialNo;
		message = new SoapEnvelope();
		message.setBody(new SoapBody());
		return this;
	}

	public SoapEnvelope registerAp(FemtoUser user, FemtoProfile profile) {
		validateState();
		
		if (profile == null) {
			throw new IllegalArgumentException("argument profile is null");
		}
		RegisterAp r = new RegisterAp();
		r.setApIdentity(createApId(profile));
		r.setApZoneName(profile.getApZone().getName());
		r.setCardId(user.getCardId());
		r.setPermissionMode(profile.getUePermissionMode().ordinal());
		r.setInterfaceVersion(interfaceVersion);
		r.setLocationDetectMode(profile.getLocationDetectMode().ordinal());
		r.setMaxUeUserCount(profile.getMaxUserCount());
		r.setMobile(user.getMobile());
		r.setPhone(user.getPhone());
		r.setSerialNo(serialNo);
		r.setUePermissionList(createUePermissionList(profile));
		r.setUserName(user.getUserName());
		r.setZipCode(user.getZipCode());
		
		message.getBody().setRegisterAp(r);
		return message;
	}

	private void validateState() {
		if (message == null) {
			throw new IllegalStateException("message is null, forget to call newMessage() first?");
		}
	}

	private ApId createApId(FemtoProfile profile) {
		ApId apid = new ApId();
		apid.setApei(profile.getApei());
		apid.setImsi(profile.getImsi());
		return apid;
	}

	private List<UePermoission> createUePermissionList(FemtoProfile profile) {
		// TOTO: AHR, what is the default permission List?
		List<UePermoission> result = new ArrayList<UePermoission>();
		for (UserEquipment ue : profile.getPermissionList()) {
			result.add(createUePermission(ue));
		}
		return result;
	}

	private UePermoission createUePermission(UserEquipment ue) {
		UePermoission up = new UePermoission();
		up.setImsi(ue.getImsi());
		up.setMsisdn(ue.getMsisdn());
		return up;
	}

	public SoapEnvelope unRegisterAp(FemtoProfile profile) {
		validateState();

		ApIdentityRequest u = new ApIdentityRequest();
		u.setInterfaceVersion(interfaceVersion);
		u.setSerialNo(serialNo);
		u.setApIdentity(createApId(profile));
		
		message.getBody().setUnRegisterAp(u);
		return message;
	}

	public SoapEnvelope modifyUeList(FemtoProfile profile, Date updateTime) {
		validateState();

		ModifyUeList u = new ModifyUeList();
		u.setInterfaceVersion(interfaceVersion);
		u.setSerialNo(serialNo);
		u.setApIdentity(createApId(profile));
		u.setMaxUeUserCount(profile.getMaxUserCount());
		u.setPermissionMode(profile.getUePermissionMode().ordinal());
		u.setTime(DEFAULT_DATE_FORMAT.format(updateTime));
		u.setUePermissionList(createUePermissionList(profile));
		u.setUePermissionListSize(profile.getPermissionList().size());
		
		message.getBody().setModifyUeList(u);
		return message;
	}

	public SoapEnvelope setLocation(FemtoProfile profile) throws AhrException {
		return setLocation(profile, null);
	}

	public SoapEnvelope setLocation(FemtoProfile profile, String defaultPlmnId) throws AhrException {
		validateState();

		SetLocation s = new SetLocation();
		s.setInterfaceVersion(interfaceVersion);
		s.setSerialNo(serialNo);
		s.setApIdentity(createApId(profile));
		s.setLocationInfoList(createLocationInfoList(profile, defaultPlmnId));
		
		message.getBody().setSetLocation(s);
		return message;
	}

	private List<LocationInfo> createLocationInfoList(FemtoProfile profile,
			String defaultPlmnId) throws AhrException {
		final int defaultRncId = 0;
		List<LocationInfo> result = new ArrayList<LocationInfo>();
		for (Cell c : profile.getCells()) {
			LocationInfo info = new LocationInfo();
			info.setCellId(toInteger(c.getCellId(), "cell.cellId"));
			info.setType(c.getType().ordinal());
			
			// LACID
			info.setLacid(0);
			if (c.getType() == CellType.TwoG && c.getLacId() != null) {
				info.setLacid(toInteger(c.getLacId(), "cell.lacId"));
			}
			
			// RNCID 
			info.setRncid(defaultRncId);
			if (c.getType() == CellType.ThreeG && c.getRnc() != null) {
				info.setRncid(toInteger(c.getRnc().getRncId(), "cell.rnc.rncId"));
			}
			
			// PLMNID
			if (StringUtils.isEmpty(c.getPlmId())) {
				Utils.debug(logger, "cell.plmId is empyt, using default plmId:{0}", defaultPlmnId);
				info.setPlmnid(toInteger(defaultPlmnId, "cell.plmId"));
			} else {
				info.setPlmnid(toInteger(c.getPlmId(), "cell.plmId"));
			}
			result.add(info);
		}
		return result;
	}
	
	private Integer toInteger(String value, String fieldName) throws AhrException {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			throw new AhrException(11004, Utils.format("failed to create AHR request, {0} is not an integer:{1}", fieldName, value));
		}
	}

	public SoapEnvelope QueryPermissionList(FemtoProfile profile) {
		validateState();

		QueryPermissionList q = new QueryPermissionList();
		q.setInterfaceVersion(interfaceVersion);
		q.setSerialNo(serialNo);
		q.setApIdentity(createApId(profile));
		
		message.getBody().setQueryPermissionList(q);
		return message;
	}

	public SoapEnvelope suspendAp(FemtoProfile profile) {
		validateState();

		ApIdentityRequest u = new ApIdentityRequest();
		u.setInterfaceVersion(interfaceVersion);
		u.setSerialNo(serialNo);
		u.setApIdentity(createApId(profile));
		
		message.getBody().setSuspendAp(u);
		return message;
	}

	public SoapEnvelope resumeAp(FemtoProfile profile) {
		validateState();

		ApIdentityRequest u = new ApIdentityRequest();
		u.setInterfaceVersion(interfaceVersion);
		u.setSerialNo(serialNo);
		u.setApIdentity(createApId(profile));
		
		message.getBody().setResumeAp(u);
		return message;
	}

}
