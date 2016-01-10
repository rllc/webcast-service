var StreamState = Object.freeze({STARTED: 0, STOPPED: 1, NETWORK_ERROR: 2, UNKNOWN: 3});
var applicationState = StreamState.UNKNOWN;

$(function () {
    console.log("ready!");
    refreshPage();
    checkStatus();
    setInterval(checkStatus, 3000);

    $("#stream-button").click(function () {
        switch (applicationState) {
            case StreamState.STARTED:
                // stop stream
                console.log("stopping stream");
                $.ajax({
                    method: 'POST',
                    url: 'webcast/stop'
                })
                    .success(function () {
                        applicationState = StreamState.STOPPED;
                    })
                    .error(function () {
                        applicationState = StreamState.NETWORK_ERROR;
                    })
                    .always(function () {
                        refreshPage();
                    });

                break;
            case StreamState.STOPPED:
            case StreamState.NETWORK_ERROR:
            case StreamState.UNKNOWN:
                // start stream
                console.log("starting stream");
                $.ajax({
                    method: 'POST',
                    url: 'webcast/start'
                })
                    .success(function () {
                        applicationState = StreamState.STARTED;
                    })
                    .error(function () {
                        applicationState = StreamState.NETWORK_ERROR;
                    })
                    .always(function () {
                        refreshPage();
                    });
                break;
        }
    });
});

function checkStatus() {
    console.log('checking stream status...');

    $.ajax({
        method: 'GET',
        url: 'webcast/status'
    })
        .success(function (data, status, jqXHR) {
            switch (data.state) {
                case 'STOPPED':
                    applicationState = StreamState.STOPPED;
                    break;
                case 'STARTED':
                    applicationState = StreamState.STARTED;
                    break;
                case 'ERROR':
                    applicationState = StreamState.ERROR;
                    break;
                case 'UNKNOWN':
                    applicationState = StreamState.UNKNOWN;
                    break;
            }
        })
        .error(function (jqXHR, status, error) {
            applicationState = StreamState.NETWORK_ERROR;
        })
        .always(function () {
            refreshPage();
        });
}

function refreshPage() {
    $("#stream-button").prop('disabled', false);
    $("#stream-status").removeClass('stream-started');
    $("#stream-status").removeClass('stream-stopped');
    $("#stream-status").removeClass('stream-error');

    switch (applicationState) {
        case StreamState.STARTED:
            $("#stream-status-text").text('STARTED');
            $("#stream-status").addClass('stream-started');

            $("#stream-button").text("STOP");
            break;
        case StreamState.STOPPED:
            $("#stream-status-text").text('STOPPED');
            $("#stream-status").addClass('stream-stopped');

            $("#stream-button").text("START");
            break;
        case StreamState.NETWORK_ERROR:
            $("#stream-status-text").text('NETWORK ERROR');
            $("#stream-status").addClass('stream-error');

            $("#stream-button").text("START");
            break;
        case StreamState.UNKNOWN:
            $("#stream-status-text").text('UNKNOWN');
            $("#stream-status").addClass('stream-unknown');

            $("#stream-button").prop('disabled', true);
            $("#stream-button").text("START");
            break;
    }
}


