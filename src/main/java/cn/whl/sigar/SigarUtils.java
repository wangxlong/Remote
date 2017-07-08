package cn.whl.sigar;

import java.io.File;
import java.nio.file.Paths;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONArray;
import org.json.JSONObject;

public class SigarUtils {
    public final static Sigar sigar = initSigar();
    private static Sigar initSigar() {
        try {
            //依赖库文件的路径，可根据实际项目自定义
            String file = Paths.get( "/home/sigar/libsigar-universal64-macosx.dylib").toString();
            File classPath = new File(file).getParentFile();          
            String path = System.getProperty("java.library.path");
            String sigarLibPath = classPath.getCanonicalPath();
            //为防止java.library.path重复加，此处判断了一下
            if (!path.contains(sigarLibPath)) {
                if (isOSWin()) {
                    path += ";" + sigarLibPath;
                } else {
                    path += ":" + sigarLibPath;
                }
                System.setProperty("java.library.path", path);
            }
            return new Sigar();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isOSWin(){//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }
    public static JSONArray infoToJsonArray(Sigar sigar) throws SigarException{
    	
    	JSONArray jsonArray=new JSONArray();
    	long timestamp=System.currentTimeMillis();
    	
    	JSONObject datajb=new JSONObject();

    	
    	CpuPerc perc = sigar.getCpuPerc();
    	JSONObject cpu_info=new JSONObject();
    	cpu_info.put("idle", perc.getIdle());
    	cpu_info.put("used", perc.getCombined());
    	
    	Mem men=sigar.getMem();
    	JSONObject mem_info=new JSONObject();
    	double memUsed = men.getActualUsed();
    	double memTotal = men.getTotal();
    	double memUsedPerc = men.getUsedPercent();
    	Double memUsedStr = Double.parseDouble(String.format("%.2f", memUsed/1024/1024/1024));// mem to string GB
    	Double memTotalStr = Double.parseDouble(String.format("%.2f", memTotal/1024/1024/1024));
    	Double memUsedPercStr = Double.parseDouble(String.format("%.2f", memUsedPerc));
    	mem_info.put("Used", memUsedStr);
    	mem_info.put("Total", memTotalStr);
    	mem_info.put("UsedPerc", memUsedPercStr);
    	
    	JSONObject disk_info=new JSONObject();
    	double diskUsed = sigar.getFileSystemUsage("/").getUsed();//disk
    	double diskTotal = sigar.getFileSystemUsage("/").getTotal();
    	double diskUsedPerc = sigar.getFileSystemUsage("/").getUsePercent();
    	Double diskUsedStr = Double.parseDouble(String.format("%.2f", diskUsed/1024/1024));//disk to String GB
    	Double diskTotalStr = Double.parseDouble(String.format("%.2f", diskTotal/1024/1024));
    	Double diskUsedPercStr = Double.parseDouble(String.format("%.2f", diskUsedPerc*100));
    	disk_info.put("Used", diskUsedStr);
    	disk_info.put("Total", diskTotalStr);
    	disk_info.put("UsedPerc", diskUsedPercStr);
    	
    	JSONObject info=new JSONObject();
    	info.put("cpu", cpu_info);
    	info.put("mem", mem_info);
    	info.put("disk", disk_info);
    	
    	JSONObject rootjb=new JSONObject();
    	rootjb.put("data", info);
    	rootjb.put("timestamp", timestamp/1000);
    	rootjb.put("suffix", "machineCollecting."+sigar.getNetInfo().getHostName().replace(".", "-"));
    	
    	jsonArray.put(rootjb);
    	
    	return jsonArray;
    }
}
