/**
 *  Chained Run-In
 *
 *  Copyright 2015 Bob Florian
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Chained Run-In Test",
    namespace: "bflorian",
    author: "Bob Florian",
    description: "Schedules itself to test for failed schedules",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section {
		input "period", "number", title: "Period (seconds)"
        input "contacts", "contact", title: "Notify", multiple: true, required: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unschedule()
	initialize()
}

def initialize() {
	runIn(period, chainedRunInHandler, [overwrite: false])
	def sec = Math.round(Math.floor(Math.random() * 60))
	def min = Math.round(Math.floor(Math.random() * 5))
    
    //schedule("$sec $min/5 * * * ?", checkHandler)
}

def chainedRunInHandler() {
	
	log.debug "Chained run-in test executing runInHandler for $app.id"
	def d = new Date()
	state.lastRunTime = d.toSystemFormat()
    state.lastRunTimeMsec = d.time
    state.stopAlertSent = false
    //for (i in 1..45) {
    //	pause(1000)
    //}
    runIn(period, chainedRunInHandler, [overwrite: false])
}

def checkHandler() {
	if (!state.stopAlertSent && now() - state.lastRunTimeMsec > 2000 * period) {
    	//sendNotificationToContacts("$app.label is no longer running", contacts)
        sendNotificationEvent("$app.label is no longer running")
        state.stopAlertSent = true
    }
}