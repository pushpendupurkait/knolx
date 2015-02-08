package com.knol.core
import java.util.Date
trait SessionRepo {
  def searchSessionByID(id: Int): Option[Knolx]
  def createNewSession(knolx: Knolx): Option[Int]
  def deleteSessionByID(id: Int): Option[Int]
  def updateSession(knolx : Knolx):Option[Int]
  def getList(): Option[scala.collection.mutable.MutableList[Knolx]]
}
case class Knolx(topic:String, date:String, knol_id:Int, id:Option[Int]=None)
{
  }
