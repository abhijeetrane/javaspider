package org.eclipse.contribution.junit.log.internal;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.jdt.core.IType;

public class LogListener implements ITestRunListener {

	public void testRunFinished(IType testClass, boolean success) {
		System.out.println(testClass.getFullyQualifiedName() + " result: " + success);
	//		try {
	//			InetAddress group= InetAddress.getByName("227.1.2.4");
	//			MulticastSocket s= new MulticastSocket(4568);
	//			s.joinGroup(group);
	//			byte msg[]= testName.getBytes();
	//			DatagramPacket hi= new DatagramPacket(msg, msg.length, group, 4568);
	//			s.send(hi);
	//			s.leaveGroup(group);
	//		} catch (UnknownHostException e) {
	//		} catch (IOException e) {
	//		}
	}
}
