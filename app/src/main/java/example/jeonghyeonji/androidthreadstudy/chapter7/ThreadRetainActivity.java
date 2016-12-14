package example.jeonghyeonji.androidthreadstudy.chapter7;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import example.jeonghyeonji.androidthreadstudy.R;

public class ThreadRetainActivity extends AppCompatActivity {
    /**외부 클래스 참조를 피하기 위해 정적 내부 클래스로 선언된 작업자 스레드이다
     스레드는 Activity 인스턴스로서의 참조를 포함한다.
     attach 메서드는 현재 실행하는 객체에 액티비티 참조를 설정하기 위해 사용된다*/


    public static class MyThread extends Thread { /** 내부 클래스 */
        private ThreadRetainActivity mActivity;

        public MyThread(ThreadRetainActivity activity) {
            mActivity = activity;
        }

        private void attach(ThreadRetainActivity activity) {
            mActivity = activity;
        }

        @Override
        public void run() {
            final String text = getTextFromNetwork();
            mActivity.setText(text);
        }

        private String getTextFromNetwork() {
            //네트워크 동작 시뮬레이션
            SystemClock.sleep(5000);
            return "text form network";
        }
    }

    private static MyThread thread;
    private TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_reatain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        textview = (TextView) findViewById(R.id.text_retain);
        Object retainObject = getLastNonConfigurationInstance();
        /** 유지된 스레드 객체가 있으면 복구한다. */
        if (retainObject != null){
            thread = (MyThread) retainObject;
            thread.attach(this);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance(){
        /** 설정변경이 일어나기전에 실행하는  스레드를 유지한다. */
        if (thread != null && thread.isAlive()){
            return thread;
        }
        return null;
    }

    public void onClickStartThead(View v){
        thread = new MyThread(this);
        thread.start();
    }

    private  void setText (final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(text);
            }
        });
    }

}
