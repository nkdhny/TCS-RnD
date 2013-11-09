package ru.tcsbank.services.auth.fingerprint

/**
 * User: alexey
 * Date: 11/9/13
 * Time: 8:12 PM
 */
case class Time(millis: Long)
case class Plugin(name: String, description: String, applicationType: Seq[String])
case class UserAgentScreen(width: Int, height: Int, depth: Int)
case class UserAgentOptions(cookiesEnabled: Boolean, sessionStorageEnabled: Boolean)

case class Fingerprint(userAgent: String,
                       screen: UserAgentScreen,
                       timeZoneOffset: Time,
                       options: UserAgentOptions,
                       plugins: Seq[Plugin])
