package org.llc.webcast.service

import groovy.util.logging.Log
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteException
import org.apache.commons.exec.ExecuteWatchdog
import org.llc.webcast.domain.ApplicationState

/**
 * Created by steve on 10/25/2015.
 */
@Log
class StreamResultHandler extends DefaultExecuteResultHandler {

    List<WebcastObserver> webcastObservers = []

    ExecuteWatchdog watchdog
    boolean isRunning = false

    public StreamResultHandler(ExecuteWatchdog watchdog) {
        this.watchdog = watchdog;
    }

    public StreamResultHandler(int exitValue) {
        super.onProcessComplete(exitValue);
    }

    public void onProcessComplete(int exitValue) {
        super.onProcessComplete(exitValue);
        isRunning = false
        log.info("[resultHandler] The stream completed with exit value [$exitValue]");
        if (exitValue == 0) {
            notifyObservers(ApplicationState.STOPPED)
        } else {
            notifyObservers(ApplicationState.ERROR)
        }

    }

    public void onProcessFailed(ExecuteException e) {
        super.onProcessFailed(e);
        isRunning = false
        notifyObservers(ApplicationState.ERROR)

        if (watchdog != null && watchdog.killedProcess()) {
            log.info("[resultHandler] The stream process timed out");
        } else {
            log.info("[resultHandler] The stream process failed to do : " + e.getMessage());

        }
    }

    private void notifyObservers(ApplicationState applicationState) {
        webcastObservers.each {
            it.update(applicationState)
        }
    }
}
