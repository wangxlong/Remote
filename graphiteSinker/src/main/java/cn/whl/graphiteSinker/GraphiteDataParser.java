package cn.whl.graphiteSinker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.whl.graphiteSinker.bean.ReceiverResult;
import cn.whl.graphiteSinker.bean.SenderResult;
import cn.whl.graphiteSinker.conf.Constants;
import org.apache.log4j.Logger;


import java.util.*;


/**
 * Created by didi on 2017/6/15.
 */
public class GraphiteDataParser implements  Runnable{


    private static Logger logger=Logger.getLogger(GraphiteDataParser.class);

    /**
     * 解析jsonarray 数据
     * @param message,timestamps(单位：秒)
     * @return
     */
    public List<String> parseJsonArrayMessage(String message)  {

        JSONArray jsonArrayMessage= JSON.parseArray(message);
        List<String> rootParsedMessageList=new LinkedList<String>();

        Iterator<Object> messageIterators=jsonArrayMessage.iterator();
        while (messageIterators.hasNext()){
            //解析最外层的
            JSONObject messageObject=(JSONObject)messageIterators.next();
            JSONObject data=(JSONObject) messageObject.get("data");
            Long timestamp= Long.valueOf((Integer)messageObject.get("timestamp"));
            StringBuilder suffix=new StringBuilder((String) messageObject.get("suffix"));
            suffix.append(Constants.PATH_SPLITE);
            //解析data
            List<String> childParsedMessageList=new LinkedList<String>();
            innerUsedParseJsonObjectMessage(data,childParsedMessageList,suffix,timestamp);//childParsedMessageList是引用类型的，传地址

            for (String line:childParsedMessageList){
                rootParsedMessageList.add(line);
            }
        }
        return rootParsedMessageList;

    }

    /**
     * 内部解析jsonobject数据使用,key是string
     * @return
     */
    private void innerUsedParseJsonObjectMessage(JSONObject jsonObject, List<String> parsedMessageList, StringBuilder parentPath, long timestamps)  {

        if(!(jsonObject instanceof JSONObject)){
            logger.error("parameter is not a JSONObject instance");
//            throw new Exception("传入的参数不是JSONObject对象");
        }
        Set<String> keys= jsonObject.keySet();
        for(String key:keys){
            Object value=jsonObject.get(key);
            StringBuilder currentPath=new StringBuilder();
            currentPath.append(parentPath.toString());
            if(value instanceof JSONObject){
                if(key.contains(Constants.PATH_SPLITE)){
                    key=key.replace(Constants.PATH_SPLITE,Constants.REPLACE_SPLITE);
                }
                if(key.contains(Constants.SP_SPLITE)){
                    key=key.replace(Constants.SP_SPLITE,Constants.REPLACE_SP_SPLITE);
                }
                currentPath.append(key).append(Constants.PATH_SPLITE);
                innerUsedParseJsonObjectMessage((JSONObject)value,parsedMessageList,currentPath, timestamps);
            }else{
                if(key.contains(Constants.PATH_SPLITE)){
                    key=key.replace(Constants.PATH_SPLITE,Constants.REPLACE_SPLITE);
                }
                if(key.contains(Constants.SP_SPLITE)){
                    key=key.replace(Constants.SP_SPLITE,Constants.REPLACE_SP_SPLITE);
                }
                currentPath.append(key).append(Constants.SPACE_SPLITE).append(value).append(Constants.SPACE_SPLITE).append(timestamps).append(Constants.ENTER_SPLITE);
                parsedMessageList.add(currentPath.toString());
            }
        }

    }
    /**
     * 解析运行
     */
    public void run() {

            while(!Thread.currentThread().isInterrupted()){

                ReceiverResult receiverResult=null;
                try{
                    receiverResult=GraphiteMonitor.receiveBlockingQueue.take();

                }catch (InterruptedException e){
                    logger.error(e.getMessage());
                }finally {

                }

                int type=1;
                logger.info("receiverResult is "+receiverResult.toString());
                String messageType=receiverResult.getMessage();

                //判断是类别,1表示string，2表示jsonobject，3表示无效
                try {
                    JSONArray jsonArrayType= JSON.parseArray(messageType);
                    Object ob=jsonArrayType.get(0);
                    if(ob instanceof  JSONObject){
                        type=2;
                    }

                }catch (Exception e){
                    type=3;
                    logger.error(e.getMessage());

                }finally {

                }

                //解析
                List<String> pareseList=new LinkedList<String>();
                if(type==2){
                    logger.info("-----------------------2---------------");
                    pareseList=parseJsonArrayMessage(receiverResult.getMessage());

                }else if(type==1){
                    logger.info("-----------------------1---------------");
                    JSONArray jsonArrayMessage= JSON.parseArray(receiverResult.getMessage());
                    Iterator<Object> jsonArrayMessageIt = jsonArrayMessage.iterator();
                    while(jsonArrayMessageIt.hasNext()){
                        pareseList.add((String)jsonArrayMessageIt.next());
                    }
                }else if(type==3){
                    continue;
                }

                SenderResult senderResult=new SenderResult();
                senderResult.setMessageList(pareseList);
                senderResult.setOffset(receiverResult.getOffset());
                senderResult.setPartition(receiverResult.getPartition());
                senderResult.setTopic(receiverResult.getTopic());

                try{
                    GraphiteMonitor.sendBlockingQueue.put(senderResult);
                }catch (InterruptedException e){
                    logger.error(e.getMessage());
                }finally {

                }

                logger.info("senderResult is "+senderResult.toString());

//                Thread.sleep(5000); //睡面1分钟
            }


    }

}
