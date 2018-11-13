package com.mmall.Socket;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/** 未付款账单提示
 * @author Huang YiCai
 * @create 2018/11/12  9:44
 */
@ServerEndpoint("/websocket")
public class ExpressWebSocket {
    /**
     * 创建list集合存登录名
     */
    private SysUserInfo sysUserInfo;
    private static List<Session> sessions =new ArrayList<Session>();
    private static List<SysUserInfo> names = new ArrayList<SysUserInfo>();

    private static Map<SysUserInfo, Session> map = new HashMap<SysUserInfo, Session>();

    //历史信息
//  private static List<String> mesageSession = new ArrayList<String>();
//  private String msg;
    private Gson gson = new Gson();
    /**
     * 打开连接
     * @param session
     */
    @OnOpen
    public void open(Session session) throws IOException {
        String queryString = session.getQueryString();
        Integer id = Integer.parseInt(queryString.split("=")[1]);
        SysUserInfoMapper sysUserInfoMapper = ApplicationContextHelper.getBeanClass(SysUserInfoMapper.class);
        sysUserInfo = sysUserInfoMapper.selectById(id);
        names.add(sysUserInfo);
        sessions.add(session);
        map.put(this.sysUserInfo, session);
    }

    /**
     * 关闭
     * @param session
     */
    @OnClose
    public void close(Session session){
        System.out.println("session关闭:"+session.getId());
        names.remove(this.sysUserInfo);
        sessions.remove(session);
    }

    /**
     * 消息
     * @param session
     * @param ms
     */
    @OnMessage
    public void message(Session session,String ms){
//      this.msg=ms;
//      mesageSession.add(ms);
        //将前台的str转为contentVO
      /*  ContentVO contentVO = gson.fromJson(ms, ContentVO.class);
        if(contentVO.getType()==1){
            //群聊
            UserNameVO nameVO = new UserNameVO();
            nameVO.setFrom(this.userName);
            nameVO.setMessage(contentVO.getMsg());
            nameVO.setTime(TimeUtile.getFormatTime("yyyy年mm月dd日   HH:MM:ss"));
            nameVO.setType(1);
            echBase(sessions, gson.toJson(nameVO));
        }else{
            //单聊
            //要发给谁
            String to = contentVO.getTo();
            Session to_session = map.get(to);
            UserNameVO nameVO = new UserNameVO();
            nameVO.setFrom(this.userName);
            nameVO.setMessage(contentVO.getMsg());
            nameVO.setTime(TimeUtile.getFormatTime("yyyy年mm月dd日   HH:MM:ss"));
            nameVO.setType(2);
            try {
                to_session.getBasicRemote().sendText(gson.toJson(nameVO));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/


    }


    /**
     * 发送未付款账单消息
     */
    public static void sendMsg(SysUserInfo sysUserInfo, Total total,Integer type){
        Session session = map.get(sysUserInfo);
        if(session==null)return;
        boolean open = session.isOpen();
        if(!open) return;
        Gson gson = new Gson();
        Map<Object, Object> map = Maps.newHashMap();
        map.put("type",type);//未付款账单(1=未付款，2=待确认)
        map.put("bill",total);
        try {
            session.getBasicRemote().sendText(gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送未付款账单消息
     */
    public static void sendMsgTotals(SysUserInfo sysUserInfo, List<Total> totals){
        Session session = map.get(sysUserInfo);
        if(session==null)return;
        boolean open = session.isOpen();
        if(!open) return;
        Gson gson = new Gson();
        Map<Object, Object> map = Maps.newHashMap();
        map.put("type",3);//未付款账单(1=未付款，2=待确认,3=批量发送)
        map.put("bill",totals);
        try {
            session.getBasicRemote().sendText(gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工单消息
     */
    public static void sendMsgAddServices(SysUserInfo sysUserInfo,String content,String wayNum,Integer type){
        Session session = map.get(sysUserInfo);
        if(session==null)return;
        boolean open = session.isOpen();
        if(!open) return;
        Gson gson = new Gson();
        Map<Object, Object> map = Maps.newHashMap();
        map.put("type",type);//工单提示
        map.put("content",content);//提示内容
        map.put("wayNum",wayNum);//运单号
        try {
            session.getBasicRemote().sendText(gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 将对象转为json
     * @param msg
     */
    private void echBase(List<Session> ss, String msg){
        for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
            Session session = (Session) iterator.next();
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
