package com.hengxuan.stock.receiver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.hengxuan.stock.Activity.ChatMsgActivity;
import com.hengxuan.stock.Activity.JgmrActivity;
import com.hengxuan.stock.Activity.MainActivity;
import com.hengxuan.stock.Activity.MyjxActivity;
import com.hengxuan.stock.DataContract;
import com.hengxuan.stock.DatabaseHelper;
import com.hengxuan.stock.R;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.utils.Log;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	// ֪ͨչʾ
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		XGNotification notific = new XGNotification();
		notific.setMsg_id(notifiShowedRlt.getMsgId());
		notific.setTitle(notifiShowedRlt.getTitle());
		notific.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1ΪActivity��2Ϊurl��3Ϊintent
		notific.setNotificationActionType(notifiShowedRlt
				.getNotificationActionType());
		// Activity,url,intent������ͨ��getActivity()���
		notific.setActivity(notifiShowedRlt.getActivity());
		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
		context.sendBroadcast(intent);
		show(context, "����1������Ϣ, " + "֪ͨ��չʾ �� " + notifiShowedRlt.toString());
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "��ע��ɹ�";
		} else {
			text = "��ע��ʧ��" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"���óɹ�";
		} else {
			text = "\"" + tagName + "\"����ʧ��,�����룺" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"ɾ���ɹ�";
		} else {
			text = "\"" + tagName + "\"ɾ��ʧ��,�����룺" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	// ֪ͨ����ص� actionType=1Ϊ����Ϣ�������actionType=0Ϊ����Ϣ�����
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// ֪ͨ��֪ͨ�������������������
			// APP�Լ�����������ض���
			// �������������activity��onResumeҲ�ܼ������뿴��3���������
			text = "֪ͨ���� :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// ֪ͨ���������������
			// APP�Լ�����֪ͨ����������ض���
			text = "֪ͨ����� :" + message;
		}
		Toast.makeText(context, "�㲥���յ�֪ͨ�����:" + message.toString(),
				Toast.LENGTH_SHORT).show();
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP��������Ĺ��̡�����
		Log.d(LogTag, text);
		show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "ע��ɹ�";
			// ��������token
			String token = message.getToken();
		} else {
			text = message + "ע��ʧ�ܣ������룺" + errorCode;
		}
		Log.d(LogTag, text);
//		show(context, text);
	}

	// ��Ϣ͸��
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "�յ���Ϣ:" + message.toString();
        Log.i(text);
//        String msg = message.getContent();
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
        handleMsg(context, customContent);
	}

    private static final int NO_MR = 1;
    private static final int NO_MY = 2;
    private static final int NO_JG = 3;
    private static final int NO_JBYQ = 4;
    private static final int NO_NNW = 5;
    private static final int NO_BS = 6;

    private void notifyMsg(Context context,String content,Intent intent,int id){
        String title = context.getResources().getString(R.string.app_name);
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/coins");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(content).
                setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setSound(uri).setContentIntent(pendingIntent);
        Notification notification;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            notification = builder.build();
        }else {
            notification = builder.getNotification();
        }
        notificationManager.notify(id,notification);
    }
    private void handleMsg(Context context,String customContent){
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("type")) {
					String type = obj.getString("type");
                    String content = obj.getString("content");
                    String contentText = null;
                    Intent resultIntent = new Intent();
                    switch (type){
                        case "mr":
                            contentText = "���վ�ѡ��Ʊ�����ݴ������ھ�ֵ�ù�ע";
                            resultIntent.setClass(context, MyjxActivity.class);
                            notifyMsg(context,contentText,resultIntent,NO_MR);
                            break;
                        case "my":
                            contentText = "���¾�ѡ��Ʊ�����ݴ������ھ�ֵ�ù�ע";
                            resultIntent.setClass(context, MyjxActivity.class);
                            notifyMsg(context, contentText, resultIntent, NO_MY);
                            break;
                        case "jg":
                            contentText = "���������Ʊ�����ע";
                            resultIntent.setClass(context, JgmrActivity.class);
                            notifyMsg(context,contentText,resultIntent,NO_JG);
                            break;
                        case "NNSTOCK":
                            if(saveData2DB(context,"NNSTOCK",content) == -1){
                                Log.e("save msg to db fail!");
                            }
//                            contentTitle = "ţţ����";
                            JSONObject json = new JSONObject(content);
                            contentText = content;
                            resultIntent.setClass(context, ChatMsgActivity.class);
                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            resultIntent.putExtra("id", Constants.NNSTOCK_ID);
                            resultIntent.putExtra("content",content);
                            notifyMsg(context, contentText, resultIntent, NO_NNW);
                            break;
                        case "JBSTOCK":
                            if(saveData2DB(context,"JBSTOCK",content) == -1){
                                Log.e("save msg to db fail!");
                            }
//                            contentTitle = "�۱�һ��";
                            contentText = content;
                            resultIntent.setClass(context, ChatMsgActivity.class);
                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            resultIntent.putExtra("id", Constants.JBSTOCK_ID);
                            resultIntent.putExtra("content",content);
                            Log.d("contentText="+contentText);
                            notifyMsg(context,contentText,resultIntent,NO_JBYQ);
                            break;
                        case "bs":
                            String code = obj.getString("code");
                            String group = obj.getString("group");
                            contentText = content;
                            break;
                        default:
                            break;
                    }
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
            Log.w("message is wrong!");
        }
		// APP����������Ϣ�Ĺ���...
//        String content = message.getContent();

    }

    /**
     *
     * @param context
     * @param name
     * @param msg
     * @return return new row id for success,or return -1
     */
    private long saveData2DB(Context context,String name,String msg){
        long newRowId = -1;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String time = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss",System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.CpMsgEntry.COLUMN_NAME,name);
        contentValues.put(DataContract.CpMsgEntry.COLUMN_TIME,time);
        contentValues.put(DataContract.CpMsgEntry.COLUMN_CONTENT,msg);
        Log.i("try to inert a msg:" + msg + ",time:" + time);
        newRowId = db.insert(DataContract.CpMsgEntry.TABLE_NAME, null,contentValues);
        return newRowId;
    }


}
