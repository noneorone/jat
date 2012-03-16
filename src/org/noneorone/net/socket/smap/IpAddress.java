package org.noneorone.net.socket.smap;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IpAddress
{
  private InetAddress ip_addr = null;
  private int port = 0;
  private byte[] additional_data;
  static boolean resolve_dns = false;
  transient int size = -1;
  protected static final Log log = LogFactory.getLog(IpAddress.class);
  static
  {

    try
    {
      resolve_dns = Boolean.valueOf(System.getProperty("resolve.dns", "false")).
          booleanValue();
    }
    catch (SecurityException ex)
    {
      resolve_dns = false;
    }
  }

  public IpAddress()
  {
  }

  public IpAddress(String i, int p)
      throws UnknownHostException
  {
    port = p;
    ip_addr = InetAddress.getByName(i);
  }

  public IpAddress(InetAddress i, int p)
  {
    ip_addr = i;
    port = p;
    if (this.ip_addr == null)
    {
      setAddressToLocalHost();
    }
  }

  private void setAddressToLocalHost()
  {
    try
    {
      ip_addr = InetAddress.getLocalHost();

    }
    catch (Exception e)
    {
      if (log.isWarnEnabled())
      {
        log.warn("exception: " + e);
      }
    }
  }

  public IpAddress(int port)
  {
    this(port, true);
  }

  public IpAddress(int port, boolean set_default_host)
  {
    this.port = port;
    if (set_default_host)
    {
      setAddressToLocalHost();
    }
  }

  public final InetAddress getIpAddress()
  {
    return ip_addr;
  }

  public final int getPort()
  {
    return port;
  }

  public final boolean isMulticastAddress()
  {
    return ip_addr != null && ip_addr.isMulticastAddress();
  }

  public final byte[] getAdditionalData()
  {
    return additional_data;
  }

  public final int compare(IpAddress other)
  {
    return compareTo(other);
  }

  public final int compareTo(Object o)
  {
    int h1, h2, rc;

    if (this == o)
    {
      return 0;
    }
    if ( (o == null) || ! (o instanceof IpAddress))
    {
      throw new ClassCastException(
          "comparison between different classes: the other object is " +
          (o != null ? o.getClass() : o));
    }
    IpAddress other = (IpAddress) o;
    if (ip_addr == null)
    {
      if (other.ip_addr == null)
      {
        return port < other.port ? -1 : (port > other.port ? 1 : 0);
      }
      else
      {
        return -1;
      }
    }

    h1 = ip_addr.hashCode();
    h2 = other.ip_addr.hashCode();
    rc = h1 < h2 ? -1 : h1 > h2 ? 1 : 0;
    return rc != 0 ? rc : port < other.port ? -1 : (port > other.port ? 1 : 0);
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    return compareTo(obj) == 0;
  }

  public final int hashCode()
  {
    return ip_addr != null ? ip_addr.hashCode() + port : port;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    if (ip_addr == null)
    {
      sb.append("<null>");
    }
    else
    {
      if (ip_addr.isMulticastAddress())
      {
        sb.append(ip_addr.getHostAddress());
      }
      else
      {
        String host_name = null;
        if (resolve_dns)
        {
          host_name = ip_addr.getHostName();
          // appendShortName(host_name, sb);
        }
        else
        {
          host_name = ip_addr.getHostAddress();
        }
        sb.append(host_name);
      }
    }
    sb.append(":").append(port);
    return sb.toString();
  }

  public void writeExternal(ObjectOutput out)
      throws IOException
  {
    if (ip_addr != null)
    {
      byte[] address = ip_addr.getAddress();
      out.writeByte(address.length); // 1 byte
      out.write(address, 0, address.length);
    }
    else
    {
      out.writeByte(0);
    }
    out.writeInt(port);
    if (additional_data != null)
    {
      out.writeBoolean(true);
      out.writeShort(additional_data.length);
      out.write(additional_data, 0, additional_data.length);
    }
    else
    {
      out.writeBoolean(false);
    }
  }

  public void readExternal(ObjectInput in)
      throws IOException,
      ClassNotFoundException
  {
    int len = in.readByte();
    if (len > 0)
    {
      //read the four bytes
      byte[] a = new byte[len];
      //in theory readFully(byte[]) should be faster
      //than read(byte[]) since latter reads
      // 4 bytes one at a time
      in.readFully(a);
      //look up an instance in the cache
      this.ip_addr = InetAddress.getByAddress(a);
    }
    //then read the port
    port = in.readInt();

    if (in.readBoolean() == false)
    {
      return;
    }
    len = in.readShort();
    if (len > 0)
    {
      additional_data = new byte[len];
      in.readFully(additional_data, 0, additional_data.length);
    }
  }

  public void writeTo(DataOutputStream out)
      throws IOException
  {
    if (ip_addr != null)
    {
      byte[] address = ip_addr.getAddress(); // 4 bytes (IPv4) or 16 bytes (IPv6)
      out.writeByte(address.length); // 1 byte
      out.write(address, 0, address.length);
    }
    else
    {
      out.writeByte(0);
    }
    out.writeInt(port);
    if (additional_data != null)
    {
      out.writeBoolean(true); // 1 byte
      out.writeShort(additional_data.length);
      out.write(additional_data, 0, additional_data.length);
    }
    else
    {
      out.writeBoolean(false);
    }
  }

  public void readFrom(DataInputStream in)
      throws IOException
  {
    int len = in.readByte();
    if (len > 0)
    {
      byte[] a = new byte[len]; // 4 bytes (IPv4) or 16 bytes (IPv6)
      in.readFully(a);
      this.ip_addr = InetAddress.getByAddress(a);
    }
    port = in.readInt();

    if (in.readBoolean() == false)
    {
      return;
    }
    len = in.readShort();
    if (len > 0)
    {
      additional_data = new byte[len];
      in.readFully(additional_data, 0, additional_data.length);
    }
  }

  public Object clone()
      throws CloneNotSupportedException
  {
    IpAddress ret = new IpAddress(ip_addr, port);
    if (additional_data != null)
    {
      ret.additional_data = new byte[additional_data.length];
      System.arraycopy(additional_data, 0, ret.additional_data, 0,
                       additional_data.length);
    }
    return ret;
  }

}
