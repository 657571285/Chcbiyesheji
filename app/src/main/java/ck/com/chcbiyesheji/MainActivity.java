package ck.com.chcbiyesheji;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private RelativeLayout wenshidu;
    private RelativeLayout kaiguan;
    private RelativeLayout wode;

    private ImageView wsd, kg, wd;
    private TextView t1, t2, t3, t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sy, new WenShidu()).commit();
        }
        setContentView(R.layout.activity_main);

        wenshidu=(RelativeLayout)findViewById(R.id.wenshidu);
        kaiguan=(RelativeLayout)findViewById(R.id.kaiguan);
        wode=(RelativeLayout)findViewById(R.id.grzx);

        wsd=(ImageView)findViewById(R.id.shouye_wendu);
        kg=(ImageView)findViewById(R.id.shouye_kaiguan);
        wd=(ImageView)findViewById(R.id.shouye_grzx);

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);

        wenshidu.setOnClickListener(new MyOnClickListener(1));
        kaiguan.setOnClickListener(new MyOnClickListener(2));
        wode.setOnClickListener(new MyOnClickListener(3));
        wsd.setImageResource(R.drawable.wendutc);
        t1.setTextColor(Color.rgb(50, 150, 10));
    }
    public class MyOnClickListener implements View.OnClickListener{

        int index;

        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View view) {
            if(index==1){
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.sy,new WenShidu()).commit();
                wsd.setImageResource(R.drawable.wendutc);
                kg.setImageResource(R.drawable.kaiguan);
                wd.setImageResource(R.drawable.wode);

                t1.setTextColor(Color.rgb(50,150,10));
                t2.setTextColor(Color.rgb(0,0,0));
                t3.setTextColor(Color.rgb(0,0,0));
            }
            if(index==2){
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.sy,new KaiGuan()).commit();
                wsd.setImageResource(R.drawable.wendu);
                kg.setImageResource(R.drawable.kaiguantc);
                wd.setImageResource(R.drawable.wode);

                t2.setTextColor(Color.rgb(50,150,10));
                t1.setTextColor(Color.rgb(0,0,0));
                t3.setTextColor(Color.rgb(0,0,0));
            }
            if(index==3){
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.sy,new WoDe()).commit();
                wsd.setImageResource(R.drawable.wendu);
                kg.setImageResource(R.drawable.kaiguan);
                wd.setImageResource(R.drawable.wodetc);

                t3.setTextColor(Color.rgb(50,150,10));
                t1.setTextColor(Color.rgb(0,0,0));
                t2.setTextColor(Color.rgb(0,0,0));
            }

        }
    }
}
