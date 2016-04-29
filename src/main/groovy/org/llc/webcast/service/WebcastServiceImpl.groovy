package org.llc.webcast.service

import groovy.util.logging.Log
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor
import org.llc.webcast.config.FfmpegConfig
import org.llc.webcast.domain.ApplicationState
import org.llc.webcast.domain.DesiredState
import org.llc.webcast.domain.OperationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * Created by steve on 10/25/2015.
 */
@Service
@Log
class WebcastServiceImpl implements WebcastService, WebcastObserver {

    private static final int MAX_STREAM_TIME = 4 * 60 * 60 * 1000 // 4 hours

    DesiredState desiredState = DesiredState.STOPPED
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
        resultHandler.webcastObservers << this
        executor = new DefaultExecutor(
                watchdog: watchdog
        )
    }

    @Override
    OperationResponse start() {
        desiredState = DesiredState.STARTED
        resultHandler.isRunning = true
        executor.execute(cmdLine, resultHandler)
        return new OperationResponse(
                message: ApplicationState.STARTED.toString(),
                state: ApplicationState.STARTED,
                success: true
        )
    }

    @Override
    OperationResponse stop() {
        desiredState = DesiredState.STOPPED
        if (resultHandler.isRunning) {
            watchdog.destroyProcess()
        }
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

    @Override
    void update(ApplicationState applicationState) {
        log.info "update application state : $applicationState"
//        switch (applicationState) {
//            case ApplicationState.STARTED:
//                if (desiredState == DesiredState.STOPPED) {
//                    log.info "stopping services.."
//                    stop()
//                }
//                break
//            case ApplicationState.STOPPED:
//            case ApplicationState.ERROR:
//                if (desiredState == DesiredState.STARTED) {
//                    log.info "restarting services.."
//                    start()
//                }
//                break
//            case ApplicationState.UNKNOWN:
//                break
//        }
    }
}
