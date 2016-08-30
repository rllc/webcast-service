package org.llc.webcast.config

import groovy.util.logging.Log
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Created by steve on 1/3/2016.
 */
@Component
@ConfigurationProperties(prefix = "ffmpeg")
@Log
class FfmpegConfig {

    String home
    String inputFormat
    String input
    String gop
    String audioCodec
    String audioBitrate
    String audioSamplingFrequency
    String numberOfAudioOutputChannels
    String outputFormat
    String outputStream
    boolean enableExperimental

    public String buildCommand() {

        def sb = new StringBuilder()
        sb << "$home/bin/ffmpeg.exe "
        sb << "-re -f $inputFormat "
        sb << "-i $input "
        sb << "-g $gop "
        sb << "-map_channel 0.0.0 "
        if (enableExperimental) {
            sb << "-strict experimental "
        }
        sb << "-acodec $audioCodec "
        sb << "-ab $audioBitrate "
        sb << "-ar $audioSamplingFrequency "
        sb << "-ac $numberOfAudioOutputChannels "
        sb << "-f $outputFormat "
        sb << " $outputStream"

        log.info("ffmpeg command : ${sb.toString()}")

        return sb.toString()
    }

}
