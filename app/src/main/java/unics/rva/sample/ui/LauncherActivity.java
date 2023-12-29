package unics.rva.sample.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Create by luochao
 * on 2023/11/3
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button btn = new Button(this);
        btn.setText("启动乐家桌面");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    WifiManager wifiManager = (WifiManager) LauncherActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    Intent it = new Intent();
                    it.setComponent(new ComponentName("com.xinjing.launcher","com.xinjing.launcher.home.HomeActivity"));
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }catch (Exception e){
                    Toast.makeText(LauncherActivity.this,"启动乐家桌面失败:" + e,Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayout.addView(btn);
        addBtn(linearLayout, "启动测试界面", v -> {
//            Intent it = new Intent(LauncherActivity.this, EffectSampleActivity.class);
//            startActivity(it);
        });

        addBtn(linearLayout, "GridView", v -> {
            Intent it = new Intent(LauncherActivity.this, GridSampleActivity.class);
            startActivity(it);
        });


        setContentView(linearLayout);
    }

    private void addBtn(ViewGroup group,String text, View.OnClickListener onClick){
        Button btn = new Button(this);
        btn.setText(text);
        btn.setOnClickListener(onClick);
        group.addView(btn);
    }
}
