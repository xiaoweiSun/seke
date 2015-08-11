package ServletPackage;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * IP������
 * @author lyh
 * @version 2012-7-5
 * @see IpGetter
 * @since
 */
public class IpGetter
{
    /**
     * ��־
     */
//    private static final Logger log = Logger.getLogger(IpGetter.class);

    /**
     * ����������
     */
    private static final String NETWORK_CARD = "eth0";

    /**
     * ����������
     */
    private static final String NETWORK_CARD_BAND = "bond0";

    /**
     * 
     * Description: �õ�������<br>
     * @return 
     * @see
     */
    public static String getLocalHostName()
    {
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (Exception e)
        {
//            log.error("IpGetter.getLocalHostName�����쳣���쳣��Ϣ��" + e.getMessage());
            return "";
        }
    }
    
    /**
     * Description : windows�»�ñ���ipv4��ַ
     * 
     */
    public static String getLocalIpUnderWindow()
    {
    	try
        {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        }
        catch (Exception e)
        {
//            log.error("IpGetter.getLocalHostName�����쳣���쳣��Ϣ��" + e.getMessage());
            return "";
        } 
    }

    /**
     * Description: linux�»�ñ���IPv4 IP<br>
     * @return 
     * @see
     */
    public static String getLocalIpUnderLinux()
    {
        String ip = "";
        try
        {
            Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>)NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements())
            {
                NetworkInterface ni = e1.nextElement();

                //���������߰�˫����
                if ((NETWORK_CARD.equals(ni.getName()))
                    || (NETWORK_CARD_BAND.equals(ni.getName())))
                {
                    Enumeration<InetAddress> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements())
                    {
                        InetAddress ia = e2.nextElement();
                        if (ia instanceof Inet6Address)
                        {
                            continue;
                        }
                        ip = ia.getHostAddress();
                    }
                    break;
                }
                else
                {
                    continue;
                }
            }
        }
        catch (SocketException e)
        {
//            log.error("IpGetter.getLocalIP�����쳣���쳣��Ϣ��" + e.getMessage());
        }
        return ip;
    }
    
    public static boolean isWindowsOS()
    {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("windows")>-1)
        {
          isWindowsOS = true;
        }
        return isWindowsOS;
    }
    
    public static String getLocalIP() {
    	if ( isWindowsOS()) {
    		return getLocalIpUnderWindow();
    	} 
    	else {
    		return getLocalIpUnderLinux();
    	}
    }
    

}
