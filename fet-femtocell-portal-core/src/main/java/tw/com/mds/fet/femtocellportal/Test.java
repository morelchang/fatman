package tw.com.mds.fet.femtocellportal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;

import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.translator.impl.SyncQueueDelegate;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class Test {

	public static void main(String[] args) throws InterruptedException,
			NumberFormatException, IOException, SchedulerException,
			ServiceException {
		SyncQueueDelegate service = Utils.getSpringBean("translatorService");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		while ((input = reader.readLine()) != null) {
			try {
				String cmd = input.substring(0, 1);
				if (cmd.equalsIgnoreCase("i")) {
					String[] reqs = StringUtils.split(input.substring(1), ",");
					sendRequest(reqs, service);
				} else {
					double time = Double.parseDouble(input);
					service.getControlJob().changeThroughput(time);
					System.out.println("rescheduling interval to " + time);
				}
			} catch (Exception e) {
				System.out.println("incorrect input try again");
			}
		}
	}

	private static void sendRequest(String[] reqs, final SyncQueueDelegate service) {
		Runnable[] rs = new Runnable[reqs.length];
		final Map<String, Date> startTimeMap = new HashMap<String, Date>();
		final Map<String, Date> endTimeMap = new HashMap<String, Date>();
		
		for (int i = 0; i < reqs.length; i++) {
			final String msisdn = reqs[i];
			rs[i] = new Runnable() {
				public void run() {
					try {
						System.out.println(MessageFormat.format(
								"[{0}] sending request:{1}", new Date().toString(), msisdn));
						startTimeMap.put(msisdn, new Date());
						service.queryImsiByMsisdn(msisdn);
						endTimeMap.put(msisdn, new Date());
					} catch (ServiceException e) {
						throw new RuntimeException(e);
					}
					System.out.println(MessageFormat.format(
							"[{0}] get response:{1}", new Date().toString(), msisdn));
				}
			};
		}
		
		for (Runnable runnable : rs) {
			new Thread(runnable).start();
		}
		
		while (endTimeMap.size() != rs.length) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		
		System.out.println("+==============+");
		for (String r : reqs) {
			System.out.println(MessageFormat.format("+{0} +{1} +{2}", r, startTimeMap.get(r).toString(), endTimeMap.get(r).toString()));
		}
		System.out.println("+==============+");
	}
}
