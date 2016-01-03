package org.llc.webcast.controller

import org.llc.webcast.domain.OperationResponse
import org.llc.webcast.service.WebcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by steve on 10/25/2015.
 */
@RestController
@RequestMapping(value = 'webcast')
class WebcastController {

    @Autowired
    WebcastService webcastService

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    OperationResponse start() {
        webcastService.start()
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    OperationResponse stop() {
        webcastService.stop()
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    OperationResponse status() {
        webcastService.status()
    }

}
