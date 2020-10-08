package kuu.atalk

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kuu.atalk.ATalkClient

class ATalkClientImpl
    (
    private val baseUrl:String
) : ATalkClient {
    suspend fun say(content:String){
        withContext(Dispatchers.IO){
            val url = "${baseUrl}?$content"
            val result = url.httpGet()
                .useHttpCache(true)
                .response()

            result.third.fold(
                success = {
                    AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build()
                        )
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                                .setSampleRate(8000)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(8000 * 2)
                        .setTransferMode(AudioTrack.MODE_STATIC)
                        .build()
                        .play()
                },
                failure = {

                }
            )
        }
    }
}