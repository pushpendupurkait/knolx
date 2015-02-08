package com.knol.core
trait KnolRepo {
  def searchKnolderByID(id: Int): Option[Knol]
  def create(knolx: Knol): Option[Int]
  def deleteKnolderByID(id: Int): Option[Int]
  def updateKnolder(knolx : Knol):Option[Int]
  def getList(): Option[scala.collection.mutable.MutableList[Knol]]
}
case class Knol(name:String, email:String, mobile:String,id:Option[Int]=None)
{
  }
