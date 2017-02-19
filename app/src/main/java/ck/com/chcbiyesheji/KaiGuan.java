package ck.com.chcbiyesheji;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by CK on 2016/11/29.
 */
public class KaiGuan extends Fragment {
    Button led1btn,led2btn,led3btn;
    Button diancisuobtn;
    Button beerbtn;
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
    TextView guangzhao;
    int hum=0;
    int tem=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_kaiguan,null);

        led1btn=(Button) view.findViewById(R.id.led1kaiguan);
        led2btn=(Button)view.findViewById(R.id.led2kaiguan);
        led3btn=(Button)view.findViewById(R.id.led3kaiguan);

        beerbtn=(Button)view.findViewById(R.id.beerkaiguan);
        diancisuobtn=(Button)view.findViewById(R.id.diancisuokaiguan);

        led1btn.setOnClickListener(new led1());

        IPText.setText("192.168.1.212:2112");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lianjie();//在将要进入界面展示前连接
    }

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

    class led1 implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isConnecting&&mSocketClient!=null){
                String output = "1";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e){
                    Toast.makeText(getContext(),"还未连接"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if (mSocketClient == null) {
                Toast.makeText(getContext(), "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class led2 implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isConnecting&&mSocketClient!=null){
                String output = "2";
                try{
                    mPrintWriterClient.println();
                    mPrintWriterClient.flush();
                }catch (Exception e){
                    Toast.makeText(getContext(),"还未连接"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient==null){
                Toast.makeText(getContext(),"未连接",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class led3 implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isConnecting&&mSocketClient!=null){
                String output = "3";
                try{
                    mPrintWriterClient.println();
                    mPrintWriterClient.flush();
                }catch (Exception e){
                    Toast.makeText(getContext(),"还未连接"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient==null){
                Toast.makeText(getContext(),"未连接",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class diancisuo implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isConnecting&&mSocketClient!=null){
                String output = "4";
                try{
                    mPrintWriterClient.println();
                    mPrintWriterClient.flush();
                }catch (Exception e){
                    Toast.makeText(getContext(),"还未连接"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient==null){
                Toast.makeText(getContext(),"未连接",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class beer implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isConnecting&&mSocketClient!=null){
                String output = "5";
                try{
                    mPrintWriterClient.println();
                    mPrintWriterClient.flush();
                }catch (Exception e){
                    Toast.makeText(getContext(),"还未连接"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient==null){
                Toast.makeText(getContext(),"未连接",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void lianjie(){
        if(isConnecting)
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
                e.printStackTrace();
            }
            mThreadClient.interrupt();
            IPText.setEnabled(true);
        }else {
            isConnecting = true;
            IPText.setEnabled(false);
            mThreadClient = new Thread(mRunnable);
            mThreadClient.start();
            //	beerlianjiexianshi.setText("通信状态:已连接");
        }
    }

    private Runnable mRunnable = new Runnable(){
        public void run(){
            //String msgText="192.168.11.254:8080";
            String msgText =IPText.getText().toString();
            if(msgText.length()<=0)
            {
                //Toast.makeText(mContext, "IP不能为空！", Toast.LENGTH_SHORT).show();
                recvMessageClient = "IP can't be empty!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            int start = msgText.indexOf(":");
            if((start == -1)||(start+1 >= msgText.length())){
                recvMessageClient = "IP address is error!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            String sIP = msgText.substring(0,start);
            String sPort =msgText.substring(start+1);
            int port = Integer.parseInt(sPort);

            Log.d("gjz", "IP:"+ sIP + ":" + port);

            try{
                mSocketClient = new Socket(sIP,port);//连接服务器
                mBufferedReaderClient = mSocketClient.getInputStream();//取得输入、输出流
                mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(),true);
                recvMessageClient = "connected to server!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }catch (Exception e){
                recvMessageClient =  "connecting IP is error:" + e.toString() + e.getMessage() + "\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }

            byte[] buffer = new byte[1024];
            int count = 0;
            while(isConnecting){
                try{
                    mBufferedReaderClient.read(buffer);
                    Message message = new Message();//通知界面
                    message.what = 2;
                    message.obj = buffer;
                    mHandler.sendMessage(message);
                }catch (Exception e){

                }
            }
        }
    };

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 2:byte buffer[]=(byte[])msg.obj;
                    refreshView(buffer);
                    break;
            }
        }
        private void refreshView(byte[] buffer){
            hum = (buffer[0]&0xff);
            tem = (buffer[2]&0xff);
            //wendu.setText("温度:"+tem+"℃");
            //shidu.setText("湿度:"+hum+"%");
        }
    };


}
