package eightBugs.osteocare.screening.data.models

data class MovementResult( val leftRom: Double,
                          val rightRom: Double,
                          val asymmetry: Double,
                          val sitToStandTime: Double,
                          val repetitions: Int,
                          val analyzedFrames: Int)
