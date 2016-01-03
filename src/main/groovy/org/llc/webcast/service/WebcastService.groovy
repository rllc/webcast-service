package org.llc.webcast.service

import org.llc.webcast.domain.OperationResponse

/**
 * Created by steve on 10/25/2015.
 */
interface WebcastService {

    OperationResponse start()

    OperationResponse stop()

    OperationResponse status()

}