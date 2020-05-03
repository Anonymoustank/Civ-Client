package practice;

object main {
    @JvmStatic
    fun main(args: Array<String>) {
    	var list = mutableListOf(0,1,2,3)
		var x = 2
		for (i in list){
			if (i == x) list.remove(x)
		}
		println(list)
	}
	


}
