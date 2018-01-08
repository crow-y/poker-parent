package org.tc.moonlighting.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Crow on 2018/1/8.
 *
 * @author Crow
 */
@ServerEndpoint(value = "/websocket")
@Component
public class GameLobby {
    private static final Logger LOG = LoggerFactory.getLogger(GameLobby.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的GameLobby对象。
    private static CopyOnWriteArrayList<GameLobby> webSocketSet = new CopyOnWriteArrayList<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        LOG.info("有新连接加入！当前在线人数为：" + getOnlineCount());
        try {
            sendMessage("当前大厅人数：" + getOnlineCount());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlieCount();
        LOG.info("有一个玩家退出！当前在线人数为：" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("来自客户端的消息：" + message);

        // 群发消息
        webSocketSet.forEach(x -> {
            try {
                x.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("IO异常");
            }
        });
    }

    /**
     * 发生错误时调用
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
        LOG.info("发生错误");
        t.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        GameLobby.onlineCount++;
    }

    public static synchronized void subOnlieCount() {
        GameLobby.onlineCount--;
    }
}
