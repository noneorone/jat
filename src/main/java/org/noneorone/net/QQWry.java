package org.noneorone.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class QQWry {  
    private String DbPath = System.getProperty("user.dir")+"\\data\\qqwry.dat";    //纯真IP数据库地址  
    private String Country, LocalStr;  
    private long IPN;  
    private int RecordCount, CountryFlag;  
    private long RangE, RangB, OffSet, StartIP, EndIP, FirstStartIP, LastStartIP, EndIPOff;  
    private RandomAccessFile fis;  
    private byte[] buff;  
      
    private long B2L(byte[] b) {  
        long ret = 0;  
        for (int i=0; i<b.length; i++) {  
            long t = 1L;  
            for (int j=0; j<i; j++) t = t * 256L;  
            ret += ((b[i]<0)?256+b[i]:b[i]) * t;  
        }  
        return ret;  
    }  
      
    private long ipToInt(String ip) {  
        String[] arr = ip.split("\\.");  
        long ret = 0;  
        for (int i=0; i<arr.length; i++) {  
            long l = 1;  
            for (int j=0; j<i; j++) l *= 256;  
            try {  
                ret += Long.parseLong(arr[arr.length-i-1]) * l;  
            } catch (Exception e) {  
                ret += 0;  
            }  
        }  
        return ret;  
    }  
      
    public void seek(String ip) throws Exception {  
        this.IPN = ipToInt(ip);  
        fis = new RandomAccessFile(this.DbPath, "r");  
        buff = new byte[4];  
        fis.seek(0);  
        fis.read(buff);  
        FirstStartIP = this.B2L(buff);  
        fis.read(buff);  
        LastStartIP = this.B2L(buff);  
        RecordCount = (int)((LastStartIP - FirstStartIP) / 7);  
        if (RecordCount <= 1) {  
            LocalStr = Country = "未知";  
            throw new Exception();  
        }  
          
        RangB = 0;  
        RangE = RecordCount;  
        long RecNo;  
          
        do {  
            RecNo = (RangB+RangE)/2;  
            getStartIP(RecNo);  
            if (IPN == StartIP) {  
               RangB = RecNo;  
               break;  
            }  
            if (IPN > StartIP)  
                RangB = RecNo;  
            else  
                RangE = RecNo;  
        } while (RangB < RangE-1);  
          
        getStartIP(RangB);  
        getEndIP();  
        getCountry(IPN);  
          
        fis.close();  
    }  
  
    private String getFlagStr(long OffSet) throws IOException {  
        int flag = 0;  
        do {  
            fis.seek(OffSet);  
            buff = new byte[1];  
            fis.read(buff);  
            flag = (buff[0]<0)?256+buff[0]:buff[0];  
            if (flag==1 || flag==2 ) {  
                buff = new byte[3];  
                fis.read(buff);  
                if (flag == 2) {  
                    CountryFlag = 2;  
                    EndIPOff = OffSet-4;  
                }  
                OffSet = this.B2L(buff);  
            } else  
                break;  
        } while (true);  
          
        if (OffSet < 12) {  
            return "";  
        } else {  
            fis.seek(OffSet);  
            return getStr();  
        }  
    }  
      
    private String getStr() throws IOException {  
        long l = fis.length();  
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();  
        byte c  = fis.readByte();  
        do {  
            byteout.write(c);  
            c = fis.readByte();  
        } while (c!=0 && fis.getFilePointer() < l);  
        return byteout.toString();  
    }  
      
    private void getCountry(long ip) throws IOException {  
        if (CountryFlag == 1 || CountryFlag == 2) {  
            Country = getFlagStr(EndIPOff+4);  
            if (CountryFlag == 1) {  
                LocalStr = getFlagStr(fis.getFilePointer());  
                if (IPN >= ipToInt("255.255.255.0") && IPN <= ipToInt("255.255.255.255")) {  
                    LocalStr = getFlagStr(EndIPOff + 21);  
                    Country = getFlagStr(EndIPOff + 12);  
                }  
            } else {  
                LocalStr = getFlagStr(EndIPOff+8);  
            }  
        } else {  
            Country = getFlagStr(EndIPOff + 4);  
            LocalStr = getFlagStr(fis.getFilePointer());  
        }  
    }  
      
    private long getEndIP() throws IOException {  
        fis.seek(EndIPOff);  
        buff = new byte[4];  
        fis.read(buff);  
        EndIP = this.B2L(buff);  
        buff = new byte[1];  
        fis.read(buff);  
        CountryFlag = (buff[0]<0)?256+buff[0]:buff[0];  
        return EndIP;  
    }  
      
    private long getStartIP(long RecNo) throws IOException {  
        OffSet = FirstStartIP + RecNo * 7;  
        fis.seek(OffSet);  
        buff = new byte[4];  
        fis.read(buff);  
        StartIP = this.B2L(buff);  
        buff = new byte[3];  
        fis.read(buff);  
        EndIPOff = this.B2L(buff);  
        return StartIP;  
    }  
    /* 
     * 转换城市中文名为分站的二级域名节点 
     */  
    private String getSitekey(String city) {  
        String sitekey="";  
            if(city.indexOf("北京",0)>0){  
                sitekey="www";  
            }else if(city.indexOf("上海",0)>0){  
                sitekey="sh";  
            }else if(city.indexOf("南京",0)>0){  
                sitekey="nj";  
            }else if(city.indexOf("广州",0)>0){  
                sitekey="gz";  
            }else if(city.indexOf("深圳",0)>0){  
                sitekey="sz";  
            }else if(city.indexOf("沈阳",0)>0){  
                sitekey="sy";  
            }else if(city.indexOf("成都",0)>0){  
                sitekey="cd";  
            }else if(city.indexOf("天津",0)>0){  
                sitekey="tj";  
            }else if(city.indexOf("重庆",0)>0){  
                sitekey="cq";  
            }else if(city.indexOf("郑州",0)>0){  
                sitekey="zz";  
            }else if(city.indexOf("武汉",0)>0){  
                sitekey="wh";  
            }else if(city.indexOf("长沙",0)>0){  
                sitekey="cs";  
            }else if(city.indexOf("济南",0)>0){  
                sitekey="jn";  
            }else if(city.indexOf("长春",0)>0){  
                sitekey="cc";  
            }else if(city.indexOf("合肥",0)>0){  
                sitekey="hf";  
            }else if(city.indexOf("杭州",0)>0){  
                sitekey="hz";  
            }else if(city.indexOf("西安",0)>0){  
                sitekey="xa";  
            }else if(city.indexOf("重庆",0)>0){  
                sitekey="cq";  
            }else{  
                sitekey="www";  
            }  
        return sitekey;  
    }  
      
    public String getLocal() { return this.LocalStr; }  
    public String getCountry() { return this.Country; }  
    public void setPath(String path) { this.DbPath = path; }  
      
    public static void main(String[] args) throws Exception {  
    	//baidu: 119.75.217.56
    	//qq: 119.147.15.17
    	//sample: 125.76.253.168
        QQWry w = new QQWry();  
        w.seek("119.147.15.17");  
        String city=w.getCountry().toString();  
        String site=w.getSitekey(city);  
        System.out.println(city);   
        System.out.println(site);   
    }  
  
}
