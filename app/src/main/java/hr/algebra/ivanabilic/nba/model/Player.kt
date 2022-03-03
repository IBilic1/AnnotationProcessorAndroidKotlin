package hr.algebra.ivanabilic.nba.model

class Player(
    var _id:Long?,
    val firstname:String,
    val lastname:String,
    val picturePath:String,
    val position:String,
    var best:Boolean
){
    override fun toString(): String
    ="$firstname $lastname"
}