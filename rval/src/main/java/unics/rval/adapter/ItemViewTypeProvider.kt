package unics.rval.adapter

interface ItemViewTypeProvider {
    fun getItemType(data: Any, position: Int): Int
}