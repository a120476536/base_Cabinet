package com.linji.mylibrary.utils.speech;

import android.content.Context;
import android.util.Log;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;

import java.io.IOException;

public class SpeechUtil {
    private static final String TAG = "Speech";

    public static void initSpeech(Context context, String authSn) {
        SpeechSynthesizer mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context); // this 是Context的之类，如Activity
        mSpeechSynthesizer.setAppId("40635709"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
        mSpeechSynthesizer.setApiKey("7W9gGMzDpuV4eLA0gXTxKSM8", "OftufjCRDBsr0lmFWpZ0GfzZYoW6buhz"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
        // 包名填写在 app/build.gradle
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUTH_SN, authSn);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0"); // 设置发声的人声音，在线生效
        OfflineResource offlineResource = createOfflineResource(context, OfflineResource.VOICE_FEMALE);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename()); // 设置发声的人声音，在线生效
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename()); // 设置发声的人声音，在线生效
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 检测参数，通过一次后可以去除，出问题再打开debug
        int i = mSpeechSynthesizer.initTts(TtsMode.OFFLINE);
        Log.d(TAG,i+"");
    }

    public static OfflineResource createOfflineResource(Context context, String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.d(TAG, "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }
}
