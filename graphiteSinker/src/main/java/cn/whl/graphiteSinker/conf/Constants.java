package cn.whl.graphiteSinker.conf;

/**
 * Created by didi on 2017/6/12.
 */
public class Constants {

    //----------------------解析使用-------------------
    public  static final  String PATH_SPLITE=".";
    public  static final  String SPACE_SPLITE=" ";
    public  static final  String REPLACE_SPLITE="_";
    public  static final  String SP_SPLITE="/";
    public  static final  String REPLACE_SP_SPLITE="|";
    public  static final  String ENTER_SPLITE="\n";

    //----------------------消费使用-------------------
    public  static final  String BOOTSTRAP_SERVERS="115.159.34.36:9092";
    public  static final  String GROUP_ID="graphite";
    public  static final  String TOPIC_NAME="machineCollecting";



    //----------------------发送使用------------------
    public  static final  String GRAPHITE_HOST="101.200.54.51";
    public  static final  int GRAPHITE_PORT=2003;

}
