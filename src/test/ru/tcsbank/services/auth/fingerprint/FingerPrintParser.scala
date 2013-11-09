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

  test("Shold parse an application type") {
    val parsed = FingerPrintParser.parseAll(FingerPrintParser.applicationType*, appType)
    println(parsed)
    assert(true)
  }

  test("Should parse a plugin") {
    val parsed = FingerPrintParser.parseAll(FingerPrintParser.plugin, plugin)
    println(parsed)
    assert(true)
  }

}
