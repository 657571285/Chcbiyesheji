package ck.com.chcbiyesheji;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by CK on 2016/11/29.
 */
public class WenShidu extends Fragment{

    TextView wendu,shidu;
    EditText IPText;
    private boolean isConnecting = false;
    private boolean onflag = false;
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
    static BufferedReader mBufferedReaderServer	= null;
    static PrintWriter mPrintWriterServer = null;
    static InputStream mBufferedReaderClient	= null;
    static PrintWriter mPrintWriterClient = null;
    private  String recvMessageClient = "";
    private  String recvMessageServer = "";
    int hum=0;
    int tem=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wenshidu,null);

        IPText=(EditText)view.findViewById(R.id.main2ip);
        wendu=(TextView)view.findViewById(R.id.wenduxianshi);
        shidu=(TextView)view.findViewById(R.id.shiduxianshi);

        IPText.setText("192.168.1.212:2112");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lianjie();
    }

    private void lianjie(){
        if(isConnecting){
            isConnecting=false;
            try{
                if(mSocketClient!=null)
                {
                    mSocketClient.close();
                    mSocketClient = null;

                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            mThreadClient.interrupt();
            IPText.setEnabled(true);
        }else{
            isConnecting = true;
            IPText.setEnabled(false);
            mThreadClient = new Thread(mRunnable);
            mThreadClient.start();
        }
    }
    //线程:监听服务器发来的消息
    private  Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            String msgText =IPText.getText().toString();
            if(msgText.length()<=0)
            {
                recvMessageClient = "IP can't be empty!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            int start = msgText.indexOf(":");
            if( (start == -1) ||(start+1 >= msgText.length()) )
            {
                recvMessageClient = "IP address is error!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            String sIP = msgText.substring(0, start);
            String sPort = msgText.substring(start+1);
            int port = Integer.parseInt(sPort);

            Log.d("gjz", "IP:"+ sIP + ":" + port);

            try
            {
                //连接服务器
                mSocketClient = new Socket(sIP, port);	//portnum
                //取得输入、输出流
                mBufferedReaderClient = mSocketClient.getInputStream();
                mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
                recvMessageClient = "connected to server!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                //break;
            }
            catch (Exception e)
            {
                recvMessageClient = "connecting IP is error:" + e.toString() + e.getMessage() + "\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            byte[] buffer = new byte[1024];
            int count = 0;
            while (isConnecting)
            {
                try
                {
                    mBufferedReaderClient.read(buffer);
                    Message message = new Message(); // 通知界面
                    message.what = 2;
                    message.obj = buffer;
                    mHandler.sendMessage(message);
                }
                catch (Exception e)
                {
                }
            }
        }
    };
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 2:
                    byte buffer[] = (byte[])msg.obj;
                    refreshView(buffer); // 接收到数据后显示
                    break;


            }
        }
        private void refreshView(byte[] buffer) {
            hum =  (buffer[0]&0xff);
            tem =  (buffer[2]&0xff);
            wendu.setText("温度:"+tem+"℃");
            shidu.setText("湿度:"+hum+"%");
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        if (isConnecting)
        {
            isConnecting = false;
            try {
                if(mSocketClient!=null)
                {
                    mSocketClient.close();
                    mSocketClient = null;

                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mThreadClient.interrupt();
        }
    }
}
