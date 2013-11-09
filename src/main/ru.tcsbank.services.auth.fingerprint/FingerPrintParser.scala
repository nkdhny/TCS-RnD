package ru.tcsbank.services.auth.fingerprint

import scala.util.parsing.combinator.RegexParsers

/**
 * User: alexey
 * Date: 11/9/13
 * Time: 8:41 PM
 */
object FingerPrintParser extends RegexParsers{

  private def applicationTypeExpr: Parser[String] = "[^,;]+".r


  def applicationType: Parser[String] = applicationTypeExpr<~"[,;]?".r

  def pluginName = new Parser[String] {
    def apply(in: FingerPrintParser.Input): FingerPrintParser.ParseResult[String] = {

      def take(in: FingerPrintParser.Input, result: String = ""):  FingerPrintParser.ParseResult[String] = {

        def hasColonColon(in: FingerPrintParser.Input): Boolean = {
          !in.atEnd && in.first == ':' && !in.rest.atEnd && in.rest.first == ':'
        }

        def witoutColons = in.rest.rest

        if(in.atEnd)
          FingerPrintParser.Failure("No colon colon at the end of line", in)
        else {
          if(hasColonColon(in))
            Success(result, witoutColons)
          else
            take(in.rest, result + in.first) //
        }
      }

      take(in)
    }
  }

  def pluginDescription = pluginName

  def plugin: Parser[Plugin] =  pluginName ~ (pluginDescription ~ (applicationType*)) ^^(t => Plugin(t._1, t._2._1, t._2._2))

}
