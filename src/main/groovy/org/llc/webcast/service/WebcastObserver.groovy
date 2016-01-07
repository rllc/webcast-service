package org.llc.webcast.service

import org.llc.webcast.domain.ApplicationState

/**
 * Created by Steven McAdams on 1/7/16.
 */
interface WebcastObserver {

    void update(ApplicationState applicationState)

}