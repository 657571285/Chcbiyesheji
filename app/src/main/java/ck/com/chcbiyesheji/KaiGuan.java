package ck.com.chcbiyesheji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
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
        lianjie();
        return view;

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

        }
    }


}
