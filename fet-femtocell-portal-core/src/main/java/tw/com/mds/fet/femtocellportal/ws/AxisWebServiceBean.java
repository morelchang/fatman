package tw.com.mds.fet.femtocellportal.ws;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class AxisWebServiceBean {

	private String endpoint;
	private String namespace;
	private String operation;
	private String[] paramNames;
	private QName[] paramTypes;
	private QName returnType;
	private String soapActionUri;

	public AxisWebServiceBean() {
		super();
	}

	public Object call(Object... paramValues)
			throws ServiceException, RemoteException {

		if (paramNames.length != paramTypes.length) {
			String msg = MessageFormat
					.format("the number of paramNames:{0} does not equal to paramTypes:{1} in parameter spec",
							paramNames.length,
							paramTypes.length);
			throw new IllegalArgumentException(msg);
		}

		if (paramNames.length != paramValues.length) {
			String msg = MessageFormat
					.format("the number of paramNames:{0} in parameter spec does not equal to paramValues:{1}",
							paramNames.length, paramValues.length);
			throw new IllegalArgumentException(msg);
		}

		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(endpoint);

		call.setOperationName(new QName(namespace, operation));
		for (int i = 0; i < paramNames.length; i++) {
			call.addParameter(new QName(namespace, paramNames[i]),
					paramTypes[i], ParameterMode.IN);
		}
		call.setReturnType(returnType);

		call.setUseSOAPAction(true);
		call.setSOAPActionURI(soapActionUri);

		return call.invoke(paramValues);
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public QName[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(QName[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public QName getReturnType() {
		return returnType;
	}

	public void setReturnType(QName returnType) {
		this.returnType = returnType;
	}

	public String getSoapActionUri() {
		return soapActionUri;
	}

	public void setSoapActionUri(String soapActionUri) {
		this.soapActionUri = soapActionUri;
	}

	@Override
	public String toString() {
		return "AxisWebServiceBean [endpoint=" + endpoint + ", namespace="
				+ namespace + ", operation=" + operation + ", paramNames="
				+ Arrays.toString(paramNames) + ", paramTypes="
				+ Arrays.toString(paramTypes) + ", returnType=" + returnType
				+ ", soapActionUri=" + soapActionUri + "]";
	}

}
