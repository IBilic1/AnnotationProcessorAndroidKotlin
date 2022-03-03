package hr.algebra.ivanabilic.nba.model

enum class Positions(private val position:String) {
    CENTER("C"),POWER_FORWARD("F"),POINT_GUARD("G");

    companion object{
        fun getPositions(position: String):String?{
            for (value in values()){
                if (value.position==position){
                    return value.toString()
                }
            }
            return null
        }
    }
}