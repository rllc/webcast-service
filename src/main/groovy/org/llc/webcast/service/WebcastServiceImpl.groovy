package org.llc.webcast.service

import groovy.util.logging.Log
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor
import org.llc.webcast.config.FfmpegConfig
import org.llc.webcast.domain.ApplicationState
import org.llc.webcast.domain.OperationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * Created by steve on 10/25/2015.
 */
@Service
@Log
class WebcastServiceImpl implements WebcastService {

    private static final int MAX_STREAM_TIME = 4 * 60 * 60 * 1000 // 4 hours

    ExecuteWatchdog watchdog
    CommandLine cmdLine
    Executor executor
    StreamResultHandler resultHandler

    @Autowired
    FfmpegConfig ffmpegConfig

    @PostConstruct
    protected void init() {
        cmdLine = CommandLine.parse(ffmpegConfig.buildCommand())

        watchdog = new ExecuteWatchdog(MAX_STREAM_TIME)
        resultHandler = new StreamResultHandler(watchdog)
        executor = new DefaultExecutor()
        executor.exitValue = 1
        executor.watchdog = watchdog
    }

    @Override
    OperationResponse start() {
        resultHandler.running = true
        executor.execute(cmdLine, resultHandler)
        return new OperationResponse(
                message: ApplicationState.STARTED.toString(),
                state: ApplicationState.STARTED,
                success: true
        )
    }

    @Override
    OperationResponse stop() {
        watchdog.destroyProcess()

        return new OperationResponse(
                message: ApplicationState.STOPPED.toString(),
                state: ApplicationState.STOPPED,
                success: true
        )
    }

    @Override
    OperationResponse status() {

        def state = ApplicationState.STOPPED
        def message = ApplicationState.STOPPED.toString()
        if (resultHandler.isRunning) {
            state = ApplicationState.STARTED
            message = ApplicationState.STARTED.toString()
        }

        return new OperationResponse(
                message: message,
                state: state,
                success: true
        )
    }
}
