package ru.tcsbank.services.auth.fingerprint

import scala.util.parsing.combinator.RegexParsers

/**
 * User: alexey
 * Date: 11/9/13
 * Time: 8:41 PM
 */
object FingerPrintParser extends RegexParsers {

  private class StopwordParser(stopword: String) extends Parser[String] {

    private def matchChar(c: Char, in: FingerPrintParser.Input) = !in.atEnd && in.first == c

    private def matchString(s: List[Char], in: FingerPrintParser.Input): Boolean = {
      s match {
        case Nil => true
        case x::xs => matchChar(x, in) && matchString(xs, in.rest)
      }
    }

    private def afterString(s: List[Char], in: FingerPrintParser.Input): FingerPrintParser.Input = {
      s match {
        case Nil => in
        case x::xs if matchChar(x, in) => afterString(xs, in.rest)
      }
    }

    private def take(s: String, in: FingerPrintParser.Input, result: String): FingerPrintParser.ParseResult[String] = {
      if(in.atEnd)
        Failure(s"Stop word $s not found", in)
      else{
        if(matchString(s.toList, in))
          Success(result, afterString(s.toList, in))
        else {
          take(s, in.rest, result + in.first)
        }
      }
    }

    def apply(in: FingerPrintParser.Input): FingerPrintParser.ParseResult[String] = {
      take(stopword, in, "")
    }
  }

  private def stopword(word: String) = new StopwordParser(word)
  private def applicationTypeExpr: Parser[String] = "[^,;]+".r

  def applicationType: Parser[String] = applicationTypeExpr<~"[,]?".r

  def plugin: Parser[Plugin] =  stopword("::") ~ (stopword("::") ~ (applicationType*)) ^^(t => Plugin(t._1, t._2._1, t._2._2))

  def screen = "\\d+".r ~ ("x" ~> "\\d+".r ~ ("x" ~> "\\d+".r <~ "###")) ^^ (t => UserAgentScreen(t._1.toInt, t._2._1.toInt, t._2._2.toInt))

  def options = stopword("###")~stopword("###") ^^ (o => UserAgentOptions(o._1.toBoolean, o._2.toBoolean))

  def fingerprint = stopword("###")~screen~stopword("###")~options~((plugin<~";".?)*) ^^ (r => {

    Fingerprint(r._1._1._1._1, r._1._1._1._2, Time(r._1._1._2.toLong), r._1._2, r._2)
  })

}
