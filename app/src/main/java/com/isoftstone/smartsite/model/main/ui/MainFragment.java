package com.isoftstone.smartsite.model.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.MainActivity;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.patroltask.ui.PatroPlanDetailsActivity;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.muckcar.ui.SlagcarInfoActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.DataQueryBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.MobileHomeBean;
import com.isoftstone.smartsite.model.inspectplan.activity.ApprovalPendingInspectPlansActivity;
import com.isoftstone.smartsite.model.inspectplan.activity.PatrolPlanActivity;
import com.isoftstone.smartsite.model.map.ui.ConstructionMonitorMapActivity;
import com.isoftstone.smartsite.model.map.ui.ConstructionSummaryActivity;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.utils.NetworkUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.utils.Utils;


/**
 * Created by zw on 2017/10/11.
 * <p>
 * <p>
 * 开发版SHA1 ： 17:02:19:67:57:D4:F4:AF:3E:AE:22:1F:95:65:9A:27:FD:F7:8D:D0
 * <p>
 * 17:02:19:67:57:D4:F4:AF:3E:AE:22:1F:95:65:9A:27:FD:F7:8D:D0;com.isoftstone.smartsite
 * <p>
 * <p>
 * 发布版SHA1 ： C3:83:83:56:68:FD:2B:BC:EE:BB:16:AF:BA:52:EC:6A:C9:24:19:D5
 * <p>
 * C3:83:83:56:68:FD:2B:BC:EE:BB:16:AF:BA:52:EC:6A:C9:24:19:D5;com.isoftstone.smartsite
 */

public class MainFragment extends BaseFragment {

    private TextView mCityTestView = null;
    private TextView mWeatherTextView = null;
    private TextView mTemperatureTextView = null;
    private TextView lab_main_unread_num = null;  //未查看消息数目
    private TextView lab_main_unHandlerTask_num = null;  //待处理任务数目
    private TextView lab_report_unread_num = null;  //未查看报告数目
    private TextView lab_vcr_unread_num = null;//视频监控设备数
    private TextView lab_air_unread_num = null;//环境监控数目
    private HttpPost mHttpPost = new HttpPost();
    private View mVideoMonitoring = null; //视频监控
    private View mAirMonitoring = null;                //环境监测
    private View mThirdPartReport = null;             //三方协同按钮
    private View mInspectPlan = null;             //巡查计划
    private View mDircar = null;//渣土车监控

    private LinearLayout mVideoMonitoringMsg = null;    //视频监控设备
    private LinearLayout mAirMonitoringMsg = null;      //环境监控设备
    //private LinearLayout mUnCheckMsg = null;            //未查看消息点击区域
    private LinearLayout mUntreatedReport = null;       //待处理报告点击区域
    private LinearLayout mUnHandlerTask = null;         //待处理任务点击
    private ListView mListView = null;
    private ImageView wuran_image = null;
    private ImageView wuran_icon = null;
    private TextView wuran_text = null;
    private TextView wuran_number = null;
    private TextView shidu_textview = null;
    private TextView fengxiang_textview = null;

    public static final int HANDLER_GET_HOME_DATA_START = 1;
    public static final int HANDLER_GET_HOME_DATA_END = 2;
    private MobileHomeBean mMobileHomeBean = null;
    private View mConstructionMonitor;

    /* 查询请求识别码 登陆成功*/
    private static final int LOGIN_RESULTS_SUCCESSFUL_CODE = 1;
    /* 查询请求识别码 登陆失败*/
    private static final int LOGIN_RESULTS_FAILED_CODE = 2;
    /* 查询请求识别码 登陆异常*/
    private static final int LOGIN_RESULTS_EXCEPTION_CODE = 3;
    private int mLoginResultCode = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        mCityTestView = (TextView) rootView.findViewById(R.id.text_city);
        lab_main_unHandlerTask_num = (TextView) rootView.findViewById(R.id.lab_main_unhandlertask_num);  //未查看消息数目
        lab_main_unHandlerTask_num.setVisibility(View.INVISIBLE);
        //lab_main_unread_num = (TextView) rootView.findViewById(R.id.lab_main_unread_num);  //未查看消息数目
        //lab_main_unread_num.setVisibility(View.INVISIBLE);
        lab_report_unread_num = (TextView) rootView.findViewById(R.id.lab_report_unread_num);  //未查看报告数目
        lab_report_unread_num.setVisibility(View.INVISIBLE);
        lab_vcr_unread_num = (TextView) rootView.findViewById(R.id.lab_vcr_unread_num);//视频监控设备数
        lab_vcr_unread_num.setVisibility(View.INVISIBLE);
        lab_air_unread_num = (TextView) rootView.findViewById(R.id.lab_air_unread_num);//环境监控数目
        lab_air_unread_num.setVisibility(View.INVISIBLE);
        mTemperatureTextView = (TextView) rootView.findViewById(R.id.text_temperature);

        /*mUnCheckMsg = (LinearLayout) rootView.findViewById(R.id.textView10);
        mUnCheckMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterUnChekMsg();
            }
        });*/
        mUnHandlerTask= (LinearLayout) rootView.findViewById(R.id.textView9);
        mUnHandlerTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPatrolMission();
            }
        });
        mUntreatedReport = (LinearLayout) rootView.findViewById(R.id.textView11);
        mUntreatedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdPartReport();
            }
        });
        mVideoMonitoringMsg = (LinearLayout) rootView.findViewById(R.id.textView12);
        mVideoMonitoringMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterVideoMonitoring();
            }
        });
        mAirMonitoringMsg = (LinearLayout) rootView.findViewById(R.id.textView13);
        mAirMonitoringMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAirMonitoringMsg();
            }
        });
        //视频监控
        mVideoMonitoring = rootView.findViewById(R.id.button_1);
        mVideoMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterVideoMonitoring();
            }
        });
        //环境监控
        mAirMonitoring = rootView.findViewById(R.id.button_2);
        mAirMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAirMonitoring();
            }
        });
        //三方协同
        mThirdPartReport = rootView.findViewById(R.id.button_3);
        mThirdPartReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdPartReport();
            }
        });
        // 渣土车监控
        mDircar = rootView.findViewById(R.id.button_4);
        mDircar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDircar();
            }
        });
        //巡查概况
        rootView.findViewById(R.id.button_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPatrolSurvey();
            }
        });
        //巡查计划
        mInspectPlan = rootView.findViewById(R.id.button_6);
        mInspectPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterInspectPlan();
            }
        });
        //巡查任务
        rootView.findViewById(R.id.button_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPatrolMission();
            }
        });
        //巡查监控
        mConstructionMonitor = rootView.findViewById(R.id.button_8);
        mConstructionMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterConstructionMonitor();
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked();
            }
        });
        wuran_image = (ImageView) rootView.findViewById(R.id.wuran_image);
        wuran_icon = (ImageView) rootView.findViewById(R.id.wuran_icon);
        wuran_text = (TextView) rootView.findViewById(R.id.wuran_text);
        wuran_number = (TextView) rootView.findViewById(R.id.wuran_number);
        shidu_textview = (TextView) rootView.findViewById(R.id.shidu_textview);
        fengxiang_textview = (TextView) rootView.findViewById(R.id.fengxiang_textview);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_GET_HOME_DATA_START: {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            mMobileHomeBean = mHttpPost.getMobileHomeData();
                            mHandler.sendEmptyMessage(HANDLER_GET_HOME_DATA_END);
                        }
                    };
                    thread.start();
                }
                break;
                case HANDLER_GET_HOME_DATA_END: {
                    setMobileHomeDataToView();
                }
                break;
            }
        }
    };

    private void setWeatherData() {
        //设置天气情况
        DataQueryBean dataQueryBean = mMobileHomeBean.getAvgEqis();
        if (dataQueryBean != null) {
            int AQI = mMobileHomeBean.getAQI();
            if (AQI < 50) {
                wuran_image.setBackgroundResource(R.drawable.wuran_you_jingdu);
                wuran_icon.setBackgroundResource(R.drawable.main_aqi);
                wuran_text.setText("优");
            } else if (AQI < 100 && AQI >= 50) {
                wuran_image.setBackgroundResource(R.drawable.wuran_liang_jingdu);
                wuran_icon.setBackgroundResource(R.drawable.main_aqi);
                wuran_text.setText("良");
            } else if (AQI < 200 && AQI >= 100) {
                wuran_image.setBackgroundResource(R.drawable.wuran_qingdu_jingdu);
                wuran_icon.setBackgroundResource(R.drawable.main_aqi);
                wuran_text.setText("轻度污染");
            } else if (AQI < 300 && AQI >= 200) {
                wuran_image.setBackgroundResource(R.drawable.wuran_zhong1du_jingdu);
                wuran_icon.setBackgroundResource(R.drawable.main_aqi);
                wuran_text.setText("中度污染");
            } else if (AQI >= 300) {
                wuran_image.setBackgroundResource(R.drawable.wuran_zhongdu_jingdu);
                wuran_icon.setBackgroundResource(R.drawable.main_aqi);
                wuran_text.setText("重度污染");
            }
            wuran_number.setText("" + AQI);
            mTemperatureTextView.setText(dataQueryBean.getAirTemperature() + "");
            shidu_textview.setText(dataQueryBean.getAirHumidity() + "%");
            fengxiang_textview.setText(dataQueryBean.getWindDirection());
        }
    }

    private void setMobileHomeDataToView() {
        mCityTestView.setText("武汉");
        if (mMobileHomeBean == null) {
            return;
        }
        /*
        int unreadMessage = mMobileHomeBean.getUnreadMessages();
        if (unreadMessage > 0) {
            lab_main_unread_num.setVisibility(View.VISIBLE);
            lab_main_unread_num.setText(unreadMessage + "");
        }
        */
        int unHandlerTask = mMobileHomeBean.getUnHandleTask();
        if (unHandlerTask > 0) {
            lab_main_unHandlerTask_num.setVisibility(View.VISIBLE);
            lab_main_unHandlerTask_num.setText(unHandlerTask + "");
        }
        int unreadPatrols = mMobileHomeBean.getUntreatedPatrols();
        if (unreadPatrols > 0) {
            lab_report_unread_num.setVisibility(View.VISIBLE);
            lab_report_unread_num.setText(unreadPatrols + "");
        }
        lab_vcr_unread_num.setText(mMobileHomeBean.getAllVses() + "");//视频监控设备数
        lab_vcr_unread_num.setVisibility(View.VISIBLE);
        lab_air_unread_num.setText(mMobileHomeBean.getAllEmes() + "");//环境监控数目
        lab_air_unread_num.setVisibility(View.VISIBLE);
        //设置天气参数
        setWeatherData();
        //设置即使消息
        InstantMessageAdapter adapter = new InstantMessageAdapter(getContext());
        adapter.setData(mMobileHomeBean.getMessages());
        mListView.setAdapter(adapter);
    }


    private void onItemClicked() {
//        ThreePartyData data = new ThreePartyData();
//        data.setType(ThreePartyData.TYPE_RECEIVE_REPORT);
//        data.setName("张珊");
//        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//        intent.putExtra(MsgFragment.FRAGMENT_TYPE, MsgFragment.FRAGMENT_TYPE_SYNERGY);
//        intent.putExtra(MsgFragment.FRAGMENT_DATA, data);
//        getActivity().startActivity(intent);
    }

    private void enterUnChekMsg() {
        //进入未查看消息
        ((MainActivity) getActivity()).setCurrentTab(2);
    }

    private void enterUntreatedReport() {
        //进入未处理报告
        ((MainActivity) getActivity()).setCurrentTab(2);
    }

    private void enterAirMonitoringMsg() {
        Intent intent = new Intent(getActivity(), PMDevicesListActivity.class);
        getActivity().startActivity(intent);
    }

    private void enterThirdPartReport() {
        //进入三方协同
        /*((MainActivity)getActivity()).setCurrentTab(2);*/
        //yanyongjun 这个地方应该是进到三方协同界面，而不是到消息fragment页
        Intent intent = new Intent(getActivity(), TripartiteActivity.class);
        startActivity(intent);
    }

    private void enterVideoMonitoring() {
        //进入视频监控

        if (!NetworkUtils.isConnected()){
            ToastUtils.showShort(mContext.getText(R.string.network_can_not_be_used_toast).toString());
            return;
        }

        if (!HttpPost.mVideoIsLogin) {
            //Utils.LogginVideoTask logginVideoTask = new Utils.LogginVideoTask((BaseActivity)getActivity(), Utils.ENTER_VIDEO_DEVICE_LIST, null);
            //logginVideoTask.execute();
            Utils.showInitVideoServerDialog((BaseActivity)mContext, Utils.ENTER_VIDEO_DEVICE_LIST, null);
        } else {
            Intent intent = new Intent(getActivity(), VideoMonitoringActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 进入渣土车监控
     */
    private void enterDircar() {
        Intent intent = new Intent(getActivity(), SlagcarInfoActivity.class);
        getActivity().startActivity(intent);
    }
    /*
     进入巡查概况
     */
    private void enterPatrolSurvey(){
        Intent intent = new Intent(getActivity(), ConstructionSummaryActivity.class);
        getActivity().startActivity(intent);
    }

    private void enterAirMonitoring() {
        //进入环境监控
        Intent intent = new Intent(getActivity(), AirMonitoringActivity.class);
        getActivity().startActivity(intent);
    }

    private void enterInspectPlan() {
        //进入巡查计划
        boolean isHasPermission = false;

        try {

            isHasPermission = HttpPost.mLoginBean.getmUserBean().getmPermission().isM_CPPA();

        } catch (Exception e) {

            isHasPermission = false;
            Log.i(TAG,"access permission exception : " + e.getMessage());

        } finally {

            if (isHasPermission) {
                //有审批计划权限
                Intent intent = new Intent(getActivity(), ApprovalPendingInspectPlansActivity.class);
                getActivity().startActivity(intent);
            } else {
                //没有审批计划权限
                Intent intent = new Intent(getActivity(), PatrolPlanActivity.class);
                getActivity().startActivity(intent);
            }

        }
        //Intent intent = new Intent(getActivity(), SelectInspectorsActivity.class);
        //getActivity().startActivity(intent);
    }

    private void enterPatrolMission() {
        //进入巡查任务
        Intent intent = new Intent(getActivity(), PatroPlanDetailsActivity.class);
        getActivity().startActivity(intent);
    }

    private void enterConstructionMonitor() {
        //进入施工监控
        Intent intent = new Intent(getActivity(), ConstructionMonitorMapActivity.class);
//        Intent intent = new Intent(getActivity(), MapSearchTaskPositionActivity.class);
        getActivity().startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(HANDLER_GET_HOME_DATA_START);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
