package org.llc.webcast.service

import groovy.util.logging.Log
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteException
import org.apache.commons.exec.ExecuteWatchdog

/**
 * Created by steve on 10/25/2015.
 */
@Log
class StreamResultHandler extends DefaultExecuteResultHandler {

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
        setRunning(false);
        log.info("[resultHandler] The stream completed with exit value [$exitValue]");
    }

    public void onProcessFailed(ExecuteException e) {
        super.onProcessFailed(e);
        setRunning(false);
        if (watchdog != null && watchdog.killedProcess()) {
            log.info("[resultHandler] The stream process timed out");
        } else {
            log.info("[resultHandler] The stream process failed to do : " + e.getMessage());
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
