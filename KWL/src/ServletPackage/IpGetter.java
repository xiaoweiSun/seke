package ServletPackage;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * IP工具类
 * @author lyh
 * @version 2012-7-5
 * @see IpGetter
 * @since
 */
public class IpGetter
{
    /**
     * 日志
     */
//    private static final Logger log = Logger.getLogger(IpGetter.class);

    /**
     * 单网卡名称
     */
    private static final String NETWORK_CARD = "eth0";

    /**
     * 绑定网卡名称
     */
    private static final String NETWORK_CARD_BAND = "bond0";

    /**
     * 
     * Description: 得到本机名<br>
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
//            log.error("IpGetter.getLocalHostName出现异常！异常信息：" + e.getMessage());
            return "";
        }
    }
    
    /**
     * Description : windows下获得本机ipv4地址
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
//            log.error("IpGetter.getLocalHostName出现异常！异常信息：" + e.getMessage());
            return "";
        } 
    }

    /**
     * Description: linux下获得本机IPv4 IP<br>
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

                //单网卡或者绑定双网卡
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
//            log.error("IpGetter.getLocalIP出现异常！异常信息：" + e.getMessage());
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
