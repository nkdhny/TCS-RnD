package ru.tcsbank.services.auth.fingerprint

import org.scalatest.FunSuite

/**
 * User: alexey
 * Date: 11/9/13
 * Time: 9:17 PM
 */

class FingerPrintParserTest extends FunSuite {

  val appType = """application/x-shockwave-flash~swf,application/futuresplash~spl"""
  val plugin = """Shockwave Flash::Shockwave Flash 11.2 r202 (see http://ya.ru)::"""+appType

  val fingerPrint = """Mozilla/5.0 (X11; Linux i686)###768x1366x24###-240###true###true###plugin one::some description::application/vnd.chromium.remoting-viewer~;anoter one::::application/x-gnome-shell-integration~"""

  test("Shold parse an application type") {
    val parsed = FingerPrintParser.parseAll(FingerPrintParser.applicationType*, appType)

    assert(parsed.successful)
    assert(parsed.get.size == 2)
  }

  test("Should parse a plugin") {
    val parsed = FingerPrintParser.parseAll(FingerPrintParser.plugin, plugin)
    assert(parsed.successful)
    assert(parsed.get.name == "Shockwave Flash")
  }

  test("Should parse a fingerprint") {
    val parsed = FingerPrintParser.parseAll(FingerPrintParser.fingerprint, fingerPrint)
    assert(parsed.successful)
    assert(parsed.get.screen == UserAgentScreen(768, 1366, 24))
    assert(parsed.get.options == UserAgentOptions(true, true))
    assert(parsed.get.plugins.size == 2)
  }

}
