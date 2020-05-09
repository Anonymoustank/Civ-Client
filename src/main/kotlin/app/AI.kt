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
            doProduce: (type: ProductionType, location: Pair<Int, Int>) -> Boolean,
            doTechnology: (type: TechnologyType) -> Boolean,
            doMoveArmy: (srcPos: Pair<Int, Int>, dstPos: Pair<Int, Int>) -> Boolean,
            doMoveWorker: (srcPos: Pair<Int, Int>, dstPos: Pair<Int, Int>) -> Boolean
    ) {
        var list = mutableListOf(0,1,2,3)
        list.remove(playerIndex)
        // Check how much food there is before making worker/army
        for (worker in players[playerIndex].workers){
            doMoveWorker(worker.position, Pair(worker.position.first, worker.position.second + 1))
        }
        var list1 = mutableListOf(ResourceType.Food, ResourceType.Trade, ResourceType.Production)
        for (z in list1){
            if (players[playerIndex].resources[z]!! < 15 && players[playerIndex].workers.size > 0){
                for (worker in players[playerIndex].workers){
                    if (worker.isValidMove(Pair(worker.position.first + 1, worker.position.second)) && worker.position.first + 1 < 32){
                        val a = map.getHarvestAmounts(Pair(worker.position.first + 1, worker.position.second))
                        if (a!![z]!! > 0){
                            doMoveWorker(worker.position, Pair(worker.position.first + 1, worker.position.second))
                        }
                    }
                    if (worker.isValidMove(Pair(worker.position.first - 1, worker.position.second)) && worker.position.first - 1 > -32){
                        val a = map.getHarvestAmounts(Pair(worker.position.first - 1, worker.position.second))
                        if (a!![z]!! > 0){
                            doMoveWorker(worker.position, Pair(worker.position.first - 1, worker.position.second))
                        }
                    }
                    if (worker.isValidMove(Pair(worker.position.first, worker.position.second + 1)) && worker.position.second + 1 < 32){
                        val a = map.getHarvestAmounts(Pair(worker.position.first, worker.position.second + 1))
                        if (a!![z]!! > 0){
                            doMoveWorker(worker.position, Pair(worker.position.first, worker.position.second + 1))
                        }
                    }
                    if (worker.isValidMove(Pair(worker.position.first, worker.position.second - 1)) && worker.position.second - 1 > -32){
                        val a = map.getHarvestAmounts(Pair(worker.position.first, worker.position.second - 1))
                        if (a!![z]!! > 0){
                            doMoveWorker(worker.position, Pair(worker.position.first, worker.position.second - 1))
                        }
                    }
                }
    
            }
        }
        
        if (players[playerIndex].resources[ResourceType.Production]!! >= 8 && players[playerIndex].armies.size < players[playerIndex].cities.size && players[playerIndex].workers.size + players[playerIndex].armies.size + 1 <= players[playerIndex].resources[ResourceType.Food]!!) {
            val randomInteger = (0..players[playerIndex].cities.size-1).shuffled().first()
            doProduce(ProductionType.Army, players[playerIndex].cities[randomInteger].position)
        }

        
        if(players[playerIndex].resources[ResourceType.Production]!! >= 8 && players[playerIndex].workers.size < players[playerIndex].cities.size + 1 && players[playerIndex].workers.size + players[playerIndex].armies.size + 1 <= players[playerIndex].resources[ResourceType.Food]!!) {
                val randomInteger = (0..players[playerIndex].cities.size-1).shuffled().first()
                doProduce(ProductionType.Worker, players[playerIndex].cities[randomInteger].position)
        }

        println("hello")

        if(players[playerIndex].resources[ResourceType.Production]!! >= 24){
            if (players[playerIndex].workers.size == 0) {
                println("This player doesn't have workers")
            }
            else{
                for(worker in players[playerIndex].workers){
                    var city_num = 0
                    for (i in players[playerIndex].cities){
                        if (worker.position.first == i.position.first && worker.position.second == i.position.second) break
                        else city_num++
                    }
                    if (city_num == players[playerIndex].cities.size) doProduce(ProductionType.City, worker.position)
                }
            }
        }

       
        for (i in list){
            if (players[i].armies.size == 0 || players[playerIndex].armies.size == 0) println("This player doesn't have armies")
            else{
            for(army in players[i].armies){
                for(my_army in players[playerIndex].armies){
                    val x_pos = my_army.position.first - army.position.first
                    val x_pos_abs = abs(x_pos)
                    val y_pos = my_army.position.second - army.position.second
                    val y_pos_abs = abs(y_pos)

                    if (x_pos_abs == 1 && y_pos_abs == 1){
                        println("Hello")
                    }
                    else if (x_pos_abs == 1){
                        if (map.getCombatMultiplier(Pair(my_army.position.first, my_army.position.second))!! - map.getCombatMultiplier(Pair(army.position.first, army.position.second))!! >= 0){
                            doMoveArmy(my_army.position, Pair(army.position.first, my_army.position.second))
                        }
                    }
                    else if (y_pos_abs == 1){
                        if (map.getCombatMultiplier(Pair(my_army.position.first, my_army.position.second))!! - map.getCombatMultiplier(Pair(army.position.first, army.position.second))!! >= 0){
                            doMoveArmy(my_army.position, Pair(my_army.position.first, army.position.second))
                        }
                    }
                    
                }
            }
        }
        }
        // BoardUnit.isValidMove, ex: army.isValidMove(position)
        //army.position.first = x, army.position.second = y

        println("hello")

        val one: Int = 1
        
        
        if (players[playerIndex].workers.size != 0){
            for (worker in players[playerIndex].workers){
                println(worker.position)
                if (worker.isValidMove(Pair(worker.position.first + 1, worker.position.second)) && worker.position.first + 1 < 32 && worker.position.first + 1 > -32){
                    doMoveWorker(worker.position, Pair(worker.position.first + 1, worker.position.second))
                }
                else if (worker.isValidMove(Pair(worker.position.first - 1, worker.position.second)) && worker.position.first - 1 < 32 && worker.position.first -1 > -32){
                    doMoveWorker(worker.position, Pair(worker.position.first - 1, worker.position.second))
                }
                else if (worker.isValidMove(Pair(worker.position.first, worker.position.second + 1)) && worker.position.second + 1 < 32){
                    doMoveWorker(worker.position, Pair(worker.position.first, worker.position.second + 1))
                }
                else if (worker.isValidMove(Pair(worker.position.first, worker.position.second - 1)) && worker.position.second - 1 < 32 && worker.position.second - 1 > -32){
                    doMoveWorker(worker.position, Pair(worker.position.first, worker.position.second - 1))
                }
            }
        }
        
    }
}