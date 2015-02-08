package com.knol.core

trait KnolSessionRepo {
    def joinedSelect(id:Int): Option[JoinedKnol]
}
case class JoinedKnol(knol_id:Int,knolx_id:Int,name:String,email:String,mobile:String,date:String){}
