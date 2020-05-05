package app

import kotlin.math.abs as abs 

// EDIT THIS FILE

class AI() {
    // Change the name that shows up on the graphical display
    // optional, but HIGHLY RECOMMENCED
    fun getName() = "pkadekodi"

    // make a move
    // map is the game map
    // players is the list of all the players, containing their resources, cities, armies, workers
    // playerIndex is the index of you player in the players list

    // api functions:
    // doProduce is a function to call to produce something (city, army, worker)
    // doTechnology is a function to call to increase offesive or defensive strength
    // doMoveArmy moves an army from srcPos to dstPos
    // doMoveWorker moves a worker from srcPos to dstPos

    fun doMove(
            map: GameMap,
            players: List<Player>,
            playerIndex: Int,
            doProduce: (type: ProductionType, location: Pair<Int, Int>) -> Unit,
            doTechnology: (type: TechnologyType) -> Unit,
            doMoveArmy: (srcPos: Pair<Int, Int>, dstPos: Pair<Int, Int>) -> Unit,
            doMoveWorker: (srcPos: Pair<Int, Int>, dstPos: Pair<Int, Int>) -> Unit
    ) {
        var list = mutableListOf(0,1,2,3)

        for (i in list){
			if (i == playerIndex) list.remove(playerIndex)
		}
        // Check how much food there is before making worker/army

        val player1 = list[0]
        val player2 = list[1]
        val player3 = list[2]

        // for example, produce a worker on one of our cities if we have enough production
        if(players[playerIndex].resources[ResourceType.Production]!! >= 8 && players[playerIndex].workers.size < players[playerIndex].cities.size && players[playerIndex].workers.size + players[playerIndex].armies.size + 1 <= players[playerIndex].resources[ResourceType.Food]!!) {
                val randomInteger = (0..players[playerIndex].cities.size-1).shuffled().first()
                doProduce(ProductionType.Worker, players[playerIndex].cities[randomInteger].position)
        }

        if players[playerIndex].resources[ResourceType.Production]!! >= 8 && players[playerIndex].armies.size < players[playerIndex].cities.size && players[playerIndex].workers.size + players[playerIndex].armies.size + 1 <= players[playerIndex].resources[ResourceType.Food]!!) {
            val randomInteger = (0..players[playerIndex].cities.size-1).shuffled().first()
            doProduce(ProductionType.Army, players[playerIndex].cities[randomInteger].position)
        }

        if(players[playerIndex].resources[ResourceType.Production]!! >= 24){
            if (players[playerIndex].workers? == null) println("This player doesn't have workers")
            else{
                for(worker in players[playerIndex].workers){
                    var city_num = 0
                    for (i in 0..players[playerIndex].cities.size-1){
                        if (worker.position == i.position) break
                        else city_num++
                    }
                    if (city_num == players[playerIndex].cities.size) doProduce(ProductionType.City, worker.position)
                }
            }
        }

       
        for (i in list){
            if (players[i].armies? == null) println("This player doesn't have armies")
            else{
            for(army in players[i].armies){
                for(my_army in players[playerIndex].armies){
                    val x_pos = my_army.position.first - army.position.first
                    val x_pos_abs = abs(x_pos)
                    val y_pos = my_army.position.second - army.position.second
                    val y_pos_abs = abs(y_pos)

                    if (x_pos_abs == 1 && y_pos_abs == 1){
                        doMoveArmy(my_army.position, Pair(my_army.position.first, my_army.position.second))
                    }
                    else if (x_pos_abs == 1){
                        if (getCombatMultiplier(Pair(my_army.position.first, my_army.position.second)) - getCombatMultiplier(Pair(army.position.first, army.position.second)) >= 0){
                            doMoveArmy(my_army.position, Pair(army.position.first, my_army.position.second))
                        }
                    }
                    else if (y_pos_abs == 1){
                        if (getCombatMultiplier(Pair(my_army.position.first, my_army.position.second)) - getCombatMultiplier(Pair(army.position.first, army.position.second)) >= 0){
                            doMoveArmy(my_army.position, Pair(my_army.position.first, army.position.second))
                        }
                    }
                    
                }
            }
        }
        }
        // BoardUnit.isValidMove, ex: army.isValidMove(position)
        //army.position.first = x, army.position.second = y
        for(army in players[playerIndex].armies) {
            if (getCombatMultiplier(Pair(army.position.first, army.position.second)) < getCombatMultiplier(Pair(army.position.first + 1, army.position.second))) doMoveArmy(army.position, Pair(army.position.first + 1, army.position.second))
            else if (getCombatMultiplier(Pair(army.position.first, army.position.second)) < getCombatMultiplier(Pair(army.position.first - 1, army.position.second))) doMoveArmy(army.position, Pair(army.position.first - 1, army.position.second))
            else if (getCombatMultiplier(Pair(army.position.first, army.position.second)) < getCombatMultiplier(Pair(army.position.first, army.position.second + 1))) doMoveArmy(army.position, Pair(army.position.first, army.position.second + 1))
            else if (getCombatMultiplier(Pair(army.position.first, army.position.second)) < getCombatMultiplier(Pair(army.position.first, army.position.second - 1))) doMoveArmy(army.position, Pair(army.position.first, army.position.second - 1))
        }
    }
}